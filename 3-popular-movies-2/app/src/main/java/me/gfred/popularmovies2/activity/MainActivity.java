package me.gfred.popularmovies2.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.adapter.MainRecyclerAdapter;
import me.gfred.popularmovies2.data.FavoriteMoviesContract;
import me.gfred.popularmovies2.model.Movie;
import me.gfred.popularmovies2.utils.DBUtils;
import me.gfred.popularmovies2.utils.JsonUtils;

public class MainActivity extends AppCompatActivity implements
        MainRecyclerAdapter.MovieClickListener, LoaderManager.LoaderCallbacks<Cursor>{
    private static final int FAVORITE_LOADER_ID = 0;
    static ArrayList<Movie> popularMovies;
    static ArrayList<Movie> topRatedMovies;
    static String json[];
    static String currentState;
    Cursor cursor;
    private MainRecyclerAdapter popularAdapter;
    private MainRecyclerAdapter topRatedAdapter;
    MainRecyclerAdapter cursorAdapter;

    @BindView(R.id.movie_recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.hasExtra("movies_data")) {
            json = intent.getExtras().getStringArray("movies_data");
            if(json != null && json.length != 0) {
                jsonParser(json);
            }
        }


        cursorAdapter = new MainRecyclerAdapter(this, cursor, this);
        popularAdapter = new MainRecyclerAdapter(this, popularMovies, this);
        topRatedAdapter = new MainRecyclerAdapter(this, topRatedMovies, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        String s = "POPULAR";
        if(savedInstanceState != null)
         s = savedInstanceState.getString("state", "POPULAR");

        switch (s) {
            case "POPULAR":
                recyclerView.setAdapter(popularAdapter);
                setTitle(R.string.popular_menu);
                break;
            case "TOP_RATED":
                recyclerView.setAdapter(topRatedAdapter);
                setTitle(R.string.toprated_menu);
                break;
            case "FAVORITE":
                recyclerView.setAdapter(cursorAdapter);
                setTitle(R.string.favorites);
                break;
        }

        currentState = s;

        getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("state", currentState);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivityForResult(intent, 2);
    }

    //get data and update cursor if favorite movies changed.



    @Override
    public void onMovieLongClicked(Movie movie) {
      AlertDialog dialog = getDialog(movie);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        switch (currentState) {
            case "POPULAR":
                menu.getItem(0).setTitle(R.string.toprated_menu);
                break;
            case "TOP_RATED":
                menu.getItem(0).setTitle(R.string.popular_menu);
                break;
            case "FAVORITE":
                menu.getItem(0).setTitle(R.string.popular_menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.top_rated) {
            if(item.getTitle().equals("Top Rated")) {
                recyclerView.setAdapter(topRatedAdapter);
                item.setTitle(R.string.popular_menu);
                setTitle(getString(R.string.toprated_menu));
                currentState = "TOP_RATED";

            } else {
                recyclerView.setAdapter(popularAdapter);
                item.setTitle(R.string.toprated_menu);
                setTitle(getString(R.string.popular_menu));
                currentState = "POPULAR";

            }
        } else if(id == R.id.favorite) {
            recyclerView.setAdapter(cursorAdapter);
            if (recyclerView.getLayoutManager().getItemCount() == 0)
                Toast.makeText(this, "You have no favorite movies yet.", Toast.LENGTH_SHORT).show();
            setTitle(R.string.favorites);
            currentState = "FAVORITE";
        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog getDialog(final Movie movie) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        getContentResolver().query()

        String message = isFavorite ? "Remove from Favorites?" : "Add to Favorites?";
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(isFavorite) {

                    getContentResolver().delete(DBUtils.deleteFavorite(movie.getId()), null, null);
                    getSupportLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, MainActivity.this);
                        Toast.makeText(builder.getContext(), "Removed from favorites",
                                Toast.LENGTH_SHORT).show();


                    dialog.dismiss();


                } else {
                    ContentValues cv = DBUtils.addMovieToFavorite(movie);
                    Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, cv);
                    if (uri != null) {
                        Toast.makeText(builder.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                dialog.dismiss();
            }
        });

       return builder.create();
    }

    void jsonParser(String[] jsonData) {
        try {
            popularMovies = JsonUtils.parseListMovies(jsonData[0]);
            topRatedMovies = JsonUtils.parseListMovies(jsonData[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mFavoriteData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mFavoriteData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mFavoriteData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // COMPLETED (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TIMESTAMP);

                } catch (Exception e) {
                    Log.e("MainActivity", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mFavoriteData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }
}
