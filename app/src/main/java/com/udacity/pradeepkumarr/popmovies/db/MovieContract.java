package com.udacity.pradeepkumarr.popmovies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pradeepkumarr on 18/04/16.
 */
public class MovieContract {



    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "movie";

        // Columns
        public static final String _ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_SYNOPSIS = "plot";

    }

}
