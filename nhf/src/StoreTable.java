import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StoreTable extends AbstractTableModel {


    List<MatElement> materials;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex == 0) {
            return "kutyus";
        }
        return null;
    }
}
