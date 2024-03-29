package me.gfred.bakingapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.adapter.RecipeRecyclerAdapter;
import me.gfred.bakingapp.model.Ingredient;
import me.gfred.bakingapp.model.Recipe;

public class RecipeFragment extends Fragment implements RecipeRecyclerAdapter.OnStepClickListener {

    @BindView(R.id.recipe_image)
    ImageView recipeImage;

    @BindView(R.id.ingredients_tv)
    TextView ingredientsTextView;

    @BindView(R.id.step_recyclerview)
    RecyclerView stepRecyclerView;

    private Recipe recipe;

    static LinearLayoutManager layoutManager;

    private OnStepClickedListener mCallback;

    public RecipeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false);

        if (recipe != null) {
            setViewElements();
        }

        return rootView;
    }

    void setViewElements() {
        if (getActivity() != null) getActivity().setTitle(recipe.getName());

        if (recipe.getImage() != null && recipe.getImage().length() > 0) {
            recipeImage.setVisibility(View.VISIBLE);
            Picasso.get().load(recipe.getImage())
                    .into(recipeImage);
        }

        String ingredients = stringifyIngredients(recipe.getIngredients());
        if (ingredients.length() > 0) ingredientsTextView.setText(ingredients);

        inflateRecyclerView();
    }

    static String stringifyIngredients(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();
        int i = 1;
        for (Ingredient n : ingredients) {
            builder.append(i)
                    .append(". ")
                    .append(n.getIngredient())
                    .append(" (")
                    .append(n.getQuantity())
                    .append(" ")
                    .append(n.getMeasure())
                    .append(" )\n");
            i += 1;
        }

        return builder.toString();
    }

    void inflateRecyclerView() {
        RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(getContext(), recipe.getSteps(), this);
        stepRecyclerView.setLayoutManager(layoutManager);
        stepRecyclerView.setNestedScrollingEnabled(false);
        adjustRecyclerViewHeight();
        stepRecyclerView.setAdapter(adapter);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onStepClick(int stepIndex) {
        mCallback.onStepClicked(stepIndex);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickedListener");
        }
    }

    // utility method to adjust height of recyclerview for steps
    void adjustRecyclerViewHeight() {
        ViewGroup.LayoutParams params = stepRecyclerView.getLayoutParams();
        int dp = recipe.getSteps().size() * 84;
        int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height;
        stepRecyclerView.setLayoutParams(params);
    }

    public interface OnStepClickedListener {
        void onStepClicked(int stepIndex);
    }


}
