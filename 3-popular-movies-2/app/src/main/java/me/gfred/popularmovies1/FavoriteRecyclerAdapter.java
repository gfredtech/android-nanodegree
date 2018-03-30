package me.gfred.popularmovies1;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.gfred.popularmovies1.data.FavoriteMoviesContract;
import me.gfred.popularmovies1.models.Movie;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder> {
    private static final String IMAGE_PARAM = "http://image.tmdb.org/t/p/w185/";

    Context mContext;

    private Cursor mCursor;

    final private MainRecyclerAdapter.MovieClickListener mclickListener;

    public FavoriteRecyclerAdapter(Context context, Cursor cursor, MainRecyclerAdapter.MovieClickListener mclickListener) {
        this.mContext = context;
        this.mCursor = cursor;
        this.mclickListener = mclickListener;
    }

    public interface MovieClickListener {
        void onMovieClicked(Movie movie);
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardview_movie_poster, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE));
        holder.mMovieTitle.setText(name);
        String image = IMAGE_PARAM + mCursor.getString
                (mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH));
        Picasso.with(mContext)
                .load(image)
                .into(holder.mMovieImage);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    void swapCursor(Cursor cursor) {

        if(mCursor != null) mCursor.close();
        mCursor = cursor;

        if(cursor != null) this.notifyDataSetChanged();
    }

    public class FavoriteViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder
            implements View.OnClickListener{
        TextView mMovieTitle;

        ImageView mMovieImage;

        FavoriteViewHolder(View itemView) {
            super(itemView);
            mMovieTitle = itemView.findViewById(R.id.movie_title);
            mMovieImage = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE));
            int id = mCursor.getInt(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
            String posterpath = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH));
            String overview = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW));
            String release = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE));
            double vote = mCursor.getDouble(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTEAVERAGE));
            Movie movie = new Movie(name, posterpath, overview, vote, release, id);
            mclickListener.onMovieClicked(movie);

        }
    }
}
