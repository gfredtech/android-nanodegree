package me.gfred.popularmovies2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.adapter.MainRecyclerAdapter;
import me.gfred.popularmovies2.data.FavoriteMoviesDBHelper;
import me.gfred.popularmovies2.model.Movie;
import me.gfred.popularmovies2.utils.DBUtils;
import me.gfred.popularmovies2.utils.JsonUtils;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdapter.MovieClickListener{
    private SQLiteDatabase db;
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
        }

        if(json != null && json.length != 0) {
            jsonParser(json);
        }

        FavoriteMoviesDBHelper helper = new FavoriteMoviesDBHelper(this);

        db = helper.getWritableDatabase();
        cursor = DBUtils.getFavoriteMovies(db);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && data != null) {
                Boolean message = data.getBooleanExtra("changed", false);

                if(message) cursorAdapter.swapCursor(DBUtils.getFavoriteMovies(db));
        }
    }

    @Override
    public void onMovieLongClicked(Movie movie) {
      AlertDialog dialog = getDialog(movie);
        dialog.show();
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

        final boolean isFavorite = DBUtils.CheckIfDataAlreadyInDBorNot(db, movie.getId());

        String message = isFavorite ? "Remove from Favorites?" : "Add to Favorites?";
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(isFavorite) {

                    if(DBUtils.removeMovieFromFavorite(db, movie.getId()))
                        Toast.makeText(builder.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();

                    cursorAdapter.swapCursor(DBUtils.getFavoriteMovies(db));
                    dialog.dismiss();


                } else {
                    DBUtils.addMovieToFavorite(db, movie);
                    cursorAdapter.swapCursor(DBUtils.getFavoriteMovies(db));
                    Toast.makeText(builder.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
}
