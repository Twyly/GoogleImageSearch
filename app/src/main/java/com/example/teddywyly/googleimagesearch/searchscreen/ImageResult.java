package com.example.teddywyly.googleimagesearch.searchscreen;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by teddywyly on 5/11/15.
 */
public class ImageResult implements Parcelable {

    public String fullUrl;
    public String thumbUrl;
    public String title;
    public Integer imageWidth;
    public Integer imageHeight;

    public ImageResult(JSONObject JSON) {
        try {
            fullUrl = JSON.getString("url");
            thumbUrl = JSON.getString("tbUrl");
            title = JSON.getString("title");
            imageWidth = JSON.getInt("width");
            imageHeight = JSON.getInt("height");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for (int i=0; i<array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    // Properties must be written and read in the same order

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullUrl);
        parcel.writeString(thumbUrl);
        parcel.writeString(title);
        parcel.writeInt(imageWidth);
        parcel.writeInt(imageHeight);
    }

    private ImageResult(Parcel in) {
        fullUrl = in.readString();
        thumbUrl = in.readString();
        title = in.readString();
        imageWidth = in.readInt();
        imageHeight = in.readInt();
    }


    public static final Parcelable.Creator<ImageResult> CREATOR
            = new Parcelable.Creator<ImageResult>() {

        @Override
        public ImageResult createFromParcel(Parcel parcel) {
            return new ImageResult(parcel);
        }

        @Override
        public ImageResult[] newArray(int i) {
            return new ImageResult[i];
        }
    };



}
