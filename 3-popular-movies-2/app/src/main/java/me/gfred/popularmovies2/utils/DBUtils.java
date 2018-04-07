package me.gfred.popularmovies2.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.gfred.popularmovies2.data.FavoriteMoviesContract;
import me.gfred.popularmovies2.models.Movie;

public class DBUtils {


    public static Cursor getFavoriteMovies(SQLiteDatabase db) {
        return db.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TIMESTAMP);
    }

    public static boolean CheckIfDataAlreadyInDBorNot(SQLiteDatabase sqldb, int id) {

        String Query = "Select * from favorite where movie_id = " + id;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static void addMovieToFavorite(SQLiteDatabase db, Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE, movie.getOriginalTitle());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTERPATH, movie.getPosterPath());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE, movie.getReleaseDate());
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTEAVERAGE, movie.getVoteAverage());
        db.insert(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, null, cv);
    }

   public static boolean removeMovieFromFavorite(SQLiteDatabase db, int id) {
        return db.delete(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=" + id, null) > 0;
    }
}
