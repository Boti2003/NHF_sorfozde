import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.RowId;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BrewingFrame extends JFrame {
    private Dimension framesize = new Dimension(MainFrame.screenSize.width/2, MainFrame.screenSize.height/2);
    private BrewingTool thisTool;

    private Recipe recipetobrew;
    private Recipe temprecipe;
    private List<JLabel> basedtPan = new ArrayList<>();
    private JButton startbrew = new JButton("Start brew:))");

    private JLabel maltlab;
    private JLabel hoplab;
    private JLabel yeastandfruitlab;
    private JLabel spicelab;
    private JPanel recipePanel;
    private JTextField quanTextF;
    private double quantifier;
    private Color bgcolor = MyColors.lightblue;
    int idx;
    int brewingQuantity;

    public BrewingFrame(int index) {
        this.setTitle("Beer Brewing");
        this.setResizable(false);
        MainFrame.openTools = true;
        idx = index;
        thisTool = MainFrame.player.tools.get(index);
        if(Objects.isNull(thisTool.getActualBrewing())) {
            newBeer();
        }
        else {
            brewingBeer();
        }
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.openTools = false;
            }
        });
    }
    public void close() {
        this.dispose();
    }
    public void closeAndReopen() {
        new BrewingFrame(idx);
        this.dispose();
    }
    private void brewingBeer() {
        this.setSize(framesize);

        this.setBackground(bgcolor);
        JLabel wtext = new JLabel("Your brewing machine is working!");
        wtext.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 30));
        JPanel northpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        northpanel.add(wtext);
        northpanel.setBackground(bgcolor);

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.LINE_AXIS));
        centralPanel.setBackground(bgcolor);

        JPanel infopanel = new JPanel();
        infopanel.setBackground(bgcolor);
        infopanel.setAlignmentX(CENTER_ALIGNMENT);

        infopanel.setLayout(new BoxLayout(infopanel, BoxLayout.Y_AXIS));
        JLabel lab = new JLabel("Your brewery with it's enthusiastic workers currently working hard on some delicious beer:))", SwingConstants.CENTER);
        JLabel lab2 = new JLabel("There are " + (thisTool.getActualBrewing().getBrewingBeer().getBrewTurn() - thisTool.getActualBrewing().getActualTurn()) + " weeks left from the process.", SwingConstants.CENTER);
        JLabel lab3 = new JLabel("<html>You are brewing <strong> " + thisTool.getActualBrewing().getQuantity() + "</strong> litres of <strong>" + thisTool.getActualBrewing().getBrewingBeer().getName() + "</strong></html>.", SwingConstants.CENTER);

        JLabel[] labs = {lab, lab2, lab3};
        for(JLabel l: labs) {
            l.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            l.setAlignmentX(CENTER_ALIGNMENT);
            l.setAlignmentY(CENTER_ALIGNMENT);
            infopanel.add(l);
        }


        JProgressBar progbar = new JProgressBar(0, thisTool.getActualBrewing().getBrewingBeer().getBrewTurn());
        progbar.setValue(thisTool.getActualBrewing().getActualTurn());
        progbar.setForeground(MyColors.aqua);
        progbar.setAlignmentX(CENTER_ALIGNMENT);
        progbar.setAlignmentY(CENTER_ALIGNMENT);
        progbar.setMaximumSize(new Dimension(framesize.width/6, 20));

        infopanel.add(Box.createRigidArea(new Dimension(framesize.width/5, 15)));
        infopanel.add(progbar);
        centralPanel.add(infopanel);

        ImageIcon ic = new ImageIcon("fermentation.png");
        JLabel piclab = new JLabel(ic);
        piclab.setAlignmentX(Component.CENTER_ALIGNMENT);
        centralPanel.add(piclab);

        this.add(northpanel, BorderLayout.NORTH);
        this.add(centralPanel, BorderLayout.CENTER);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void newBeer() {
        this.setSize(framesize);
        this.setBackground(bgcolor);
        JLabel wtext = new JLabel("Brew a new beer!");
        wtext.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 30));
        JPanel northpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        northpanel.add(wtext);
        northpanel.setBackground(bgcolor);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setAlignmentX(CENTER_ALIGNMENT);
        sidePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        JLabel tooltxt = new JLabel("Tool Data");
        tooltxt.setFont(new Font("Bernard Mt Condensed", Font.PLAIN, 20));
        tooltxt.setAlignmentX(CENTER_ALIGNMENT);

        JLabel caplabel = new JLabel("Capacity: " + thisTool.getCapacity() + " l");
        caplabel.setAlignmentX(CENTER_ALIGNMENT);
        JLabel cost = new JLabel("Maintenance Cost: " + String.format("%,d",thisTool.getMaintenanceCost()) + "/month");
        cost.setAlignmentX(CENTER_ALIGNMENT);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(MyColors.lightgreen);
        updateButton.setFocusable(false);
        //updateButton.setForeground(Color.white);
        updateButton.setAlignmentX(CENTER_ALIGNMENT);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int buycost = MainFrame.gData.tools.get(thisTool.getLevel()).getCostToBuy();
                String buycoststr = String.format("%,d", buycost);
                if(buycost > MainFrame.player.money) {
                    JOptionPane.showMessageDialog(null, "You don't have enough money to upgrade! It would cost " + buycoststr +".");
                }
                else {
                    String[] options = {"Upgrade", "No, thanks"};
                    int selection = JOptionPane.showOptionDialog(null, "<html>You are about to upgrade your brewing tool. It would cost " + buycoststr + ", and comes with a monthly maintenance cost of " + String.format("%,d",MainFrame.gData.tools.get(thisTool.getLevel()).getMaintenanceCost()) +
                            ".<br/> The upgrade would increase this tool's capacity to " + MainFrame.gData.tools.get(thisTool.getLevel()).getCapacity() + " litres. Are you wish to proceed?</html>",
                            "Upgrade brewing tool", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if(selection == 0) {
                        MainFrame.player.money -= buycost;
                        MainFrame.player.tools.set(idx, MainFrame.gData.tools.get(thisTool.getLevel()));
                        MainFrame.updateData();
                        closeAndReopen();

                    }
                }
            }
        });


        sidePanel.add(tooltxt);
        sidePanel.add(Box.createRigidArea(new Dimension(framesize.width/4, 15)));
        sidePanel.add(caplabel);
        sidePanel.add(Box.createRigidArea(new Dimension(framesize.width/4, 15)));
        sidePanel.add(cost);
        sidePanel.add(Box.createRigidArea(new Dimension(framesize.width/4, 25)));
        if(thisTool.getLevel() < 3) {
            sidePanel.add(updateButton);
        }
        sidePanel.setBackground(bgcolor);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(bgcolor);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JLabel lab = new JLabel("Choose a recipe to brew:))");
        lab.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 25));
        lab.setAlignmentX(RIGHT_ALIGNMENT);
        mainPanel.add(lab);
        mainPanel.add(Box.createRigidArea(new Dimension((int)(framesize.width*0.75), 15)));

        JPanel choosePan = new JPanel();
        choosePan.add(new JLabel("Please write the amount of beer you'd like to make: "));

        quanTextF = new JTextField();
        quanTextF.setColumns(5);
        quanTextF.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                proceed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                proceed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                proceed();
            }

            public void proceed() {
                if(quanTextF.getText().isEmpty()) {
                    brewingQuantity = 0;
                    updateRecipePan();
                } else {
                    try {
                        int amount = Integer.parseInt(quanTextF.getText());
                        if (amount < 0 || amount > thisTool.getCapacity()) {
                            JOptionPane.showMessageDialog(null, "Please give a valid number beetwen 1 and " + thisTool.getCapacity());
                            brewingQuantity = 0;
                        } else {
                            brewingQuantity = amount;
                            updateRecipePan();
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Please give a valid number beetwen 1 and " + thisTool.getCapacity());
                        brewingQuantity = 0;
                        updateRecipePan();
                    }
                }

            }
        });
        choosePan.add(quanTextF);
        choosePan.add(new JLabel(" l"));
        choosePan.setBackground(bgcolor);
        choosePan.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        choosePan.setMaximumSize(new Dimension((int)(framesize.width*0.75), framesize.height/6));


        JComboBox recipeselector = new JComboBox(MainFrame.player.recipes.keySet().toArray());
        recipeselector.setMaximumSize(new Dimension(framesize.width/6, 30));
        recipeselector.setSelectedItem(null);
        recipeselector.setAlignmentX(LEFT_ALIGNMENT);
        choosePan.add(recipeselector);

        recipeselector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temprecipe = MainFrame.player.recipes.get(recipeselector.getSelectedItem());
                updateRecipePan();

            }
        });
        mainPanel.add(choosePan);

        recipePanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 50, 20));
        JPanel basedPan = new JPanel(new GridLayout(0, 1));
        for(int i = 0; i < 5; i++) {
            basedtPan.add(new JLabel());
            basedPan.add(basedtPan.get(i));
        }

        basedPan.setBackground(bgcolor);
        recipePanel.add(basedPan);
        recipePanel.add(maltlab = new JLabel("Choose a recipe to brew!"));
        recipePanel.add(hoplab= new JLabel());
        recipePanel.add(yeastandfruitlab = new JLabel());
        recipePanel.add(spicelab = new JLabel());
        recipePanel.setBackground(bgcolor);



        startbrew.setBackground(MyColors.lightgreen);
        startbrew.setEnabled(false);
        startbrew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(enoughMat()) {
                    MainFrame.player.tools.get(idx).setActualBrewing(new BrewingProcess(recipetobrew,brewingQuantity));
                     for(String key: Data.keys) {
                         for(String k: recipetobrew.materials.get(key).keySet()) {
                             MainFrame.player.materials.get(key).get(k).setQuantity(MainFrame.player.materials.get(key).get(k).getQuantity()-recipetobrew.materials.get(key).get(k).getQuantity());
                         }
                     }
                    close();
                }
                else {
                    JOptionPane.showMessageDialog(null, "<html>You don't have enough material to brew this recipe:(( <br/> Go and grab some more at the shop!!</html>");
                }
            }
        });


        mainPanel.add(recipePanel);
        mainPanel.add(startbrew);
        mainPanel.add(Box.createRigidArea(new Dimension((int)(framesize.width*0.75), 30)));


        this.add(northpanel, BorderLayout.NORTH);
        this.add(sidePanel, BorderLayout.EAST);
        this.add(mainPanel, BorderLayout.CENTER);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    private boolean enoughMat() {
        for(String key: Data.keys) {
            for(String k: recipetobrew.materials.get(key).keySet()) {
                if(MainFrame.player.materials.get(key).containsKey(k)) {
                    if(MainFrame.player.materials.get(key).get(k).getQuantity() < recipetobrew.materials.get(key).get(k).getQuantity()) {
                        return false;
                    }
                }
                else {
                    return false;
                }

            }
        }
        return true;
    }
    private void updateRecipePan() {
        if(brewingQuantity > 0 && !Objects.isNull(temprecipe)) {
            quantifier = (double) brewingQuantity /50;

            recipetobrew = new Recipe(temprecipe.getName(), temprecipe.getStyle(), temprecipe.getPrice(), temprecipe.getAlcohol(), temprecipe.getPopularity(), temprecipe.getBrewTurn());
            for(String key: Data.keys) {
                for(MatElement mate: temprecipe.materials.get(key).values()) {
                    recipetobrew.materials.get(key).put(mate.getMat().getName(), new MatElement(mate.getMat(), mate.getQuantity()*quantifier));
                }
            }

            JPanel baseDataPan = new JPanel(new GridLayout(0, 1));
            String[] baseData = {"<html><font size=4>Name</font>: " + recipetobrew.getName() + "</html>", "<html><font size=4>Style:</font> " + recipetobrew.getStyle(), "<html><font size=4>Popularity:</font> " + recipetobrew.getPopularity(), "<html><font size=4>Alcohol:</font> " + recipetobrew.getAlcohol() + "%", "<html><font size=4>Brewing time:</font> " + recipetobrew.getBrewTurn() + " weeks"};
            int u = 0;
            for (JLabel lab : basedtPan) {
                lab.setText(baseData[u]);
                u++;
            }

            JLabel[] maltandhops = {maltlab, hoplab};
            for (int k = 0; k < 2; k++) {
                String[] mats = new String[recipetobrew.materials.get(Data.keys[k]).size() + 1];
                int i = 1;
                mats[0] = "<font size=4>" + Data.keys[k].substring(0, 1).toUpperCase() + Data.keys[k].substring(1) + ":</font>";
                for (MatElement mate : recipetobrew.materials.get(Data.keys[k]).values()) {
                    mats[i] = mate.getQuantity() + " " + mate.getMat().getUnit() + " " + mate.getMat().getName();
                    i++;
                }
                maltandhops[k].setText(MainFrame.multiLineGenerator(mats, 7));
            }

            String[] frutsandyeasts = new String[(!recipetobrew.materials.get(Data.keys[2]).isEmpty() ? recipetobrew.materials.get(Data.keys[2]).size() + 1 : 0) + (!recipetobrew.materials.get(Data.keys[3]).isEmpty() ? recipetobrew.materials.get(Data.keys[3]).size() + 1 : 0)];
            int i = 0;
            if (frutsandyeasts.length > 0) {
                for (int k = 2; k < 4; k++) {
                    if (!recipetobrew.materials.get(Data.keys[k]).isEmpty()) {
                        frutsandyeasts[i] = "<font size=4>" + Data.keys[k].substring(0, 1).toUpperCase() + Data.keys[k].substring(1) + ":</font>";
                        i++;
                    }
                    for (MatElement mate : recipetobrew.materials.get(Data.keys[k]).values()) {
                        frutsandyeasts[i] = mate.getQuantity() + " " + mate.getMat().getUnit() + " " + mate.getMat().getName();
                        i++;
                    }
                    frutsandyeasts[i - 1] += "<br/>";
                }
                yeastandfruitlab.setText(MainFrame.multiLineGenerator(frutsandyeasts, 5));
            }

            String[] spices = new String[(!recipetobrew.materials.get(Data.keys[4]).isEmpty() ? recipetobrew.materials.get(Data.keys[4]).size() + 1 : 0)];
            int j = 0;
            if (!recipetobrew.materials.get(Data.keys[4]).isEmpty()) {
                spices[j] = "<font size=4>" + Data.keys[4].substring(0, 1).toUpperCase() + Data.keys[4].substring(1) + ":</font>";
                j++;
                for (MatElement mate : recipetobrew.materials.get(Data.keys[4]).values()) {
                    spices[j] = mate.getQuantity() + " " + mate.getMat().getUnit() + " " + mate.getMat().getName();
                    j++;
                }
                spicelab.setText(MainFrame.multiLineGenerator(spices, 7));
            }
            startbrew.setEnabled(true);
            recipePanel.setVisible(true);
        }
        else {
            startbrew.setEnabled(false);
            recipePanel.setVisible(false);
        }



    }
}
