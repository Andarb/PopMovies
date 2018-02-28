package com.slackar.popularmovies;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.slackar.popularmovies.Utils.RetrofitClient;
import com.slackar.popularmovies.data.MoviesList;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    // Key for the movie ID value passed with intent from MainActivity
    public static final String MOVIE_ID_INTENT_KEY = "movie_id";

    // Backdrop url details
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String BACKDROP_SIZE = "w342";
    private static final String BACKDROP_URL = BASE_URL + BACKDROP_SIZE;

    // Movie details and labels
    @BindView(R.id.movie_details_view)
    TextView mMovieDetailsView;
    @BindView(R.id.backdrop_iv)
    ImageView mBackdropIV;
    @BindView(R.id.title_tv)
    TextView mTitleTV;
    @BindView(R.id.release_date_tv)
    TextView mReleaseDateTV;
    @BindView(R.id.vote_average_tv)
    TextView mVoteAverageTV;
    @BindView(R.id.overview_tv)
    TextView mOverviewTV;

    // Loading indicator
    @BindView(R.id.details_loading_pb)
    ProgressBar mLoadingPB;

    // Error message
    @BindView(R.id.error_message_details)
    LinearLayout mErrorMessageView;
    @BindView(R.id.error_tv)
    TextView mErrorTV;

    private com.slackar.popularmovies.data.Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        String id = getIntent().getStringExtra(MOVIE_ID_INTENT_KEY);
        retrieveMovieDetails(id);
    }

    /* Download and parse movie details using Retrofit */
    private void retrieveMovieDetails(String movieId) {
        hideErrorMessage();
        Call<com.slackar.popularmovies.data.Movie> getCall = RetrofitClient.getMovieDetails(movieId);

        getCall.enqueue(new Callback<com.slackar.popularmovies.data.Movie>() {
            @Override
            public void onResponse(Call<com.slackar.popularmovies.data.Movie> call, Response<com.slackar.popularmovies.data.Movie> response) {
                if (response.isSuccessful()) {
                    mMovie = response.body();

                    populateUI();
                    mLoadingPB.setVisibility(View.INVISIBLE);
                } else {
                    showErrorMessage(getString(R.string.error_internet));
                    Log.w(TAG, getString(R.string.error_server_status) + response.code());
                }
            }

            @Override
            public void onFailure(Call<com.slackar.popularmovies.data.Movie> call, Throwable t) {
                showErrorMessage(getString(R.string.error_internet));
            }
        });

    }

    /* Set all the movie details */
    private void populateUI() {
        Picasso.with(this).load(BACKDROP_URL + mMovie.getBackdropPath()).into(mBackdropIV);
        mTitleTV.setText(mMovie.getTitle());
        mReleaseDateTV.setText(mMovie.getReleaseDate());
        mVoteAverageTV.setText(String.valueOf(mMovie.getVoteAverage()));
        mOverviewTV.setText(mMovie.getOverview());
    }

    /* Hides the error message and makes the movie details visible again */
    private void hideErrorMessage() {
        mErrorMessageView.setVisibility(View.GONE);
        mMovieDetailsView.setVisibility(View.VISIBLE);
        mLoadingPB.setVisibility(View.VISIBLE);
    }

    /* Shows the error message and hides everything else */
    private void showErrorMessage(String error) {
        mLoadingPB.setVisibility(View.GONE);
        mMovieDetailsView.setVisibility(View.GONE);
        mErrorTV.setText(error);
        mErrorMessageView.setVisibility(View.VISIBLE);
    }
}
