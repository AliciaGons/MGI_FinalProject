package com.example.mgi_finalproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class DataManager {


    static class fetchData extends AsyncTask<String, Integer, String[][]> {

        String[][] articles;
        private final SearchFragment fragment;

        public fetchData(SearchFragment fragment) {
            this.fragment = fragment;
        }


        @Override
        protected String[][] doInBackground(String... strings) {

            String searchWords = strings[0].replaceAll("\\s", "");

            String searchURL = "https://content.guardianapis.com/search?api-key=4f732a4a-b27e-4ac7-9350-e9d0b11dd949&q=" + searchWords;

            // Call Guardian News to return list of articles
            try {
                // create URL object of server to contact
                URL url = new URL(searchURL);

                // open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String responseText = sb.toString(); //result is the whole string

                JSONObject jsonResponse = new JSONObject(responseText);
                JSONObject r = jsonResponse.getJSONObject("response");
                JSONArray results = r.getJSONArray("results");

                articles = new String[results.length()][3];

                for (int i = 0; i < results.length(); i++) {
                    JSONObject article = results.getJSONObject(i);
                    String title = article.getString("webTitle");
                    String webURL = article.getString("webUrl");
                    String section = article.getString("sectionName");

                    articles[i][0] = title;
                    articles[i][1] = webURL;
                    articles[i][2] = section;

                }

                Log.d("complete", Arrays.deepToString(articles));

                return articles;


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        protected void onPostExecute(String[][] results) {
            if (results != null) {
                fragment.updateList(results); // this passes the data back to the UI

                Log.d("complete", "onPostExecute");
            }
        }

    }

    static void addFavourite(Context context, String[] article) throws JSONException {

        String title = article[0];
        String url = article[1];
        String section = article[2];

        String PREFS_FILE = "com.example.sharedPreferences.SavedArticles";
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //Load existing data
        String json = prefs.getString("FAV_LIST", "[]");
        JSONArray favList = new JSONArray(json);

        // Create JSON object for the new article
        JSONObject articleJson = new JSONObject();
        try {
            articleJson.put("title", article[0]);
            articleJson.put("url", article[1]);
            articleJson.put("section", article[2]);
            favList.put(articleJson); // Add it to the list
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // add back to file
        editor.putString("FAV_LIST", favList.toString());
        editor.apply();

        Log.d("completelist", json);
    }

    public static String[][] getFavourite(Context context) throws JSONException {
        String PREFS_FILE = "com.example.sharedPreferences.SavedArticles";
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        //Load saved data
        String favList = prefs.getString("FAV_LIST", "[]");

        String[][] articles;

        JSONArray outerArray = new JSONArray(favList);
        articles = new String[outerArray.length()][3];

        for (int i = 0; i < outerArray.length(); i++) {
            JSONObject obj = outerArray.getJSONObject(i);
            articles[i][0] = obj.getString("title");
            articles[i][1] = obj.getString("url");
            articles[i][2] = obj.getString("section");
        }

        return articles;
    }
}