package clients.catalogue;

import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockReader;
import java.util.Observable;

public class CatalogueModel extends Observable {

    private StockReader stock = null;
    private CustomerBridge customerBridge = null;

    public CatalogueModel(MiddleFactory middleFactory) {
        try {
            stock = middleFactory.makeStockReader();
        } catch (Exception e) {
            DEBUG.error("Creation of catalogue model " + e.getMessage());
        }
    }
    public void setCustomerBridge(CustomerBridge customerBridge) {
        this.customerBridge = customerBridge;
    }
    public void addToCart(String productNumber) {
        try {
            if (stock.exists(productNumber)) {
                Product product = stock.getDetails(productNumber);
                if(product != null && customerBridge != null) {
                    customerBridge.addProductToCustomerClient(productNumber);
                    String message = product.getDescription() + " added to cart";
                    setChanged();
                    notifyObservers(message);
                }
            }
        } catch (Exception e) {
            DEBUG.error("Cataloguemodel.addToCart "+ e.getMessage());
            setChanged();
            notifyObservers("Error adding product to cart:"+e.getMessage());
        }
    }
}
