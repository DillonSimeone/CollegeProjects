import java.awt.EventQueue;
import javax.swing.JFrame;

public class TeamHambre  extends JFrame
{
   
   public TeamHambre()
   {
      initUI();
   }
   
   private void initUI()
   {
      add(new Board());
      
      setResizable(false);
      pack();
      
      setTitle("HAMBRE!!!!");
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable() //This ensures that everything won't run on a single thread.
      {
         @Override
         public void run()
         {
            TeamHambre Hambre = new TeamHambre(); //Starts up the game!
            Hambre.setVisible(true); //Makes the game visible!
         }
      });
   }
}