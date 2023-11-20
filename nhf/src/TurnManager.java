import java.util.Objects;

public class TurnManager {

    public static void newTurn() {
        MainFrame.recipeFrame.dispose();
        MainFrame.toolFrame.dispose();
        MainFrame.storageFrame.dispose();
        MainFrame.marketFrame.dispose();
        MainFrame.shopFrame.dispose();
        MainFrame.openShop = false;
        MainFrame.openTools = false;
        MainFrame.openMarket = false;
        MainFrame.openStorage = false;
        MainFrame.openRecipe= false;

        MainFrame.player.turn += 1;
        if(MainFrame.player.turn % 4 == 0) {
            int sum =0;
            for(BrewingTool t: MainFrame.player.tools) {
                sum += t.getMaintenanceCost();
            }
            MainFrame.player.money -= sum;
        }
        MainFrame.updateData();
        manageBrewing();
        managePopularity();
        manageMarket();

    }

    private static void manageBrewing() {
        for(BrewingTool t: MainFrame.player.tools) {
            if(!Objects.isNull(t.getActualBrewing())) {
                t.getActualBrewing().setActualTurn(t.getActualBrewing().getActualTurn()+1);
                if(t.getActualBrewing().getActualTurn() >= t.getActualBrewing().getBrewingBeer().getBrewTurn()) {
                    Recipe finishedBeerRecipe = t.getActualBrewing().getBrewingBeer();
                    if(MainFrame.player.beers.containsKey(finishedBeerRecipe.getName()))
                    {
                        MainFrame.player.beers.get(finishedBeerRecipe.getName()).setQuantity(MainFrame.player.beers.get(finishedBeerRecipe.getName()).getQuantity() + t.getActualBrewing().getQuantity());
                    }
                    else {
                        MainFrame.player.beers.put(finishedBeerRecipe.getName(), new BeerElement(t.getActualBrewing().getQuantity(), new Beer(finishedBeerRecipe.getName(), finishedBeerRecipe.getStyle(), finishedBeerRecipe.getPopularity(), finishedBeerRecipe.getAlcohol())));
                    }
                    t.setActualBrewing(null);
                }

            }
        }
    }

    private static void managePopularity() {

    }

    private static void manageMarket() {

    }
}
