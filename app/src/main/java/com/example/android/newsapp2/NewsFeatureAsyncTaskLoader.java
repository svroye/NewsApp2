package com.example.android.newsapp2;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 *
 */

public class NewsFeatureAsyncTaskLoader extends AsyncTaskLoader<List<NewsFeatures>> {

    String Url;

    public NewsFeatureAsyncTaskLoader(Context context, String s) {
        super(context);
        Url = s;
    }

    //loadng of the data should occur in a background thread
    @Override
    public List<NewsFeatures> loadInBackground() {
        if (Url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of NewsFeatures.
        List<NewsFeatures> earthquakes = HelperMethods.fetchNews(Url);
        return earthquakes;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
