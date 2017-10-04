import java.awt.event.*;
import java.util.*;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
*  This is the class that pulls together the spaceship! Or whatever.
*  @author Dillon Simeone
*/

public class Craft extends Sprite
{
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   int boardWidth = (int)screenSize.getWidth();
   int boardHeight = (int)screenSize.getHeight() - 100;
   int dx, dy, player;
   ArrayList<Missile> missiles;
   
   /**
   *  Since this class extands the Sprite class, using super here passes x and y to Sprite's default con.!
   *  @parm int x, int y The spaceship's postion on the x and y axis!
   */
   public Craft(int x, int y, int player)
   {
      super(x,y);
      this.player = player;
      
      if(this.player > 2)
         this.player = 2;
         
      if(this.player < 0)
         this.player = 1;
         
      initCraft();
   }
   
   /**
   *  This makes the spaceship from an image in the same folder as the program, and sets the hitbox!
   */
   public void initCraft()
   {
      missiles = new ArrayList<>();
      if(player == 1)
         loadImage("GorillaPlayer.gif");
      else if(player == 2)
         loadImage("ChickenPlayer.gif");
      
      getImageDimensions();
   }
   
   /**
   *  Updates the craft's current postion!
   */
   public void move()
   {
      x += dx;
      y += dy;
      
      if(x < 1)
         x = 1;
      if(x > boardWidth)
         x = boardWidth - 1;
      
      //Note to self: The craft's current postion is measured by the upper-right corner. That is why when it heads to the bottom of the screen, it goes all the way offscreen then suddenly
      //teleports to the top. It's that corner. Gotta to adjust for the height of the craft, and perfect. (Get values from Sprite class.)
      if(y < 0) //The top of the board.
         y = boardHeight - this.getHeight() - 10; //Moves to the bottom of the screen.
      if(y > boardHeight - this.getHeight() ) //The bottom of the screen.
         y = 1; //The bottom of the screen.
   }
   /**
   *  Spawns a new missile that stays in the game until it strikes a enemy spaceship, or goes off the screen!
   */
   public void fire()
   {
      if(this.isVis() == true) //This resolves a bug that would allow defeated players to keep on firing after death!
         missiles.add(new Missile(x + width, y + height / 2, player));
   }
   
   /**
   *  Gunman, how many missiles do we have flying around out there now!?
   */
   public ArrayList<Missile> getMissiles()
   {
      return missiles;
   }
   
   /**
   *  This is what one uses to determine which key have been pressed!
   *  @parm KeyEvent e The pressed key.
   */
   public void keyPressed(KeyEvent e)
   {
      int key = e.getKeyCode();
      if(player == 1)
      {
         if(key == KeyEvent.VK_CONTROL || key == KeyEvent.VK_SPACE)
            fire();
         if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            dx = -4;
         if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            dx = 4;
         if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
            dy = -4;
         if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
            dy = 4;
      }
      else if(player == 2)
      {
         if(key == KeyEvent.VK_NUMPAD0)
            fire();
         if(key == KeyEvent.VK_NUMPAD4)
            dx = -4;
         if(key == KeyEvent.VK_NUMPAD6)
            dx = 4;
         if(key == KeyEvent.VK_NUMPAD8)
            dy = -4;
         if(key == KeyEvent.VK_NUMPAD5)
            dy = 4;
      }
      
      if(key == KeyEvent.VK_ESCAPE)
         System.exit(0);
   }
   
   /**
   *  Without this, once a key is pressed, the spaceship would just keep on moving that direction until the oppositing key is pressed.
   *  @parm KeyEvent e The released Key.
   */
   public void keyReleased(KeyEvent e)
   {
      int key = e.getKeyCode();
      
      if(player == 1)
      {
         if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            dx = 0;
         if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            dx = 0;
         if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
            dy= 0;
         if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
            dy = 0;
      }
      else if(player == 2)
      {
         if(key == KeyEvent.VK_NUMPAD4)
            dx = 0;
         if(key == KeyEvent.VK_NUMPAD6)
            dx = 0;
         if(key == KeyEvent.VK_NUMPAD8)
            dy = 0;
         if(key == KeyEvent.VK_NUMPAD5)
            dy = 0;
      }
   }
}