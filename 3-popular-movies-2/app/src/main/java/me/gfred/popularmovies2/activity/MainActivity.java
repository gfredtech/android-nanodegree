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
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.adapter.MainRecyclerAdapter;
import me.gfred.popularmovies2.data.FavoriteMoviesDBHelper;
import me.gfred.popularmovies2.models.Movie;
import me.gfred.popularmovies2.utils.DBUtils;
import me.gfred.popularmovies2.utils.JsonUtils;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdapter.MovieClickListener{
    private SQLiteDatabase db;
    ArrayList<Movie> popularMovies;
    ArrayList<Movie> topRatedMovies;
    String json[];
    Boolean isPopular;
    Cursor cursor;

    @BindView(R.id.movie_recyclerview)
    RecyclerView recyclerView;


    MainRecyclerAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        json = new String[2];

        Intent intent = getIntent();
        if(intent.hasExtra("movies_data")) {
            json = intent.getExtras().getStringArray("movies_data");
        }

        FavoriteMoviesDBHelper helper = new FavoriteMoviesDBHelper(this);

        db = helper.getWritableDatabase();
        cursor = DBUtils.getFavoriteMovies(db);

        cursorAdapter = new MainRecyclerAdapter(this, cursor, this);

        if(json != null) {

            try {
                popularMovies = JsonUtils.parseListMovies(json[0]);
                topRatedMovies = JsonUtils.parseListMovies(json[1]);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, popularMovies, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        isPopular = true;
    }


    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2) {
            if(data != null) {
                Boolean message = data.getBooleanExtra("changed", false);

                if(message) cursorAdapter.swapCursor(DBUtils.getFavoriteMovies(db));
            }
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.top_rated) {
            if(isPopular) {
                MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, topRatedMovies, this);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(adapter);
                isPopular = false;
                item.setTitle(R.string.popular_menu);
                setTitle(getString(R.string.toprated_menu));
            } else {
                MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, popularMovies, this);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(adapter);
                isPopular = true;
                item.setTitle(R.string.toprated_menu);
                setTitle(getString(R.string.popular_menu));
            }
        } else if(id == R.id.favorite) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(cursorAdapter);
            setTitle(R.string.favorites);

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
}
