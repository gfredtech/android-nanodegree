package me.gfred.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.data.FavoriteMoviesContract;
import me.gfred.popularmovies2.data.FavoriteMoviesDBHelper;
import me.gfred.popularmovies2.model.MovieResults;

import static me.gfred.popularmovies2.utils.ApiKey.IMAGE_PARAM;

/**
 * Created by Gfred on 2/27/2018.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<MovieResults.Movie> movies;

    private Cursor mCursor;

    final private MovieClickListener mclickListener;


    public MainRecyclerAdapter(Context mContext, MovieClickListener clickListener) {
        this.mContext = mContext;
        this.mclickListener = clickListener;
        new FavoriteMoviesDBHelper(mContext).getReadableDatabase();
    }

    public void setDataSource(List<MovieResults.Movie> movieList) {
        this.movies = movieList;
        this.mCursor = null;
        this.notifyDataSetChanged();
    }

    public void setDataSource(Cursor results) {
        this.mCursor = results;
        this.movies = null;
        this.notifyDataSetChanged();
    }

    public interface MovieClickListener {
        void onMovieClicked(MovieResults.Movie movie);

        void onMovieLongClicked(MovieResults.Movie movie);
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
            Picasso.get().load(image).into(holder.mMovieImage);

        } else if(mCursor != null) {

            if(!mCursor.moveToPosition(position)) return;

            String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE));
            holder.mMovieTitle.setText(name);
            String image = IMAGE_PARAM + mCursor.getString
                    (mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH));
            Picasso.get().load(image).into(holder.mMovieImage);
        }


    }



    @Override
    public int getItemCount() {
        if(movies != null) return movies.size();
        else if(mCursor!= null) return mCursor.getCount();
        return -1;
    }

    public void swapCursor(Cursor cursor) {

        if(mCursor != null) {
            mCursor.close();
        }

        mCursor = cursor;

        if(cursor != null) {
            this.notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.movie_title)
        TextView mMovieTitle;

        @BindView(R.id.movie_image)
        ImageView mMovieImage;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            MovieResults.Movie movie;
           if(movies!= null) movie = movies.get(getAdapterPosition());
           else {
               mCursor.moveToPosition(getAdapterPosition());
               movie = cursorClick();
           }

            mclickListener.onMovieClicked(movie);
        }

        @Override
        public boolean onLongClick(View v) {
            MovieResults.Movie movie;
            if(movies!= null) movie = movies.get(getAdapterPosition());
            else {
                mCursor.moveToPosition(getAdapterPosition());
                movie = cursorClick();
            }

            mclickListener.onMovieLongClicked(movie);
            return false;
        }
    }

    private MovieResults.Movie cursorClick() {
        String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE));
        int id = mCursor.getInt(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
        String posterpath = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH));
        String overview = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW));
        String release = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE));
        double vote = mCursor.getDouble(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTEAVERAGE));
        return new MovieResults.Movie(name, posterpath, overview, vote, release, id);
    }


}
