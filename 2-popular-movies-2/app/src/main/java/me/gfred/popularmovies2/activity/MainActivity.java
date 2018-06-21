package me.gfred.popularmovies2.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.adapter.MainRecyclerAdapter;
import me.gfred.popularmovies2.data.FavoriteMoviesContract;
import me.gfred.popularmovies2.model.MovieResults;
import me.gfred.popularmovies2.utils.ApiInterface;
import me.gfred.popularmovies2.utils.DBUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.gfred.popularmovies2.utils.ApiKey.API_KEY;
import static me.gfred.popularmovies2.utils.ApiKey.createRetrofitApi;

public class MainActivity extends AppCompatActivity implements
        MainRecyclerAdapter.MovieClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int FAVORITE_LOADER_ID = 0;
    private static String type = "POPULAR";

    static List<MovieResults.Movie> popularMovies;
    static List<MovieResults.Movie> topRatedMovies;
    Cursor favoriteMovies;

    MainRecyclerAdapter adapter;
    MainRecyclerAdapter cursorAdapter;

    @BindView(R.id.movie_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            ApiInterface apiInterface = createRetrofitApi();
            apiInterface.getPopularMovies(API_KEY).enqueue(popularMoviesCallback);

            getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, null, this);
        } else {
            type = savedInstanceState.getString("state", "POPULAR");
            popularMovies = savedInstanceState.getParcelableArrayList("popular_movies");
            topRatedMovies = savedInstanceState.getParcelableArrayList("toprated_movies");

            switch (type) {
                case "POPULAR":
                    Log.d("MainActivity", type + " loaded");
                    inflateView(popularMovies);
                    setTitle(R.string.popular_menu);
                    break;

                case "TOP_RATED":
                    inflateView(topRatedMovies);
                    Log.d("MainActivity", type + " loaded");
                    setTitle(R.string.toprated_menu);
                    break;

                case "FAVORITE":
                    Log.d("MainActivity", type + " loaded");
                    setTitle(R.string.favorites);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("state", type);
        outState.putParcelableArrayList("popular_movies", (ArrayList<? extends Parcelable>) popularMovies);
        outState.putParcelableArrayList("toprated_movies", (ArrayList<? extends Parcelable>) topRatedMovies);
    }

    @Override
    public void onMovieClicked(MovieResults.Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    public void onMovieLongClicked(MovieResults.Movie movie) {
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
        switch (type) {
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
        if (id == R.id.top_rated) {
            if (item.getTitle().equals("Top Rated")) {
                inflateView(topRatedMovies);
                item.setTitle(R.string.popular_menu);
                setTitle(getString(R.string.toprated_menu));
                type = "TOP_RATED";

            } else {
                inflateView(popularMovies);
                item.setTitle(R.string.toprated_menu);
                setTitle(getString(R.string.popular_menu));
                type = "POPULAR";

            }

            return true;

        } else if (id == R.id.favorite) {
            inflateView();
            if (recyclerView.getLayoutManager().getItemCount() == 0)
                Snackbar.make(mainLayout, "You have no favorite movies yet.", Snackbar.LENGTH_SHORT).show();

            setTitle(R.string.favorites);
            type = "FAVORITE";
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog getDialog(final MovieResults.Movie movie) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final Cursor cursor = getContentResolver().query(DBUtils.queryFavorite(movie.getId()),
                null,
                null,
                null,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TIMESTAMP);

        final boolean isFavorite = cursor != null && cursor.getCount() == 1;


        String message = isFavorite ? "Remove from Favorites?" : "Add to Favorites?";
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (isFavorite) {
                    cursor.close();

                    getContentResolver().delete(DBUtils.deleteFavorite(movie.getId()), null, null);
                    getSupportLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, MainActivity.this);
                    Snackbar.make(mainLayout, R.string.removed_from_favorites, Snackbar.LENGTH_SHORT).show();

                    dialog.dismiss();


                } else {
                    ContentValues cv = DBUtils.addMovieToFavorite(movie);
                    Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, cv);
                    if (uri != null) {
                        Snackbar.make(mainLayout, R.string.added_to_favorites, Snackbar.LENGTH_SHORT).show();
                        getSupportLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, MainActivity.this);
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


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the favorites
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

            public void deliverResult(Cursor data) {
                mFavoriteData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (cursorAdapter == null) {
            cursorAdapter = new MainRecyclerAdapter(this, this);
            cursorAdapter.setDataSource(data);
        } else {
            cursorAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (cursorAdapter != null) {
            cursorAdapter.swapCursor(null);
        }
    }

    Callback<MovieResults> popularMoviesCallback = new Callback<MovieResults>() {
        @Override
        public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
            popularMovies = response.body().getResults();
            ApiInterface apiInterface = createRetrofitApi();
            apiInterface.getTopRatedMovies(API_KEY).enqueue(topRatedMoviesCallback);

        }

        @Override
        public void onFailure(Call<MovieResults> call, Throwable t) {
            t.printStackTrace();
        }
    };


    Callback<MovieResults> topRatedMoviesCallback = new Callback<MovieResults>() {
        @Override
        public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
            topRatedMovies = response.body().getResults();
            inflateView(type.equals("POPULAR") ? popularMovies : topRatedMovies);

        }

        @Override
        public void onFailure(Call<MovieResults> call, Throwable t) {
            t.printStackTrace();
        }
    };

    void inflateView(List<MovieResults.Movie> results) {
        adapter = new MainRecyclerAdapter(this,
                this);
        adapter.setDataSource(results);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, calculateSpanCount(this)));
        recyclerView.setAdapter(adapter);
    }

    void inflateView() {
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, calculateSpanCount(this)));
        recyclerView.setAdapter(cursorAdapter);

    }

    public static int calculateSpanCount(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}
