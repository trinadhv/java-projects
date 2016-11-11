

import ch.ntb.usb.Device;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Bone.Type;

import java.io.IOException;
public class RobotArm
{
  // lengths of arm elements (in mm)
  private static final int GRIPPER_LEN = 110;   
  private static final int LOWER_ARM = 110;
  private static final int UPPER_ARM = 90;
  private static final int BASE_HEIGHT = 70;


  private Joint base, wrist, shoulder, elbow;
  private Joint[] joints;    // so can iterate through all the joints easily
  private ArmCommunicator armComms;


  public RobotArm()
  {
     armComms = new ArmCommunicator();

     base = new Joint(JointID.BASE, armComms);
     wrist = new Joint(JointID.WRIST, armComms);
     shoulder = new Joint(JointID.SHOULDER, armComms);
     elbow = new Joint(JointID.ELBOW, armComms);

     joints = new Joint[4];   // order is important; used by moveTo()
     joints[0] = base;
     joints[1] = wrist;
     joints[2] = shoulder;
     joints[3] = elbow;
  }  // end of RobotArm()

	
  public void close()
  {
    for(Joint j : joints)
      j.saveState();
    armComms.close();
  }  // end of close()
	

  public void moveToZero()
  { 
    System.out.println("Moving to zero...");
    for(int i=joints.length-1; i >= 0; i--)
      joints[i].turnToAngle(0);     // process joints in reverse
  }   // end of moveToZero()



  public void turnByOffset(JointID jid, int offsetAngle)
  {
    if (jid ==  JointID.WRIST)
      wrist.turnByOffset(offsetAngle);
    else if (jid == JointID.ELBOW)
      elbow.turnByOffset(offsetAngle);
    else if (jid == JointID.SHOULDER)
      shoulder.turnByOffset(offsetAngle);
    else if (jid == JointID.BASE)
      base.turnByOffset(offsetAngle);
    else 
      System.out.println("Unknown joint ID: " + jid);
  } // end of turnByOffset()


  public void turnToAngle(JointID jid, int angle)
  {
    if (jid ==  JointID.WRIST)
      wrist.turnToAngle(angle);
    else if (jid == JointID.ELBOW)
      elbow.turnToAngle(angle);
    else if (jid == JointID.SHOULDER)
      shoulder.turnToAngle(angle);
    else if (jid == JointID.BASE)
      base.turnToAngle(angle);
    else 
      System.out.println("Unknown joint ID: " + jid);
  } // end of turnToAngle()



  public void openGripper(boolean isOpen)
  { armComms.openGripper(isOpen); }


  public void setLight(boolean turnOn)
  {  armComms.setLight(turnOn);  }


  public void wait(int ms)
  { armComms.wait(ms);  }
  
  
  
  public void moveback()
  {
    armComms.turn(JointID.ELBOW, ArmCommunicator.NEGATIVE, 650);
	 armComms.turn(JointID.WRIST, ArmCommunicator.NEGATIVE, 120);
	// armComms.turn(JointID.SHOULDER, ArmCommunicator.NEGATIVE, 450);
  }



  public void showAngles()
  {
    System.out.println("Current Angles:");
    for(Joint j : joints)
      System.out.print( "  " + j.getName() + ": " + j.getCurrAngle());
    System.out.println();
  }  // end of showAngles()


  // ------------------- inverse kinematics -------------------------


  public boolean moveTo(Coord3D pt)
  {  return moveTo(pt.getX(), pt.getY(), pt.getZ());  }


  public boolean moveTo(int x, int y, int z)
  {
    System.out.println("-----");
    System.out.println("Moving to (" + x + ", " + y + ", " + z + ")...");

    int[] angles = calcIK(x, y, z);
    if (angles == null)
      return false;

    if (!withinRanges(angles)) {
      System.out.println("Move Cancelled");
      return false;
    }
    else {
      for (int i=0; i < angles.length; i++)
        joints[i].turnToAngle(angles[i]);
      return true;
    }
  }  // end of moveTo()


  private boolean withinRanges(int[] angles)
  // report angles that are out of range;
  // angles order in array must be same as joints[] array
  {
    boolean hasProbs = false;
    // System.out.println("IK angles for (" + x + ", " + y + ", " + z + ")");
    for (int i=0; i < angles.length; i++) {
      // System.out.println("  " + joints[i].getName() + ": " + angles[i]);
      if (!joints[i].isInRange(angles[i])) {
        System.out.println("  " + joints[i].getName() + " out of range");
        hasProbs = true;
      }
    }
    return !hasProbs;
  }  // end of withinRanges()



