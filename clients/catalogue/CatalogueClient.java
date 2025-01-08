package clients.catalogue;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class CatalogueClient extends Application {
    public static void main(String args[]){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        String stockURL = getParameters().getRaw().size() > 0
                        ? getParameters().getRaw().get(0)
                        : null;
        RemoteMiddleFactory remoteMiddleFactory = new RemoteMiddleFactory();
        remoteMiddleFactory.setStockRInfo(stockURL);
        displayGUI(remoteMiddleFactory, primaryStage);
    }

    private static void displayGUI(RemoteMiddleFactory remoteMiddleFactory, Stage window) {
        window.setTitle("Product Catalogue javafx");
        CatalogueModel model = new CatalogueModel(remoteMiddleFactory);
        CatalogueView view  = new CatalogueView(window, remoteMiddleFactory, 0,0);
        CatalogueController cont = new CatalogueController(model, view);
        view.setController(cont);
        model.addObserver(view);
    }
}
