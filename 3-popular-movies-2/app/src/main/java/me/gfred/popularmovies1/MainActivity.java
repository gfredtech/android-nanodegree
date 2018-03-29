package me.gfred.popularmovies1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies1.data.FavoriteMoviesContract;
import me.gfred.popularmovies1.data.FavoriteMoviesDBHelper;
import me.gfred.popularmovies1.models.Movie;
import me.gfred.popularmovies1.utils.JsonUtils;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdapter.MovieClickListener {
    private SQLiteDatabase db;
    ArrayList<Movie> popularMovies;
    ArrayList<Movie> topRatedMovies;
    String json[];
    Boolean isPopular;

    @BindView(R.id.movie_recyclerview)
    RecyclerView recyclerView;
    FavoriteRecyclerAdapter mAdapter;

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

        Cursor cursor = getFavoriteMovies();

        mAdapter = new FavoriteRecyclerAdapter(this, cursor);


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
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.toggle_layout) {
            if(isPopular) {
                MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, topRatedMovies, this);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(adapter);
                isPopular = false;
                item.setTitle(R.string.popular_menu);
            } else {
                MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, popularMovies, this);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(adapter);
                isPopular = true;
                item.setTitle(R.string.toprated_menu);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private Cursor getFavoriteMovies() {
        return db.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TIMESTAMP);
    }
}
