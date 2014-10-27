
import model.PetrukState;
import ui.PetrukUI;

class PetrukApp {

   public static void main(String[] args) {
     try {
       String vers = System.getProperty("java.version");
       if (vers.compareTo("1.1.2") < 0) {
         System.out.println("!!!WARNING: Swing must be run with a " +
                            "1.1.2 or higher version VM!!!");
       }

       PetrukUI petrukUI = new PetrukUI(PetrukState.GetInstance());
       petrukUI.setVisible(true);
       } catch (Throwable t) {
         System.out.println("uncaught exception: " + t);
         t.printStackTrace();
       }
   }
}
