import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PData implements Serializable {
    String gameName;
    int turn;
    int money;
    int reputation;

    Map<String, HashMap<String, MatElement>> materials = new HashMap<String,HashMap<String, MatElement>>();
    Map<String, Recipe> recipes = new HashMap<String, Recipe>();
    Map<String, BeerElement> beers = new HashMap<String, BeerElement>();
    List<BrewingTool> tools = new ArrayList<BrewingTool>();

    public PData(String gameName, int turn, int money, int reputation) {
        this.gameName = gameName;
        this.turn = turn;
        this.money = money;
        this.reputation = reputation;
    }
}
