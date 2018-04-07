package me.gfred.popularmovies1.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.gfred.popularmovies1.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Pair<String, String>> reviews;

    public ReviewsAdapter(Context context, List<Pair<String, String>> reviews) {
        this.mContext = context;
        this.reviews = reviews;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        System.out.println("deux");
        view = inflater.inflate(R.layout.fragment_review, parent, false);
        return new ReviewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.review.setText(reviews.get(position).first);
        holder.review.setMovementMethod(new ScrollingMovementMethod());
        holder.author.setText(reviews.get(position).second);
        System.out.println("cocc");
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView review;
        TextView author;

        public MyViewHolder(View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.review_tv);
            author = itemView.findViewById(R.id.review_author_tv);
        }
    }
}
