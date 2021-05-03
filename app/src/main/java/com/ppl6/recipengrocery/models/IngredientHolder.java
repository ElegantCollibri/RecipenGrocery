package com.ppl6.recipengrocery.models;

import com.ppl6.recipengrocery.util.Categories;
import com.ppl6.recipengrocery.util.Units;
import com.ppl6.recipengrocery.util.Units.Mass;
import com.ppl6.recipengrocery.util.Units.Volume;
import com.ppl6.recipengrocery.util.Units.Misc;
import com.ppl6.recipengrocery.util.Categories.Category;

public class IngredientHolder {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private String name;
  private double amount;
  private String unit;
  private String category;
  private boolean inStock = false;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public IngredientHolder(String name, Category category, double amount, Volume unit) {
    this.name = name;
    this.category = category.name();
    this.amount = amount;
    this.unit = unit.name();
  }

  public IngredientHolder(String name, Category category, double amount, Mass unit) {
    this.name = name;
    this.category = category.name();
    this.amount = amount;
    this.unit = unit.name();
  }

  public IngredientHolder(String name, Category category, double amount, Misc unit) {
    this.name = name;
    this.category = category.name();
    this.amount = amount;
    this.unit = unit.name();
  }

  public IngredientHolder(String name, String category, double amount, String unit) {
    this.unit = Units.getNameFromName(unit);
    this.category = Categories.getCategoryFromName(category).name();
    this.name = name;
    this.amount = amount;
  }

  public IngredientHolder() {
    name = "";
    category = "";
    amount = 0;
    unit = "";
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public double getAmount() {
    return amount;
  }

  public String getUnit() {
    return unit;
  }

  public boolean getInStock() {
    return inStock;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -

  public void setName(String name) {
    this.name = name;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }
}
