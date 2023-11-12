import java.io.Serializable;

public class BrewingProcess implements Serializable {
    private Recipe brewingBeer;
    int actualTurn;
    public BrewingProcess(Recipe recipe) {
        brewingBeer = recipe;
        actualTurn = 0;
    }
}
