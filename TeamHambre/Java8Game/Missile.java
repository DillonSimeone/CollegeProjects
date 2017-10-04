import java.awt.Dimension;
import java.awt.Toolkit;

public class Missile extends Sprite
{
   private final int MISSILE_SPEED = 4;
   
   //This sets teh size of the screen.
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   int boardWidth = (int)screenSize.getWidth();
   int player;
   
   public Missile(int x, int y, int player)
   {
      super(x, y);
      this.player = player;
      initMissile();
   }
   
   private void initMissile()
   {
      if(player == 1)
         loadImage("banana.gif");
      else
         loadImage("egg.gif");
         
      getImageDimensions();
   }
   
   public void move()
   {
      x += MISSILE_SPEED;
      
      if(x > boardWidth)
         vis = false;
   }
}