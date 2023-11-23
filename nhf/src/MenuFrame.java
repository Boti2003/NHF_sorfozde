import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFrame extends JFrame implements ActionListener {

    private PData selectedGamePlay;
    private JButton loadGame;
    private JButton newGame;
    private JTextField field;
    private List<JPanel> panels;

    public MenuFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(new Dimension((int)(MainFrame.screenSize.width*0.75), (int)(MainFrame.screenSize.height/2)));

        JPanel northPanel = new JPanel();
        JLabel wtext = new JLabel("Welcome to the game!");
        wtext.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
        northPanel.add(wtext);

        loadGame = new JButton("Load game!");
        loadGame.setPreferredSize(new Dimension(100, 20));
        loadGame.setAlignmentX(RIGHT_ALIGNMENT);
        loadGame.addActionListener(this);
        loadGame.setEnabled(false);


        JScrollPane listsrc = new JScrollPane(initLoadPanel());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        if(MainFrame.gData.players.isEmpty()) {
            JLabel label = new JLabel("Start a new game:))");
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
            label.setAlignmentX(CENTER_ALIGNMENT);
            centerPanel.add(label, BorderLayout.CENTER);
        }
        else {
            centerPanel.add(listsrc);
            centerPanel.add(loadGame);
        }

        JPanel newGamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 50));
        newGamePanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        JLabel txt = new JLabel("Name your future brewery:))");

        field = new JTextField();
        field.setColumns(30);
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();

            }
            public void warn() {
                if(!field.getText().isEmpty()) {
                    newGame.setEnabled(true);
                    for(PData p: MainFrame.gData.players)
                    {
                        if(p.gameName.equals(field.getText())) {
                            JOptionPane.showMessageDialog(null, "Brewery name already taken!");
                            newGame.setEnabled(false);
                        }
                    }
                }
                else {
                    newGame.setEnabled(false);
                }
            }
        });

        newGame = new JButton("Start a new game:))");
        newGame.addActionListener(this);
        newGame.setEnabled(false);
        newGamePanel.add(txt);
        newGamePanel.add(field);
        newGamePanel.add(newGame);



        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(newGamePanel, BorderLayout.SOUTH);
        this.add(Box.createRigidArea(new Dimension(100, 0)), BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(100, 0)), BorderLayout.WEST);


        this.setVisible(true);
        this.setLocationRelativeTo(null);



    }

    public JPanel initLoadPanel() {
        JPanel loadPanel = new JPanel();
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
        panels = new ArrayList<>();

        for(PData dat: MainFrame.gData.players) {
            JPanel pan = new JPanel();
            pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
            JLabel lab = new JLabel("Load game: "+ dat.gameName);
            lab.setAlignmentX(Component.CENTER_ALIGNMENT);
            pan.add(lab);
            pan.add(Box.createRigidArea(new Dimension(0, 5)));
            JLabel lab2 = new JLabel("Money: " + String.format("%,d",dat.money) + "; Date: "+ Data.startDate.plusWeeks(dat.turn).format(DateTimeFormatter.ofPattern("YYYY.MM.DD")));
            lab2.setAlignmentX(Component.CENTER_ALIGNMENT);
            pan.add(lab2);
            pan.setBorder(BorderFactory.createLineBorder(Color.black, 2));
            pan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            panels.add(pan);
            loadPanel.add(pan);
            loadPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            pan.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(JPanel p: panels) {
                        if(pan.equals(p)) {
                            pan.setBorder(BorderFactory.createLineBorder(Color.red, 3));
                            selectedGamePlay = dat;
                            loadGame.setEnabled(true);
                        }
                        else {
                            p.setBorder(BorderFactory.createLineBorder(Color.black, 2));
                        }
                    }



                }
            });
        }
        return loadPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(loadGame)) {
            MainFrame.player = selectedGamePlay;
            new MainFrame();
            this.dispose();
        }
        else if(e.getSource().equals(newGame)) {
            PData gamePlay = new PData(field.getText(), 0, 1000000, 1);
            gamePlay.tools.add(MainFrame.gData.tools.get(0));
            MainFrame.gData.players.add(gamePlay);
            for (String k: Data.keys) {
                gamePlay.materials.put(k, new HashMap<>());
            }
            MainFrame.player = gamePlay;
            new MainFrame();
            this.dispose();

        }

    }
}
