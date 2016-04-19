package com.udacity.pradeepkumarr.popmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pradeepkumarr on 04/16/16.
 */
public class Review implements Parcelable {


    private String author;
    private String content ;

    public Review(String author, String content) {
        this.setAuthor(author);
        this.setContent(content);
    }

    private Review(Parcel in){
        setAuthor(in.readString());
        setContent(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getAuthor());
        parcel.writeString(getContent());
    }

    public final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }

    };


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
