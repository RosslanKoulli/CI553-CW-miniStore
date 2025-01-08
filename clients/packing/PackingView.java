package clients.packing;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;


import java.util.Observable;
import java.util.Observer;
//NEW Switch from swing to javafx
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
/**
 * Implements the Packing view.

 */

public class PackingView implements Observer
{
  private static final String PACKED = "Packed";

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final Label      pageTitle  = new Label();
  private final Label      theAction  = new Label();
  private final TextArea   theOutput  = new TextArea();
  private final Button     theBtPack= new Button( PACKED );
 
  private OrderProcessing theOrder     = null;

  private PackingController cont= null;

  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public PackingView(  Stage stage, MiddleFactory mf, int x, int y )
  {
    try                                           // 
    {      
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    // Setting up the layout
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10));
    grid.setHgap(10);
    grid.setVgap(10);

    // Setting up the Ui components
    pageTitle.setText( "Packing Bought Order" );                        
    grid.add( pageTitle,1,0,2,1 );

    theBtPack.setOnAction(e -> cont.doPacked());
    grid.add(theBtPack, 0,1);

    grid.add(theAction,1,1,2,1);

    theOutput.setPrefRowCount(15);
    theOutput.setEditable(false);
    grid.add(theOutput,1,2,2,1);

    Scene scene = new Scene(grid,W,H);
    stage.setScene(scene);
    stage.setX(x);
    stage.setY(y);
    stage.show();
  }
  
  public void setController( PackingController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )
  {
	  PackingModel model  = (PackingModel) modelC;
    String        message = (String) arg;

    Platform.runLater( () -> {
      theAction.setText( message );

      Basket basket =  model.getBasket();
      if ( basket != null )
      {
        theOutput.setText( basket.getDetails() );
      } else {
        theOutput.setText("");
      }
    } );
  }

}

