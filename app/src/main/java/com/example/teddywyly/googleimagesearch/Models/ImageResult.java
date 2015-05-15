package com.example.teddywyly.googleimagesearch.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by teddywyly on 5/11/15.
 */
public class ImageResult implements Serializable {


    private static final long serialVersionUID = 3651874441433098830L;
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

}
