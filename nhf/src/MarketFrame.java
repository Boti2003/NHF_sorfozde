import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class MarketFrame extends JFrame {
    private Dimension framesize = new Dimension((int)(MainFrame.screenSize.width*0.75), (int)(MainFrame.screenSize.height*0.75));
    private Color pubFestPanBgColor = MyColors.orchid;



    public Map<Pub, PubSellData> sellPubs = new HashMap<>();
    public Map<Festival, FestSellData> sellFests = new HashMap<>();



    public MarketFrame() {
        MainFrame.openMarket = true;
        this.setTitle("Pubs and festivals");
        this.setResizable(false);
        JPanel centerPubPanel = new JPanel();
        centerPubPanel.setLayout(new BoxLayout(centerPubPanel, BoxLayout.Y_AXIS));

        for (Pub p: MainFrame.player.pubs) {
            centerPubPanel.add(pubPanel(p));
            centerPubPanel.add(Box.createRigidArea(new Dimension((int)(framesize.width*0.75), 20)));
        }

        JPanel centerFestPanel = new JPanel();
        centerFestPanel.setLayout(new BoxLayout(centerFestPanel, BoxLayout.Y_AXIS));
        for (Festival f: MainFrame.player.festivals) {
            centerFestPanel.add(festivalPanel(f));
            centerFestPanel.add(Box.createRigidArea(new Dimension((int)(framesize.width*0.75), 20)));
        }


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setMaximumSize(framesize);

        JScrollPane srcPubPan = new JScrollPane(centerPubPanel);
        srcPubPan.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        srcPubPan.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        srcPubPan.setSize((int)(framesize.width*0.83),framesize.height);
        JPanel centrePub = new JPanel();
        centrePub.setLayout(new BoxLayout(centrePub,BoxLayout.PAGE_AXIS));
        srcPubPan.setAlignmentX(Component.CENTER_ALIGNMENT);
        centrePub.add(srcPubPan);
        srcPubPan.validate();


        JScrollPane srcFestPan = new JScrollPane(centerFestPanel);
        srcFestPan.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        srcFestPan.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        srcFestPan.setSize(new Dimension((int)(framesize.width*0.83),framesize.height));
        JPanel centreFest = new JPanel();
        centreFest.setLayout(new BoxLayout(centreFest,BoxLayout.PAGE_AXIS));
        srcFestPan.setAlignmentX(Component.CENTER_ALIGNMENT);
        centreFest.add(srcFestPan);
        srcFestPan.validate();


        tabbedPane.addTab("Pubs",centrePub);
        tabbedPane.addTab("Beer Fests", centreFest);


        srcPubPan.revalidate();
        calibratePubListeners();
        calibrateFestListeners();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.openMarket = false;

            }
        });
        this.add(tabbedPane, BorderLayout.CENTER);

        this.pack();
        this.setSize(framesize);
        this.setVisible(true);
        this.setLocationRelativeTo(null);



    }

    private JPanel pubPanel(Pub pubToShow) {
        JPanel pan = new JPanel(new GridLayout(0,1));
        pan.setBackground(pubFestPanBgColor);
        pan.setMaximumSize(new Dimension((int)(framesize.width*0.80),(int)(framesize.height*0.3)));
        pan.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        JPanel nameformatter = new JPanel(new FlowLayout(FlowLayout.CENTER,0,15));
        nameformatter.setBackground(pubFestPanBgColor);
        JLabel nameTxt = new JLabel(pubToShow.getName());
        nameTxt.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        nameformatter.add(nameTxt);
        pan.add(nameformatter);

        JPanel infopan = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        infopan.setBackground(pubFestPanBgColor);

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




        JPanel rpan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,5));
        rpan.setBackground(pubFestPanBgColor);
        JLabel lab = new JLabel("Choose a beer and amount that you'd like to sell to the pub: ");
        rpan.add(lab);
        pan.add(rpan);

        JPanel actionpan = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,5));
        actionpan.setBackground(pubFestPanBgColor);


        JLabel sum = new JLabel("= 0");

        JTextField txtField = new JTextField();
        txtField.setColumns(5);


        List<String> beers= new ArrayList<>();
        for(String st: pubToShow.getTypes()) {
            for(BeerElement element: MainFrame.player.beers.values()) {
                if(element.getBeer().getStyle().equals(st) && element.getQuantity() >= pubToShow.getMinAmount()) {
                    beers.add(element.getBeer().getName());
                }
            }
        }
        JComboBox cbox = new JComboBox(beers.toArray());
        cbox.setSelectedItem(null);
        actionpan.add(txtField);
        actionpan.add(cbox);
        actionpan.add(sum);
        pan.add(actionpan);


        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        button.setBackground(pubFestPanBgColor);
        JButton sell = new JButton("Sell!");
        sell.setEnabled(false);
        button.add(sell);
        sellPubs.put(pubToShow, new PubSellData(sell, txtField, cbox, sum));

        pan.add(button);

        return pan;
    }
    private void calibratePubListeners() {
        for(Pub p: sellPubs.keySet()) {
            PubSellData thisData = sellPubs.get(p);
            thisData.tfield.getDocument().addDocumentListener(new DocumentListener() {
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

                    if(thisData.tfield.getText().isEmpty()) {
                        thisData.sellquan = 0;
                        thisData.sum.setText("= 0");
                        thisData.pubButton.setEnabled(false);

                    }
                    else {
                        try {
                            double  q = Double.parseDouble(thisData.tfield.getText());
                            if(q < 0 || q > p.getMaxAmount()) {
                                JOptionPane.showMessageDialog(null, "Please give a valid number between "+ p.getMinAmount() +" and " + p.getMaxAmount()+ "!");
                                thisData.sellquan = 0;
                                thisData.sum.setText("= 0");
                                thisData.pubButton.setEnabled(false);
                            }
                            else if(q < p.getMinAmount()) {
                                thisData.sellquan = 0;
                                thisData.sum.setText("= 0");
                                thisData.pubButton.setEnabled(false);
                            }
                            else {
                                thisData.sellquan = (q/50);
                                thisData.sum.setText("= " + String.format("%,d",(int)(thisData.sellquan* thisData.price)));
                                if(!Objects.isNull(thisData.chosenBeer)) {
                                    thisData.pubButton.setEnabled(true);
                                }
                            }
                        } catch (NumberFormatException e ) {
                            JOptionPane.showMessageDialog(null, "Please give a valid number!");
                            thisData.sellquan = 0;
                            thisData.sum.setText("= 0");
                            thisData.pubButton.setEnabled(false);
                        }
                    }
                }
            });
            thisData.choser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    thisData.chosenBeer = MainFrame.player.beers.get(thisData.choser.getSelectedItem());
                    thisData.price = (int) (p.getBasePrice() * MainFrame.gData.styles.get(thisData.chosenBeer.getBeer().getStyle()));
                    thisData.sum.setText("= " + String.format("%,d",(int)(thisData.sellquan*thisData.price)));

                    if(thisData.sellquan > 0) {
                        thisData.pubButton.setEnabled(true);
                    }
                }
            });
            thisData.pubButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(thisData.chosenBeer.getQuantity() < thisData.sellquan*50) {
                            JOptionPane.showMessageDialog(null,"You don't have enough beer to fulfill this order:((");
                        }
                        else {
                            MainFrame.player.beers.get(thisData.chosenBeer.getBeer().getName()).setQuantity((int)(thisData.chosenBeer.getQuantity()- thisData.sellquan*50));
                            MainFrame.player.money += (int)(thisData.price* thisData.sellquan);
                            MainFrame.updateData();
                            MainFrame.player.repuPoints += thisData.chosenBeer.getBeer().getPopularity()*thisData.sellquan*50;
                            System.out.println("outgeco: " +MainFrame.player.repuPoints);
                            close();

                        }
                }
            });
        }
    }

    private void calibrateFestListeners() {
        for(Festival f: sellFests.keySet()) {
            FestSellData thisData = sellFests.get(f);
            thisData.choser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    thisData.chosenBeer = MainFrame.player.beers.get(thisData.choser.getSelectedItem());
                    thisData.festButton.setEnabled(true);
                }
            });
            thisData.festButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.player.beers.get(thisData.chosenBeer.getBeer().getName()).setQuantity((int)(thisData.chosenBeer.getQuantity()-f.getBeerAmount()));
                    MainFrame.player.money += f.getMoney();
                    MainFrame.updateData();
                    MainFrame.player.repuPoints += thisData.chosenBeer.getBeer().getPopularity()*f.getBeerAmount();
                    MainFrame.player.festivals.remove(f);
                    close();
                }
            });
        }
    }



    private JPanel festivalPanel(Festival festivalToShow) {
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.setBackground(pubFestPanBgColor);
        pan.setMaximumSize(new Dimension((int)(framesize.width*0.80),(int)(framesize.height*0.4)));
        pan.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));

        JPanel nameformatter = new JPanel(new FlowLayout(FlowLayout.CENTER,0,15));
        nameformatter.setBackground(pubFestPanBgColor);
        JLabel nameTxt = new JLabel(festivalToShow.getName());
        nameTxt.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        nameformatter.add(nameTxt);
        pan.add(nameformatter);

        JPanel infopan = new JPanel();
        infopan.setLayout(new BoxLayout(infopan, BoxLayout.Y_AXIS));
        infopan.setBackground(pubFestPanBgColor);
        infopan.setAlignmentX(Component.CENTER_ALIGNMENT);

        infopan.add(getCenterText("<html> \tThis is the annual <strong>" + festivalToShow.getName() + "</strong> and this year you brewery got invited:)) They are looking for <strong>" + festivalToShow.getBeerAmount() + "</strong> litres of beer in the style of <strong>"
                + festivalToShow.getTypes().get(0) + "</strong> or <strong>" + festivalToShow.getTypes().get(1) + "</strong>. </html>"));
        infopan.add(getCenterText("<html>They are willing to pay a decent price of <strong>" +
                String.format("%,d",(int)(festivalToShow.getMoney())) + "</strong> for <strong>" + festivalToShow.getBeerAmount() +
                "</strong> litres of beer.</html>"));
        infopan.add(getCenterText("<html> You can fulfill this order before the start date of the festival: <strong>"+MainFrame.generateDate(festivalToShow.getDate())+"</strong></html>"));


        pan.add(infopan);



        List<String> beers= new ArrayList<>();
        for(String st: festivalToShow.getTypes()) {
            for(BeerElement element: MainFrame.player.beers.values()) {
                if(element.getBeer().getStyle().equals(st) && element.getQuantity() >= festivalToShow.getBeerAmount()) {
                    beers.add(element.getBeer().getName());
                }
            }
        }
        JPanel rpan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,5));
        rpan.setBackground(pubFestPanBgColor);
        JLabel lab = new JLabel("Choose a beer that you would like to sell on the festival: ");
        lab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        rpan.add(lab);
        pan.add(rpan);

        JPanel actionpan = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,5));
        actionpan.setBackground(pubFestPanBgColor);

        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        button.setBackground(pubFestPanBgColor);
        JButton sellFestButton = new JButton("Sell!");
        sellFestButton.setEnabled(false);
        button.add(sellFestButton);

        JComboBox cbox = new JComboBox(beers.toArray());
        cbox.setSelectedItem(null);
        sellFests.put(festivalToShow, new FestSellData(sellFestButton, cbox));
        actionpan.add(cbox);
        pan.add(actionpan);
        pan.add(button);
        return pan;
    }

    private JPanel getCenterText(String txtToshow) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        row.setBackground(pubFestPanBgColor);
        JLabel infoLab = new JLabel(txtToshow);
        infoLab.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        row.add(infoLab);
        return row;
    }

    public void close() {
        this.dispose();
        MainFrame.openMarket = false;
    }
}

