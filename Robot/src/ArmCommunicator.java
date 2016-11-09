
import java.io.*;
import java.util.Scanner;

import ch.ntb.usb.*;
import ch.ntb.usb.Device;

import java.io.IOException;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Bone.Type;


public class ArmCommunicator
{
  public final static int POSITIVE = 0;
  public final static int NEGATIVE = 1;
      // +ve for forward/right turns; -ve for backwards/left turns


  private final static short VENDOR_ID = (short)0x1267;
  private final static short PRODUCT_ID = (short)0x0;
          // the IDs were obtained by looking at the robot arm using USBDeview

  private final static int GRIPPER_PERIOD = 1700;   // ms time to open/close


  private Device dev = null;   // used to communicate with the USB device

  // start state for the light
  private boolean isLightOn = false;


  public ArmCommunicator()
  { 
    System.out.println("Looking for device: (vendor: " + toHexString(VENDOR_ID) + 
                                          "; product: " + toHexString(PRODUCT_ID) + ")");
    dev = USB.getDevice(VENDOR_ID, PRODUCT_ID);
    try {
      System.out.println("Opening device");
      dev.open(1, 0, 0); 
      // open device with configuration 1, interface 0 and no alt interface
    }
    catch (USBException e) {
      System.out.println(e);
      System.exit(1);
    }
  }  // end of ArmCommunicator()



	
  public void close()
  {
    System.out.println("Closing device");
    try {
      if (dev != null)
        dev.close();
    }
    catch (USBException e) {
      System.out.println(e);
      System.exit(1);
    }
  }  // end of close()
	

  // ------------------------------ command ops --------------------------

  /*
   First byte:
     Gripper close == 0x01     Gripper open == 0x02
  */

  public void openGripper(boolean isOpen)
  {  
    if (isOpen) {
      System.out.println("  Gripper: open");
      sendCommand(0x02, 0x00, GRIPPER_PERIOD); 
    }
    else {
      System.out.println("  Gripper: close");
      sendCommand(0x01, 0x00, GRIPPER_PERIOD); 
    }
  }  // end of openGripper()



  public void setLight(boolean turnOn)
  // Third byte: Light on/off
  {  isLightOn = turnOn; 
     System.out.println("  Light is on: " + isLightOn);
     sendControl(0x00, 0x00, getLightVal(isLightOn));
  }  


  private int getLightVal(boolean isLightOn)
  // light on/off
  {  return (isLightOn)? 0x01 : 0x00;  }



  public void turn(JointID jid, int dir, int period)
  /* First byte:
     Wrist forwards == 0x04          Wrist backwards == 0x08
     Elbow forwards == 0x10          Elbow backwards == 0x20
     Shoulder forwards == 0x40       Shoulder backwards == 0x80
   
   Second byte:
     Base rotate right == 0x01  Base rotate left == 0x02
  */
  {
    int opCode1 = 0x00;
    int opCode2 = 0x00;

    if (jid == JointID.BASE)
      opCode2 = (dir == POSITIVE)? 0x01 : 0x02;
    else if (jid == JointID.SHOULDER)
      opCode1 = (dir == POSITIVE)? 0x80 : 0x40;
    else if (jid == JointID.ELBOW)
      opCode1 = (dir == POSITIVE)? 0x20 : 0x10;
    else if (jid == JointID.WRIST)
      opCode1 = (dir == POSITIVE)? 0x08 : 0x04;
    else
      System.out.println("Unknown joint ID: " + jid);

    if (period < 0) {
      System.out.println("Turn period cannot be negative");
      period = 0;
    }
  
    System.out.println("  " + jid + " timed turn: " + dir + " " + period + "ms");
    sendCommand(opCode1, opCode2, period);
  }  // end of turn()



  private void sendCommand(int opCode1, int opCode2, int period)
  // execute the operation for period millisecs
  {
    int opCode3 = getLightVal(isLightOn);     // third byte == light on/off
    if (dev != null) {
      sendControl(opCode1, opCode2, opCode3);
      wait(period);
      sendControl(0, 0, opCode3);    // stop arm
    }
  }  // end of sendCommand()




  private void sendControl(int opCode1, int opCode2, int opCode3)
  // send a USB control transfer
  {
/*
    System.out.println("Sending ops: <" + toHexString(opCode1) + ", " + 
                                          toHexString(opCode2) + ", " +
                                          toHexString(opCode3) + ">");
*/
    byte[] bytes = { new Integer(opCode1).byteValue(),
                     new Integer(opCode2).byteValue(),
                     new Integer(opCode3).byteValue()
                   };
    try {
      int rval = dev.controlMsg(
                    USB.REQ_TYPE_DIR_HOST_TO_DEVICE | 
                    USB.REQ_TYPE_TYPE_VENDOR,        //0x40, 
                    0x06, 0x0100, 0, 
                    bytes, bytes.length, 2000, false);
      // System.out.println("usb_control_msg() result: " + rval);
      if (rval < 0) {
        System.out.println("Control Error (" + rval + "):\n  " + 
                                             LibusbJava.usb_strerror() );
      }
    }
    catch (USBException e) {
      System.out.println(e);
    }
  }  // end of sendControl()



  private String toHexString(int b)
  // chanage the hexadecimal integer into "0x.." string format
  {  
    String hex = Integer.toHexString(b);  
    if (hex.length() == 1)
      return "0x0" + hex;
    else
      return "0x" + hex;
  }  // end of toHexString




  public void wait(int ms)
  // sleep for the specified no. of millisecs
  { 
    // System.out.println("Waiting " + ms + " ms...");
    try {
      Thread.sleep(ms);
    }
    catch(InterruptedException e) {}
  }  // end of wait()


