import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Data implements Serializable {

    Map<String, HashMap<String, Material>> materials = new HashMap<String,HashMap<String,Material>>();
    Map<String, Recipe> recipes = new HashMap<String, Recipe>();
    List<BrewingTool> tools = new ArrayList<BrewingTool>();
    static String[] keys = { "malts", "hops", "yeasts", "fruits", "spices"};
    static LocalDate startDate = LocalDate.of(2020, 1, 1);
    public Data() {
        for(int i = 0; i < keys.length; i++) {
            materials.put(keys[i], new HashMap<String, Material>());
        }
    }



    //esemenyek, pubok







}
