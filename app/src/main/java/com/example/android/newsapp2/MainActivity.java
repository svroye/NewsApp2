package com.example.android.newsapp2;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.order;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeatures>> {

    String key = "913f18e3-2a83-4c55-9135-bc90410184d4";
    String urlStart = "https://content.guardianapis.com/search?";

    NewsFeaturesAdapter adapter;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check the internet connection of the device
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nw = cm.getActiveNetworkInfo();
        boolean isConnected = nw != null && nw.isConnectedOrConnecting();

        //find the ListView in the xml file to set the adapter to
        final ListView listView = (ListView) findViewById(R.id.list_view);
        //find empty view for the case when the List is empty
        emptyView = (TextView) findViewById(R.id.empty_view);
        //create adapter + set the adapter and empty tetx view to the Listview
        adapter = new NewsFeaturesAdapter(this, new ArrayList<NewsFeatures>());
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);

        //set ClickListener, such that a browser opens when the user clicks on
        //a news item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position).getUrl();
                Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(n);
            }
        });

        //if the device is conneced to the internet, start loading the data, i.e. create url
        //make http request,... If there is no connection, show this to the user by setting the
        //the text of the empty view to the corresponding string value
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            emptyView.setText(R.string.no_internet);
            ProgressBar p = (ProgressBar) findViewById(R.id.progress);
            p.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<NewsFeatures>> onCreateLoader(int id, Bundle args) {
        //ffirst the url should be created based on the user preferences
        Uri baseUri = Uri.parse(urlStart);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String section = sharedPrefs.getString(getString(R.string.setting_section), getString(R.string.all_value));
        String orderBy = sharedPrefs.getString(getString(R.string.setting_order_by), getString(R.string.order_by_newest_value));


        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("api-key", key);
        if (!section.equals("")) {
            uriBuilder.appendQueryParameter("section", section);
        }
        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NewsFeatureAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeatures>> loader, List<NewsFeatures> data) {
        //when loading is finished, clear previous adapter and set new data to the adapter
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }

        //set the empty text to "no news found" for the case the List is empty + hide progressbar
        emptyView.setText(R.string.empty_text);
        ProgressBar p = (ProgressBar) findViewById(R.id.progress);
        p.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeatures>> loader) {
        //clear the adapter when the loader is reset
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //open the activity of the Settings when the settings button is pressed
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
