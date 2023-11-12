import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {
    private int price;
    private String name;
    private String style;
    private double popularity;
    private double alcohol;
    private int brewTurn;

    Map<String, HashMap<String, MatElement>> materials = new HashMap<String,HashMap<String,MatElement>>();



    public Recipe(String name, String style, int price, double alcohol, double popularity, int brewTurn) {
        this.price = price;
        this.name = name;
        this.style = style;
        this.popularity = popularity;
        this.brewTurn = brewTurn;
        this.alcohol = alcohol;
        for(int i = 0; i < Data.keys.length; i++) {
            materials.put(Data.keys[i], new HashMap<String, MatElement>());
        }
    }

    public int getBrewTurn() {
        return brewTurn;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getAlcohol() {
        return alcohol;
    }
}
