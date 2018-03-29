package me.gfred.popularmovies1.data;

import android.provider.BaseColumns;

public class FavoriteMoviesContract {
    public static final class FavoriteMoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
