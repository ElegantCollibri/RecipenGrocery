package com.ppl6.recipengrocery.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ppl6.recipengrocery.util.MaterApplication;
import com.ppl6.recipengrocery.room.MaterRepository;
import com.ppl6.recipengrocery.room.entities.Summary;

import java.util.List;

public class SummaryViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;
  private LiveData<List<Summary>> recipeSummaryList; // Cached copy


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public SummaryViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    MaterApplication materApplication = (MaterApplication) application;
    repository = materApplication.getRepository();

    // Set variables from repo
    recipeSummaryList = repository.getLiveAllSummaries();
  }


  // - - - - - - - - - - - - - - - Get summaries - - - - - - - - - - - - - - -

  public LiveData<List<Summary>> getAllRecipeSummaries() {
    return recipeSummaryList;
  }

  /**
   * Simple method for returning all recipes from the database. Must be run on another thread.
   * @return a list of all summaries in the database
   */
  public List<Summary> getAllSummaries() {
    return repository.getAllSummaries();
  }


  // - - - - - - - - - - - - - - - Delete recipes - - - - - - - - - - - - - - -

  public void deleteRecipe(int recipeId) {
    repository.deleteRecipe(recipeId);
  }

}
