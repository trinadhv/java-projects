
import java.io.IOException;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Bone.Type;
public class Sample {
	
	
	 public static void main(String[] args) {
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
	    }
}


class SampleListener extends Listener {

    private static final Type TYPE_DISTAL = null;
    private static final Type FINGER=null;

	public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
    }

    public void onFrame(Controller controller) {
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
    		   //    System.out.println(" "+x1+" "+y1+" "+z1);
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
  		   //  System.out.println(" "+x2+" "+y2+" "+z2);
    		       
    		   // distance=(int)Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)+Math.pow(z2-z1,2));
    		    System.out.println(hand.pinchStrength());
    		   //               
    		     }
    		        
    		    }
    		    
    		  } 
    		  
    		}
    	  
    }
}