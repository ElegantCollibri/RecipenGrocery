package com.gerardbradshaw.tomatoes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gerardbradshaw.tomatoes.R;
import com.gerardbradshaw.tomatoes.pojos.RecipeIngredientPojo;
import com.gerardbradshaw.tomatoes.pojos.RecipePojo;
import com.gerardbradshaw.tomatoes.room.entities.RecipeIngredient;
import com.gerardbradshaw.tomatoes.viewholders.AddIngredientViewHolder;
import com.gerardbradshaw.tomatoes.viewholders.AddStepViewHolder;
import com.gerardbradshaw.tomatoes.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  // Layout views
  private EditText titleInput;
  private EditText descriptionInput;

  // Dynamically added view references
  private List<AddIngredientViewHolder> ingredientViewHolders;
  private List<AddStepViewHolder> stepViewHolders;

  // Data objects
  private RecipeViewModel viewModel;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);

    // Initialize the ViewModel
    viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

    // Get a handle on the views
    Button addIngredientButton = findViewById(R.id.addRecipeActivity_addIngredientButton);
    Button addStepButton = findViewById(R.id.addRecipeActivity_addStepButton);
    Button saveButton = findViewById(R.id.addRecipeActivity_saveButton);
    titleInput = findViewById(R.id.addRecipeActivity_titleInput);
    descriptionInput = findViewById(R.id.addRecipeActivity_descriptionInput);

    // Initialize the ViewHolders
    ingredientViewHolders = new ArrayList<>();
    stepViewHolders = new ArrayList<>();

    // Set listener for addIngredientButton
    addIngredientButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addIngredient();
      }
    });

    // Set listener for addStepButton
    addStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addStep();
      }
    });

    // Set listener for saveButton
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveRecipe();
      }
    });

  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  private void addIngredient() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipeActivity_addIngredientLayout);

    // Inflate the view
    LinearLayout addIngredientView =
        (LinearLayout) inflater.inflate(R.layout.add_ingredient, insertPoint, false);

    // Get the children of the View
    EditText nameInput = (EditText) addIngredientView.getChildAt(0);
    EditText amountInput = (EditText) addIngredientView.getChildAt(1);
    Spinner unitsSpinner = (Spinner) addIngredientView.getChildAt(2);

    // Save the new views to the list
    ingredientViewHolders.add(new AddIngredientViewHolder(nameInput, amountInput, unitsSpinner));

    // Get the index of the view
    int index = ingredientViewHolders.size() - 1;

    // Insert the view into main view
    insertPoint.addView(addIngredientView,index, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  private void addStep() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipeActivity_addStepLayout);

    // Inflate the view
    LinearLayout addStepView =
        (LinearLayout) inflater.inflate(R.layout.add_step, insertPoint, false);

    // Get the children of the View
    TextView number = (TextView) addStepView.getChildAt(0);
    EditText step = (EditText) addStepView.getChildAt(1);

    // Get the numberOfSteps of the view and the index
    int numberOfSteps = stepViewHolders.size();
    String numberOfStepsString;

    if(numberOfSteps < 1) {
      numberOfStepsString = "1. ";

    } else {
      int stepNumber = numberOfSteps + 1;
      numberOfStepsString = stepNumber + ". ";
    }

    // Set the text of the step number
    number.setText(numberOfStepsString);

    // Save the new EditText to the list
    stepViewHolders.add(new AddStepViewHolder(number, step));

    // Insert the view into the main view
    insertPoint.addView(addStepView, numberOfSteps, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


  }

  private void saveRecipe() {

    // Create setup fields
    boolean allFieldsOk = true;
    int hintColor = getResources().getColor(R.color.colorPrimary);

    // Check the title
    if(titleInput.getText().toString().isEmpty()) {
      titleInput.setHintTextColor(hintColor);
      allFieldsOk = false;
    }

    // Check the description
    if(descriptionInput.getText().toString().isEmpty()) {
      descriptionInput.setHintTextColor(hintColor);
      allFieldsOk = false;
    }

    // Check the ingredients
    for(AddIngredientViewHolder holder : ingredientViewHolders) {

      if(holder.getNameInput().getText().toString().isEmpty()) {
        holder.getNameInput().setHintTextColor(hintColor);
        allFieldsOk = false;
      }

      if(holder.getAmountInput().getText().toString().isEmpty()) {
        holder.getAmountInput().setHintTextColor(hintColor);
        allFieldsOk = false;
      }

      // TODO add checker for spinner
    }

    // Check the steps
    for(AddStepViewHolder holder : stepViewHolders) {
      if(holder.getStep().getText().toString().isEmpty()) {
        holder.getStep().setHintTextColor(hintColor);
        allFieldsOk = false;
      }
    }

    // If all is well, add the recipe to the database
    if(allFieldsOk) {

      // Create a RecipePojo object
      RecipePojo recipe = new RecipePojo();

      // Set up lists for steps and ingredients
      List<RecipeIngredientPojo> ingredients = new ArrayList<>();
      List<String> steps = new ArrayList<>();

      // Get the ingredients info from each ViewHolder and add them to the list
      for(AddIngredientViewHolder holder : ingredientViewHolders) {
        ingredients.add(new RecipeIngredientPojo(
            holder.getNameInput().getText().toString(),
            Double.parseDouble(holder.getAmountInput().getText().toString()),
            RecipeIngredientPojo.Unit.NO_UNIT));

        // TODO implement spinner functionality and retrieval
      }

      // Get the steps from each ViewHolder and add them to the list
      for(AddStepViewHolder holder : stepViewHolders) {
        steps.add(holder.getStep().getText().toString());
      }

      // Add everything to the recipe
      recipe.setTitle(titleInput.getText().toString());
      recipe.setDescription(descriptionInput.getText().toString());
      recipe.setRecipeIngredients(ingredients);
      recipe.setSteps(steps);

      // Save the recipe to the database
      viewModel.insertRecipeHolder(recipe);

      // TODO add a ProgressBar

    } else {
      Toast
          .makeText(this, "Please complete indicated fields", Toast.LENGTH_SHORT)
          .show();
    }
  }

}
