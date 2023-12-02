import com.sun.tools.javac.Main;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

        manageBrewing();
        managePopularity();
        manageMarket();
        MainFrame.updateData();
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
                    JOptionPane.showMessageDialog(null, "The brewing of the " + t.getActualBrewing().getQuantity() + "litres of " + finishedBeerRecipe.getName() +" is finished now:)) ");
                    t.setActualBrewing(null);
                }

            }
        }
    }

    private static void managePopularity() {
        int k = MainFrame.player.repuPoints / 500;
        if(k> 0) {
            for(int i = 0;i < k; i++) {
                MainFrame.player.repuPoints -= 500;
                Random rand = new Random();
                int random = rand.nextInt(0,100);
                if(random <10) {
                    MainFrame.player.reputation += 0.3;
                }
                else if (random > 10 && random < 30) {
                    MainFrame.player.reputation += 0.2;
                }
                else {
                    MainFrame.player.reputation += 0.1;
                }
            }

        }
        else {
            Random rand = new Random();
            int random = rand.nextInt(0,100);
            if(random < 10) {
                MainFrame.player.reputation -=0.1;
            }
            else if ( random >10 && random < 15) {
                MainFrame.player.reputation += 0.1;
            }
        }

    }

    public static void manageMarket() {
        for (Pub p : MainFrame.gData.pubs) {
            if(p.getMinPopularity() <= MainFrame.player.reputation && !MainFrame.player.pubs.contains(p)) {
                MainFrame.player.pubs.add(p);
                JOptionPane.showMessageDialog(null, "A pub just found your brewery with a business deal:)) Go check it out!!");
            }
        }


        List<String> fnames = new ArrayList<>();
        LocalDate actDate = Data.startDate.plusWeeks(MainFrame.player.turn);
        for(Festival fes: MainFrame.player.festivals) {
            fnames.add(fes.getName());
        }
        for(Festival f: MainFrame.gData.festivals) {
            actDate = Data.startDate.plusWeeks(MainFrame.player.turn);
            LocalDate festDate = LocalDate.of(actDate.getYear(),f.getDate().getMonth().getValue(), f.getDate().getDayOfMonth());

            if(f.getMinPopularity() <= MainFrame.player.reputation && !fnames.contains(f.getName()) && festDate.plusWeeks(-6).isBefore(actDate) && !festDate.isBefore(actDate)) {
                MainFrame.player.festivals.add(new Festival(f.getName(), festDate, f.getBeerAmount(), f.getTypes(),f.getMoney(), f.getMinPopularity()));
                JOptionPane.showMessageDialog(null, "A new festival came to town:)) Go check it out!!");

            }
        }
        List<Festival> festToDelete = new ArrayList<>();
        for (Festival fest: MainFrame.player.festivals) {
            LocalDate festDate = LocalDate.of(actDate.getYear(),fest.getDate().getMonth().getValue(), fest.getDate().getDayOfMonth());
            if(festDate.isBefore(actDate)) {
                festToDelete.add(fest);
                JOptionPane.showMessageDialog(null, "Unfortunately the " + fest.getName() + " has started, this year without you for sure:(( But don't be too sad, its held every year:))");
            }
        }
        MainFrame.player.festivals.removeAll(festToDelete);
        System.out.println(MainFrame.player.festivals.size());
    }
}
