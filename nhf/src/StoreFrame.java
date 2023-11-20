import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class StoreFrame extends JFrame {
    private List<StoreTable> matTablesData;
    private List<JTable> matTables;
    private BeerStoreTable beerStore;
    private JTable beerTable;

    public StoreFrame()  {
        this.setSize((int)(MainFrame.screenSize.width/2), (int)(MainFrame.screenSize.height/2));
        MainFrame.openStorage = true;
        this.setTitle("Storage");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        initTables();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.openStorage = false;
            }
        });

    }

    private void initTables() {
        matTablesData = new ArrayList<StoreTable>();
        matTables = new ArrayList<JTable>();
        //UIManager.put("TabbedPane.selected", MyColors.lightPeach);
        JTabbedPane pane = new JTabbedPane();

        //Material table initalization
        int i = 0;
        for(String key: Data.keys) {
            matTablesData.add(new StoreTable(MainFrame.player.materials.get(key).values().stream().toList()));
            matTables.add(new JTable(matTablesData.get(i)));
            matTables.get(i).setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(matTables.get(i));
            pane.addTab(key.substring(0,1).toUpperCase()+key.substring(1), scrollPane);
            i++;
        }
        beerStore = new BeerStoreTable(MainFrame.player.beers.values().stream().toList());
        beerTable = new JTable(beerStore);
        JScrollPane scrpane = new JScrollPane(beerTable);
        pane.addTab("Beers",scrpane );

        pane.setFont(new Font("Georgia", Font.PLAIN, 16));
        //pane.setBackground(MyColors.darkOrange);


        this.add(pane, BorderLayout.CENTER);

    }

}
