package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
//NEW changing it from swing to javaFx

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 * View of the model 
 */
public class CashierView implements Observer
{
  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels
  
  private static final String CHECK  = "Check";
  private static final String BUY    = "Buy";
  private static final String BOUGHT = "Bought/Pay";

  private final Label      pageTitle  = new Label();
  private final Label      theAction  = new Label();
  private final TextField  theInput   = new TextField();
  private final TextArea   theOutput  = new TextArea();
  private final Button     theBtCheck = new Button( CHECK );
  private final Button     theBtBuy   = new Button( BUY );
  private final Button     theBtBought= new Button( BOUGHT );

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private CashierController cont       = null;
  
  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-coordinate of position of window on screen 
   * @param y     y-coordinate of position of window on screen  
   */
          
  public CashierView(  Stage stage,  MiddleFactory mf, int x, int y  )
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }

    // NEW Setting up the layout
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10));
    grid.setHgap(10);
    grid.setVgap(10);

    // NEW Setting up the Ui components
    pageTitle.setText("Thank You for Shopping at MiniStrore ");
    grid.add(pageTitle, 1,0, 2,1);

    grid.add(theBtCheck, 0, 1);
    theBtCheck.setOnAction(e -> cont.doCheck(theInput.getText()));

    grid.add(theBtBuy, 0, 2);
    theBtBuy.setOnAction(e ->cont.doBuy());

    grid.add(theBtBought, 0, 4);
    theBtBought.setOnAction(e -> cont.doBought());

    grid.add(theAction, 1, 1,2,1);
    grid.add(theInput,1,2,2,1);

    theOutput.setPrefRowCount(10);
    theOutput.setEditable(false);
    grid.add(theOutput,1,3,2,2);

    Scene scene = new Scene(grid,W,H);
    stage.setScene(scene);
    stage.setX(x);
    stage.setY(y);
    stage.show();

    theInput.requestFocus();

  }

  /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CashierController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg ) {
    CashierModel model = (CashierModel) modelC;
    String message = (String) arg;
    Platform.runLater(() -> {
      theAction.setText(message);

      Basket basket = model.getBasket();
      if (basket == null) {
        theOutput.setText("Customers order");
      }else {
        theOutput.setText(basket.getDetails());
      }
      theInput.requestFocus();               // Focus is here
    });

  }
}
