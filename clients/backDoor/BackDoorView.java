package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;


import java.util.Observable;
import java.util.Observer;
// NEW Switching the code from swing to javafx
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
 * Implements the Customer view.
 */

public class BackDoorView implements Observer
{
  private static final String RESTOCK  = "Add";
  private static final String CLEAR    = "Clear";
  private static final String QUERY    = "Query";
 
  private static final int H = 400;       // Height of window pixels
  private static final int W = 500;       // Width  of window pixels

  private final Label      pageTitle  = new Label();
  private final Label      theAction  = new Label();
  private final TextField  theInput   = new TextField();
  private final TextField  theInputNo = new TextField();
  private final TextArea   theOutput  = new TextArea();
  private final Button     theBtClear = new Button( CLEAR );
  private final Button     theBtRStock = new Button( RESTOCK );
  private final Button     theBtQuery = new Button( QUERY );

  private StockReadWriter theStock     = null;
  private BackDoorController cont= null;

  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public BackDoorView(  Stage stage, MiddleFactory mf, int x, int y )
  {
    try {
      theStock = mf.makeStockReadWriter();          // Database access
    } catch ( Exception e ) {
      System.out.println("Exception: " + e.getMessage() );
    }

    // Setting up the layout
    GridPane grid = new GridPane();
    grid.setHgap( 10 );
    grid.setVgap( 10 );
    grid.setPadding( new Insets(10) );

    // Setting up the ui components
    pageTitle.setText("Staff check and manage stock");
    grid.add(pageTitle, 1, 0,2,1);

    grid.add(theBtQuery, 0,1);
    theBtQuery.setOnAction(e -> cont.doQuery(theInput.getText()));
    grid.add(theAction,1,1,2,1);

    grid.add(theBtRStock, 0,2);
    theBtRStock.setOnAction(e ->cont.doRStock(theInput.getText(), theInputNo.getText()));
    grid.add(theInput, 1,2);
    grid.add(theInputNo, 2,2);


    grid.add(theBtClear, 0,3);
    theBtClear.setOnAction(e ->cont.doClear());

    theOutput.setPrefRowCount(10);
    theOutput.setEditable(false);
    grid.add(theOutput,1,3,2,2);

    theInput.setPromptText("Product ID");
    theInputNo.setPromptText("Quantity");
    theInputNo.setText("0");


    Scene scene = new Scene(grid,W,H);
    stage.setScene(scene);
    stage.setTitle("Backdoor client javafx mvc");
    stage.setX(x);
    stage.setY(y);
    stage.show();
    theInput.requestFocus();
  }
  
  public void setController( BackDoorController c )
  {
    cont = c;
  }

  /**
   * Update the view, called by notifyObservers(theAction) in model,
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )  
  {
    BackDoorModel model  = (BackDoorModel) modelC;
    String        message = (String) arg;
    Platform.runLater(()->{
      theAction.setText( message );

      theOutput.setText( model.getBasket().getDetails() );
      theInput.requestFocus();

    });
  }

}