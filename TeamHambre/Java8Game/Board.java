import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import java.awt.Image;


/**
*  The board! Interesting thing to note, because of the timer in Java.swingx and Java.Awt, Timer cannot be used if .*; are used for both because both have a timer class!
*  @author Dillon Simeone
*/

public class Board extends JPanel implements ActionListener
{
   Timer timer; //This is used for the game's tick, so the game knows how quickly to update the postion of everything, check the rules and repaint the board.
   Craft craftOne, craftTwo;
   ArrayList<Enemy> enemies;
   boolean ingame;
   final int ICRAFT_X = 40, ICRAFT_Y = 60, DELAY = 15, ENEMY_AMOUNT = 20;
   int doomCounter = 0, murderedEnemiesOne = 0, murderedEnemiesTwo = 0;
   //This sets the size of the game board.
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   int boardWidth = (int)screenSize.getWidth();
   int boardHeight = (int)screenSize.getHeight() - 100;
   
   Random rand = new Random();
   
   
   ImageIcon ii = new ImageIcon("Forest.gif");
   Image bgImage = ii.getImage();
   /**
   *  Returns a image object from a ImageIcon. 
   *  @returns image
   **/
   public Image getImage()
   {
      return bgImage;
   } 
   
   public Board()
   {
      initBoard();
   }
   
   /**
   *  Creates a new board. This is excellent for quickly resetting the game.
   */
   public void initBoard()
   {
      addKeyListener(new TAdapter()); //Better name for this??
      setFocusable(true); //Important for the KeyListeners because key events only go to stuff that have focus.
      ingame= true; //Determines when the player/NPCs/missiles are destroyed.
      
      setPreferredSize(new Dimension(boardWidth, boardHeight)); //The board size.
      
      craftOne = new Craft(ICRAFT_X, ICRAFT_Y*2, 1); //Spawns the player one's avater at those locations.
      craftTwo = new Craft(ICRAFT_X, ICRAFT_Y/2, 2); //Spawns the player two's avater at those locations.
      
      initEnemies(ENEMY_AMOUNT); //Spawn the enemies!
      
      timer = new Timer(DELAY, this); //The game's tick. Lower number means everything moves faster.
      timer.start(); 
   }
   
   /**
   *  Spawns the enemies!
   *  @parms int amount The amount of enemies to spawn!  
   */
   public void initEnemies(int amount)
   {
      doomCounter++; //You know the doom song? Dooooom Dooooom Doooooooooom
      enemies = new ArrayList<>(); //ArrayLists are win.
      
      for(int i = 0; i < amount; i++)
      {
         int xPos = boardWidth + rand.nextInt((boardWidth)); //Spwans the aliens one screen over to the left.
         int yPos = rand.nextInt(boardHeight) - 100; //Limits wher ethe aliens can spawn on the Y-axis. They spawn 100 pixel away from the top and bottom of the screen.
         if(yPos < 100)
            yPos = 100;
            
         enemies.add(new Enemy(xPos, yPos, doomCounter)); //Doom Counter here affects their speeds.
      }
      
   }
   
   /**
   *  Draws the frames, or the gameover screen if the player is not in the game anymore. Call this with repaint().
   */
   @Override
   public void paintComponent(Graphics g)
   {
      this.setDoubleBuffered(true); //Makes it so the graphic will be drawn off screen, and once they're drawn, they appear on the game screen. It was supposed to be enabled in JPanels by default... Setting this to true here seemed to fix the flicking issues.
      super.setDoubleBuffered(true); //I'm not sure if this.set..., or super.set... is making it to run better. I'm tired of programming at the moment... Will figure it out later.
      //Toolkit.getDefaultToolkit().sync(); 
      super.paintComponent(g); //Paints the components. This makes sure the background is painted around the updated compenents, not over them. 
      if(ingame)
      {
         g.drawImage(bgImage,0,0, boardWidth, boardHeight, null);
         drawObjects(g); //Redraws the players, missiles and enemies at their current location.
      }
      else
         drawGameOver(g);
         
      Toolkit.getDefaultToolkit().sync(); //This ensures that the pixels will show up on the screen. Most video cards/whatever don't need this, but there are speical cases. Without this, the images would just get stored into a bufferer, then once the buffer becomes full... Spills onto the screen!
      // See this: https://bugs.openjdk.java.net/browse/JDK-8068529 
   }
   /**
   *  This is used for updating everything's postions in the game.
   *  @parm Graphics g The board. The everything.
   */
   private void drawObjects(Graphics g)
   {
      if(craftOne.isVis()) //Player 1
         g.drawImage(craftOne.getImage(), craftOne.getX(), craftOne.getY(), this);
         
      if(craftTwo.isVis()) //Player 2
         g.drawImage(craftTwo.getImage(), craftTwo.getX(), craftTwo.getY(), this);
         
      ArrayList<Missile> msOne = craftOne.getMissiles();
      ArrayList<Missile> msTwo = craftTwo.getMissiles();
      
      for(Missile m : msOne) //The player one's missiles. (Bananas.)
      {
         if(m.isVis())
            g.drawImage(m.getImage(), m.getX(), m.getY(), this);
      }
      for(Missile m : msTwo) //The player two's missiles. (Eggs.)
      {
         if(m.isVis())
            g.drawImage(m.getImage(), m.getX(), m.getY(), this);
      }
      
      for(Enemy e : enemies)//The enemies.
      {
         if(e.isVis())
            g.drawImage(e.getImage(), e.getX(), e.getY(), this);
      }
      
      g.setColor(Color.WHITE);
      g.drawString("Player One: Enemies murdered " + murderedEnemiesOne, 5, 15); //Enemies killed.
      g.drawString("Player Two: Enemies murdered " + murderedEnemiesTwo, 5, 30); //Enemies killed.
      g.drawString("Enemies left: " + enemies.size(), 5, 45); //Enemies left.
      g.drawString("Doom Counter: " + doomCounter, 5, 60); // Enemy's speed: doomCounter*2.
   }
   
