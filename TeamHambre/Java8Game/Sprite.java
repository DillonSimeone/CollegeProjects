import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;

/**
*  The sprite maker for the game! Such amaze, such simple!
*  @author Dillon Simeone
*/

public class Sprite
{
   int x, y, width, height;
   boolean vis;
   Image image;
   
   /**
   *  The Sprite object! Such image!
   *  @parms int x The location on the X-axis, int y The location on the Y-axis. 
   */
   public Sprite(int x, int y)
   {
      this.x = x;
      this.y = y;
      vis = true; //When this is set to false, the board class will detect this and remove objects with this attribute. 
   }
   
   /**
   *  This returns a image's dimensions to determine the sprite using the image's size. It makes the code very flexiable! The hitbox function also calls on this to rezise itself perfectly around the image!
   */
   public void getImageDimensions()
   {
      width = image.getWidth(null);
      height = image.getHeight(null);
   }
   
   /**
   *  As it says on the tin, load up a path image into this and it'll make the image
   *  for the getImageDimensions() function!
   *
   *  ImageIcon is the only Javax.Swing-releated bit of code here!
   *
   *  @parms String imageName The path to the image!
   */
   public void loadImage(String imageName)
   {
      ImageIcon ii = new ImageIcon(imageName);
      image = ii.getImage();
   }
   
   /**
   *  Returns the sprite's current image!
   *  @return Image image The current image!
   */
   public Image getImage()
   {
      return image;
   }
   
   /**
   *  Returns the sprite's location on the X-axis!
   *  @return int x Location on X-axis.
   */
   public int getX()
   {
      return x;
   }
   
   /**
   *  Returns the sprite's location on the Y-axis!
   *  @return int y Location on Y-axis.
   */
   public int getY()
   {
      return y;
   }
   
   /**
   * Returns the sprite's width!
   * @return int width The width in pixels!
   */
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }
   
   /**
   * Determine whatever to show the sprite or not.
   * @return vis This is used by other classes to determine whatever the object with this attribute needs to be removed or not.
   */
   public boolean isVis()
   {
      return vis;
   }
   
   /**
   *  Set the sprite as visible or not.
   *  @parms Boolean visible False sets the sprite to nonbisible, true does the opposite!
   */
   public void setVis(Boolean visible)
   {
      vis = visible;
   }
   
   /**
   *  Makes a width*height sized rectangle ready to be spawned at x, y!
   *  This is the hitbox.
   *  This is used by both the player character and the nonplayer characters.
   */
   public Rectangle getBounds()
   {
      return new Rectangle(x, y, width, height);
   }
}