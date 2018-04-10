package me.gfred.popularmovies2.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.adapter.ReviewsAdapter;
import me.gfred.popularmovies2.adapter.TrailersAdapter;
import me.gfred.popularmovies2.data.FavoriteMoviesDBHelper;
import me.gfred.popularmovies2.model.Movie;
import me.gfred.popularmovies2.utils.DBUtils;
import me.gfred.popularmovies2.utils.JsonUtils;
import me.gfred.popularmovies2.utils.NetworkUtils;

/**
 * Created by Gfred on 3/3/2018.
 */

public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailerClickListener {
    static boolean isFavorite;
    SQLiteDatabase db;
    static Movie movie;
    Cursor cursor;
    Context context;

    static final String IMAGE_PARAM = "http://image.tmdb.org/t/p/w500/";
    static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

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

    @BindView(R.id.review_recyclerview)
    RecyclerView reviewRecyclerView;

    @BindView(R.id.trailers_recyclerview)
    RecyclerView trailerRecyclerView;

    @BindView(R.id.textView5)
    TextView reviewsTitle;

    @BindView(R.id.trailers_text)
    TextView trailersText;

    TrailersAdapter trailersAdapter;


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
            populateUI(movie);
        }

        context = this;

         trailersAdapter = new TrailersAdapter(this, this);

    }

    void populateUI(Movie movie) {

        makeQuery(movie.getId());
        setTitle(movie.getOriginalTitle());
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
       cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.getItem(0);
        if(DBUtils.CheckIfDataAlreadyInDBorNot(db, movie.getId())) item.setTitle(R.string.remove_frm_favs);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean changed = false;

        if(id == R.id.detail_favorite) {

            if(isFavorite) {
                if(DBUtils.removeMovieFromFavorite(db, movie.getId())) {
                    changed = true;
                    Toast.makeText(this, R.string.removed_from_favorites,
                            Toast.LENGTH_SHORT).show();
                    item.setTitle(R.string.add_to_favs);
                    isFavorite = false;
                }
            } else {
                DBUtils.addMovieToFavorite(db, movie);
                Toast.makeText(this, R.string.added_to_favorites,
                        Toast.LENGTH_SHORT).show();
                item.setTitle(R.string.remove_frm_favs);
                changed = true;
                isFavorite = true;
            }

            Intent intentMessage = new Intent();

            //tells the cursor object to update to reflect latest information, since it's changed.
            intentMessage.putExtra("changed", changed);
            setResult(2, intentMessage);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClicked(String url) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + url)));
    }

    public class TrailerReviewTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... urls) {
            URL reviewURl = urls[0];
            URL trailerURL = urls[1];

            String[] jsonResults = new String[2];
            jsonResults[0] = NetworkUtils.getResponseFromHttpUrl(reviewURl);
            jsonResults[1] = NetworkUtils.getResponseFromHttpUrl(trailerURL);
            return jsonResults;

        }

        @Override
        protected void onPostExecute(String[] strings) {
            List<Pair<String,String>> reviews = null;
            List<Pair<String, String>> trailers = null;
            try {
                trailers = JsonUtils.parseTrailers(strings[0]);
               reviews = JsonUtils.parseReviews(strings[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(reviews != null && reviews.size() > 0) {
                movie.setReviews(reviews);
                displayReviews(movie.getReviews());
           }

           if(trailers!= null && trailers.size() > 0) {
               movie.setTrailers(trailers);
              displayTrailers(movie.getTrailers());
           }

        }
    }

    private void displayTrailers(List<Pair<String, String>> trailers) {
        trailersText.setVisibility(View.VISIBLE);

        trailersAdapter.setTrailers(trailers);
        trailerRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        trailerRecyclerView.setAdapter(trailersAdapter);
        trailerRecyclerView.setClickable(true);
    }

    void displayReviews(List<Pair<String, String>> reviews) {

        //enable visibility of reviews label, and scale height of recyclerview to display items
        reviewsTitle.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = reviewRecyclerView.getLayoutParams();
        int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height;
        reviewRecyclerView.setLayoutParams(params);

        //load reviews into recyclerview
        ReviewsAdapter adapter = new ReviewsAdapter(context, reviews);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false));
        reviewRecyclerView.setAdapter(adapter);
    }


    private void makeQuery(int id) {
        URL trailers = NetworkUtils.buildMovieTrailersQuery(id);
        URL reviews = NetworkUtils.buildMovieReviewsQuery(id);
        new TrailerReviewTask().execute(trailers, reviews);
    }

}
