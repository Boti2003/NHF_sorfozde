import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopTable extends AbstractTableModel {


    public List<MatElement> materialCart;
    public int base;
    private ShopFrame frame;

    public ShopTable(List<Material> materials, int base, ShopFrame frame) {
        materialCart = new ArrayList<>();
        int i = 0;
        for (Material mat: materials) {
            materialCart.add(new MatElement(mat, 0));
            i++;
        }
        this.frame = frame;


        this.base = base;
    }

    @Override
    public int getRowCount() {
        return materialCart.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 2) {
            try {
                materialCart.get(rowIndex).setQuantity(Double.parseDouble((String) aValue));
            }
            catch(NumberFormatException ne) {
                JOptionPane.showMessageDialog(frame, "Give a valid number value, please!");
            }

        }
        fireTableCellUpdated(rowIndex, columnIndex);
        fireTableCellUpdated(rowIndex, columnIndex+1);
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
                return String.format("%,d",materialCart.get(rowIndex).getMat().getPrice());
            case 2:
                if(materialCart.get(rowIndex).getQuantity()> 0) return (int)materialCart.get(rowIndex).getQuantity();
                else return "..";
            case 3:
                return String.format("%,d",(int)materialCart.get(rowIndex).getQuantity() * (materialCart.get(rowIndex).getMat().getPrice()/base));
            default: return null;
        }

    }
}