  // ------------------------------------ test rig --------------------------


  private static final int DELAY = 250;


  public static void main(String[] args)
  {
   // ArmCommunicator arm = new ArmCommunicator();
    

   
  //  System.out.println("Enter a single letter command (and <ENTER>:");
  
 /* printHelp();
    Scanner s=new Scanner(System.in);
    Console console = System.console();
    String line = null;
    char ch;

    System.out.print(">> ");
    
    while (true) {
    	line=s.nextLine();
      if (line.length() == 0)
        break;
      ch = line.charAt(0);
      if (ch == 'q')
        break;
      else if (ch == '?')
        printHelp();
      else
        doArmOp(ch, arm);
      System.out.print(">> ");
    }*/
   
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
  
    // restore start state
    // arm.openGripper(true);
    // arm.setLight(false);

   // arm.close();
  }  // end of main()


  private static void printHelp()
  {
    System.out.println("  Gripper: close == w ; open == s");
    System.out.println("  Wrist:     fwd == e ; back == d");
    System.out.println("  Elbow:     fwd == r ; back == f");
    System.out.println("  Shoulder:  fwd == u ; back == j");
    System.out.println("  Base:     left == k ; right == i");
    System.out.println("  Light:      on == l ; off == p");
    System.out.println("            quit == q ; help == ?");
  }  // end of printHelp()


  private static void doArmOp(char ch, ArmCommunicator arm)
  // use POSITIVE for forwards/right turns; NEGATIVE for backwards/left turns
  {
     if (ch == 'w')         // gripper close
       arm.openGripper(false);
     else if (ch == 's')    // gripper open
       arm.openGripper(true);
     else if (ch == 'e')    // wrist forwards
       arm.turn(JointID.WRIST, ArmCommunicator.POSITIVE, DELAY); 
     else if (ch == 'd')    // wrist backwards
       arm.turn(JointID.WRIST, ArmCommunicator.NEGATIVE, DELAY);
     else if (ch == 'r')    // elbow forwards
       arm.turn(JointID.ELBOW, ArmCommunicator.POSITIVE, DELAY); 
     else if (ch == 'f')    // elbow backwards
       arm.turn(JointID.ELBOW, ArmCommunicator.NEGATIVE, DELAY);
     else if (ch == 'u')    // shoulder forwards
       arm.turn(JointID.SHOULDER, ArmCommunicator.POSITIVE, DELAY);
     else if (ch == 'j')    // shoulder backwards
       arm.turn(JointID.SHOULDER, ArmCommunicator.NEGATIVE, DELAY);
     else if (ch == 'k')    // base left
       arm.turn(JointID.BASE, ArmCommunicator.NEGATIVE, DELAY); 
     else if (ch == 'i')    // base right
       arm.turn(JointID.BASE, ArmCommunicator.POSITIVE, DELAY);
     else if (ch == 'l')    // light on
       arm.setLight(true);
     else if (ch == 'p')    // light off
       arm.setLight(false);
     else
       System.out.println("Unknown command: " + ch);
  }  // end of doArmOp() 

}  // end of ArmCommunicator class

class SampleListener extends Listener {

	ArmCommunicator arm;

	public void onConnect(Controller controller) {
		arm = new ArmCommunicator();
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
    }

    public void onFrame(Controller controller) {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int flag=0;
    	int isopen=1;
    	boolean isclosed=false;
    	  Frame frame = controller.frame();
    	 int x1,x2,y1,y2,z1,z2;
    	 x1=0;
    	 x2=0;
    	 y1=0;
    	 y2=0;
    	 z1=0;
    	 z2=0;
    	 
    	 
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
    		    for(Bone.Type boneType : Bone.Type.values()) {
    		        Bone bone = finger.bone(boneType);
    		     if(bone.type()==Bone.Type.TYPE_DISTAL)
    		     {
    		     //  System.out.println("finger:"+finger.type());
    		       x1=(int)bone.center().getX();
    		       y1=(int)bone.center().getY();
    		       z1=(int)bone.center().getZ();
    		  //  System.out.println(" "+x1+" "+y1+" "+z1);
    		     }
    		        
    		    }
    		    
    		  } 
    		  
    		  if(finger.type()==Finger.Type.TYPE_THUMB)
    		  {
    		    for(Bone.Type boneType : Bone.Type.values()) {
    		        Bone bone = finger.bone(boneType);
    		     if(bone.type()==Bone.Type.TYPE_DISTAL)
    		     {
    		   //  System.out.println("finger:"+finger.type());
    		     x2=(int)bone.center().getX();
  		         y2=(int)bone.center().getY();
  		         z2=(int)bone.center().getZ();
  		    System.out.println(" "+x2+" "+y2+" "+z2);
    		       
    		
    		   
    		    int pinch=(int)hand.pinchStrength();
    		  System.out.println(pinch);
    		    
    	  if(pinch<1)
    	  {  
    		 if(isopen==0)
    		 {
    		  System.out.println("opening gripper");  
    		 isopen=1;
    		 }
    		 else
    		 {
    			 System.out.println("already opened");   
    			 
    		 }
    		 
    		  
    	  }
    	  
    	  else if(pinch>0.8)
    	  {
    		  if(isopen==1)
    		  {
    		  System.out.println("closing gripper");  
    		  isopen=0;
    		  }
    		  else
     		 {
     			 System.out.println("already closed");   
     			 
     		 }
    	  }
    		   //               
    		     }
    		        
    		    }
    		    
    		  } 
    		  
    		}
    	  
    }
}
