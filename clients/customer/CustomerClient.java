package clients.customer;

import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import javafx.application.Application;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javafx.stage.Stage;
import javax.swing.*;
import static clients.packing.PackingClient.displayGUI;


/**
 * The standalone Customer Client
 */
//NEW This is the new customer client
public class CustomerClient extends Application{
  @Override
  public void start(Stage primaryStage){
    String stockURL = getParameters().getRaw().size() < 1
            ? Names.STOCK_R
            : getParameters().getRaw().get(0);

    RemoteMiddleFactory remoteMiddleFactory = new RemoteMiddleFactory();
    remoteMiddleFactory.setStockRInfo(stockURL);
    displayGUI(remoteMiddleFactory, primaryStage);
  }
  private void displayGUI(MiddleFactory middleFactory,Stage primaryStage){
    CustomerModel model = new CustomerModel(middleFactory);
    CustomerView view = new CustomerView(primaryStage, middleFactory, 0,0);
    CustomerController controller = new CustomerController(model, view);
    view.setController(controller);
    model.addObserver(view);
  }
  public static void main(String[] args) {
    launch(args);
  }

}