   /**
   *  This is only triggered when both of the players are set to not visible. Game over screen.
   */
   private void drawGameOver(Graphics g)
   {
      ImageIcon ii = new ImageIcon("HambreShot.jpg");
      Image bgImage = ii.getImage();
      g.drawImage(bgImage,0,0, boardWidth, boardHeight, null);
      
      String msg = "Dicks out for Hambre";
      Font small = new Font("Helvetica", Font.BOLD, 40);
      FontMetrics fm = getFontMetrics(small);
      int spacing = 50;
      
      g.setColor(Color.white);
      g.setFont(small); 
      g.drawString(msg, (boardWidth - fm.stringWidth(msg)) / 2, boardHeight / 2);
      if(murderedEnemiesOne == 0)
         g.drawString("Player One: You're a nice guy, huh?", (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing));
      else if(murderedEnemiesOne < 20)
         g.drawString("Player One: Enemies murdered " + murderedEnemiesOne, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing));
      else if(murderedEnemiesOne >= 20 && murderedEnemiesOne < 40)
         g.drawString("Player One: Enemies slaughtered " + murderedEnemiesOne, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing));  
      else if(murderedEnemiesOne >= 40 && murderedEnemiesOne < 60)
         g.drawString("Player One: Enemies butchered " + murderedEnemiesOne, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing));
      else if(murderedEnemiesOne >= 60)
         g.drawString("Player One: You like killing, don't you? Dead Enemies " + murderedEnemiesOne, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing));
         
      if(murderedEnemiesTwo == 0)
         g.drawString("Player Two: You're a nice guy, huh?", (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing*2));
      else if(murderedEnemiesTwo < 20)
         g.drawString("Player Two: Enemies murdered " + murderedEnemiesTwo, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing*2));
      else if(murderedEnemiesTwo >= 20 && murderedEnemiesTwo < 40)
         g.drawString("Player Two: Enemies slaughtered " + murderedEnemiesTwo, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing*2));  
      else if(murderedEnemiesTwo >= 40 && murderedEnemiesTwo < 60)
         g.drawString("Player Two: Enemies butchered " + murderedEnemiesTwo, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing*2));
      else if(murderedEnemiesTwo >= 60)
         g.drawString("Player Two: You like killing, don't you? Dead Enemies " + murderedEnemiesOne, (boardWidth - fm.stringWidth(msg)/2) / 3, ((boardHeight/2)+spacing*2));
         
      if(murderedEnemiesOne > murderedEnemiesTwo)
         g.drawString("Winner: Player 1. You could had saved Hambre if you had gone on a little bit longer!", (boardWidth/2 - fm.stringWidth(msg)) / 3, ((boardHeight/2)+spacing*3));
      else if(murderedEnemiesOne < murderedEnemiesTwo)
         g.drawString("Winner: Player 2. You could had saved Hambre if you had gone on a little bit longer!", (boardWidth/2 - fm.stringWidth(msg)) / 3, ((boardHeight/2)+spacing*3));
      else
         g.drawString("Winner: Everyone! You could had saved Hambre if you had gone on a little bit longer!", (boardWidth/2 - fm.stringWidth(msg)) / 3, ((boardHeight/2)+spacing*3));   }
   /**
   *  This is how the game update the postion of everything.
   */
   @Override
   public void actionPerformed(ActionEvent e)
   {
      inGame();
      
      updateCraft(); //Updates the player's postion based on the player's inputs since the last update.
      updateMissiles(); //Move the missiles forward, and check if they've struck an enemy.
      updateEnemies(); //Move the enemies toward the player's side of the board.
      
      checkCollisions(); //If the player is hit by an alien, game over.
      
      repaint(); //Paints the new frame. This is how the game loops.
   }
   /**
   *  Without this, the game would keep on running after the player dies or the game is won.
   */
   private void inGame()
   {
      if(!ingame)
         timer.stop();
   }
   /**
   *  Just check if the player's craft haven't been set to invisible, which is when it's defeated.
   */
   private void updateCraft()
   {
      if(craftOne.isVis())
         craftOne.move();
      else
      {
         craftOne.loadImage(null); //This ensures that no hitboxes are left behind when a player is set to not vis.
         craftOne.getImageDimensions();
      }
         
      if(craftTwo.isVis())
         craftTwo.move();
      else
      {
         craftTwo.loadImage(null);
         craftTwo.getImageDimensions();
      }
   }
   
   /**
   *  Move the missiles forward and check if they've struck anything.
   */
   private void updateMissiles()
   {
      ArrayList<Missile> msOne = craftOne.getMissiles();
      ArrayList<Missile> msTwo = craftTwo.getMissiles();
      
      for(int i = 0; i < msOne.size(); i++)
      {
         Missile m = msOne.get(i);
         
         if(m.isVis())
            m.move();
         else
         {
            msOne.remove(i);
         }
      }
      
      for(int i = 0; i < msTwo.size(); i++)
      {
         Missile m = msTwo.get(i);
         
         if(m.isVis())
            m.move();
         else
         {
            msTwo.remove(i);
         }
      }
   }
   /**
   *  Move the enemy forward, and check the rules.
   */
   private void updateEnemies()
   {
      if(enemies.isEmpty())
      {
         initEnemies(ENEMY_AMOUNT + (doomCounter * rand.nextInt(ENEMY_AMOUNT))); //The pain train doesn't stop!
        // ingame = false; //They're all defeated, somehow!
        // return;
      }
         
      for(int i = 0; i < enemies.size(); i++) //Foreach loop cannot be used here because that is read-only.
      {  
         // Count-down for the explosion before setting them to vis(false) here?
         // See comment around line 336 for my train of thought.
         if(enemies.get(i).isVis())
         {
            int random = rand.nextInt(4);
            if(random == 0)
               enemies.get(i).setDy(rand.nextInt(doomCounter*3));
            else if(random == 1)
               enemies.get(i).setDy(rand.nextInt(doomCounter*2));
            else if(random == 2)
               enemies.get(i).setDy((rand.nextInt(doomCounter) * -1));
            else
               enemies.get(i).setDy(-1);
               
            enemies.get(i).move(); //Move toward the left side of the board.
         }
         else
         {
            enemies.remove(i); //They've been struck by a missile!
         }
      }
   }
   /**
   *  If the hitbox of the craft and the enemies touches eachother... Both disappears!
   */
   public void checkCollisions()
   {
      Rectangle r3One = craftOne.getBounds();
      Rectangle r3Two = craftTwo.getBounds();
            
      for(Enemy enemy : enemies)
      {
         Rectangle r2 = enemy.getBounds();
         
         if(r3One.intersects(r2))
         {
            craftOne.setVis(false);
            enemy.setVis(false);
            //ingame = false;
         }
         
         if(r3Two.intersects(r2))
         {
            craftTwo.setVis(false);
            enemy.setVis(false);
            //ingame = false;
         }
         
         if((craftOne.isVis() == false) && (craftTwo.isVis() == false))
            ingame = false; //Game over! Everyone's DEAD.
      }
         //If a missile hits an enemy... Both disappears!
      ArrayList<Missile> msOne = craftOne.getMissiles();
      ArrayList<Missile> msTwo = craftTwo.getMissiles();
         
      for(Missile m : msOne)
      {
         Rectangle r1 = m.getBounds();
            
         for(Enemy enemy : enemies)
         {
            Rectangle r2 = enemy.getBounds();
            if(r1.intersects(r2))
            {
               // Possible to change enemy's image to a explosion for a few frames before setting the enemy's vis to false?
               // Use Enemy's super class's function: loadImage("pathToImage")... The new size should be automally resized to match the old image's size.
               // Timer?
               // In the 
               m.setVis(false);
               enemy.setVis(false);
               murderedEnemiesOne++; //Who's going to tell their family what happened?
            }
         }
      }
      
      for(Missile m : msTwo)
      {
         Rectangle r1 = m.getBounds();
            
         for(Enemy enemy : enemies)
         {
            Rectangle r2 = enemy.getBounds();
            if(r1.intersects(r2)) //When the missile hits the enemy
            {
               //Boss.setHP(-1);
               //if(Boss.getHP < 0);
               //   remove boss;
               m.setVis(false);
               enemy.setVis(false);
               murderedEnemiesTwo++; //Who's going to tell their family what happened?
            }
         }
      }
   }
      /**
      *  Detects the player inputs for the craft.
      */
   private class TAdapter extends KeyAdapter
   {
      @Override
         public void keyReleased(KeyEvent e)
      {
         craftOne.keyReleased(e);
         craftTwo.keyReleased(e);
      }
         
      @Override
         public void keyPressed(KeyEvent e)
      {
         craftOne.keyPressed(e);
         craftTwo.keyPressed(e);
      }
   }
      
}