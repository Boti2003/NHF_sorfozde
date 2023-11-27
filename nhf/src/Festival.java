import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Festival implements Serializable {
 private String name;
 private LocalDate date;
 private int beerAmount;
 private List<String> types;
 private int money;
 private double minPopularity;

 public Festival(String name, LocalDate date, int beerAmount, List<String> types, int money, double minPopularity) {
  this.name = name;
  this.date = date;
  this.beerAmount = beerAmount;
  this.types = types;
  this.money = money;
  this.minPopularity = minPopularity;
 }

 public String getName() {
  return name;
 }

 public LocalDate getDate() {
  return date;
 }

 public int getBeerAmount() {
  return beerAmount;
 }

 public List<String> getTypes() {
  return types;
 }

 public int getMoney() {
  return money;
 }

 public double getMinPopularity() {
  return minPopularity;
 }
}
