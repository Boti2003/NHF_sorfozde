import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StoreFrame extends JFrame {
    private List<StoreTable> matTablesData;
    private List<JTable> matTables;

    public StoreFrame()  {
        this.setSize((int)(MainFrame.screenSize.width/2), (int)(MainFrame.screenSize.height/2));
        this.setTitle("Storage");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        initTables();

    }

    private void initTables() {
        matTablesData = new ArrayList<StoreTable>();
        matTables = new ArrayList<JTable>();
        JTabbedPane pane = new JTabbedPane();

        int i = 0;
        for(String key: Data.keys) {
            matTablesData.add(new StoreTable(Main.player.materials.get(key).values().stream().toList()));
            matTables.add(new JTable(matTablesData.get(i)));
            matTables.get(i).setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(matTables.get(i));
            pane.addTab(key.substring(0,1).toUpperCase()+key.substring(1), scrollPane);
            i++;
        }


        this.add(pane, BorderLayout.CENTER);

    }

}
