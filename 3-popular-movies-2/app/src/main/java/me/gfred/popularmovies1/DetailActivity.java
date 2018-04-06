package me.gfred.popularmovies1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies1.data.FavoriteMoviesDBHelper;
import me.gfred.popularmovies1.models.Movie;
import me.gfred.popularmovies1.utils.DBUtils;

/**
 * Created by Gfred on 3/3/2018.
 */

public class DetailActivity extends AppCompatActivity {
    static boolean isFavorite;

    SQLiteDatabase db;
    Movie movie;
    Cursor cursor;


    static final String IMAGE_PARAM = "http://image.tmdb.org/t/p/w500/";
    @BindView(R.id.image_iv)
    ImageView imageView;

    @BindView(R.id.original_title_tv)
    TextView title;

    @BindView(R.id.overview_tv)
    TextView overview;

    @BindView(R.id.rating_tv)
    TextView vote;

    @BindView(R.id.release_date_tv)
    TextView releaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra("movie")) {
            movie = intent.getParcelableExtra("movie");
        }

        if (movie != null) {
            setTitle(movie.getOriginalTitle());
            populateUI(movie);
        }

    }

    void populateUI(Movie movie) {

        String image = IMAGE_PARAM + movie.getPosterPath();
        Log.v("IMAGE_URL", image);

        Picasso.with(DetailActivity.this)
                .load(image).into(imageView);

        title.setText(movie.getOriginalTitle());
        overview.setText(movie.getOverview());
        vote.setText(String.valueOf(movie.getVoteAverage()));
        releaseDate.setText(movie.getReleaseDate());

        db = new FavoriteMoviesDBHelper(this).getReadableDatabase();
        cursor = DBUtils.getFavoriteMovies(db);
       isFavorite = DBUtils.CheckIfDataAlreadyInDBorNot(db, movie.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.getItem(0);
        if(isFavorite) item.setTitle("Remove from favorites");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean changed = false;
        if(id == R.id.detail_favorite) {
            //TODO: add to favorite
            if(isFavorite) {
                if(DBUtils.removeMovieFromFavorite(db, movie.getId())) {
                    changed = true;
                    Toast.makeText(this, "Removed from favorites",
                            Toast.LENGTH_SHORT).show();
                }
                item.setTitle("Add to favorites");


            } else {
                DBUtils.addMovieToFavorite(db, movie);
                Toast.makeText(this, "Added to favorites",
                        Toast.LENGTH_SHORT).show();
                item.setTitle("Remove from favorites");
                changed = true;
            }

            Intent intentMessage=new Intent();

            //tells the cursor object to update to reflect latest information, since it's changed.
            intentMessage.putExtra("changed", changed);
            setResult(2,intentMessage);
        }
        return super.onOptionsItemSelected(item);
    }
}
