package me.gfred.bakingapp.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.model.Recipe;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder> {
    final private RecipeClickListener mRecipeClickListener;
    private Context mContext;
    private List<Recipe> recipeArrayList;

    public MainRecyclerAdapter(Context context, List<Recipe> recipes, RecipeClickListener recipeClickListener) {
        this.mContext = context;
        this.recipeArrayList = recipes;
        this.mRecipeClickListener = recipeClickListener;

    }

    @NonNull
    @Override
    public MainRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_recipe, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.MyViewHolder holder, int position) {
        holder.recipeTitle.setText(recipeArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public interface RecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_title)
        TextView recipeTitle;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = recipeArrayList.get(getAdapterPosition());
            mRecipeClickListener.onRecipeClick(recipe);

        }
    }
}
