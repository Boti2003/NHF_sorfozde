import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * A játékot vezérlő, és a fő ablakot létrehozó osztály
 */
public class MainFrame extends JFrame {
    //Map<String, JComponent> statusBarComponents = new HashMap<String, JComponent>();

    //statusBar components
    public static Data gData = new Data();
    public static PData player;
    private static JLabel moneyText;
    private static JLabel reputationText;
    private static JLabel dateText;
    private JLabel nameText;

    private static List<JButton> cookingUnits = new ArrayList<JButton>();
    private JButton market;
    private JButton recipes;
    private JButton matMarket;
    private JButton storage;
    private JButton nextTurn;
    private JButton buyNewTool;

    public static boolean openStorage = false;
    public static boolean openRecipe = false;
    public static boolean openShop = false;
    public static boolean openTools = false;
    public static boolean openMarket = false;

    public static JFrame storageFrame = new JFrame();
    public static JFrame shopFrame = new JFrame();
    public static JFrame toolFrame = new JFrame();
    public static JFrame recipeFrame = new JFrame();
    public static JFrame marketFrame = new JFrame();


    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Program belépési pontja, elindítja az adatok beolvasását, majd megnyitja a menüt reprezentáló ablakot
     * @param args
     */
    public static void main(String[] args) {

        try {
            DataManager.buildDataStructure();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        new MenuFrame();


    }

    /**
     * Konstruktor a fő ablakhoz, itt épül fel az elsődleges vezérlő ablak által használt GUI.
     */
    public MainFrame() {
        this.setTitle("Brew Your Dream");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        UIManager.put("Button.select", MyColors.lightPeach);
        this.setBackground(MyColors.mandarin);


        JPanel nextTurnPanel = new JPanel();
        nextTurn = makeActionButton(new ImageIcon("hourglass.png"), MyColors.mandarin, new Dimension((int)(screenSize.width*0.05), (int)(screenSize.width*0.05)));
        nextTurnPanel.setLayout(new BoxLayout(nextTurnPanel, BoxLayout.Y_AXIS));
        nextTurnPanel.add(Box.createRigidArea(new Dimension(screenSize.width/13, (int)(screenSize.height*0.65))));
        nextTurnPanel.add(nextTurn);
        nextTurnPanel.setBackground(MyColors.mandarin);
        nextTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TurnManager.newTurn();
            }
        });

        JPanel plusToolPanel = new JPanel();
        buyNewTool = makeActionButton(new ImageIcon("plus.png"), MyColors.mandarin, new Dimension((int)(screenSize.width*0.05),(int)(screenSize.width*0.05)));
        plusToolPanel.setLayout(new BoxLayout(plusToolPanel, BoxLayout.Y_AXIS));
        plusToolPanel.add(Box.createRigidArea(new Dimension(screenSize.width/13, (int)(screenSize.height*0.65))));
        plusToolPanel.add(buyNewTool);
        buyNewTool.setAlignmentX(CENTER_ALIGNMENT);
        plusToolPanel.setBackground(MyColors.mandarin);

        buyNewTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.tools.size() >= 3) {
                    JOptionPane.showMessageDialog(null,  "You can't buy more brewing tools, even your brewery has limitations:(( Don't forget that you can still upgrade them, if not all of them are on Level 3!");
                } else {
                    String[] options = {"Buy", "No, thanks"};
                    int selection = JOptionPane.showOptionDialog(null, "<html>You are about to buy a new brewing tool. It would cost " + String.format("%,d",MainFrame.gData.tools.get(0).getCostToBuy()) + ", and comes with a monthly maintenance cost of " + String.format("%,d",MainFrame.gData.tools.get(0).getMaintenanceCost()) +
                                    ".<br/> The tool's capacity is " + MainFrame.gData.tools.get(0).getCapacity() + " litres. Are you wish to proceed?</html>",
                            "Buy new brewing tool", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if(selection == 0) {
                        if(player.money <= gData.tools.get(0).getCostToBuy()) {
                            JOptionPane.showMessageDialog(null, "You don't have enough money to buy a new brewing tool:((");
                        }
                        else {
                            player.tools.add(new BrewingTool(gData.tools.get(0).getMaintenanceCost(), gData.tools.get(0).getCostToBuy(), gData.tools.get(0).getCapacity(), gData.tools.get(0).getLevel()));
                            player.money -= gData.tools.get(0).getCostToBuy();
                            updateData();
                            upgradeCookingUnitUI();
                        }

                    }
                }

            }
        });



        this.add(initStatusBar(), BorderLayout.NORTH);
        this.add(initCookingUnit(), BorderLayout.CENTER);
        this.add(initActionBar(), BorderLayout.SOUTH);
        this.add(nextTurnPanel, BorderLayout.EAST);
        this.add(plusToolPanel, BorderLayout.WEST);
        this.setVisible(true);
        adjustAfterRendered();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DataManager.saveData();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * GUI frissítése az adatok megváltozása esetén
     */
    public static void updateData() {
        moneyText.setText(String.format("%,d",player.money));
        reputationText.setText(String.format(Locale.US,"%.1f",player.reputation));
        dateText.setText(generateDate(Data.startDate.plusWeeks(player.turn)));
        upgradeCookingUnitUI();
    }

    /**
     * Általános metódus kívánt formátumú String generálására egy dátumértékből.
     * @param date LocalDate típusú objektum az átalakítandó dátummal
     * @return String érték a megfelelő formátummal
     */
    public static String generateDate(LocalDate date) {
        String strReturnDat= String.valueOf(date.getYear());
        if(date.getMonth().getValue() < 10) {
            strReturnDat += ".0"+date.getMonth().getValue();
        }
        else {
            strReturnDat += "." +date.getMonth().getValue();
        }
        if(date.getDayOfMonth() < 10) {
            strReturnDat += ".0"+date.getDayOfMonth()+".";
        }
        else {
            strReturnDat += "." +date.getDayOfMonth()+".";
        }
        return strReturnDat;

    }

    /**
     * Sörfőzőegységeket ábrázoló GUI frissítése főzőegység fejlesztés és vásárlás után
     */
    private static void upgradeCookingUnitUI() {
        Dimension cookingUnitDim = new Dimension(screenSize.width/5, screenSize.width/5);
        for (int i = 0; i < 3; i++) {
            JButton cookingUnit = cookingUnits.get(i);
            if(player.tools.size()-1 >= i) {
                ImageIcon brewingUnit = new ImageIcon();
                switch(player.tools.get(i).getLevel()) {
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
                cookingUnit.setEnabled(true);

            }
            else {
                cookingUnit.setEnabled(false);
                cookingUnit.setIcon(new ImageIcon(new ImageIcon("brewing.png").getImage().getScaledInstance(cookingUnitDim.width, cookingUnitDim.height,Image.SCALE_SMOOTH)));

            }

        }
    }


    /**
     * Az állapotsáv kinézetének beállítása, miután a szövegek renderelése megtörtént
     */
    public void adjustAfterRendered() {
        int compwidth= moneyText.getWidth() + reputationText.getWidth() + dateText.getWidth() + 25*5 +100;
        nameText.setPreferredSize(new Dimension(screenSize.width - compwidth, 35));
    }

    /**
     * Sörfőzőegységek kinézetének inicializálása indításkor
     * @return egységeket ábrázoló gombokat tartalmazó JPanel
     */
    private JPanel initCookingUnit() {
        Dimension cookingUnitDim = new Dimension(screenSize.width/5, screenSize.width/5);
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
            if(player.tools.size()-1 >= i) {
               ImageIcon brewingUnit = new ImageIcon();
                switch(player.tools.get(i).getLevel()) {
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
                cookingUnit.setEnabled(true);

            }
            else {

                cookingUnit.setEnabled(false);
                cookingUnit.setIcon(new ImageIcon(new ImageIcon("brewing.png").getImage().getScaledInstance(cookingUnitDim.width, cookingUnitDim.height,Image.SCALE_SMOOTH)));

            }
            cookingUnitPanel.add(cookingUnit);
            cookingUnits.add(cookingUnit);
        }
        cookingUnits.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!openTools) {
                    toolFrame = new BrewingFrame(0);
                    recipeFrame.dispose();
                    openRecipe = false;
                }
            }
        });
        cookingUnits.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!openTools) {
                    toolFrame = new BrewingFrame(1);
                    recipeFrame.dispose();
                    openRecipe = false;
                }
            }
        });
        cookingUnits.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!openTools) {
                    toolFrame = new BrewingFrame(2);
                    recipeFrame.dispose();
                    openRecipe = false;
                }
            }
        });



        return cookingUnitPanel;
    }

    /**
     * Állapotsáv(pénz, népszerúség, főzde neve, dátum) inicializálása
     * @return állapotsávot tartalmazó JPanel
     */
    private JPanel initStatusBar() {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0)); //(int)(screenSize.getWidth()*0.013)
        statusBar.setBackground(MyColors.hibiscus);
        Dimension iconDim = new Dimension(35,35);

        nameText = new JLabel(player.gameName);
        nameText.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 30));
        nameText.setHorizontalAlignment(JLabel.CENTER);

        statusBar.add(moneyText = makeLabel(new ImageIcon("coins.png"),String.format("%,d",player.money), iconDim));
        statusBar.add(reputationText= makeLabel(new ImageIcon("star.png"), String.format(Locale.US, "%.1f",player.reputation), iconDim));
        statusBar.add(nameText);
        statusBar.add(Box.createRigidArea(new Dimension(100, 35)));
        statusBar.add(dateText = makeLabel(new ImageIcon("calendar.png"), generateDate(Data.startDate.plusWeeks(player.turn)), iconDim));

        return statusBar;
    }

    /**
     *Vezérlősávot hoz létre, amivel meg lehet nyitni a megfelelő ablakokat
     * @return vezérlő gombokat tartalmazó JPanel
     */
    private JPanel initActionBar() {
        JPanel actionBar = new JPanel();
        Dimension actionButDim = new Dimension((int)(screenSize.height*0.075), (int)(screenSize.height*0.075));
        actionBar.setPreferredSize(new Dimension(1000, (int)(screenSize.height*0.11)));
        actionBar.setLayout(new FlowLayout(FlowLayout.CENTER,30, 25));
        Color actionBarBgColor = MyColors.hibiscus;

        actionBar.add(market = makeActionButton(new ImageIcon("pub.png"), actionBarBgColor, actionButDim));
        actionBar.add(recipes = makeActionButton(new ImageIcon("recipe.png"), actionBarBgColor, actionButDim));
        actionBar.add(matMarket = makeActionButton(new ImageIcon("food-stand.png"), actionBarBgColor, actionButDim));
        actionBar.add(storage = makeActionButton(new ImageIcon("warehouse.png"), actionBarBgColor, actionButDim));
        actionBar.setBackground(actionBarBgColor);
        storage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!openStorage ) {
                    storageFrame = new StoreFrame();
                    shopFrame.dispose();
                    openShop = false;
                }
            }
        });
        matMarket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!openShop) {
                    shopFrame = new ShopFrame();
                    storageFrame.dispose();
                    openStorage = false;

                }
            }
        });
        recipes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!openRecipe) {
                    recipeFrame = new RecipeFrame();
                    toolFrame.dispose();
                    openTools = false;

                }
            }
        });
        market.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!openMarket) {
                    marketFrame = new MarketFrame();

                }
            }
        });

        return actionBar;

    }

    /**
     * Vezérlő gomb létrehozása
     * @param img gomb ikonja
     * @param bgcolor gomb háttérszíne
     * @param butDim gomb mérete
     * @return kész gomb JButton objektuma
     */
    private JButton makeActionButton(ImageIcon img, Color bgcolor, Dimension butDim) {
        JButton button = new JButton();
        button.setVisible(true);
        button.setIcon(new ImageIcon(img.getImage().getScaledInstance(butDim.width, butDim.height, Image.SCALE_SMOOTH)));
        button.setFocusable(false);
        button.setBorder(null);
        button.setBackground(bgcolor);
        return button;
    }

    /**
     * Az állapotsáv feliratait létrehozó metódus
     * @param img felirat ikonja
     * @param text felirat szövege
     * @param imgD gomb mérete
     * @return kész felirat JLabel objektuma
     */
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

    /**
     * Több soros szövegeket HTML segítségével generáló metódus
     * @param strings sorok tömbje
     * @param preferredLinesize sorok ideális száma
     * @return String objektum a kész szöveggel
     */
    public static String multiLineGenerator(String[] strings, int preferredLinesize) {
        String finalStr = "<html>" + strings[0];
        for(int i = 1; i < strings.length; i++) {
            finalStr = finalStr + "<br/>" + strings[i];
        }
        for(int i = 0; i < preferredLinesize - strings.length; i++) {
            finalStr = finalStr + "<br/>";
        }
        finalStr = finalStr + "</html>";
        return finalStr;
    }
}
