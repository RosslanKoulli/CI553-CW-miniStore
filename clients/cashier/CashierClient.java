package clients.cashier;

import catalogue.*;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;
// NEW Changing from swing to javafx
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The standalone Cashier Client.
 */


public class CashierClient extends Application
{
   public static void main (String args[])
   {
       launch(args);
   }

  @Override
  public void start(Stage primaryStage) {
       String stockURL = getParameters().getRaw().size() < 1
               ?Names.STOCK_RW
               : getParameters().getRaw().get(0);
       String orderURL = getParameters().getRaw().size() < 2
               ? Names.ORDER
               : getParameters().getRaw().get(1);
       RemoteMiddleFactory remoteMiddleFactory = new RemoteMiddleFactory();
       remoteMiddleFactory.setStockRWInfo(stockURL);
       remoteMiddleFactory.setOrderInfo(orderURL);
       displayGUI(remoteMiddleFactory,primaryStage);
  }
  private static void displayGUI(MiddleFactory mf, Stage window)
  {     

    window.setTitle( "Cashier Client (MVC RMI)");

    CashierModel      model = new CashierModel(mf);
    CashierView       view  = new CashierView(window, mf, 0, 0 );
    CashierController cont  = new CashierController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    model.askForUpdate();
  }
}
