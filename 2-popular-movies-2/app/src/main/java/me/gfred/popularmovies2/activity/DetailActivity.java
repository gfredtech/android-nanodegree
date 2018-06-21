package me.gfred.popularmovies2.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies2.R;
import me.gfred.popularmovies2.adapter.ReviewsAdapter;
import me.gfred.popularmovies2.adapter.TrailersAdapter;
import me.gfred.popularmovies2.data.FavoriteMoviesContract;
import me.gfred.popularmovies2.model.Movie;
import me.gfred.popularmovies2.model.Reviews;
import me.gfred.popularmovies2.model.Trailers;
import me.gfred.popularmovies2.utils.ApiInterface;
import me.gfred.popularmovies2.utils.DBUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.gfred.popularmovies2.data.FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
import static me.gfred.popularmovies2.utils.ApiKey.API_KEY;
import static me.gfred.popularmovies2.utils.ApiKey.createRetrofitApi;

/**
 * Created by Gfred on 3/3/2018.
 */

public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailerClickListener {
    static boolean isFavorite;

    static Movie movie;
    Context context;

    static final String IMAGE_PARAM_LARGE = "http://image.tmdb.org/t/p/w500/";
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
    ReviewsAdapter reviewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.hasExtra("movie")) {
            movie = intent.getParcelableExtra("movie");

            if (movie != null) {
                ApiInterface apiInterface = createRetrofitApi();
                apiInterface.getMovieTrailers(movie.getId(), API_KEY).enqueue(movieTrailersCallback);
                populateUI(movie);
            }
        }

        context = this;
    }

    void populateUI(Movie movie) {

        setTitle(movie.getOriginalTitle());
        String image = IMAGE_PARAM_LARGE + movie.getPosterPath();
        Log.v("IMAGE_URL", image);

        Picasso.with(DetailActivity.this)
                .load(image).into(imageView);

        title.setText(movie.getOriginalTitle());
        overview.setText(movie.getOverview());
        vote.setText(String.valueOf(movie.getVoteAverage()));
        releaseDate.setText(movie.getReleaseDate());

        Cursor cursor = getContentResolver().query(DBUtils.queryFavorite(movie.getId()),
                null,
                null,
                null,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TIMESTAMP);

        if (cursor != null) {
            isFavorite = cursor.getCount() == 1;

            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.getItem(0);
        if (isFavorite) item.setTitle(R.string.remove_frm_favs);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.detail_favorite) {

            if (isFavorite) {
                Uri uri = DBUtils.deleteFavorite(movie.getId());
                getContentResolver().delete(uri, null, null);
                Toast.makeText(this, R.string.removed_from_favorites,
                        Toast.LENGTH_SHORT).show();
                item.setTitle(R.string.add_to_favs);
                isFavorite = false;

            } else {
                ContentValues cv = DBUtils.addMovieToFavorite(movie);
                Uri uri = getContentResolver().insert(CONTENT_URI, cv);

                if (uri != null) {
                    Toast.makeText(this, R.string.added_to_favorites,
                            Toast.LENGTH_SHORT).show();
                    item.setTitle(R.string.remove_frm_favs);

                    isFavorite = true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClicked(String url) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + url)));
    }


    private void displayTrailers(List<Trailers> trailers) {
        trailersText.setVisibility(View.VISIBLE);

        trailersAdapter = new TrailersAdapter(context, this);
        trailersAdapter.setTrailers(trailers);
        trailerRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        trailerRecyclerView.setAdapter(trailersAdapter);
        trailerRecyclerView.setClickable(true);
    }

    void displayReviews(List<Reviews> reviews) {

        if (reviewRecyclerView.getAdapter() == null) {

            //enable visibility of reviews label, and scale height of recyclerview to display items
            reviewsTitle.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = reviewRecyclerView.getLayoutParams();
            int height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = height;
            reviewRecyclerView.setLayoutParams(params);

            //load reviews into recyclerview
            reviewsAdapter = new ReviewsAdapter(context, reviews);
            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false));
            reviewRecyclerView.setAdapter(reviewsAdapter);
            reviewsAdapter.notifyDataSetChanged();
        }
    }

    Callback<List<Trailers>> movieTrailersCallback = new Callback<List<Trailers>>() {
        @Override
        public void onResponse(Call<List<Trailers>> call, Response<List<Trailers>> response) {
            displayTrailers(response.body());
            ApiInterface apiInterface = createRetrofitApi();
            apiInterface.getMovieReviews(movie.getId(), API_KEY).enqueue(movieReviewsCallback);

        }

        @Override
        public void onFailure(Call<List<Trailers>> call, Throwable t) {

        }
    };

    Callback<List<Reviews>> movieReviewsCallback = new Callback<List<Reviews>>() {
        @Override
        public void onResponse(Call<List<Reviews>> call, Response<List<Reviews>> response) {
            displayReviews(response.body());
        }

        @Override
        public void onFailure(Call<List<Reviews>> call, Throwable t) {

        }
    };

}
