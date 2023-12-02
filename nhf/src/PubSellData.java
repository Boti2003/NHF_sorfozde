import javax.swing.*;

public class PubSellData {
    JButton pubButton;
    JTextField tfield;
    JComboBox choser;
    double sellquan = 0;
    BeerElement chosenBeer;
    JLabel sum;
    int price = 0;

    public PubSellData(JButton pubButton, JTextField tfield, JComboBox choser, JLabel sum) {
        this.pubButton = pubButton;
        this.tfield = tfield;
        this.price = 0;
        this.sellquan = 0;
        this.chosenBeer = null;
        this.choser = choser;
        this.sum = sum;

    }
}