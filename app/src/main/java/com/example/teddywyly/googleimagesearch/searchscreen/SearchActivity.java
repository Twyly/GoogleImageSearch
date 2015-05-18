package com.example.teddywyly.googleimagesearch.searchscreen;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;

import com.etsy.android.grid.StaggeredGridView;
import com.example.teddywyly.googleimagesearch.detailscreen.ImageDisplayActivity;
import com.example.teddywyly.googleimagesearch.helpers.ErrorHelper;
import com.example.teddywyly.googleimagesearch.settingsscreen.SettingsDialog;
import com.example.teddywyly.googleimagesearch.R;


import org.json.JSONException;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements SettingsDialog.SettingsDialogListener {

    private SearchView svQuery;
    private StaggeredGridView sgvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private GoogleSearchSettings searchSettings;
    private NetworkManager networkManager;
    private ProgressBar pbImageFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchSettings = new GoogleSearchSettings();
        networkManager = new NetworkManager(this);

        setupViews();
        setupAdapter();

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

        // Add Progress Footer View
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress, null, false);
        sgvResults.addFooterView(footerView);
        pbImageFetch = (ProgressBar) footerView.findViewById(R.id.pbImageFetch);
        pbImageFetch.setVisibility(View.GONE);

    }

    public void setupAdapter() {
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        sgvResults.setAdapter(aImageResults);
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
                getSupportActionBar().setTitle(s);
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

        if (svQuery == null || page < 1) {
            return;
        }

        pbImageFetch.setVisibility(View.VISIBLE);
        if (page == 1) {
            imageResults.clear();
            aImageResults.notifyDataSetChanged();
        }

        //String query = svQuery.getQuery().toString();
        // Always contains the latest search query for infinate scroll, even if user presses back
        String query = getSupportActionBar().getTitle().toString();


        networkManager.fetchImages(page, query, searchSettings.settingsAsParameters(), new NetworkManager.OnImageFetchListener() {
            @Override
            public void onSucess(ArrayList<ImageResult> images) {

                imageResults.addAll(images);
                aImageResults.notifyDataSetChanged();
                pbImageFetch.setVisibility(View.GONE);

            }
            @Override
            public void onFailure(Throwable throwable) {
                pbImageFetch.setVisibility(View.GONE);
                if (throwable instanceof JSONException) {
                    ErrorHelper.showErrorAlert(SearchActivity.this, ErrorHelper.ErrorType.GENERIC);
                } else {
                    ErrorHelper.showErrorAlert(SearchActivity.this, ErrorHelper.ErrorType.NETWORK);
                }
            }
        });
    }

    @Override
    public void onSaveSettings(GoogleSearchSettings settings) {
        searchSettings = settings;
        fetchImagesForPage(1);
    }
}
