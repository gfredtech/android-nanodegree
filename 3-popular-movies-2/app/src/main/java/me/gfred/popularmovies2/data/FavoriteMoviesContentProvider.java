package me.gfred.popularmovies2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static me.gfred.popularmovies2.data.FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;

public class FavoriteMoviesContentProvider extends ContentProvider {

    private FavoriteMoviesDBHelper moviesDBHelper;


    public static final int FAVORITE = 100;

    public static final int FAVORITE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE, FAVORITE);
        matcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE + "/#", FAVORITE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        moviesDBHelper = new FavoriteMoviesDBHelper(context);
        return true;
    }

    
    @Override
    public Cursor query(@NonNull Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        final SQLiteDatabase db = moviesDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match) {
            case FAVORITE:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case FAVORITE_ID:
                System.out.println(uri.toString());
                String id = uri.getPathSegments().get(1);
                System.out.println(id);
                String mSelection = "movie_id=?";
                String[] mSelectionArgs = new String[]{id};

                returnCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnCursor;
    }

    
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    
    @Override
    public Uri insert(@NonNull Uri uri,  ContentValues values) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVORITE:
                long id = db.insert(TABLE_NAME, null, values);
                if(id > 0) {

                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri,  String selection,  String[] selectionArgs) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int deleted;

        switch (match) {
            case FAVORITE_ID:
                String id = uri.getPathSegments().get(1);
                deleted = db.delete(TABLE_NAME,
                        "movie_id=?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Uknown uri: " + uri);
        }

        if(deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
