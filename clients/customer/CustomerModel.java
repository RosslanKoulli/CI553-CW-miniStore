package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import clients.catalogue.CustomerBridge;
import debug.DEBUG;
import javafx.scene.image.WritableImage;
import middle.*;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
//NEW 2 Adding the customer bridge so catalogue choice has connection with the customer
public class CustomerModel extends Observable implements CustomerBridge
{

  @Override
  public void addProductToCustomerClient(String productNumber) {
    doCheck(productNumber);
    doBuy();
  }
  private Product     theProduct = null;          // Current product
  private Basket      theBasket  = null;          // Bought items
  private String      pn = "";                    // Product being processed
  private StockReadWriter theStock = null;
  //private StockReader     theStock     = null;
  private OrderProcessing theOrder     = null;
  private ImageIcon thePic = null;

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf)
  {
    try                                          // 
    {
      theStock = mf.makeStockReadWriter();
      theOrder = mf.makeOrderProcessing();          // Database access
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    theBasket = makeBasket();                    // Initial Basket
  }
  
  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum )
  {
    theBasket.clear();                          // Clear s. list
    String theAction = "";
    pn  = productNum.trim();                    // Product no.
    int    amount  = 1;                         //  & quantity
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails( pn ); //  Product
        if ( pr.getQuantity() >= amount )       //  In stock?
        { 
          theAction =                           //   Display 
            String.format( "%s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              pr.getQuantity() );               //    quantity
          pr.setQuantity( amount );             //   Require 1
          theBasket.add( pr );                  //   Add to basket

          ImageIcon icon = theStock.getImage(pn);
          thePic = theStock.getImage( pn );

        } else {                                //  F
          theAction =                           //   Inform
            pr.getDescription() +               //    product not
            " not in stock" ;                   //    in stock
        }
      } else {                                  // F
        theAction =                             //  Inform Unknown
          "Unknown product number " + pn;       //  product number
      }
    } catch( StockException e )
    {
      DEBUG.error("CustomerClient.doCheck()\n%s",
      e.getMessage() );
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Clear the products from the basket
   */
  public void doClear()
  {
    String theAction = "";
    theBasket.clear();                        // Clear s. list
    theAction = "Enter Product Number";       // Set display
    thePic = null;                            // No picture
    setChanged(); notifyObservers(theAction);
  }
  
  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */ 
  public ImageIcon getPicture()
  {
    return thePic;
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   *
   */
  public void doBuy(){
    String action = "";
    try {
      if (theBasket != null && theBasket.size() > 1) {
        Product product1 = theBasket.get( 0 );
        boolean stockBought = theStock.buyStock(product1.getProductNum(),product1.getQuantity());
        if (stockBought) {
          action = "Added "+ product1.getProductNum();
        }else{
          action = "Not available in stock";
        }
      }else {
        action = "No product has been selected";
      }
    }catch (StockException e) {
      DEBUG.error("CustomerModel.doBuy", e.getMessage());
      action = e.getMessage();
    }
    setChanged(); notifyObservers(action);
  }
  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new Basket();
  }
}

