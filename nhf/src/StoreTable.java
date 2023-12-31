import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoreTable extends AbstractTableModel {


    List<MatElement> materials;
    public StoreTable(List<MatElement> materials) {
        this.materials = new ArrayList<>();
        for(MatElement mate : materials) {
            if(mate.getQuantity() > 0) {
                this.materials.add(mate);
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        String[] columnames = {"Name", "Quantity"};
        return columnames[column];
    }

    @Override
    public int getRowCount() {


        return materials.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return materials.get(rowIndex).getMat().getName();
                case 1:
                    return materials.get(rowIndex).getQuantity() + " " + materials.get(rowIndex).getMat().getUnit();
                default: return null;
            }
    }
}
