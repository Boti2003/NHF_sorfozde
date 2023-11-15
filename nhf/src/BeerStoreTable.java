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
        String[] columnames = {"Name", "Style", "Alcohol", "Popularity", "Quantity"};
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
            switch (columnIndex) {
                case 0:
                    return beers.get(rowIndex).getBeer().getName();
                case 1:
                    return beers.get(rowIndex).getBeer().getStyle();
                case 2:
                    return beers.get(rowIndex).getBeer().getAlcohol();
                case 3:
                    return beers.get(rowIndex).getBeer().getPopularity();
                case 4:
                    return beers.get(rowIndex).getQuantity() + " l";
                default: return null;
            }
    }
}
