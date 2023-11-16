import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShopFrame extends JFrame implements ActionListener {
    private List<ShopTable> matShopTablesData;
    private List<JTable> matTables;
    private JTabbedPane pane;
    private JButton purchase;
    private int[] bases = {10, 100, 10, 1, 100};

    public ShopFrame()  {
        this.setSize((int)(MainFrame.screenSize.width/2), (int)(MainFrame.screenSize.height/2));
        this.setTitle("Material Market");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel purchaseButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        purchase = new JButton("Buy!");
        purchase.addActionListener(this);
        purchaseButtonPanel.add(purchase);
        this.add(BorderLayout.SOUTH, purchaseButtonPanel);

        initTables();

    }

    private void initTables() {
        matShopTablesData = new ArrayList<ShopTable>();
        matTables = new ArrayList<JTable>();
        pane = new JTabbedPane();

        int i = 0;
        for(String key: Data.keys) {
            matShopTablesData.add(new ShopTable(Main.gData.materials.get(key).values().stream().toList(), bases[i]));
            matTables.add(new JTable(matShopTablesData.get(i)));
            matTables.get(i).setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(matTables.get(i));
            pane.addTab(key.substring(0,1).toUpperCase()+key.substring(1), scrollPane);
            System.out.println("FRom shopFrame: " +i);
            i++;
        }

        this.add(pane, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("kiskutya");
    }
}
