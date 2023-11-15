import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static Data gData = new Data();
    public static PData player = new PData();

    public static void main(String[] args) {
        /*Malt amarillo = new Malt(2,"Amarillo");
        System.out.println(amarillo.getQuantity());
        amarillo.setQuantity(3);
        System.out.println(amarillo.getQuantity());*/

        /*Malt malt = new Malt(23, "Amarillo");

        List<Material> ls = new ArrayList<Material>();
        ls.add(malt);
        ls.get(0).name = "kutyus";
        System.out.println(malt.name);*/
        player.tools.add(new BrewingTool(100, 1000, 250,3));
        player.gameName= "Downtown Brewery";
        player.money = 100000000;
        MainFrame mainFrame = new MainFrame(player);
        mainFrame.adjustAfterRendered();







        try {
            DataManager.buildDataStructure();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        try {
            FileOutputStream fout = new FileOutputStream(new File("proba"));
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(Main.gData);
            out.close();
            FileInputStream fin = new FileInputStream(new File("proba"));
            ObjectInputStream in = new ObjectInputStream(fin);
            Main.gData = (Data) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        for(String key: Data.keys) {
            player.materials.put(key, new HashMap<String, MatElement>());
            for (Material mat : gData.materials.get(key).values()) {
                player.materials.get(key).put(mat.getName(), new MatElement(mat, 100));
            }
        }


        for (int i = 0; i < Data.keys.length; i++) {
            System.out.println(Data.keys[i]);
            for (String k : Main.gData.materials.get(Data.keys[i]).keySet()) {
                Material mat = Main.gData.materials.get(Data.keys[i]).get(k);
                System.out.println("\t\t"+ k +": " + mat.name + " " + mat.price + " " + mat.unit);
            }
        }
        for(Recipe rec: gData.recipes.values()) {
            System.out.println(rec.getName() + " " + rec.getStyle() + " "+ rec.getPrice() + " "+ rec.getAlcohol() + "% "+ rec.getPopularity() + " " + rec.getBrewTurn() +" weeks");
            for(String k: rec.materials.keySet()) {
                System.out.println(k + ":");
                for(MatElement mat: rec.materials.get(k).values()) {
                    System.out.println( "\t"+ mat.getQuantity() + " " + mat.getMat().unit + " " +mat.getMat().name + " "+ mat.getMat().price);
                }
            }

        }
    }
}