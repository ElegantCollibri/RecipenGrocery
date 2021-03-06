package com.ppl6.recipengrocery.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ppl6.recipengrocery.models.IngredientHolder;
import com.ppl6.recipengrocery.models.RecipeHolder;
import com.ppl6.recipengrocery.room.MaterRepository;
import com.ppl6.recipengrocery.util.Units.Mass;
import com.ppl6.recipengrocery.util.Units.Volume;
import com.ppl6.recipengrocery.util.Units.Misc;
import com.ppl6.recipengrocery.util.Categories.Category;

import java.util.ArrayList;
import java.util.List;

public class MaterApplication extends Application {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;

  private AsyncTaskScheduler taskScheduler = new AsyncTaskScheduler();

  private final String sharedPrefFile = "com.gerardbradshaw.mater";
  private final String FIRST_LAUNCH = "launched";


  // - - - - - - - - - - - - - - - Application methods - - - - - - - - - - - - - - -

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize the repo and shared prefs
    repository = new MaterRepository(this);
    SharedPreferences sharedPrefs = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);

    // Check if the application has been launched before. If not, create some recipes.
    boolean isFirstLaunch = sharedPrefs.getBoolean(FIRST_LAUNCH, true);
    if (isFirstLaunch) {

      // Updated the firstLaunched status
      SharedPreferences.Editor prefEditor = sharedPrefs.edit();
      prefEditor.putBoolean(FIRST_LAUNCH, false);
      prefEditor.apply();

      // Create the default recipes
      RecipeHolder lasagneRecipeHolder = createLasagneRecipeHolder();
      RecipeHolder curryRecipeHolder = createCurryRecipeHolder();
      RecipeHolder satayRecipeHolder = createSatayRecipeHolder();

      // Add the recipes to the database
      repository.insertRecipeFromHolder(satayRecipeHolder);
      repository.insertRecipeFromHolder(curryRecipeHolder);
      repository.insertRecipeFromHolder(lasagneRecipeHolder);

      // Store the lasagne image
      int lasagneImageId = this.getResources().getIdentifier(
          "img_lasagne", "drawable", getPackageName());
      repository.storeBitmap(lasagneRecipeHolder.getTitle(), lasagneImageId);

      // Store the curry image
      int curryImageId = this.getResources().getIdentifier(
          "img_curry", "drawable", getPackageName());
      repository.storeBitmap(curryRecipeHolder.getTitle(), curryImageId);

    }
  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  private RecipeHolder createLasagneRecipeHolder() {

    // Create a new RecipeHolder object
    RecipeHolder holder = new RecipeHolder();

    // Set the title and description of the holder
    holder.setTitle("Vegan Lasagne");
    holder.setDescription("A delicious comfort food that will leave you thinking \"I can't believe I just ate a whole lasagne\".");
    holder.setImageDirectory("Default image");
    holder.setServings(8);

    // Create the cooking steps
    List<String> steps = new ArrayList<>();
    steps.add("Dice the sweet potato, zucchini, and capsicum into small cubes.");
    steps.add("Saut?? diced vegetables in large fry pan on medium-high temperature for 10 minutes or until sweet potato has softened.");
    steps.add("Add diced mater and Beyond burgers to pan. Break up the burger patties and combine.");
    steps.add("Add red wine and set pan to simmer, stirring occasionally.");
    steps.add("While vegetables are simmering, line baking dish with lasagne sheets and place Vegenaise into a snap-lock bag.");
    steps.add("When most of the juice has boiled away (some juice is desirable to properly soften the lasagne sheets), use a spoon to spread a thin layer of the mix over the lasagne sheets.");
    steps.add("Cover the mix with cheese. Add another 2 layers of lasagne sheets, vegetable mix, and cheese as before.");
    steps.add("Make a small cut in the corner of the snap-lock bag and squeeze (pipe) the vegenaise over the top layer of cheese.");
    steps.add("Bake for ~30 minutes at 215??C (420??F) or until golden brown on top.");
    steps.add("Allow lasagne to cool for 5-10 minutes and slice into desired portion sizes.");
    steps.add("Enjoy!");

    // Add the steps to the holder
    holder.setSteps(steps);

    // Create a IngredientHolder object
    List<IngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String beyondBurgers = "Beyond burgers";
    String lasagneSheets = "lasagne sheets";
    String vegenaise = "Vegenaise";

    // Add each ingredient to the list
    ingredients.add(new IngredientHolder(
        "sweet potato", Category.FRESH_FRUIT_AND_VEG, 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "capsicum", Category.FRESH_FRUIT_AND_VEG, 1d, Misc.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "zucchini", Category.FRESH_FRUIT_AND_VEG, 1d, Misc.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "frozen spinach", Category.COLD_FOOD, 100d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "diced tomato", Category.CANNED_GOODS, 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        beyondBurgers, Category.COLD_FOOD, 4d, Misc.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "merlot", Category.BEVERAGES, 500d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        lasagneSheets, Category.RICE_AND_PASTA, 1d, Misc.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "vegan cheese slices", Category.COLD_FOOD, 18d, Misc.NO_UNIT));

    ingredients.add(new IngredientHolder(
        vegenaise, Category.COLD_FOOD, 100d, Mass.GRAMS));

    // Add the Ingredients to the Recipe
    holder.setIngredientHolders(ingredients);

    // Return the holder
    return holder;

  }

  private RecipeHolder createCurryRecipeHolder() {

    // Create a new RecipeHolder object
    RecipeHolder holder = new RecipeHolder();

    holder.setTitle("Tikka Masala Curry");
    holder.setDescription("Tired of hot curries? Try this bad boy; not too spicy, not too weak.");
    holder.setImageDirectory("Default image");
    holder.setServings(20);

    // Create the cooking steps
    List<String> steps = new ArrayList<>();
    steps.add("Prepare steam pot on hotplate.");
    steps.add("Dice the carrots and potatoes and add them to the steam pot.");
    steps.add("Dice the tofu.");
    steps.add("Add tofu, bamboo shoots, water, and curry sauce to a large pot. Simmer on low temperature.");
    steps.add("Steam the broccoli in the microwave per packet directions.");
    steps.add("Prepare rice in rice cooker or stove and turn on.");
    steps.add("Add steamed potatoes, carrots, and broccoli to the curry pot when softened and simmer for 20 minutes, stirring frequently.");
    steps.add("Add coconut milk and simmer for a further 10 minutes on a very low temperature. Stir frequently.");
    steps.add("Enjoy!");

    // Add the steps to the recipe
    holder.setSteps(steps);

    // Create the IngredientsHolder object
    List<IngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String tofu = "firm tofu";
    String curryPaste = "Patak's concentrated Tikka Masala curry paste";
    String coconutMilk = "coconut milk";

    // Add each ingredient to the list
    ingredients.add(new IngredientHolder(
        "rice (dry)", Category.RICE_AND_PASTA, 5d, Volume.AU_CUPS));

    ingredients.add(new IngredientHolder(
        tofu, Category.COLD_FOOD, 454d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "frozen broccoli", Category.COLD_FOOD, 454d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "carrots", Category.FRESH_FRUIT_AND_VEG, 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "potatoes", Category.FRESH_FRUIT_AND_VEG, 800d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "bamboo shoots", Category.CANNED_GOODS, 225d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        curryPaste, Category.INTERNATIONAL_FOOD, 566d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        coconutMilk, Category.CANNED_GOODS, 600d, Volume.MILLILITRES));

    // Add the list to the RecipeHolder
    holder.setIngredientHolders(ingredients);

    return holder;
  }

  private RecipeHolder createSatayRecipeHolder() {

    // Create a new RecipeHolder object
    RecipeHolder holder = new RecipeHolder();

    holder.setTitle("Tofu Satay");
    holder.setDescription("Smooth, nutty, and just the right amount of fantastic.");
    holder.setImageDirectory("Default image");
    holder.setServings(12);

    // Create the cooking steps
    List<String> steps = new ArrayList<>();
    steps.add("Dice tofu into cubes and fry in saucepan on low temperature. Turn and cook without oil until golden brown.");
    steps.add("Wash and slice bok-choy into 1/2 inch pieces. Set aside.");
    steps.add("Boil water in a medium pot and add cook pasta per packet directions. Set aside once finished.");
    steps.add("Set tofu aside and saut?? capsicum and broccoli in the saucepan.");
    steps.add("Add bok-choy to saucepan during final 5 minutes of saut??. All vegetables should be hot and crispy.");
    steps.add("For the sauce, add peanut butter, soy sauce, sesame oil, lime juice, and water to a microwave safe pourer and microwave on high for 3 minutes.");
    steps.add("Blend sauce with electric mixer until it makes a smooth paste.");
    steps.add("Add pasta, vegetables, tofu, and sauce to a bowl and serve.");
    steps.add("Enjoy!");

    // Add the steps to the recipe
    holder.setSteps(steps);

    // Create the IngredientHolder object
    List<IngredientHolder> ingredients = new ArrayList<>();

    // Define the names of ingredients that contain allergens
    String tofu = "firm tofu";
    String pasta = "pasta";
    String peanutButter = "peanut butter";
    String soySauce = "soy sauce";

    // Add each ingredient to the list
    ingredients.add(new IngredientHolder(
        tofu, Category.COLD_FOOD, 600d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "capsicum", Category.FRESH_FRUIT_AND_VEG, 1d, Misc.NO_UNIT));

    ingredients.add(new IngredientHolder(
        "frozen broccoli", Category.COLD_FOOD, 454d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        "bok-choy", Category.FRESH_FRUIT_AND_VEG, 2d, Misc.NO_UNIT));

    ingredients.add(new IngredientHolder(
        pasta, Category.RICE_AND_PASTA, 320d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        peanutButter, Category.CEREALS_AND_SPREADS, 285d, Mass.GRAMS));

    ingredients.add(new IngredientHolder(
        soySauce, Category.OILS_AND_CONDIMENTS, 90d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        "sesame oil", Category.OILS_AND_CONDIMENTS, 60d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        "lime juice", Category.JUICES, 120d, Volume.MILLILITRES));

    ingredients.add(new IngredientHolder(
        "water", Category.NO_CATEGORY, 180d, Volume.MILLILITRES));

    // Add the list to the RecipeHolder
    holder.setIngredientHolders(ingredients);

    // Return the holder

    return holder;

  }

  public MaterRepository getRepository() {
    return repository;
  }

  public AsyncTaskScheduler getTaskScheduler() {
    return taskScheduler;
  }

}
