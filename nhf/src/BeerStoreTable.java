import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BeerStoreTable extends AbstractTableModel {


    List<BeerElement> beers;
    public BeerStoreTable(List<BeerElement> beers) {
        this.beers = new ArrayList<>();
        for(BeerElement beere : beers) {
            if(beere.getQuantity() > 0) {
                this.beers.add(beere);
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        String[] columnames = {"Name", "Style", "Popularity", "Alcohol", "Quantity"};
        return columnames[column];
    }

    @Override
    public int getRowCount() {


        return beers.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
            /*switch (columnIndex) {
                case 0:
                    return materials.get(rowIndex).getMat().getName();
                case 1:
                    return materials.get(rowIndex).getQuantity() + " " + materials.get(rowIndex).getMat().getUnit();
                default: return null;
            }*/
        return null;
    }
}
