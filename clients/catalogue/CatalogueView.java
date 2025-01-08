package clients.catalogue;

import catalogue.Product;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.StockReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javafx.embed.swing.SwingFXUtils;
import javax.swing.ImageIcon;
import javafx.scene.layout.VBox;

public class CatalogueView implements Observer {
    private static final int H = 900;
    private static final int W = 600;

    private final Label pageTitle = new Label();
    private final GridPane gridProducts = new GridPane();
    private StockReader stock = null;
    private CatalogueController cont = null;
    private ArrayList<Product> products = new ArrayList<>();

    public CatalogueView(Stage stage, MiddleFactory middleFactory, int x, int y) {
        try {
            stock = middleFactory.makeStockReader();
        } catch (Exception e){
            System.out.println("Error creating stock reader "+ e.getMessage());
        }

        // Setting up the layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        pageTitle.setText("Catalogue with available products");
        pageTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Verdana;");
        mainLayout.getChildren().add(pageTitle);

        gridProducts.setHgap(20);
        gridProducts.setVgap(20);
        mainLayout.getChildren().add(gridProducts);

        loadProducts();

        Scene scene = new Scene(mainLayout, W, H);
        stage.setScene(scene);
        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    private void loadProducts(){
        gridProducts.getChildren().clear();
        int row = 0;
        int column = 0;

        try {
            for (String productID: new String []{"0001", "0002", "0003", "0004", "0005", "0006", "0007"}){
                if(stock.exists(productID)){
                    Product product = stock.getDetails(productID);
                    products.add(product);

                    VBox productCard = createProductCard(product);
                    gridProducts.add(productCard,column,row);

                    column++;
                    if (column > 2){
                        column = 0;
                        row++;
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error loading products "+ e.getMessage());
        }
    }

    private VBox createProductCard(Product product){
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-padding: 10;");
        try  {
            ImageIcon icon = stock.getImage(product.getProductNum());
            if (icon != null){
                BufferedImage bufferedImage = new BufferedImage(
                        icon.getIconWidth(),
                        icon.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics1 = bufferedImage.createGraphics();
                icon.paintIcon(null, graphics1, 0,0);
                graphics1.dispose();

                Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageView imageView = new ImageView(fxImage);
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                card.getChildren().add(imageView);
            }
        }catch(Exception e){
            System.out.println("Error creating product card "+ e.getMessage());
        }
        Label nameLabel = new Label(product.getDescription());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label priceLabel = new Label(String.format("%.2f", product.getPrice()));
        Label stockLabel = new Label("In stock; " + product.getQuantity());

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            if(cont != null){
                cont.doAddToCart(product.getProductNum());
            }
        });
        card.getChildren().addAll(nameLabel,priceLabel,stockLabel,addButton);
        return card;
    }
    public void setController(CatalogueController controller){
        cont = controller;
    }

    @Override
    public void update(Observable modelc, Object arg) {
        CatalogueModel model = (CatalogueModel) modelc;
        Platform.runLater(() -> {
            loadProducts();
        });

    }
}
