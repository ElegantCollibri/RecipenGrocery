package com.gerardbradshaw.mater.activities.addrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gerardbradshaw.mater.R;
import com.gerardbradshaw.mater.activities.recipedetail.RecipeDetailActivity;
import com.gerardbradshaw.mater.pojos.RecipeHolder;
import com.gerardbradshaw.mater.pojos.RecipeIngredientHolder;
import com.gerardbradshaw.mater.room.entities.RecipeIngredient;
import com.gerardbradshaw.mater.viewholders.IngredientInputViewHolder;
import com.gerardbradshaw.mater.viewholders.StepInputViewHolder;
import com.gerardbradshaw.mater.helpers.Units.MiscUnits;
import com.gerardbradshaw.mater.viewmodels.ImageViewModel;
import com.gerardbradshaw.mater.viewmodels.DetailViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRecipeActivity extends AppCompatActivity {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private DetailViewModel detailViewModel;
  private ImageViewModel imageViewModel;

  private EditText titleInput;
  private EditText descriptionInput;
  private EditText servingsInput;
  private TextView imageName;
  private Toolbar toolbar;
  private Bitmap image;

  private List<IngredientInputViewHolder> ingredientViewHolders = new ArrayList<>();
  private List<StepInputViewHolder> stepViewHolders = new ArrayList<>();
  private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
  private List<RecipeIngredientHolder> recipeIngredientHolders = new ArrayList<>();
  private List<AddIngredientItemHolder> addIngredientItemHolders = new ArrayList<>();

  private AddIngredientListAdapter ingredientListAdapter;

  private static final int REQUEST_IMAGE_IMPORT = 1;
  private static final String LOG_TAG = "AddRecipeActivity";


  // - - - - - - - - - - - - - - - Activity methods - - - - - - - - - - - - - - -

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_recipe);
    detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
    imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

    // Get a handle on the views
    titleInput = findViewById(R.id.addRecipe_titleInput);
    descriptionInput = findViewById(R.id.addRecipe_descriptionInput);
    servingsInput = findViewById(R.id.addRecipe_servingsInput);
    imageName = findViewById(R.id.addRecipe_imageNameTextView);
    toolbar = findViewById(R.id.addRecipe_toolbar);

    // Set up the Toolbar
    toolbar.setTitle(getString(R.string.addRecipe_pageHeader));
    setSupportActionBar(toolbar);

    // Set up Ingredient RecyclerView
    ingredientListAdapter = new AddIngredientListAdapter(this);
    RecyclerView recyclerView = findViewById(R.id.addRecipe_ingredientRecyclerView);
    recyclerView.setAdapter(ingredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Set listener for selectImageButton
    Button selectImageButton = findViewById(R.id.addRecipe_selectImageButton);
    selectImageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        importImage();
      }
    });

    // Set listener for addIngredientButton
    Button addIngredientButton = findViewById(R.id.addRecipe_addIngredientButton);
    addIngredientButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addIngredientToView();
      }
    });

    // Set listener for addStepButton
    Button addStepButton = findViewById(R.id.addRecipe_addStepButton);
    addStepButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addStepToView();
      }
    });

    // Set listener for saveButton
    Button saveButton = findViewById(R.id.addRecipe_saveButton);
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        saveRecipeToRepository();
      }
    });

    // Set up cancel button
    Button cancelButton = findViewById(R.id.addRecipe_cancel);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showCancelDialog();
      }
    });

    // Pre-fill data if loading from existing recipe
    int recipeId = getIntent().getIntExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, 0);
    if (recipeId != 0) {
      toolbar.setTitle(getString(R.string.addRecipe_pageHeader));
      loadExistingRecipe(recipeId);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    if (requestCode == REQUEST_IMAGE_IMPORT &&
        resultCode == RESULT_OK &&
        data != null) {

      // Get the URI of the imageName and add it to the app.
      Uri imageUri = data.getData();
      assert imageUri != null;

      // Set the view
      imageName.setText(getFileName(imageUri));

      importImageFromUri(imageUri);

    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  // - - - - - - - - - - - - - - - Button clicked methods - - - - - - - - - - - - - - -

  private void importImage() {
    // Create the intent to import an imageName
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

    // Filter files only to those that can be "opened" and directly accessed as a stream.
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    // Only show images.
    intent.setType("image/*");

    startActivityForResult(intent, REQUEST_IMAGE_IMPORT);
  }

  private void addIngredientsToRecycler() {
    //ingredientListAdapter.setRecipeIngredientList(recipeIngredients);
  }

  private void addIngredientToView() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipe_addIngredientLayout);

    // Inflate the view
    LinearLayout addIngredientView =
        (LinearLayout) inflater.inflate(R.layout.ingredient_input, insertPoint, false);

    // Get the children of the View
    EditText nameInput = (EditText) addIngredientView.getChildAt(0);
    EditText amountInput = (EditText) addIngredientView.getChildAt(1);
    Spinner unitsSpinner = (Spinner) addIngredientView.getChildAt(2);

    // Save the new views to the list
    ingredientViewHolders.add(new IngredientInputViewHolder(nameInput, amountInput, unitsSpinner));

    // Get the index of the view
    int index = ingredientViewHolders.size() - 1;

    // Insert the view into main view
    insertPoint.addView(addIngredientView,index, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  private void addStepToView() {

    // Instantiate a LayoutInflater
    LayoutInflater inflater = (LayoutInflater) getApplicationContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // Get the insert point
    ViewGroup insertPoint = findViewById(R.id.addRecipe_addStepLayout);

    // Inflate the view
    LinearLayout addStepView =
        (LinearLayout) inflater.inflate(R.layout.step_input, insertPoint, false);

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
    stepViewHolders.add(new StepInputViewHolder(number, step));

    // Insert the view into the main view
    insertPoint.addView(addStepView, numberOfSteps, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


  }

  private void saveRecipeToRepository() {

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
    for(IngredientInputViewHolder holder : ingredientViewHolders) {

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
    for(StepInputViewHolder holder : stepViewHolders) {
      if(holder.getStep().getText().toString().isEmpty()) {
        holder.getStep().setHintTextColor(hintColor);
        allFieldsOk = false;
      }
    }

    // If all is well, add the recipe to the database
    if(allFieldsOk) {

      // Create a RecipeHolder object
      RecipeHolder recipe = new RecipeHolder();

      // Set up lists for steps and ingredients
      List<RecipeIngredientHolder> ingredients = new ArrayList<>();
      List<String> steps = new ArrayList<>();

      // Get the ingredients info from each ViewHolder and add them to the list
      for(IngredientInputViewHolder holder : ingredientViewHolders) {
        ingredients.add(new RecipeIngredientHolder(
            holder.getNameInput().getText().toString(),
            Double.parseDouble(holder.getAmountInput().getText().toString()),
            MiscUnits.NO_UNIT));

        // TODO implement spinner functionality and retrieval
      }

      // Get the steps from each ViewHolder and add them to the list
      for(StepInputViewHolder holder : stepViewHolders) {
        steps.add(holder.getStep().getText().toString());
      }

      // Add everything to the recipe
      recipe.setTitle(titleInput.getText().toString());
      recipe.setDescription(descriptionInput.getText().toString());
      recipe.setImageDirectory(imageName.getText().toString());
      recipe.setRecipeIngredients(ingredients);
      recipe.setSteps(steps);

      // Save the recipe to the database
      detailViewModel.insertRecipeHolder(recipe);

      // Save the image
      if (image != null) {
        imageViewModel.saveImage(recipe.getTitle(), image);
      }

      finish();

      // TODO add a ProgressBar

    } else {
      Toast
          .makeText(this, "Please complete indicated fields", Toast.LENGTH_SHORT)
          .show();
    }
  }

  private void showCancelDialog() {
    // Set up dialog for user confirmation
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddRecipeActivity.this);
    alertBuilder.setMessage(getString(R.string.discard_changes));

    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        finish();
      }
    });

    alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        // Do nothing
      }
    });

    alertBuilder.show();
  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

  private void loadExistingRecipe(int recipeId) {

    // Start AsyncTask to load RecipeHolder for recipe


    // Set title
    detailViewModel.getLiveTitle(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        titleInput.setText(s);
      }
    });

    // Set servings
    detailViewModel.getLiveServings(recipeId).observe(this, new Observer<Integer>() {
      @Override
      public void onChanged(Integer i) {
        servingsInput.setText(Integer.toString(i));
      }
    });

    // Set description
    detailViewModel.getLiveDescription(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        descriptionInput.setText(s);
      }
    });

    // Set image name
    detailViewModel.getLiveImageDirectory(recipeId).observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        imageName.setText(s);
      }
    });

    // Set the ingredients
    detailViewModel.getLiveRecipeIngredients(recipeId).observe(this, new Observer<List<RecipeIngredient>>() {
      @Override
      public void onChanged(List<RecipeIngredient> recipeIngredients) {
        AddRecipeActivity.this.recipeIngredients = recipeIngredients;
        addIngredientsToRecycler();
      }
    });

  }

  private void importImageFromUri(@NonNull Uri uri) {
    try {
      // Use the MediaStore to load the imageName.
      Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

      if (image != null) {

        this.image = image;
        // Tell the presenter to import this imageName.
        // TODO the imageName has been imported as a bitmap.
        //  Add the imageName to the database using the repo.
        //mPresenter.onImportImage(imageName);

      }
    } catch (IOException e) {
      e.printStackTrace();
      Log.e(LOG_TAG, "Error: " + e.getMessage() + "Could not open URI: "
          + uri.toString());
    }
  }

  private String getFileName(@NonNull Uri uri) {
    String result = null;
    if (Objects.requireNonNull(uri.getScheme()).equals("content")) {
      Cursor cursor = getContentResolver().query(uri, null, null, null, null);
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        assert cursor != null;
        cursor.close();
      }
    }
    if (result == null) {
      result = uri.getPath();
      assert result != null;
      int cut = result.lastIndexOf('/');
      if (cut != -1) {
        result = result.substring(cut + 1);
      }
    }
    return result;
  }


  // - - - - - - - - - - - - - - - Load Recipe AsyncTask - - - - - - - - - - - - - - -

  private class LoadRecipeAsyncTask extends AsyncTask<Integer, Void, RecipeHolder> {

    // Member variables
    private EditText titleInput;
    private EditText servingsInput;
    private EditText descriptionInput;
    private TextView imageNameView;
    private AddIngredientListAdapter addIngredientListAdapter;
    private List<RecipeIngredientHolder> recipeIngredientHolders;


    // Constructor
    LoadRecipeAsyncTask(EditText titleInput,
                        EditText servingsInput,
                        EditText descriptionInput,
                        TextView imageNameView,
                        AddIngredientListAdapter addIngredientListAdapter,
                        List<RecipeIngredientHolder> recipeIngredientHolders) {
      this.titleInput = titleInput;
      this.servingsInput = servingsInput;
      this.descriptionInput = descriptionInput;
      this.imageNameView = imageNameView;
      this.addIngredientListAdapter = addIngredientListAdapter;
      this.recipeIngredientHolders = recipeIngredientHolders;
    }

    // AsyncTask methods
    @Override
    protected RecipeHolder doInBackground(Integer... integers) {
      int recipeId = integers[0];

      return detailViewModel.getRecipeHolder(recipeId);
    }

    @Override
    protected void onPostExecute(RecipeHolder recipeHolder) {
      super.onPostExecute(recipeHolder);

      titleInput.setText(recipeHolder.getTitle());
      servingsInput.setText(Integer.toString(recipeHolder.getServings()));
      descriptionInput.setText(recipeHolder.getDescription());
      imageNameView.setText(recipeHolder.getImageDirectory());
      recipeIngredientHolders = recipeHolder.getRecipeIngredients();
      addIngredientListAdapter.setRecipeIngredientList(recipeIngredientHolders);

    }
  }


}
