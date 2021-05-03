package com.ppl6.recipengrocery.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ppl6.recipengrocery.util.MaterApplication;
import com.ppl6.recipengrocery.room.MaterRepository;

import java.io.File;

public class ImageViewModel extends AndroidViewModel {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private MaterRepository repository;
  private LiveData<Integer> imageUpdateNotifier;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public ImageViewModel(@NonNull Application application) {
    super(application);

    // Downcast the application and set the repository
    MaterApplication materApplication = (MaterApplication) application;
    repository = materApplication.getRepository();

    // Set variables from repo
    imageUpdateNotifier = repository.bitmapUpdateNotifier();
  }


  // - - - - - - - - - - - - - - - Methods - - - - - - - - - - - - - - -

  public LiveData<Integer> imageUpdateNotifier() {
    return imageUpdateNotifier;
  }

  public void saveImage(String recipeTitle, Bitmap image) {
    repository.storeBitmap(recipeTitle, image);
  }

  public File getFile(String recipeTitle) {
    return repository.getFile(recipeTitle);
  }
}
