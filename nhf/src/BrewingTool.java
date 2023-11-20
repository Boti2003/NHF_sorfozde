import java.io.Serializable;

public class BrewingTool implements Serializable {
    private BrewingProcess actualBrewing = null;
    private int maintenanceCost;
    private int costToBuy;
    private int capacity;
    private int level;

    public BrewingProcess getActualBrewing() {
        return actualBrewing;
    }

    public void setActualBrewing(BrewingProcess actualBrewing) {
        this.actualBrewing = actualBrewing;
    }

    public BrewingTool(int maintenanceCost, int costToBuy, int capacity, int level) {
        this.maintenanceCost = maintenanceCost;
        this.costToBuy = costToBuy;
        this.capacity = capacity;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    public int getCostToBuy() {
        return costToBuy;
    }

    public int getCapacity() {
        return capacity;
    }
}
