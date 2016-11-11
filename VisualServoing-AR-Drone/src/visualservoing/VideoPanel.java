package visualservoing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class VideoPanel extends JPanel{
	private BufferedImage image; 
	 public VideoPanel(){  
         super();   
    }  
  public void setImage(BufferedImage rawImage)
  {
	  image=rawImage;
  }
	 public void paintComponent(Graphics g){  
         super.paintComponent(g);   
         if (this.image==null) return;         
          g.drawImage(this.image,10,10,this.image.getWidth(),this.image.getHeight(), null);
    }
}
