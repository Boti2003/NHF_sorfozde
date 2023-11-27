import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Pub implements Serializable {
    private String name;
    private int minAmount;
    private int maxAmount;
    private List<String> types;
    private int basePrice;
    private double minPopularity;

    public Pub(String name, int minAmount, int maxAmount, List<String> types, int basePrice, double minPopularity) {
        this.name = name;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.types = types;
        this.basePrice = basePrice;
        this.minPopularity = minPopularity;
    }

    public String getName() {
        return name;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public List<String> getTypes() {
        return types;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public double getMinPopularity() {
        return minPopularity;
    }
}
