import java.io.Serializable;

public class BrewingTool implements Serializable {
    BrewingProcess actualBrewing;
    private int maintenanceCost;
    private int costToBuy;
    private int capacity;
    private int level;

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
