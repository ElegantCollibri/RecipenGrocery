package com.ppl6.recipengrocery.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ppl6.recipengrocery.room.entities.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertIngredient(Ingredient ingredient);

  @Update
  void updateIngredient(Ingredient ingredient);

  @Delete
  void deleteIngredient(Ingredient... ingredients);


  // - - - - - - - - - - - - - - - LiveData queries - - - - - - - - - - - - - - -

  @Query("select * from ingredient_table where recipe_id = :recipeId")
  LiveData<List<Ingredient>> getLiveIngredients(int recipeId);


  // - - - - - - - - - - - - - - - Non-LiveData queries - - - - - - - - - - - - - - -

  @Query("select name from ingredient_table where recipe_id = :recipeId")
  List<String> getIngredientNames(int recipeId);

  @Query("select * from ingredient_table where recipe_id = :recipeId")
  List<Ingredient> getIngredients(int recipeId);

  @Query("select * from ingredient_table")
  List<Ingredient> getAllIngredients();

  @Query("select * from ingredient_table where ingredient_id = :ingredientId limit 1")
  Ingredient getIngredient(int ingredientId);
}
