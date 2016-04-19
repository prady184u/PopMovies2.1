package com.udacity.pradeepkumarr.popmovies.domain;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.pradeepkumarr.popmovies.db.MovieContract;

/**
 * Created by pradeepkumarr on 03/03/16.
 */
public class Movie implements Parcelable {


    private String poster_path;
    private String overview ;
    private String  release_date ;
    private String  id ;// 293660
    private String original_title ;
    private String title ;
    private String backdrop_path ;
    private double popularity ;
    private int vote_count ;
    private double  vote_average ;// 7.33


    public Movie(String poster_path, String overview, String release_date, String id, String original_title, String title, String backdrop_path, double popularity, int vote_count, double vote_average) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.original_title = original_title;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
    }

    private Movie(Parcel in){
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readString();
        original_title = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readDouble();
        vote_count = in.readInt();
        vote_average = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(id);
        parcel.writeString(original_title);
        parcel.writeString(title);
        parcel.writeString(backdrop_path);
        parcel.writeDouble(popularity);
        parcel.writeInt(vote_count);
        parcel.writeDouble(vote_average);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };


    public Movie(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry._ID));
        this.title = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE));
        this.overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
        this.poster_path = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_POSTER_URL));
        this.backdrop_path = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_BACKDROP_URL));
        this.release_date = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
        this.vote_average = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RATING));
        this.vote_count = cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_VOTE_COUNT));
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }



}
