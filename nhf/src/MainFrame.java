import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
    //Map<String, JComponent> statusBarComponents = new HashMap<String, JComponent>();

    //statusBar components
    private JLabel moneyText;
    private JLabel reputationText;
    private JLabel dateText;
    private JLabel nameText;

    private List<JButton> cookingUnits = new ArrayList<JButton>();
    private JButton market;
    private JButton recipes;
    private JButton matMarket;
    private JButton storage;
    private JButton nextTurn;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


    public MainFrame(PData pData) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //this.setSize(1366, 768);
        UIManager.put("Button.select", Color.LIGHT_GRAY);

        JPanel nextTurnPanel = new JPanel();
        nextTurn = makeActionButton(new ImageIcon("hourglass.png"), MyColors.mandarin, new Dimension(95, 95));
        nextTurnPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 90, 725));
        nextTurnPanel.setPreferredSize(new Dimension(200, 100));
        nextTurnPanel.add(nextTurn);
        nextTurnPanel.setBackground(MyColors.mandarin);

        JPanel space = new JPanel();
        space.setPreferredSize(new Dimension(200,100));
        space.setBackground(MyColors.mandarin);

        this.add(initStatusBar(pData), BorderLayout.NORTH);
        this.add(initCookingUnit(pData), BorderLayout.CENTER);
        this.add(initActionBar(), BorderLayout.SOUTH);
        this.add(nextTurnPanel, BorderLayout.EAST);
        this.add(space, BorderLayout.WEST);
        this.setVisible(true);
    }

    public void adjustAfterRendered() {
        int compwidth= moneyText.getWidth() + reputationText.getWidth() + dateText.getWidth() + 25*5 +100;
        System.out.println(compwidth);
        nameText.setPreferredSize(new Dimension(screenSize.width - compwidth, 35));
    }

    private JPanel initCookingUnit(PData pData) {
        Dimension cookingUnitDim = new Dimension(350, 350);
        JPanel cookingUnitPanel = new JPanel();
        Color cookingUnitbgColor = MyColors.mandarin;
        cookingUnitPanel.setBackground(cookingUnitbgColor);
        cookingUnitPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 200));
        for (int i = 0; i < 3; i++) {
            JButton cookingUnit = new JButton();
            cookingUnit.setVisible(true);
            cookingUnit.setPreferredSize(cookingUnitDim);
            cookingUnit.setBackground(cookingUnitbgColor);
            cookingUnit.setBorder(null);
            cookingUnit.setFocusable(false);
            if(pData.tools.size()-1 >= i) {
               ImageIcon brewingUnit = new ImageIcon();
                switch(pData.tools.get(i).getLevel()) {
                    case 1:
                        brewingUnit = new ImageIcon("brewing1.png");
                        break;
                    case 2:
                        brewingUnit = new ImageIcon("brewing2.png");
                        break;
                    case 3:
                        brewingUnit = new ImageIcon("brewing3.png");
                        break;
                }
                cookingUnit.setIcon(new ImageIcon(brewingUnit.getImage().getScaledInstance(cookingUnitDim.width, cookingUnitDim.height, Image.SCALE_SMOOTH)));

            }
            else {

                cookingUnit.setEnabled(false);
                cookingUnit.setIcon(new ImageIcon(new ImageIcon("brewing.png").getImage().getScaledInstance(cookingUnitDim.width, cookingUnitDim.height,Image.SCALE_SMOOTH)));

            }
            cookingUnitPanel.add(cookingUnit);
            cookingUnits.add(cookingUnit);
        }

        return cookingUnitPanel;
    }


    private JPanel initStatusBar(PData pData) {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0)); //(int)(screenSize.getWidth()*0.013)
        statusBar.setBackground(MyColors.hibiscus);
        Dimension iconDim = new Dimension(35,35);

        nameText = new JLabel(pData.gameName);
        nameText.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 30));
        nameText.setHorizontalAlignment(JLabel.CENTER);

        statusBar.add(moneyText = makeLabel(new ImageIcon("coins.png"),String.format("%,d",pData.money), iconDim));
        statusBar.add(reputationText= makeLabel(new ImageIcon("star.png"), Double.toString(pData.reputation), iconDim));
        statusBar.add(nameText);
        statusBar.add(Box.createRigidArea(new Dimension(100, 35)));
        statusBar.add(dateText = makeLabel(new ImageIcon("calendar.png"), Data.startDate.plusWeeks(pData.turn).format(DateTimeFormatter.ofPattern("YYYY.MM.DD")), iconDim));

        return statusBar;
    }

    private JPanel initActionBar() {
        JPanel actionBar = new JPanel();
        Dimension actionButDim = new Dimension(80, 80);
        actionBar.setPreferredSize(new Dimension(1000, 120));
        actionBar.setLayout(new FlowLayout(FlowLayout.CENTER,30, 30));
        Color actionBarBgColor = MyColors.hibiscus;

        actionBar.add(market = makeActionButton(new ImageIcon("pub.png"), actionBarBgColor, actionButDim));
        actionBar.add(recipes = makeActionButton(new ImageIcon("recipe.png"), actionBarBgColor, actionButDim));
        actionBar.add(matMarket = makeActionButton(new ImageIcon("food-stand.png"), actionBarBgColor, actionButDim));
        actionBar.add(storage = makeActionButton(new ImageIcon("warehouse.png"), actionBarBgColor, actionButDim));
        actionBar.setBackground(actionBarBgColor);
        storage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StoreFrame();
            }
        });

        return actionBar;

    }
    private JButton makeActionButton(ImageIcon img, Color bgcolor, Dimension butDim) {
        JButton button = new JButton();
        button.setVisible(true);
        button.setIcon(new ImageIcon(img.getImage().getScaledInstance(butDim.width, butDim.height, Image.SCALE_SMOOTH)));
        button.setFocusable(false);
        button.setBorder(null);
        button.setBackground(bgcolor);
        return button;
    }

    private JLabel makeLabel(ImageIcon img, String text, Dimension imgD) {
        JLabel label = new JLabel();
        Image userImg = img.getImage().getScaledInstance(imgD.width, imgD.height,Image.SCALE_SMOOTH);
        img = new ImageIcon(userImg);
        label.setVisible(true);
        label.setText(text);
        label.setIcon(img);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        label.setVisible(true);
        return label;
    }
}
