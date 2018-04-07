package me.gfred.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.data.FavoriteMoviesContract;
import me.gfred.popularmovies2.data.FavoriteMoviesDBHelper;
import me.gfred.popularmovies2.model.Movie;

/**
 * Created by Gfred on 2/27/2018.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder> {
    private static final String IMAGE_PARAM = "http://image.tmdb.org/t/p/w185/";
    private Context mContext;
    private ArrayList<Movie> movies;
    SQLiteDatabase db;

    private Cursor mCursor;



    final private MovieClickListener mclickListener;

    public MainRecyclerAdapter(Context mContext, ArrayList<Movie> movies, MovieClickListener clickListener) {
        this.movies = movies;
        this.mContext = mContext;
        this.mCursor = null;
        this.mclickListener = clickListener;
    }

    public MainRecyclerAdapter(Context mContext, Cursor cursor, MovieClickListener clickListener) {
        this.mCursor = cursor;
        this.movies = null;
        this.mContext = mContext;
        this.mclickListener = clickListener;
        db = new FavoriteMoviesDBHelper(mContext).getReadableDatabase();
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
        if(movies != null) {

            holder.mMovieTitle.setText(movies.get(position).getOriginalTitle());
            String image = IMAGE_PARAM + movies.get(position).getPosterPath();
            Picasso.with(mContext)
                    .load(image)
                    .into(holder.mMovieImage);

        } else if(mCursor != null) {


            if(!mCursor.moveToPosition(position)) return;

            String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE));
            holder.mMovieTitle.setText(name);
            String image = IMAGE_PARAM + mCursor.getString
                    (mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH));
            Picasso.with(mContext)
                    .load(image)
                    .into(holder.mMovieImage);
        }
    }



    @Override
    public int getItemCount() {
        if(movies != null) return movies.size();
        else if(mCursor!= null) return mCursor.getCount();
        return -1;
    }

    public void swapCursor(Cursor cursor) {

        if(mCursor != null) mCursor.close();

        mCursor = cursor;

        if(cursor != null) this.notifyDataSetChanged();
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
            Movie movie;
           if(movies!= null) movie = movies.get(getAdapterPosition());
           else {
               mCursor.moveToPosition(getAdapterPosition());
               movie = cursorClick();
           }

            mclickListener.onMovieClicked(movie);
        }

        @Override
        public boolean onLongClick(View v) {
            Movie movie;
            if(movies!= null) movie = movies.get(getAdapterPosition());
            else {
                mCursor.moveToPosition(getAdapterPosition());
                movie = cursorClick();
            }

            mclickListener.onMovieLongClicked(movie);
            return false;
        }
    }

    Movie cursorClick() {
        String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE));
        int id = mCursor.getInt(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
        String posterpath = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH));
        String overview = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW));
        String release = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE));
        double vote = mCursor.getDouble(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTEAVERAGE));
        Movie movie = new Movie(name, posterpath, overview, vote, release, id);
       return movie;
    }


}
