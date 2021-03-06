package com.ppl6.recipengrocery.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.ppl6.recipengrocery.util.Units;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    tableName = "ingredient_table",
    foreignKeys = {
    @ForeignKey(entity = Summary.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = CASCADE)},
    indices = {@Index(value = "recipe_id")})
public class Ingredient {

  // - - - - - - - - - - - - - - - DB columns - - - - - - - - - - - - - - -

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "ingredient_id")
  private int ingredientId;

  @ColumnInfo(name = "recipe_id")
  private int recipeId;

  @NonNull
  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "category")
  private String category;

  @ColumnInfo(name = "amount")
  private double amount;

  @ColumnInfo(name = "units")
  private String units;

  @ColumnInfo(name = "in_stock")
  private boolean inStock;


  // - - - - - - - - - - - - - - - Constructor(s) - - - - - - - - - - - - - - -

  @Ignore
  private Ingredient(int recipeId, double amount, String units) {
    this.recipeId = recipeId;
    this.amount = amount;
    this.units = units;
    inStock = false;
    name = "No name";
  }

  public Ingredient(String name, String category, int recipeId, double amount, String units) {
    this.name = name;
    this.recipeId = recipeId;
    this.amount = amount;
    this.units = units;
    this.category = category;
    inStock = false;
  }

  @Ignore
  public Ingredient(String name, int recipeId, double amount, String units) {
    this.name = name;
    this.recipeId = recipeId;
    this.amount = amount;
    this.units = units;
    category = "Uncategorised";
    inStock = false;
  }


  // - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - -

  public int getIngredientId() {
    return ingredientId;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public double getAmount() {
    return amount;
  }

  public String getUnits() {
    return units;
  }

  public boolean getInStock() {
    return inStock;
  }


  // - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - -


  public void setIngredientId(int ingredientId) {
    this.ingredientId = ingredientId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setUnits(Units.Volume units) {
    this.units = units.name();
  }

  public void setUnits(Units.Mass units) {
    this.units = units.name();
  }

  public void setUnits(Units.Misc units) {
    this.units = units.name();
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }

}
