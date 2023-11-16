import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RecipeFrame extends JFrame {
    private Dimension frameSize = new Dimension((int)(MainFrame.screenSize.width/2), (int)(MainFrame.screenSize.height/2));
    public RecipeFrame() {
        this.setSize(frameSize.width, frameSize.height);
        this.setTitle("Recipe Store");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        for(int i = 0; i < 5; i++) {
            pan.add(drawRecipeUnit());
            pan.add(Box.createRigidArea(new Dimension((int)(MainFrame.screenSize.width/2.5), 20)));
        }
        JScrollPane pane = new JScrollPane(pan);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        JPanel textpan = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("Recipes");
        textpan.add(label);
        label.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 25));
        this.add(pane, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(frameSize.width/12, frameSize.height)), BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(frameSize.width/12, frameSize.height)), BorderLayout.WEST);
        this.add(Box.createRigidArea(new Dimension(frameSize.width, frameSize.height/15)), BorderLayout.SOUTH);
        this.add(textpan, BorderLayout.NORTH);

    }

    private JPanel drawRecipeUnit() {
        JPanel unit = new JPanel();
        unit.setPreferredSize(new Dimension((int)(frameSize.width/1.25), frameSize.height/4));
        unit.setBackground(MyColors.mandarin);
        unit.setVisible(true);
        String[] beericons = new String[11];
        for(int i = 0; i < 11; i++) {
            beericons[i] = "beer(" + i + ").png";
        }

        Random random = new Random();

        ImageIcon icon = new ImageIcon(beericons[random.nextInt(0,10)]);
        JLabel picture = new JLabel(icon);
        
        unit.add(picture);

        return unit;
    }
}
