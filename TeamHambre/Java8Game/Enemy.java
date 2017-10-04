import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

public class Enemy extends Sprite
{
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   int boardWidth = (int)screenSize.getWidth();
   int boardHeight = (int)screenSize.getHeight();
   Random rand = new Random();
   int dy;
   
   private int initalX, speed;
   public Enemy(int x, int y, int speed)
   {
      super(x, y);
      initalX = x;
      this.speed = speed;
      initEnemy(rand.nextInt(4));
   }
   
   /**
   *  Makes the enemy.
   *  @parm int type The type of enemy to create.
   */
   private void initEnemy(int type)
   {
      if(type == 0)
         loadImage("warrior.gif");
         //HP = 1...
      if(type == 1)
         loadImage("brainJar.gif");
      if(type == 2)
         loadImage("monster.gif");
      if(type == 3)
         loadImage("MonkeyEnemy.gif");
         //Boss type...
         //Boss's HP...
         
      getImageDimensions(); //This is what resizes the hitbox to the image's size.
   }
   
   public void setDy(int dy)
   {
      this.dy = dy;
   }
   
   /**
   *  If the aliens leaves the screen on the left side... They teleport to the right side! Doom!
   */
   public void move()
   {
      if(speed > 10) //If the enemies reach light speed, the game breaks right away.
            speed = 10;
            
      if(x < 0)
      {
         x = boardWidth;
         speed = speed * (1 + rand.nextInt(speed)); // If the enemy reaches the left side of the screen, the enemy's new speed becomes something between the current doom counter and one!
      }
      
      if(y < 0) //The top of the board.
         y = boardHeight - this.getHeight()* 2 -10; //Moves to the bottom of the screen.
      if(y > boardHeight - this.getHeight() *2) //The bottom of the screen.
         y = 1; //The bottom of the screen.
         
      x -= speed * 2;
      y -= dy; //Note to self: This only applies on creation, not on each update. Change this value on each repaint.
   }
}