package me.gfred.popularmovies2.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.model.Reviews;


//adapter for movie reviews

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Reviews> reviews;

    public ReviewsAdapter(Context context, List<Reviews> reviews) {
        this.mContext = context;
        this.reviews = reviews;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        view = inflater.inflate(R.layout.cardview_review, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.review.setText(reviews.get(position).getContent());
        holder.review.setMovementMethod(new ScrollingMovementMethod());
        holder.author.setText(reviews.get(position).getAuthor());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView review;
        TextView author;

        MyViewHolder(View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.review_tv);
            author = itemView.findViewById(R.id.review_author_tv);
        }
    }
}
