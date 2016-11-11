package visualservoing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Tracking {
	
	  private CascadeClassifier face_cascade;  
	  private BufferedImage image;  
	  
	  public Tracking(){  
	         // face_cascade=new CascadeClassifier("./cascades/lbpcascade_frontalface_alt.xml");  
	         //..didn't have not much luck with the lbp
		  System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
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
	
	  
	  
	  public BufferedImage matToBufferedImage(Mat matrix) {  
          MatOfByte mb=new MatOfByte();  
          Imgcodecs.imencode(".jpg", matrix, mb);  
          try {  
               this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
          } catch (IOException e) {  
               e.printStackTrace();  
               return null; // Error  
          }  
       return this.image; // Successful  
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
         System.out.println("detecting faces");
         if(faces.toArray().length>0)
         {
       	 System.out.println(String.format("Detected %s faces", faces.toArray().length));  
           
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
