package com.udacity.pradeepkumarr.popmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.udacity.pradeepkumarr.popmovies.R;
import com.udacity.pradeepkumarr.popmovies.adaptors.MovieGridAdapter;
import com.udacity.pradeepkumarr.popmovies.adaptors.ReviewAdapter;
import com.udacity.pradeepkumarr.popmovies.adaptors.TrailerAdapter;
import com.udacity.pradeepkumarr.popmovies.asynctask.FetchFavoritesMovieTask;
import com.udacity.pradeepkumarr.popmovies.asynctask.FetchMovieReviewsTask;
import com.udacity.pradeepkumarr.popmovies.asynctask.FetchMovieTrailerTask;
import com.udacity.pradeepkumarr.popmovies.common.MovieUtil;
import com.udacity.pradeepkumarr.popmovies.db.MovieContract;
import com.udacity.pradeepkumarr.popmovies.db.MovieDbHelper;
import com.udacity.pradeepkumarr.popmovies.domain.Movie;
import com.udacity.pradeepkumarr.popmovies.domain.Review;
import com.udacity.pradeepkumarr.popmovies.domain.Trailer;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 * @author pradeepkumarr
 */
public class MovieDetailActivityFragment extends Fragment {

    private MovieGridAdapter movieGridAdapter;
    private Toolbar mToolbar;
    public  TextView titleTV ;
    public TextView synopsisTV;
    public TextView ratingTV;
    public ImageView posterimage;
    public ImageView backgroundView;
    public TextView release_dateTV;
    public String sort_by;
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private ListView reviewListView;
    private ListView trailerListView;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    public MovieDetailActivityFragment() {
        setHasOptionsMenu(true);
    }
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private MenuItem favMenuItem;
    private Movie mMovie;
    private static final String ARG_MOVIE = "movie";

    private final String LOG_TAG  = MovieDetailActivityFragment.class.getSimpleName();

    private final String FORECAST_HASH_TAG  = "#PopularMovies1";

    /*private String synopsis;
    private String releaseDate;
    private String movieTitle;
    private String rating;
    private String posterPath;
    private String backgroundPosterPath;

    public String id;
    public String id;*/
    public static final String image_poster_url = "http://image.tmdb.org/t/p/w185/";
    public MovieDbHelper helper ;
    public Bundle extras;
    public boolean favoriteStatus;
    public boolean mTwoPane = false;

    public static MovieDetailActivityFragment newInstance(Movie movie) {
        MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(ARG_MOVIE);
        }
        /*if(mMovie!=null)
            getActivity().setTitle(mMovie.getTitle());

        /*if (!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }*/

        helper = new MovieDbHelper(getActivity());

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        synopsisTV = (TextView) rootView.findViewById(R.id.synopsis);
        titleTV =  ((TextView) rootView.findViewById(R.id.title));

        release_dateTV = (TextView) rootView.findViewById(R.id.release_date);

        //backgroundView = (ImageView) rootView.findViewById(R.id.backgroundView);
        ratingTV = (TextView) rootView.findViewById(R.id.ratingBar);

        posterimage = (ImageView) rootView.findViewById(R.id.poster_image);

        Intent intent = getActivity().getIntent();


        extras = intent.getExtras();



