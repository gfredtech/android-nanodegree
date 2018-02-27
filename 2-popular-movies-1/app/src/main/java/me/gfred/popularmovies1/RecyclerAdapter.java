package me.gfred.popularmovies1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import me.gfred.popularmovies1.models.Movie;

/**
 * Created by Gfred on 2/27/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Movie> movies;


    public RecyclerAdapter(Context mContext, List<Movie> movies) {
        this.movies = movies;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardview_movie_poster, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mMovieTitle.setText(movies.get(position).getOriginalTitle());
        Picasso.with(mContext)
                .load(movies.get(position).getPosterPath())
                .into(holder.mMovieImage);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_title)
        TextView mMovieTitle;
        @BindView(R.id.movie_image)
        ImageView mMovieImage;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
