package me.gfred.popularmovies2.data;

import android.provider.BaseColumns;

public class FavoriteMoviesContract {
    public static final class FavoriteMoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTERPATH = "posterpath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTEAVERAGE = "vote";
        public static final String COLUMN_RELEASE = "release_date";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
