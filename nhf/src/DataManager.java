import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataManager {


    static File rfile = new File("recipes.txt"); //recept txt
    static File efile; //esemenyek txt
    static File dfile = new File("general_data"); //general data sorositott file
    static File pdfile; // player data sorositott file
    static File tfile; // eszkozok file
    public static void buildDataStructure() throws FileNotFoundException {

        if(!dfile.exists()) {
            for (int i = 0; i < Data.keys.length; i++) {
                readMatfile(Data.keys[i] +".txt", Main.gData.materials.get(Data.keys[i]));
            }
            readRecipefile(rfile);
        }




    }
    public static void readRecipefile(File rFile) throws FileNotFoundException {
        Scanner fSc = new Scanner(rFile);
        int determinate = Integer.parseInt(fSc.nextLine());
        while(fSc.hasNext()) {
            String[] temp = fSc.nextLine().split("\t");
            Main.gData.recipes.put(temp[0], new Recipe(temp[0], temp[1], Integer.parseInt(temp[2]), Double.parseDouble(temp[3]), Double.parseDouble(temp[4]), Integer.parseInt(temp[5])));
            for (int i =0; i < Data.keys.length; i++) {
                String[] mats = temp[6+i].split(";");
                for(String mat: mats) {
                    String[] elements =mat.split(":");
                    Main.gData.recipes.get(temp[0]).materials.get(Data.keys[i]).put(elements[0], new MatElement(Main.gData.materials.get(Data.keys[i]).get(elements[0]),Double.parseDouble(elements[1])));
                }
            }
        }

    }
    public static void readMatfile(String file, Map<String, Material> materialMap) throws FileNotFoundException {
        File matData = new File(file);
        Scanner filescan = new Scanner(matData);
        int determinate = Integer.parseInt(filescan.nextLine());
        while(filescan.hasNext()) {
            String[] temp = filescan.nextLine().split("\t");
            switch(file) {
                case "hops.txt":
                    materialMap.put(temp[0], new Hop(Integer.parseInt(temp[1]),temp[0]));
                    break;
                case "malts.txt":
                    materialMap.put(temp[0], new Malt(Integer.parseInt(temp[1]),temp[0]));
                    break;
                case "yeasts.txt":
                    materialMap.put(temp[0], new Yeast(Integer.parseInt(temp[1]),temp[0]));
                    break;
                case "fruits.txt":
                    materialMap.put(temp[0], new Fruit(Integer.parseInt(temp[1]),temp[0]));
                    break;
                case "spices.txt":
                    materialMap.put(temp[0], new Spice(Integer.parseInt(temp[1]),temp[0]));
                    break;

            }

        }
    }

}
