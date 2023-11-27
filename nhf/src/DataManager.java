import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataManager {
    static File rfile = new File("recipes.txt"); //recept txt
    static File pfile = new File("pubs.txt");
    static File ffile = new File("festivals.txt");
    static File sfile = new File("styles.txt");
    static File dfile = new File("general_data"); //general data sorositott file
    public static void buildDataStructure() throws IOException, ClassNotFoundException {

        if(!dfile.exists()) {
            for (int i = 0; i < Data.keys.length; i++) {
                MainFrame.gData.materials.put(Data.keys[i], readMatfile(Data.keys[i] +".txt"));
            }
            readRecipefile();
            readPubFile();
            readFestFile();
            readStyleFile();
            initBrewingTools();
        }
        else {
            FileInputStream fin = new FileInputStream(dfile);
            ObjectInputStream in = new ObjectInputStream(fin);
            MainFrame.gData = (Data) in.readObject();
            in.close();

        }
    }
    public static void initBrewingTools() {
        MainFrame.gData.tools.add(new BrewingTool(50000, 150000, 250, 1));
        MainFrame.gData.tools.add(new BrewingTool(75000, 250000, 500, 2));
        MainFrame.gData.tools.add(new BrewingTool(100000, 400000, 1000, 3));
    }
    public static void readStyleFile() throws FileNotFoundException {
        Scanner fSc = new Scanner(sfile);
        while (fSc.hasNext()) {
            String[] split = fSc.nextLine().split("\t");
            MainFrame.gData.styles.put(split[0], Double.parseDouble(split[1]));
        }
    }

    public static void readPubFile() throws FileNotFoundException {
        Scanner fSc = new Scanner(pfile);
        while(fSc.hasNext()) {
            String[] temp = fSc.nextLine().split("\t");
            MainFrame.gData.pubs.add(new Pub(temp[0],Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Arrays.stream(temp[3].split(";")).toList(), Integer.parseInt(temp[4]), Double.parseDouble(temp[5])));
        }

    }

    public static void readFestFile() throws FileNotFoundException {
        Scanner fSc = new Scanner(ffile);
        while(fSc.hasNext()) {
            String[] temp = fSc.nextLine().split("\t");
            String[] date = temp[1].split(";");
            MainFrame.gData.festivals.add(new Festival(temp[0], LocalDate.of(2020,Integer.parseInt(date[0]),Integer.parseInt(date[1])), Integer.parseInt(temp[2]), Arrays.stream(temp[3].split(";")).toList(), Integer.parseInt(temp[4]), Double.parseDouble(temp[5])));
        }

    }




    public static void readRecipefile() throws FileNotFoundException {
        Scanner fSc = new Scanner(rfile);
        while(fSc.hasNext()) {
            String[] temp = fSc.nextLine().split("\t");
            MainFrame.gData.recipes.put(temp[0], new Recipe(temp[0], temp[1], Integer.parseInt(temp[2]), Double.parseDouble(temp[3]), Double.parseDouble(temp[4]), Integer.parseInt(temp[5])));
            for (int i =0; i < Data.keys.length; i++) {
                String[] mats = temp[6+i].split(";");
                for(String mat: mats) {
                    String[] elements =mat.split(":");
                    MainFrame.gData.recipes.get(temp[0]).materials.get(Data.keys[i]).put(elements[0], new MatElement(MainFrame.gData.materials.get(Data.keys[i]).get(elements[0]),Double.parseDouble(elements[1])));
                }
            }
        }

    }
    public static HashMap<String, Material> readMatfile(String file) throws FileNotFoundException {
        HashMap<String, Material> materialMap = new HashMap<>();
        File matData = new File(file);
        Scanner filescan = new Scanner(matData);
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
        return materialMap;
    }

    public static void saveData() throws IOException {
        FileOutputStream fout = new FileOutputStream(dfile);
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(MainFrame.gData);
        out.close();
    }


}
