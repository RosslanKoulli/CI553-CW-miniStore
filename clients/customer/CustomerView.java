package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import clients.Picture;
import javafx.embed.swing.SwingFXUtils;
import middle.MiddleFactory;
import middle.StockReader;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

// NEW Changing the gui from javafx to swing.
// NEW Imports needed, unnecessary code will be commented out
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.awt.Graphics2D;
/**
 * Implements the Customer view.
 */

public class CustomerView implements Observer
{

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  /**
   * NEW Adding the new javafx version
   */
  private final Label pageTitle = new Label();
  private final Label theAction = new Label();
  private final TextField theInput = new TextField();
  private final TextArea theOutput = new TextArea();
  private final Button theBtCheck = new Button("Check");
  private final Button theBtClear = new Button("Clear");

  private final ImageView thePicture = new ImageView();
  private StockReader theStock = null;
  private CustomerController cont = null;

  /**
   * Construct the view
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */

  // NEW Changing customer view so that it can work with javafx
  public CustomerView(Stage stage, MiddleFactory middleFactory, int x, int y ){
    try {
      theStock = middleFactory.makeStockReader();
    } catch(Exception e) {
      System.out.println("Customer view exception caught: "+ e);
    }

    // Setting up the layout
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setHgap(10);
    grid.setVgap(10);

    //Setting up the ui components
    pageTitle.setText("Search products: ");
    grid.add(pageTitle, 1,0, 2,1);

    theBtCheck.setOnAction(e -> cont.doCheck(theInput.getText()));
    grid.add(theBtCheck, 0, 1);

    theBtClear.setOnAction(e -> cont.doClear());
    grid.add(theBtClear, 0, 2);
    grid.add(theAction, 1, 1,2,1);
    grid.add(theInput, 1, 2,2,1);

    theOutput.setPrefRowCount(10);
    theOutput.setEditable(false);
    grid.add(theOutput, 1, 3,2,1);
    thePicture.setFitWidth(80);
    thePicture.setFitHeight(80);
    grid.add(thePicture, 0,3);

    Scene scene = new Scene(grid, W, H);
    stage.setScene(scene);
    stage.setTitle("Customer client javaFX ");
    stage.setX(x);
    stage.setY(y);
    stage.show();
    theInput.requestFocus();
  }

  @Override
  public void update(Observable modelC, Object arg){
    CustomerModel model = (CustomerModel) modelC;
    String message = (String) arg;

    // We will be using Platform.runLater becuase the UI will be update from a different thread
    Platform.runLater(() -> {
      theAction.setText(message);
      if (model.getPicture() == null){
        thePicture.setImage(null);
      }else {
        // Here wwe will convert from ImageIcon to javaFx image
        BufferedImage image = new BufferedImage(
                model.getPicture().getIconWidth(),
                model.getPicture().getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D graphics = image.createGraphics();
        model.getPicture().paintIcon(null, graphics, 0, 0);
        graphics.dispose();
        javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(image, null);
        thePicture.setImage(fxImage);
      }
      theOutput.setText(model.getBasket().getDetails());
      theInput.requestFocus();
    });
  }

  public void setController( CustomerController c )
  {
    cont = c;
  }


}
