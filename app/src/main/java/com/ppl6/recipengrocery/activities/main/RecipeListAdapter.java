package com.ppl6.recipengrocery.activities.main;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ppl6.recipengrocery.R;
import com.ppl6.recipengrocery.room.entities.Summary;
import com.ppl6.recipengrocery.viewmodels.ImageViewModel;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private final LayoutInflater inflater;
  private List<Summary> summaryList; // Cached copy
  private RecipeClickedListener recipeClickedListener;
  private Context context;
  private ImageViewModel imageViewModel;
  private static String LOG_TAG = "GGG - RecipeListAdapter";


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public RecipeListAdapter(Context context, ImageViewModel imageViewModel) {
    this.context = context;
    inflater = LayoutInflater.from(context);

    this.imageViewModel = imageViewModel;
  }


  // - - - - - - - - - - - - - - - Adapter methods - - - - - - - - - - - - - - -

  /**
   * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
   *
   * @param parent the ViewGroup into which the new View will be added after it is bound to an adapter position.
   * @param viewType the view type of the new View
   * @return RecipeViewHolder: The inflated view.
   */
  @NonNull
  @Override
  public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View itemView = inflater.inflate(R.layout.recipe_list_item, parent, false);
    return new RecipeViewHolder(itemView, this);

  }

  /**
   * Called by RecyclerView to display the data at the specified position and set up input
   * listeners. This method updates the contents of the itemView to reflect the item at the given
   * position.
   *
   * @param holder the ViewHolder which should be updated to represent the contents of the item at the given position in the data set
   * @param position the position of the item within the adapter's data set
   */
  @Override
  public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {

    if (summaryList != null) {

      // Retrieve the data for that position and add the data to the view
      Summary currentSummary = summaryList.get(position);
      String title = currentSummary.getTitle();
      holder.recipeTitleView.setText(title);
      holder.recipeDescriptionView.setText(currentSummary.getDescription());
      Uri imageDirectory = Uri.parse(currentSummary.getImageDirectory());

      Glide.with(context)
          .load(imageViewModel.getFile(title))
          .placeholder(context.getDrawable(R.drawable.img_placeholder_main))
          .into(holder.recipeImageView);

    } else {
      holder.recipeTitleView
          .setText(context.getResources().getString(R.string.main_text_no_recipes));

      holder.recipeDescriptionView
          .setText(context.getResources().getString(R.string.main_text_no_recipes_instructions));
    }

    // Set up listener for clicks
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Get the RecipeHolder at the current position
        Summary currentRecipe = summaryList.get(position);

        // Call the onRecipeClicked method (called in MainActivity using an override)
        if (recipeClickedListener != null) {
          recipeClickedListener.onRecipeClicked(currentRecipe, holder.recipeImageView);
        }
      }
    });

  }

  /**
   * Adapter method to return the number of items to be displayed in the RecyclerView.
   * This method is called many times, and when it is first called, this.summaryList has not
   * been updated. This means, initially, it will return zero.
   *
   * @return the number of items
   */
  @Override
  public int getItemCount() {

    if(summaryList != null) {
      return summaryList.size();

    } else {
      return 0;
    }

  }


  // - - - - - - - - - - - - - - - Helper methods - - - - - - - - - - - - - - -

  public void setSummaryList(List<Summary> summaryList) {
    this.summaryList = summaryList;
    notifyDataSetChanged();
  }

  public Summary getRecipeIdAtPosition(int position) {
    return summaryList.get(position);
  }

  public void setRecipeClickedListener(RecipeClickedListener recipeClickedListener) {
    this.recipeClickedListener = recipeClickedListener;
  }


  // - - - - - - - - - - - - - - - ViewHolder Class - - - - - - - - - - - - - - -

  class RecipeViewHolder extends RecyclerView.ViewHolder {

    // Member variables
    final TextView recipeTitleView;
    final TextView recipeDescriptionView;
    final ImageView recipeImageView;
    final RecipeListAdapter adapter;

    // Constructor
    public RecipeViewHolder(@NonNull View itemView, RecipeListAdapter adapter) {
      super(itemView);

      // Initialize the views and adapter.
      recipeTitleView = itemView.findViewById(R.id.recipeListItem_title);
      recipeDescriptionView = itemView.findViewById(R.id.recipeListItem_summary);
      recipeImageView = itemView.findViewById(R.id.recipeListItem_image);
      this.adapter = adapter;
    }
  }


  // - - - - - - - - - - - - - - - RecipeClickedListener Interface - - - - - - - - - - - - - - -

  public interface RecipeClickedListener {
    void onRecipeClicked(Summary summary, ImageView imageView);
  }

}