  private int[] calcIK(int x, int y, int z)
  /* Use (x,y,z) coordinate to calculate the IK
     angles for the base, elbow, shoulder and wrist joints. 
  */
  {
    int extent2 = (x*x) + (y*y);
    int maxExtent = GRIPPER_LEN + LOWER_ARM + UPPER_ARM;
    if (extent2 > (maxExtent*maxExtent)) {
      System.out.println("Coordinate (" + x + ", " + y + ", " + z +
                           ") is too far away on the XY plane");
      return null;
    }

    // base angle and radial distance from x,y coordinates 
    double baseAngle = Math.toDegrees( Math.atan2(x,y) );
    double rdist = Math.sqrt((x*x) + (y*y));
          // radial distance now treated as the y coordinate for the arm 
    
    // wrist position 
    double wristZ = z - BASE_HEIGHT; 
    double wristY = rdist - GRIPPER_LEN;
    // System.out.printf("wrist (y,z): (%.2f, %.2f)\n", wristY, wristZ);

    // shoulder-wrist squared distance (swDist2)
    double swDist2 = (wristZ * wristZ) + (wristY * wristY);
    // System.out.printf("swDist2: %.2f\n", swDist2);

    // shoulder-wrist angle to ground 
    double swAngle1 = Math.atan2(wristZ, wristY);
  
    double triVal = (UPPER_ARM*UPPER_ARM + swDist2 - LOWER_ARM*LOWER_ARM) / 
                               (2 * UPPER_ARM * Math.sqrt(swDist2));
    // System.out.printf("triVal: %.8f\n", triVal);
    if (triVal > 1.0) {
      System.out.println("Arm not long enough to reach coordinate");
      return null;
    }
    double swAngle2 = Math.acos(triVal);
    // System.out.printf("swAngles: %.2f, %.2f)\n", 
    //                       Math.toDegrees(swAngle1), Math.toDegrees(swAngle2));

    double shoulderAngle = 90.0 - Math.toDegrees(swAngle1 + swAngle2);
  
    // elbow angle 
    double ewAngle = Math.acos(
              (UPPER_ARM*UPPER_ARM + LOWER_ARM*LOWER_ARM - swDist2) / 
              (2 * UPPER_ARM * LOWER_ARM) );
    double elbowAngle = 180.0 - Math.toDegrees(ewAngle);
  
    double wristAngle = 90.0 - (shoulderAngle + elbowAngle);

    // round angles to integers
    int baseAng = (int)Math.round(baseAngle);
    int shoulderAng = (int)Math.round(shoulderAngle);
    int elbowAng = (int)Math.round(elbowAngle);
    int wristAng = (int)Math.round(wristAngle);

    int[] angles = new int[] {baseAng, wristAng, shoulderAng, elbowAng};
    return angles;      // order must be same as joints[]
  }  // end of calcIK()


  // ------------------- forward kinematics -------------------------


  public Coord3D getCoord()
  {  
    int baseAngle = base.getCurrAngle();
    int shoulderAngle = shoulder.getCurrAngle();
    int elbowAngle = elbow.getCurrAngle();
    // wristAngle not needed

    if (!base.isInRange(baseAngle))
      System.out.println("  base angle (" + baseAngle + ") out of range");

    if (!shoulder.isInRange(shoulderAngle))
      System.out.println("  shoulder angle (" + shoulderAngle + ") out of range");

    if (!elbow.isInRange(elbowAngle))
      System.out.println("  elbow angle (" + elbowAngle + ") out of range");

    double baseAng = Math.toRadians(baseAngle);
    double shoulderAng =  Math.toRadians(shoulderAngle);
    double elbowAng = Math.toRadians(elbowAngle);

    int seAngle = 180 - (shoulderAngle + elbowAngle);
    double seAng =  Math.toRadians(seAngle);

    double radialDist = UPPER_ARM*Math.sin(shoulderAng) +
                        LOWER_ARM*Math.sin(seAng) + GRIPPER_LEN;
    int x = (int)Math.round( radialDist*Math.sin(baseAng) );
    int y = (int)Math.round( radialDist*Math.cos(baseAng) );

    int z = (int)Math.round( BASE_HEIGHT + UPPER_ARM*Math.cos(shoulderAng) -
                            LOWER_ARM*Math.cos(seAng) );
   
    Coord3D pt = new Coord3D(x, y, z);
    // System.out.println("getCoord() pos: " + pt);

    return pt;
  }  // end of getCoord()


  // -------------------------- item movement --------------------------

