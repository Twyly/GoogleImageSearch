package com.example.teddywyly.googleimagesearch;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.teddywyly.googleimagesearch.searchscreen.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by teddywyly on 5/14/15.
 */


public class NetworkManager {

    private final String SEACH_ENDPOINT = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
    private final int ITEMS_PER_FETCH = 8;

    public interface OnImageFetchListener {
        public void onSucess(ArrayList<ImageResult> images);
        public void onFailure(Throwable throwable);
    }

    private Context context;
    public NetworkManager(Context context) {
        this.context = context;
    }

    public void fetchImages(int page, String query, HashMap<String,String> parameters, final OnImageFetchListener listener) {

        if (!isNetworkAvailable()) {
            if (listener != null) {
                listener.onFailure(new Throwable("No Internet"));
            }
            return;
        }

        RequestParams params = new RequestParams();
        params.put("q", query);
        params.put("start", ITEMS_PER_FETCH * (page - 1));
        params.put("rsz", Integer.toString(ITEMS_PER_FETCH));

        for (HashMap.Entry<String, String> entry : parameters.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SEACH_ENDPOINT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJSON = null;
                try {
                    imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    if (listener != null) {
                        listener.onSucess(ImageResult.fromJSONArray(imageResultsJSON));
                    }
                } catch (JSONException e) {
                    if (listener != null) {
                        listener.onFailure(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (listener != null) {
                    listener.onFailure(throwable);
                }
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
