package clients.backDoor;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

// NEW Changing the code from swing to javafx
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * The standalone BackDoor Client
 */


public class BackDoorClient extends Application
{
   public static void main (String args[]){
       launch(args);
   }

   @Override
   public void start (Stage primaryStage) {
     String stockURL = getParameters().getRaw().size() < 1     // URL of stock RW
                     ? Names.STOCK_RW      //  default  location
                     : getParameters().getRaw().get(0);            //  supplied location
     String orderURL = getParameters().getRaw().size()< 2     // URL of order
                     ? Names.ORDER         //  default  location
                     : getParameters().getRaw().get(1);            //  supplied location
     
    RemoteMiddleFactory remoteMiddleFactory = new RemoteMiddleFactory();
    remoteMiddleFactory.setStockRWInfo( stockURL );
    remoteMiddleFactory.setOrderInfo  ( orderURL );        //
    displayGUI(remoteMiddleFactory, primaryStage);                       // Create GUI
  }
  
  private static void displayGUI(MiddleFactory mf, Stage window)
  {     

    window.setTitle( "BackDoor Client (MVC RMI)");

    BackDoorModel      model = new BackDoorModel(mf);
    BackDoorView       view  = new BackDoorView( window, mf, 0, 0 );
    BackDoorController cont  = new BackDoorController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model - view is observer, model is Observable
  }
}
