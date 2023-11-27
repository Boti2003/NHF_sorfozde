import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MarketFrame extends JFrame {
    Dimension framesize = new Dimension((int)(MainFrame.screenSize.width*0.75), (int)(MainFrame.screenSize.height*0.75));
    double sellquan = 0;
    int price = 0;
    JButton sellButton;
    BeerElement chosenbeer;
    public MarketFrame() {
        MainFrame.openMarket = true;
        this.setSize(framesize);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(pubPanel(MainFrame.gData.pubs.get(0)), BorderLayout.CENTER);

        JScrollPane srcPan = new JScrollPane(centerPanel);
        srcPan.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        srcPan.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.openMarket = false;
            }
        });
        this.add(srcPan, BorderLayout.CENTER);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    private JPanel pubPanel(Pub pubToShow) {
        JPanel pan = new JPanel(new GridLayout(0,1));
        pan.setMaximumSize(new Dimension((int)(framesize.width*0.80),(int)(framesize.height*0.3)));
        pan.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        JPanel nameformatter = new JPanel(new FlowLayout(FlowLayout.CENTER,0,15));
        JLabel nameTxt = new JLabel(pubToShow.getName());
        nameTxt.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        nameformatter.add(nameTxt);
        pan.add(nameformatter);

        JPanel infopan = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));


        String infostr = "<html> \tIn this order this pub is looking for a minimum of <strong>" + pubToShow.getMinAmount() + "</strong> litres, and a maximum of <strong>" + pubToShow.getMaxAmount() + "</strong> litres from beer in the style of <strong>";
        if(pubToShow.getTypes().size() == 1) {
            infostr += pubToShow.getTypes().get(0) + "</strong>.";
        }
        else if(pubToShow.getTypes().size() == 2) {
            infostr += pubToShow.getTypes().get(0) + "</strong> and <strong>" + pubToShow.getTypes().get(1) + "</strong>. </html>";
        }
        JLabel infoLab = new JLabel(infostr);
        infoLab.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        infopan.add(infoLab);

        infostr = "<html>They are willing to pay a decent price of <strong>";

        if(pubToShow.getTypes().size() == 1) {
            infostr += String.format("%,d",(int)(MainFrame.gData.styles.get(pubToShow.getTypes().get(0))* pubToShow.getBasePrice())) + "</strong> per 50 litres.";
        }
        else if(pubToShow.getTypes().size() == 2) {
            infostr += String.format("%,d",(int)(MainFrame.gData.styles.get(pubToShow.getTypes().get(0))* pubToShow.getBasePrice())) + "</strong> for <strong>"
                    + pubToShow.getTypes().get(0) + "</strong> and <strong>" + String.format("%,d",(int)(MainFrame.gData.styles.get(pubToShow.getTypes().get(1))* pubToShow.getBasePrice()))+
                    "</strong> for <strong>" + pubToShow.getTypes().get(1) + "</strong> per 50 litres.</html>";
        }
        JLabel infoLab2 = new JLabel(infostr);
        infoLab2.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLab2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        infopan.add(infoLab2);

        pan.add(infopan);



        List<String> beers= new ArrayList<>();
        for(String st: pubToShow.getTypes()) {
            for(BeerElement element: MainFrame.player.beers.values()) {
                if(element.getBeer().getStyle().equals(st) && element.getQuantity() >= pubToShow.getMinAmount()) {
                    beers.add(element.getBeer().getName());
                }
            }
        }
        JPanel rpan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,5));
        JLabel lab = new JLabel("Choose a beer and amount that you'd like to sell to the pub: ");
        rpan.add(lab);
        pan.add(rpan);

        JPanel actionpan = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,5));



        JLabel sum = new JLabel("= 0");

        JTextField txtField = new JTextField();
        txtField.setColumns(5);

        txtField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                change();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                change();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                change();
            }
            public void change() {
                if(txtField.getText().isEmpty()) {
                    sellquan = 0;
                    sum.setText("= 0");
                    sellButton.setEnabled(false);
                }
                else {
                    try {
                        double  q =Double.parseDouble(txtField.getText());
                        if(q < 0 || q > pubToShow.getMaxAmount()) {
                            JOptionPane.showMessageDialog(null, "Please give a valid number between "+ pubToShow.getMinAmount() +" and " + pubToShow.getMaxAmount()+ "!");
                            sellquan = 0;
                            sum.setText("= 0");
                            sellButton.setEnabled(false);
                        }
                        else if(q < pubToShow.getMinAmount()) {
                            sellquan = 0;
                            sum.setText("= 0");
                            sellButton.setEnabled(false);
                        }
                        else {
                            sellquan = (q/50);
                            sum.setText("= " + String.format("%,d",(int)(sellquan*price)));
                            sellButton.setEnabled(true);
                        }
                    } catch (NumberFormatException e ) {
                        JOptionPane.showMessageDialog(null, "Please give a valid number!");
                        sellquan = 0;
                        sum.setText("= 0");
                        sellButton.setEnabled(false);
                    }
                }
            }
        });

        JComboBox cbox = new JComboBox(beers.toArray());
        cbox.setSelectedItem(null);
        cbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenbeer = MainFrame.player.beers.get(cbox.getSelectedItem());
                price = (int) (pubToShow.getBasePrice() * MainFrame.gData.styles.get(chosenbeer.getBeer().getStyle()));
                sum.setText("= " + String.format("%,d",(int)(sellquan*price)));
                if(sellquan > 0) {
                    sellButton.setEnabled(true);
                }
            }
        });

        actionpan.add(txtField);
        actionpan.add(cbox);
        actionpan.add(sum);

        pan.add(actionpan);


        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        sellButton = new JButton("Sell!");
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chosenbeer.getQuantity() < sellquan*50) {
                    JOptionPane.showMessageDialog(null,"You don't have enough beer to fulfill this order:((");
                }
                else {
                    MainFrame.player.beers.get(chosenbeer.getBeer().getName()).setQuantity((int)(chosenbeer.getQuantity()-sellquan*50));
                    MainFrame.player.money += price;
                    MainFrame.updateData();
                    MainFrame.player.repuPoints += chosenbeer.getBeer().getPopularity()*sellquan*50;
                    System.out.println("outgeco: " +MainFrame.player.repuPoints);
                    close();

                }
            }
        });
        button.add(sellButton);
        pan.add(button);


        return pan;
    }
    public void close() {
        this.dispose();
        MainFrame.openMarket = false;
    }
}
