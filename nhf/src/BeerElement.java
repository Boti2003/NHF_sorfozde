import java.io.Serializable;

public class BeerElement implements Serializable {
    private int quantity;
    private Beer beer;

    public BeerElement(int quantity, Beer beer) {
        this.quantity = quantity;
        this.beer = beer;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Beer getBeer() {
        return beer;
    }
}
