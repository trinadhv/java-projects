package visualservoing;


import java.awt.*;  

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import javax.imageio.ImageIO;  
import javax.swing.*;  
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;  
import org.opencv.core.MatOfByte;  
import org.opencv.core.MatOfRect;  
import org.opencv.core.Point;  
import org.opencv.core.Rect;  
import org.opencv.core.Scalar;  
import org.opencv.core.Size;  
import org.opencv.imgcodecs.*;  
import org.opencv.videoio.*;

import de.yadrone.base.ARDrone;
import de.yadrone.base.video.ImageListener;

import org.opencv.imgproc.Imgproc;  
import org.opencv.objdetect.CascadeClassifier;  

class FacePanel extends JPanel{  
     private static final long serialVersionUID = 1L;  
     private BufferedImage image;  
     // Create a constructor method  
     public FacePanel(){  
          super();   
     }  
     /*  
      * Converts/writes a Mat into a BufferedImage.  
      *   
      * @param matrix Mat of type CV_8UC3 or CV_8UC1  
      * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
      */       
     public boolean matToBufferedImage(Mat matrix) {  
          MatOfByte mb=new MatOfByte();  
          Imgcodecs.imencode(".jpg", matrix, mb);  
          try {  
               this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
          } catch (IOException e) {  
               e.printStackTrace();  
               return false; // Error  
          }  
       return true; // Successful  
     }  
     public void paintComponent(Graphics g){  
          super.paintComponent(g);   
          if (this.image==null) return;         
           g.drawImage(this.image,10,10,this.image.getWidth(),this.image.getHeight(), null);
     }
        
}  
class FaceDetector {  
     private CascadeClassifier face_cascade;  
     // Create a constructor method  
     public FaceDetector(){  
         // face_cascade=new CascadeClassifier("./cascades/lbpcascade_frontalface_alt.xml");  
         //..didn't have not much luck with the lbp
         
        face_cascade=new CascadeClassifier("H://workspace//opencvtest1//bin//opencvtest1//haarcascade_frontalface_alt.xml"); 
          if(face_cascade.empty())  
          {  
               System.out.println("--(!)Error loading A\n");  
                return;  
          }  
          else  
          {  
                     System.out.println("Face Classifier");  
          }  
     }  
     
     public Mat detect(Mat inputframe){  
          Mat mRgba=new Mat();  
          Mat mGrey=new Mat();  
          MatOfRect faces = new MatOfRect();  
          inputframe.copyTo(mRgba);  
          inputframe.copyTo(mGrey);  
          Imgproc.cvtColor( mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);  
          Imgproc.equalizeHist( mGrey, mGrey );  
          face_cascade.detectMultiScale(mGrey, faces);  
          if(faces.toArray().length>0)
          {
        	 // System.out.println(String.format("Detected %s faces", faces.toArray().length));  
            
          }
           
          
          //if 100000 is 30 
          //the 50000 is x x=30*50000/100000
          for(Rect rect:faces.toArray())  
          {  
               Point center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5 );  
              
               
               double area=rect.width*rect.height;
               double distance= (area*30)/100000;
               System.out.println("Position (x,y): ("+rect.x +" , "+rect.y+"), Distance: "+area+" cm");
               Size s = new Size( rect.width*0.5, rect.height*0.5);
               Imgproc.rectangle(mRgba,  //where to draw the box
           		    new Point(rect.x, rect.y),   //bottom left
           		    new Point(rect.x + rect.width, rect.y + rect.height), //top right 
           		    new Scalar(0, 255, 0)); 
          }  
          return mRgba;  
     }  
}  
public class TestTrack {  
    
	public static void main(String arg[]) throws InterruptedException{  
      // Load the native library.  
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
      //or ...     System.loadLibrary("opencv_java244");       

      //make the JFrame
      JFrame frame = new JFrame("Visual Servoing - Experiment : Trinadh Venna");  
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     
      FaceDetector faceDetector=new FaceDetector();  
      FacePanel facePanel = new FacePanel();  
      frame.setSize(640,360); //give the frame some arbitrary size 
      frame.setBackground(Color.BLUE);
      frame.add(facePanel,BorderLayout.CENTER);       
      frame.setVisible(true);       
      
      //Open and Read from the video stream  
       Mat webcam_image=new Mat();  
       VideoCapture webCam =new VideoCapture(0);  
       
       ARDrone drone=new ARDrone();
       drone.start();
       drone.getVideoManager().addImageListener(new ImageListener() {
   	    public void imageUpdated(BufferedImage newImage)
   	    {
   	        BufferedImage image = newImage;
   	        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
   	         Mat webcam_image = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
   	        webcam_image.put(0, 0, pixels);

   	        webcam_image=faceDetector.detect(webcam_image);
            facePanel.matToBufferedImage(webcam_image);  
            facePanel.repaint(); 
   	       
   	        SwingUtilities.invokeLater(new Runnable() {
   	            public void run()
   	            {
   	                facePanel.repaint();
   	            }
   	        }); 
   	       
   	    }
   	});
   
      /*  if( webCam.isOpened())  
          {  
           Thread.sleep(500); /// This one-time delay allows the Webcam to initialize itself  
           while( true )  
           {  
        	 webCam.read(webcam_image);  
             if( !webcam_image.empty() )  
              {   
            	  Thread.sleep(200); /// This delay eases the computational load .. with little performance leakage
                   frame.setSize(webcam_image.width()+40,webcam_image.height()+60);  
                   //Apply the classifier to the captured image  
                   
                    webcam_image=faceDetector.detect(webcam_image);
                   facePanel.matToBufferedImage(webcam_image);  
                   facePanel.repaint(); 
                  //Display the image  
                    
              }  
              else  
              {   
                   System.out.println(" --(!) No captured frame from webcam !");   
                   break;   
              }  
             }  
            }
            */
           webCam.release(); //release the webcam
 
      } //end main 
	
}