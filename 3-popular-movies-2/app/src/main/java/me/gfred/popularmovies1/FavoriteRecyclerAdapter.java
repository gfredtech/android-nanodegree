package me.gfred.popularmovies1;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import me.gfred.popularmovies1.data.FavoriteMoviesContract;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder> {

    private Context mContext;

    private Cursor mCursor;

    public FavoriteRecyclerAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class FavoriteViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {


        public FavoriteViewHolder(View itemView) {
            super(itemView);
        }
    }
}
