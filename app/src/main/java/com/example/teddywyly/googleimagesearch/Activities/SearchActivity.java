package com.example.teddywyly.googleimagesearch.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.support.v7.widget.SearchView;

import com.etsy.android.grid.StaggeredGridView;
import com.example.teddywyly.googleimagesearch.Adapters.ImageResultsAdapter;
import com.example.teddywyly.googleimagesearch.EndlessScrollListener;
import com.example.teddywyly.googleimagesearch.Fragments.SettingsDialog;
import com.example.teddywyly.googleimagesearch.Models.GoogleSearchSettings;
import com.example.teddywyly.googleimagesearch.Models.ImageResult;
import com.example.teddywyly.googleimagesearch.NetworkManager;
import com.example.teddywyly.googleimagesearch.R;


import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private SearchView svQuery;
    private StaggeredGridView sgvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private GoogleSearchSettings searchSettings;
    private NetworkManager networkManager;

    private Boolean isFetching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchSettings = new GoogleSearchSettings();
        networkManager = new NetworkManager(this);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        sgvResults.setAdapter(aImageResults);

    }

    public void setupViews() {
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
                fetchImagesForPage(1);
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

        if (svQuery == null || isFetching || page < 1) {
            return;
        }

        isFetching = true;
        String query = svQuery.getQuery().toString();

        networkManager.fetchImages(page, query, searchSettings.settingsAsParameters(), new NetworkManager.OnImageFetchListener() {
            @Override
            public void onSucess(ArrayList<ImageResult> images) {
                if (page == 1) {
                    imageResults.clear(); // If new search
                    aImageResults.notifyDataSetChanged();
                }
                imageResults.addAll(images);
                aImageResults.notifyDataSetChanged();
                isFetching = false;
            }
            @Override
            public void onFailure(Throwable throwable) {
                isFetching = false;
            }
        });
    }

    public void showErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.network_failure_warning)
        .setCancelable(true)
        .setPositiveButton(R.string.casual_dialog_accept,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onSaveSettings(GoogleSearchSettings settings) {
        searchSettings = settings;
        Log.d("Debug", "Save!");
    }
}
