import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopTable extends AbstractTableModel {


    public static List<MatElement> materialCart;
    private int base;

    public ShopTable(List<Material> materials, int base) {
        materialCart = new ArrayList<>();
        int i = 0;
        for (Material mat: materials) {
            materialCart.add(new MatElement(mat, 0));
            System.out.println("From shop:" + materialCart.get(i).getMat().getName());
            i++;
        }
        System.out.println(materialCart.size());

        this.base = base;
    }

    @Override
    public int getRowCount() {
        return materialCart.size();
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0: return "Name";
            case 1: return "Price (/" + base + " " +materialCart.get(0).getMat().getUnit() + ")";
            case 2: return "Amount to buy (" + materialCart.get(0).getMat().getUnit() +")";
            case 3: return "Total price";
            default: return null;
        }
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return materialCart.get(rowIndex).getMat().getName();
            case 1:
                return materialCart.get(rowIndex).getMat().getPrice();
            case 2:
                return "..";
            case 3:
                return materialCart.get(rowIndex).getQuantity() * (materialCart.get(rowIndex).getMat().getPrice()/base);
            default: return null;
        }

    }
}
