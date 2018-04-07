package me.gfred.popularmovies2.activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.net.URL;

import me.gfred.popularmovies2.utils.NetworkUtils;

/**
 * Created by Gfred on 3/9/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isOnline()) {
            makePopularMoviesQuery();

        } else {
            Toast.makeText(this,
                    "Please enable your internet connection first and try again",
                    Toast.LENGTH_SHORT).show();

            Thread t = new Thread() {
                public void run() {
                    try {
                        //sleep thread for 3 seconds
                        sleep(3000);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            t.start();
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class PopularMoviesQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... urls) {
            URL popularURL = urls[0];
            URL topRatedURL = urls[1];
            String jsonResults[] = new String[2];

            jsonResults[0] = NetworkUtils.getResponseFromHttpUrl(popularURL);
            jsonResults[1] = NetworkUtils.getResponseFromHttpUrl(topRatedURL);
            return jsonResults;
        }

        @Override
        protected void onPostExecute(String[] s) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("movies_data", s);
                startActivity(intent);
                finish();
            }
    }

    private void makePopularMoviesQuery() {
        URL popularMovies = NetworkUtils.buildPopularMoviesQuery();
        URL topRatedMovies = NetworkUtils.buildTopRatedMoviesQuery();
        new PopularMoviesQueryTask().execute(popularMovies, topRatedMovies);
    }
}
