package me.gfred.popularmovies1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import me.gfred.popularmovies1.models.Movie;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Gfred on 2/27/2018.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder> {
    private static final String IMAGE_PARAM = "http://image.tmdb.org/t/p/w185/";
    private Context mContext;
    private ArrayList<Movie> movies;



    final private MovieClickListener mclickListener;

    MainRecyclerAdapter(Context mContext, ArrayList<Movie> movies, MovieClickListener clickListener) {
        this.movies = movies;
        this.mContext = mContext;
        this.mclickListener = clickListener;
    }

    public interface MovieClickListener {
        void onMovieClicked(Movie movie);
        void onMovieLongClicked(Movie movie);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardview_movie_poster, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.mMovieTitle.setText(movies.get(position).getOriginalTitle());
            String image = IMAGE_PARAM + movies.get(position).getPosterPath();
            Picasso.with(mContext)
                    .load(image)
                    .into(holder.mMovieImage);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView mMovieTitle;

        ImageView mMovieImage;

        MyViewHolder(View itemView) {
            super(itemView);
            mMovieTitle = itemView.findViewById(R.id.movie_title);
            mMovieImage = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Movie movie = movies.get(getAdapterPosition());

            mclickListener.onMovieClicked(movie);
        }

        @Override
        public boolean onLongClick(View v) {

            Movie movie = movies.get(getAdapterPosition());
            mclickListener.onMovieLongClicked(movie);
            return false;
        }
    }

}
