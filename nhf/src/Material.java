import java.io.Serializable;

public abstract class Material implements Serializable {
    protected String unit;
    protected String name;
    protected int price;

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
}