  public void moveItem(Coord3D fromPt, Coord3D toPt)
  {
    boolean hasMoved = moveTo(fromPt);
    showAngles();
    System.out.println("From Coord: " + getCoord() );

    if (hasMoved) {
      openGripper(false);
      
      turnByOffset(JointID.ELBOW, -30);     // so off the floor
      System.out.println("Off-floor Coord: " + getCoord() );
      
      moveTo(toPt);   // in mm; no checking of result
      showAngles();
      System.out.println("To Coord: " + getCoord() );
      openGripper(true);
    }
  }  // end of moveItem()



  // ------------------------------------ test rig --------------------------
  // MODIFY main() to test out different arm movements

  public static void main(String[] args)
  {
  
	
   SampleListener listener = new SampleListener();
   Controller controller = new Controller();

// Have the sample listener receive events from the controller
   controller.addListener(listener);
    System.out.println("Press Enter to quit...");
    try {
    System.in.read();
  } catch (IOException e) {
    e.printStackTrace();
   }

controller.removeListener(listener);

    // tests that cause errors:
    // boolean hasMoved = robotArm.moveTo(0, 300, 130);   // reaching too far
    // boolean hasMoved = robotArm.moveTo(150, 250, 130);  // too far
    // boolean hasMoved = robotArm.moveTo(0, 8, 65);    
                          // wrist and shoulder out of range
    // boolean hasMoved = robotArm.moveTo(0, 150, 65);      // elbow out of range

    // tests in methods:
    // moveInLine(robotArm);
   //  shiftItem(robotArm);

    // move to, grab, move to, release
   // robotArm.openGripper(true);   // open gripper

   // robotArm.openGripper(false);

	// robotArm.moveTo(250,150,50);
    // robotArm.wait(2000);
    
	
      // close


    // robotArm.moveTo(-150, 200, 65);   // in mm
  //  robotArm.moveToZero();
//	robotArm.moveback();

	 
   // robotArm.openGripper(true);     // open

    // report angles and coords
  //  robotArm.showAngles();
  //  System.out.println("Coord: " + robotArm.getCoord() );
  // robotArm.close();
    
  }  // end of main()



  private static void shiftItem(RobotArm robotArm)
  // moves object from one coord to another
  {
    Coord3D fromPt =  new Coord3D(150, 150, 65);     // in mm
    Coord3D toPt =  new Coord3D(-150, 200, 65);

    robotArm.moveItem(fromPt, toPt);
  }  // end of shiftItem()



  private static void moveInLine(RobotArm robotArm)
  // moves arm in a straight line 
  {
     for(int x = -100; x <= 100; x += 20) {
       robotArm.moveTo(x, 200, 80);
       robotArm.wait(500);
     }
  }  // end of moveInLine()


}  // end of RobotArm class


class SampleListener extends Listener {

  RobotArm robotArm;
	int flag=0;

	public void onConnect(Controller controller) {
		robotArm = new RobotArm();
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
    }

    public void onFrame(Controller controller) {
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    	  Frame frame = controller.frame();
		 
		  
		  if(frame.hands().count()==0)
		  {
		   if(flag==0)
		   {
		       System.out.println("put your hand to start");
		    }
			if(flag==1)
			{
			System.out.println("Closing device");
	        robotArm.moveToZero();
			robotArm.moveback();
			robotArm.showAngles();
			robotArm.close();
			System.exit(0);
			}
		  }
		  else{
		  flag=1;
		  }
    	 int x1,x2,y1,y2,z1,z2;
         
    	 
    	 int distance=0;

    	   /*System.out.println("Frame id: " + frame.id()
    	                   + ", timestamp: " + frame.timestamp()
    	                   + ", hands: " + frame.hands().count()
    	                   + ", fingers: " + frame.fingers().count()
    	                   + ", tools: " + frame.tools().count()
    	                   + ", gestures " + frame.gestures().count()); */
    	
    	  
    	  for (Finger finger : frame.fingers()) {
    		  Hand hand = frame.hands().frontmost();
			 
    		  if(finger.type()==Finger.Type.TYPE_INDEX)
    		  {
			  
			   int pinch=(int)hand.pinchStrength();
    		 // System.out.println(pinch);
			  
    		    for(Bone.Type boneType : Bone.Type.values()) {
    		        Bone bone = finger.bone(boneType);
    		     if(bone.type()==Bone.Type.TYPE_DISTAL)
    		     {
    		     //  System.out.println("finger:"+finger.type());
    		       x1=(int)bone.center().getX();
    		       y1=(int)bone.center().getY();
    		       z1=(int)bone.center().getZ();
    		      System.out.println(" "+x1+" "+y1+" "+z1);
				  robotArm.moveTo(x1,(-1*z1),y1);
				  
				  
    		     }
    		        
    		    }
    		    
    		  } 
    		  
    		
    		  
    		}
    	  
    }
	
	
}
