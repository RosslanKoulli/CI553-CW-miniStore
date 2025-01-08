package clients.catalogue;

public class CatalogueController {
    private CatalogueModel model = null;
    private CatalogueView view = null;

    public CatalogueController(CatalogueModel model, CatalogueView view) {
        this.model = model;
        this.view = view;
    }

    public void doAddToCart(String productNumber){
        model.addToCart(productNumber);
    }
}
