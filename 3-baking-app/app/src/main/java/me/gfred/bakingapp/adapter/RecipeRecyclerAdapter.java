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
import me.gfred.bakingapp.model.Step;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.RecipeViewHolder> {

    private final Context mContext;
    private final List<Step> mStepList;
    private final OnStepClickListener mClickListener;

    public RecipeRecyclerAdapter(Context context, List<Step> stepList, OnStepClickListener listener) {
        mContext = context;
        mStepList = stepList;
        mClickListener = listener;
    }

    public interface OnStepClickListener {
        void onStepClick(Step step);
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_step, parent, false);
        return new RecipeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.stepDescription.setText(mStepList.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_description)
        TextView stepDescription;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Step step = mStepList.get(getAdapterPosition());
            mClickListener.onStepClick(step);

        }
    }
}
