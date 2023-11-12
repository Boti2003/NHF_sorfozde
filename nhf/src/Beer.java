import java.io.Serializable;

public class Beer implements Serializable {
    private String name;
    private String style;
    private double popularity;
    private double alcohol;

    public Beer(String name, String style, double popularity, double alcohol) {
        this.name = name;
        this.style = style;
        this.popularity = popularity;
        this.alcohol = alcohol;
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
