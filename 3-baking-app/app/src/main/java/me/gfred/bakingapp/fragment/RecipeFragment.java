package me.gfred.bakingapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.activity.StepActivity;
import me.gfred.bakingapp.adapter.RecipeRecyclerAdapter;
import me.gfred.bakingapp.model.Ingredient;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.model.Step;

public class RecipeFragment extends Fragment implements RecipeRecyclerAdapter.OnStepClickListener{

    @BindView(R.id.recipe_image)
    ImageView recipeImage;

    @BindView(R.id.ingredients_tv)
    TextView ingredientsTextView;

    @BindView(R.id.step_recyclerview)
    RecyclerView stepRecyclerView;

    private Recipe recipe;
    private Context mContext;

    public RecipeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);

        if(recipe != null) {
            if(getActivity() != null) getActivity().setTitle(recipe.getName());

            if (recipe.getImage() != null && recipe.getImage().length() > 0) {
                recipeImage.setVisibility(View.VISIBLE);
                Picasso.get().load(recipe.getImage())
                        .into(recipeImage);
            }

            StringBuilder builder = new StringBuilder();
            int i = 1;
            for (Ingredient n : recipe.getIngredients()) {
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

            if (builder.length() > 0) ingredientsTextView.setText(builder.toString());


            RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(mContext, recipe.getSteps(), this);
            stepRecyclerView.setLayoutManager(new LinearLayoutManager
                    (mContext, LinearLayoutManager.VERTICAL, false));
            stepRecyclerView.setNestedScrollingEnabled(false);
            adjustRecyclerViewHeight();
            stepRecyclerView.setAdapter(adapter);
        }

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public void onStepClick(int index) {
        Intent intent = new Intent(getActivity(), StepActivity.class);
        intent.putParcelableArrayListExtra("steps", new ArrayList<Step>(){{
            addAll(recipe.getSteps());
        }});
        intent.putExtra("index", index);
        startActivity(intent);
    }

    void adjustRecyclerViewHeight() {
        ViewGroup.LayoutParams params = stepRecyclerView.getLayoutParams();
        int dp = recipe.getSteps().size() * 114;
        int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height;
        stepRecyclerView.setLayoutParams(params);
    }
}
