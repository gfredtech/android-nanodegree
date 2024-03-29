package me.gfred.popularmovies2.utils;

import android.content.ContentValues;
import android.net.Uri;

import me.gfred.popularmovies2.data.FavoriteMoviesContract;
import me.gfred.popularmovies2.model.MovieResults;

import static me.gfred.popularmovies2.data.FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;

public class DBUtils {

    public static ContentValues addMovieToFavorite(MovieResults.Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE, movie.getOriginalTitle());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH, movie.getPosterPath());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE, movie.getReleaseDate());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTEAVERAGE, movie.getVoteAverage());
        return cv;
    }

   public static Uri deleteFavorite(int id) {
        String stringId = Integer.toString(id);
       return CONTENT_URI.buildUpon().appendPath(stringId).build();
    }

    public static Uri queryFavorite(int id) {
        String stringId = Integer.toString(id);
        return CONTENT_URI.buildUpon().appendPath(stringId).build();
    }
}
