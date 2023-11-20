import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeFrame extends JFrame {
    private Dimension frameSize = new Dimension((int)(MainFrame.screenSize.width/1.7), (int)(MainFrame.screenSize.height/1.7));
    private List<Recipe> selectedRecipes = new ArrayList<>();
    private JLabel sumMoney = new JLabel();
    private int sumToBuy = 0;
    private JButton purchaseButton;
    public RecipeFrame() {
        this.setSize(frameSize.width, frameSize.height);
        this.setTitle("Recipe Store");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        for(Recipe rec: MainFrame.player.recipes.values()) {
            pan.add(drawRecipeUnit(rec, true));
            pan.add(Box.createRigidArea(new Dimension((int)(MainFrame.screenSize.width/2.5), 20)));
        }

        for(Recipe rc: MainFrame.gData.recipes.values()) {
            if(!MainFrame.player.recipes.containsKey(rc.getName())) {
                pan.add(drawRecipeUnit(rc, false));
                pan.add(Box.createRigidArea(new Dimension((int)(MainFrame.screenSize.width/2.5), 20)));
            }
        }



        JScrollPane pane = new JScrollPane(pan);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        JPanel textpan = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("Recipes");
        textpan.add(label);
        label.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 25));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        purchaseButton = new JButton("Buy!");
        updateSouthPanel();
        south.add(sumMoney);
        south.add(purchaseButton);
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MainFrame.player.money < sumToBuy) {
                    showMessage("You don't have enough money to buy this!");
                } else {
                    for (Recipe rec : selectedRecipes) {
                        MainFrame.player.recipes.put(rec.getName(), rec);
                    }
                    MainFrame.player.money -= sumToBuy;
                    MainFrame.updateData();
                    closeAndReOpen();

                }
            }
        });

        this.add(pane, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(frameSize.width/12, frameSize.height)), BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(frameSize.width/12, frameSize.height)), BorderLayout.WEST);
        this.add(south, BorderLayout.SOUTH);
        this.add(textpan, BorderLayout.NORTH);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.openRecipe = false;
            }
        });

    }
    private void closeAndReOpen() {
        new RecipeFrame();
        this.dispose();
    }
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    private void updateSouthPanel() {
        sumToBuy = 0;
        for(Recipe rec: selectedRecipes) {
            sumToBuy+= rec.getPrice();

        }
        if(selectedRecipes.isEmpty()) {
            sumMoney.setText("Choose a recipe to buy!");
            purchaseButton.setEnabled(false);
        }
        else {
            sumMoney.setText("Price of selected recipes: " + String.format("%,d",sumToBuy));
            purchaseButton.setEnabled(true);
        }
    }

    private JPanel drawRecipeUnit(Recipe thisrecipe, boolean enabled) {

        JPanel unit = new JPanel(new GridLayout());
        unit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 158));
        unit.setVisible(true);
        String[] beericons = new String[11];
        for(int i = 0; i < 11; i++) {
            beericons[i] = "beer(" + i + ").png";
        }

        Random random = new Random();

        ImageIcon icon = new ImageIcon(beericons[random.nextInt(0,10)]);
        JLabel picture = new JLabel(icon);
        picture.setEnabled(enabled);
        Color bgcolor;

        if (enabled) {
            bgcolor = MyColors.lightPeach;
        } else {
            bgcolor = Color.lightGray;
        }
        unit.setBackground(bgcolor);

        JPanel baseDataPanel = new JPanel(new GridLayout(0, 1));
        String[] baseData = {"<html><font size=4>Name</font>: " + thisrecipe.getName() + "</html>", "<html><font size=4>Style:</font> "+ thisrecipe.getStyle(), "<html><font size=4>Popularity:</font> " + thisrecipe.getPopularity(), "<html><font size=4>Alcohol:</font> " + thisrecipe.getAlcohol() + "%", "<html><font size=4>Brewing time:</font> " + thisrecipe.getBrewTurn() + " weeks"};
        for(String str: baseData) {
            JLabel lab = new JLabel(str);
            baseDataPanel.add(lab);
        }
        baseDataPanel.setBackground(bgcolor);

        unit.add(picture);
        unit.add(baseDataPanel);
        for(int k = 0; k < 2; k++) {
           String[] mats = new String[thisrecipe.materials.get(Data.keys[k]).size() + 1];
            int i = 1;
            mats[0] = "<font size=4>"+Data.keys[k].substring(0, 1).toUpperCase() + Data.keys[k].substring(1) + ":</font>";
            for (MatElement mate : thisrecipe.materials.get(Data.keys[k]).values()) {
                mats[i] = mate.getQuantity() + " " + mate.getMat().getUnit() + " " + mate.getMat().getName();
                i++;
            }
            unit.add(new JLabel(MainFrame.multiLineGenerator(mats, 8)));
        }
        String[] frutsandyeasts = new String[(!thisrecipe.materials.get(Data.keys[2]).isEmpty() ? thisrecipe.materials.get(Data.keys[2]).size()+1:0) + (!thisrecipe.materials.get(Data.keys[3]).isEmpty() ? thisrecipe.materials.get(Data.keys[3]).size()+1:0)];
        int i = 0;
        if(frutsandyeasts.length > 0) {
            for (int k = 2; k < 4; k++) {
                if (!thisrecipe.materials.get(Data.keys[k]).isEmpty()) {
                    frutsandyeasts[i] = "<font size=4>"+Data.keys[k].substring(0, 1).toUpperCase() + Data.keys[k].substring(1) + ":</font>";
                    i++;
                }
                for (MatElement mate : thisrecipe.materials.get(Data.keys[k]).values()) {
                    frutsandyeasts[i] = mate.getQuantity() + " " + mate.getMat().getUnit() + " " + mate.getMat().getName();
                    i++;
                }
                frutsandyeasts[i-1] += "<br/>";
            }
            unit.add(new JLabel(MainFrame.multiLineGenerator(frutsandyeasts, 6)));
        }

        String[] spices = new String[(!thisrecipe.materials.get(Data.keys[4]).isEmpty() ? thisrecipe.materials.get(Data.keys[4]).size()+1:0)];
        int j = 0;
        if(!thisrecipe.materials.get(Data.keys[4]).isEmpty()) {
            spices[j] = "<font size=4>"+Data.keys[4].substring(0, 1).toUpperCase() + Data.keys[4].substring(1) + ":</font>";
            j++;
            for(MatElement mate: thisrecipe.materials.get(Data.keys[4]).values()) {
                spices[j] = mate.getQuantity() + " " + mate.getMat().getUnit() + " " + mate.getMat().getName();
                j++;
            }
            unit.add(new JLabel(MainFrame.multiLineGenerator(spices,8)));
        }
        unit.addMouseListener(new MouseAdapter() {
            boolean selected = false;
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!enabled) {
                    if (!selected) {
                        unit.setBorder(BorderFactory.createLineBorder(Color.red, 4));
                        selected = true;
                        selectedRecipes.add(thisrecipe);
                    } else {
                        unit.setBorder(BorderFactory.createEmptyBorder());
                        selected = false;
                        selectedRecipes.remove(thisrecipe);

                    }
                    updateSouthPanel();
                }
            }
        });


        return unit;
    }


}
