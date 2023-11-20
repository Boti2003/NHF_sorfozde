import java.io.Serializable;

public class BrewingProcess implements Serializable {
    private Recipe brewingBeer;
    private int actualTurn;
    private int quantity;
    public BrewingProcess(Recipe recipe, int quantity) {
        this.quantity = quantity;
        brewingBeer = recipe;
        actualTurn = 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setActualTurn(int actualTurn) {
        this.actualTurn = actualTurn;
    }

    public Recipe getBrewingBeer() {
        return brewingBeer;
    }

    public int getActualTurn() {
        return actualTurn;
    }
}
