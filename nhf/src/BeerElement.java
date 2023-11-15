import java.io.Serializable;

public class BeerElement implements Serializable {
    int quantity;
    Beer beer;

    public BeerElement(int quantity, Beer beer) {
        this.quantity = quantity;
        this.beer = beer;
    }

    public int getQuantity() {
        return quantity;
    }

    public Beer getBeer() {
        return beer;
    }
}
