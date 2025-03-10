package clients;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.cashier.CashierController;
import clients.cashier.CashierModel;
import clients.cashier.CashierView;
import clients.catalogue.CatalogueController;
import clients.catalogue.CatalogueModel;
import clients.catalogue.CatalogueView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import clients.packing.PackingController;
import clients.packing.PackingModel;
import clients.packing.PackingView;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import java.awt.*;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Starts all the clients (user interface)  as a single application.
 * Good for testing the system using a single application.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Shine University of Brighton
 * @version year-2024
 */


// NEW Adjusting the Main so that it works with javaFX instead of swing
public class Main extends Application
{
  public static void main (String args[])
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    begin();
  }
  /**
   * Starts the system (Non distributed)
   */

  public void begin()
  {
    //DEBUG.set(true); /* Lots of debug info */
    MiddleFactory mlf = new LocalMiddleFactory();  // Direct access
    startCustomerGUI_MVC( mlf );
    startCashierGUI_MVC( mlf );
    startPackingGUI_MVC( mlf );
    startBackDoorGUI_MVC( mlf );
    //NEW 2 Added the catalogue
    startCatalogueGUI_MVC( mlf);
  }
  
  /**
  * start the Customer client, -search product
  * @param mlf A factory to create objects to access the stock list
  */
  public void startCustomerGUI_MVC(MiddleFactory mlf )
  {
    //NEW Changing from swing to javaFX
    Stage  window = new Stage();
    window.setTitle( "Customer Client MVC");
    Dimension pos = PosOnScrn.getPos();
    
    CustomerModel model      = new CustomerModel(mlf);
    CustomerView view        = new CustomerView( window, mlf, pos.width, pos.height );
    CustomerController cont  = new CustomerController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model, ---view is observer, model is Observable

  }

  /**
   * start the cashier client - customer check stock, buy product
   * @param mlf A factory to create objects to access the stock list
   */
  public void startCashierGUI_MVC(MiddleFactory mlf )
  {
    Stage  window = new Stage();
    window.setTitle( "Cashier Client MVC");
    Dimension pos = PosOnScrn.getPos();
    
    CashierModel model      = new CashierModel(mlf);
    CashierView view        = new CashierView(window, mlf, pos.width, pos.height );
    CashierController cont  = new CashierController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    model.askForUpdate();            // Initial display
  }

  /**
   * start the Packing client - for warehouse staff to pack the bought order for customer, one order at a time
   * @param mlf A factory to create objects to access the stock list
   */
  
  public void startPackingGUI_MVC(MiddleFactory mlf)
  {
    Stage  window = new Stage();
    window.setTitle( "Packing Client MVC");
    Dimension pos = PosOnScrn.getPos();
    
    PackingModel model      = new PackingModel(mlf);
    PackingView view        = new PackingView( window, mlf, pos.width, pos.height );
    PackingController cont  = new PackingController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model

  }
  
  /**
   * start the BackDoor client - store staff to check and update stock
   * @param mlf A factory to create objects to access the stock list
   */
  public void startBackDoorGUI_MVC(MiddleFactory mlf )
  {
    Stage window = new Stage();

    window.setTitle( "BackDoor Client MVC");
    Dimension pos = PosOnScrn.getPos();
    
    BackDoorModel model      = new BackDoorModel(mlf);
    BackDoorView view        = new BackDoorView( window, mlf, pos.width, pos.height );
    BackDoorController cont  = new BackDoorController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
  }
  /**
   * NEW 2
   * start the catalogue client - store items and user can pick and add them
   * @param mlf A factory to create objects to access the stock list
   */
  public void startCatalogueGUI_MVC(MiddleFactory mlf ){
    Stage  window = new Stage();
    CatalogueModel model = new CatalogueModel(mlf);
    CatalogueView view = new CatalogueView(window, mlf, 0, 0);
    CatalogueController cont  = new CatalogueController( model, view );
    view.setController( cont );
    model.addObserver( view );
  }
  
}
