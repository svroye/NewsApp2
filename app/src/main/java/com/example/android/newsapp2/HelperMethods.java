package com.example.android.newsapp2;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class contains all the helper methods that are needed to create a URL,
 * make an http request
 * etc
 */

public final class HelperMethods {

    private static final String LOG_KEY = HelperMethods.class.toString();
    /**
     * private constructor; this class is just to combine all the relevant methods used for
     * requesting the data, but the user should never make an object instance of this class
     */
    private HelperMethods() {

    }

    /**
     * method called to create a URL out of a String
     * @param s : string which needs to be transformed to a URL
     * @return : URL of the input string, or null if a wrong input string was present
     */
    private static URL createUrl(String s) {
        URL url;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            return null;
        }
        return url;
    }

    /**
     *  make http request with the server specified by the url, read the data, and put it in a
     *  String which contains the JSON response
     * @param url : url for making the request
     * @return : string containing the JSON response
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";
        HttpURLConnection urlConnection = null;
        InputStream input = null;
        if (url == null) {
            return response;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            input = urlConnection.getInputStream();
            response = readFromStream(input);
        } catch (IOException e) {
            Log.e(LOG_KEY, "Failed to make HTTP request");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (input != null) {
                input.close();
            }
        }
        return response;
    }

    /**
     * takes in an InputStream of data, reads it and then transforms it into a String called the JSONResponse
     * @param input  ; inputStream
     * @return : string called the JSONResponse
     * @throws IOException
     */
    private static String readFromStream(InputStream input) throws IOException {
        StringBuilder output = new StringBuilder();
        if (input != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *  extracts the fields needed to create a NewsFeature object and sorts all of these objects
     *  intoo a List
     * @param newsfeaturesJSON : JSON response from the http request, from which the
     *                         desired fields should be extracted
     * @return : List of NewsFeatures used to display to the user
     */
    public static List<NewsFeatures> extractFeaturesFromJson(String newsfeaturesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsfeaturesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding NewsFeatures to
        List<NewsFeatures> newsFeatures = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject root = new JSONObject(newsfeaturesJSON);
            JSONObject response = root.getJSONObject("response");
            Log.v(LOG_KEY, response.toString());
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNewsFeature = results.getJSONObject(i);
                String title = currentNewsFeature.getString("webTitle");
                String url = currentNewsFeature.getString("webUrl");
                String sec = currentNewsFeature.getString("sectionName");
                String date = currentNewsFeature.getString("webPublicationDate");
                NewsFeatures n = new NewsFeatures(title, url, sec, date);
                newsFeatures.add(n);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_KEY, "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return newsFeatures;
    }

    /**
     * combines all of the above methods to create a URL, make http request, read data
     * and extract the desired fields from the json response
     * @param requestUrl : string representing the url to start with
     * @return : List of Newsfeatures
     */
    public static List<NewsFeatures> fetchNews(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_KEY, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of NewsFeatures
        List<NewsFeatures> newsList = extractFeaturesFromJson(jsonResponse);

        // Return the list of NewsFeatures
        return newsList;
    }
}
