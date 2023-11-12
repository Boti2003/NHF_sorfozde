import javax.swing.*;

public class StoreFrame extends JFrame {

    public StoreFrame()  {
        this.setSize((int)(MainFrame.screenSize.width/2), (int)(MainFrame.screenSize.height/2));
        this.setTitle("Storage");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

    }

}
