import java.io.Serializable;

public class MatElement implements Serializable {
    Material mat;
    double quantity;

    public MatElement(Material mat, double quantity) {
        this.mat = mat;
        this.quantity = quantity;
    }
}
