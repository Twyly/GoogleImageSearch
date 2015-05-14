package com.example.teddywyly.googleimagesearch.Activities;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.support.v7.widget.SearchView;

import com.etsy.android.grid.StaggeredGridView;
import com.example.teddywyly.googleimagesearch.Adapters.ImageResultsAdapter;
import com.example.teddywyly.googleimagesearch.EndlessScrollListener;
import com.example.teddywyly.googleimagesearch.Fragments.SettingsDialog;
import com.example.teddywyly.googleimagesearch.Models.GoogleSearchSettings;
import com.example.teddywyly.googleimagesearch.Models.ImageResult;
import com.example.teddywyly.googleimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private final String SEACH_ENDPOINT = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
    private final int ITEMS_PER_FETCH = 8;

    //private EditText etQuery;
    private SearchView svQuery;
    private StaggeredGridView sgvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private GoogleSearchSettings searchSettings;
    private Boolean isFetching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchSettings = new GoogleSearchSettings(ITEMS_PER_FETCH);
        searchSettings.color = GoogleSearchSettings.ImageColor.RED;
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        sgvResults.setAdapter(aImageResults);


    }

    public void setupViews() {
        //etQuery = (EditText)findViewById(R.id.etQuery);
        sgvResults = (StaggeredGridView)findViewById(R.id.sgvResults);
        sgvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(i);
                intent.putExtra("result", result);
                startActivity(intent);
            }
        });
        sgvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchImagesForPage(page);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        svQuery = (SearchView) MenuItemCompat.getActionView(searchItem);

        svQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Debug", "Query!");
                fetchImagesForPage(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.miSettings) {
            showSettingsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SettingsDialog settingsDialog = SettingsDialog.newInstance(searchSettings);
        settingsDialog.show(fm, "fragment_settings");
    }


    public void fetchImagesForPage(final int page) {

        if (svQuery == null || isFetching) {
            return;
        }
        isFetching = true;
        String query = svQuery.getQuery().toString();

        String searchUrl = SEACH_ENDPOINT + "&start=" + searchSettings.resultsPerPage*page + searchSettings.settingsAsQueryString() + "&q=" + query;
        Log.d("DEBUG", searchUrl);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJSON = null;
                try {
                    imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    if (page == 0) {
                        imageResults.clear(); // If new search
                        aImageResults.notifyDataSetChanged();
                    }
                    imageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                    aImageResults.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isFetching = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                isFetching = false;
            }
        });
    }

    @Override
    public void onSaveSettings(GoogleSearchSettings settings) {
        searchSettings = settings;
        Log.d("Debug", "Save!");
    }
}
