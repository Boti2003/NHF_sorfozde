import javax.swing.*;
import java.awt.*;

public class StoreFrame extends JFrame {

    public StoreFrame()  {
        this.setSize((int)(MainFrame.screenSize.width/2), (int)(MainFrame.screenSize.height/2));
        this.setTitle("Storage");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        initTables();

    }

    private void initTables() {
        JTabbedPane pane = new JTabbedPane();
        JPanel panel = new JPanel();
        panel.add(new Label("Kiskutya"));
        JPanel panel2 = new JPanel();
        pane.addTab("Nagykutya", panel);
        pane.addTab("Hatalmas kutya", panel2);
        this.add(pane, BorderLayout.CENTER);

    }

}
