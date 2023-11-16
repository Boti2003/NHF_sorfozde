import java.io.Serializable;

public class MatElement implements Serializable {
    private Material mat;
    private double quantity;

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public MatElement(Material mat, double quantity) {
        this.mat = mat;
        this.quantity = quantity;
    }

    public Material getMat() {
        return mat;
    }

    public double getQuantity() {
        return quantity;
    }
}
