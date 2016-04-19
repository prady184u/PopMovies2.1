package com.udacity.pradeepkumarr.popmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pradeepkumarr on 04/16/16.
 */
public class Trailer implements Parcelable {
    private String link;
    private String thumbnail;
    private String name;


    public Trailer(String link,String thumbnail,String name){
        this.setLink(link);
        this.setThumbnail(thumbnail);
        this.setName(name);
    }

    public Trailer(Parcel in) {
        setLink(in.readString());
        setThumbnail(in.readString());
        setName(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getLink());
        parcel.writeString(getThumbnail());
        parcel.writeString(getName());
    }
    public final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>(){

        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size ];
        }
    };

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