        reviewListView = (ListView)rootView.findViewById(R.id.reviewList);
        trailerListView = (ListView)rootView.findViewById(R.id.trailerList);




        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String trailerLink = trailers.get(position).getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerLink));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {

                    startActivity(intent);
                }

            }
        });

        if(savedInstanceState!=null){
            loadFromSavedInstance(savedInstanceState);


        }else{
            if(extras !=null && !extras.isEmpty()){
                //mMovie= intent.getParcelableExtra("movie");

                if(mMovie == null){
                    mMovie = extras.getParcelable("movie");
                }
                /*
                releaseDate = intent.getStringExtra("release_date");

                movieTitle = intent.getStringExtra("title");

                rating = intent.getStringExtra("rating");

                posterPath = intent.getStringExtra("poster_path");

                id = intent.getStringExtra("id");
                backgroundPosterPath = intent.getStringExtra("backgroundPosterPath");*/

                loadMovieInfo();

            }else{
                if(mMovie != null){
                    loadMovieInfo();
                }

            }

            trailers = new ArrayList<>();
            reviews = new ArrayList<>();


            if(MovieUtil.isOnline(getActivity()) && mMovie !=null) {
                new MovieReviewTask().execute(mMovie.getId());
                new MovieTrailerTask().execute(mMovie.getId());
            }else {
                MovieUtil.showDialogWhenOffline(getActivity(), sort_by);
            }


            trailerAdapter = new TrailerAdapter(getActivity(), trailers);
            trailerListView.setAdapter(trailerAdapter);
            reviewAdapter = new ReviewAdapter(getActivity(), reviews);
            reviewListView.setAdapter(reviewAdapter);


        }


        return rootView;
    }

    private void loadFromSavedInstance(Bundle savedInstanceState) {
        //extras = savedInstanceState.getBundle("extras");
/*
        movieTitle = savedInstanceState.getString("movieTitle");

        rating = savedInstanceState.getString("rating");

        releaseDate = savedInstanceState.getString("releaseDate");

        synopsis = savedInstanceState.getString("synopsis");

        posterPath = savedInstanceState.getString("posterPath");
        backgroundPosterPath = savedInstanceState.getString("backgroundPosterPath");

        id = savedInstanceState.getString("id");
        */
        mMovie = savedInstanceState.getParcelable("movie");

        loadMovieInfo();

        trailers = savedInstanceState.getParcelableArrayList("trailers");
        reviews = savedInstanceState.getParcelableArrayList("reviews");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments!=null) {

            mMovie = arguments.getParcelable("movie");
            mTwoPane = arguments.getBoolean("mTwoPane");


        }



    }


    private void loadMovieInfo() {

        synopsisTV.setText(mMovie.getOverview());
        release_dateTV.setText(mMovie.getRelease_date());
        titleTV.setText(mMovie.getTitle());
        ratingTV.setText(Double.toString(mMovie.getVote_average()));


        Picasso.with(getContext())
                .load(image_poster_url +mMovie.getPoster_path())
                .placeholder(R.drawable.popmovies)
                .fit()
                .into(posterimage);

       /* Picasso.with(getContext())
                .load(image_poster_url +backgroundPosterPath)
                .placeholder(R.drawable.popmovies)
                .fit()
                .into(backgroundView);


*/

    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        if(mMovie != null)
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + FORECAST_HASH_TAG);
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        //
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }


        favMenuItem = menu.findItem(R.id.action_favorite);
        if(mMovie != null) {
            //new FavSelectionCheckTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getFavoriteCursor());
        }
        favMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!favoriteStatus) {
                    // Toast.makeText(getActivity(), "before set Fav", Toast.LENGTH_SHORT).show();
                    String result = saveToFavorites();
                    Toast.makeText(getActivity(), mMovie.getTitle() + result, Toast.LENGTH_SHORT).show();
                    favMenuItem.setIcon(R.drawable.favorite);
                } else {
                    Toast.makeText(getActivity(), mMovie.getTitle() + "is already your favorite", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setTitle(movieTitle);
        }
*/

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState !=null)
            loadFromSavedInstance(savedInstanceState);
    }



    private void addReviewsToAdaptor(ArrayList<Review> reviewList) {
        if(reviewList != null){
            reviewAdapter.clear();
            for(Review movie : reviewList){
                reviewAdapter.add(movie);
            }
             reviewAdapter.notifyDataSetChanged();

        }


    }


    private class MovieReviewTask extends FetchMovieReviewsTask {

        @Override
        protected void onPostExecute(ArrayList<Review> moviesList) {
            addReviewsToAdaptor(moviesList);
        }
    }

    private void addTrailerToAdaptor(ArrayList<Trailer> trailerList) {
        if(trailerList != null){
            trailerAdapter.clear();
            for(Trailer trailer : trailerList){
                trailerAdapter.add(trailer);
            }
            trailerAdapter.notifyDataSetChanged();

        }


    }


    private class MovieTrailerTask extends FetchMovieTrailerTask {

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailerList) {
            addTrailerToAdaptor(trailerList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*outState.putString("title", movieTitle);
        outState.putString("synopsis", synopsis);
        outState.putString("releaseDate", releaseDate);
        outState.putString("posterPath", posterPath);
        outState.putString("rating", rating);
        outState.putString("movieTitle", movieTitle);
        outState.putString("id", id);
        outState.putBundle("extras", extras);
        outState.putString("backgroundPosterPath", backgroundPosterPath);*/
        outState.putParcelable("movie",mMovie);
        outState.putParcelableArrayList("trailers", trailers);
        outState.putParcelableArrayList("reviews", reviews);

    }
    private String saveToFavorites() {

        SQLiteDatabase db = helper.getWritableDatabase();

        if (db == null) {

            return "DB is Null";
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, mMovie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, mMovie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, mMovie.getPoster_path());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getVote_average());
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, mMovie.getBackdrop_path());

        long newRowID = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
        db.close();

        String toastMessage;
        if (newRowID >= 0) {
            toastMessage = " added to favorites";
        } else {
            toastMessage = " already saved";
        }

        return toastMessage;
    }


    private Cursor getFavoriteCursor() {

        SQLiteDatabase db = helper.getReadableDatabase();


        String[] projection = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_URL,
                MovieContract.MovieEntry.COLUMN_BACKDROP_URL,
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_SYNOPSIS,
                MovieContract.MovieEntry.COLUMN_RATING
        };

        // String order = MovieContract.MovieEntry.RATING + " DESC";

        return db.query(MovieContract.MovieEntry.TABLE_NAME,
                projection,
                "_id = "+mMovie.getId(),
                null,
                null,
                null,"1");
    }
    private class FavSelectionCheckTask extends FetchFavoritesMovieTask {

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesList) {
            if(moviesList.size() > 0){
                favMenuItem.setIcon(R.drawable.favorite);
                //boolean useful to disable the on clicklsitener
                favoriteStatus = true;
            }else{
                favMenuItem.setIcon(R.drawable.favourite_add);
                favoriteStatus = false;
            }
        }
    }




}
