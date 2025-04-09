
package com.example.mgi_finalproject;

import static android.app.ProgressDialog.show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class SearchFragment extends Fragment {

    SearchView searchBar;
    ListView searchList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchList = view.findViewById(R.id.search_list);

        ProgressBar progressBar = view.findViewById(R.id.bar);
        progressBar.setVisibility(View.GONE);

        searchBar = view.findViewById(R.id.search_bar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                new DataManager.fetchData(SearchFragment.this, progressBar).execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setVisibility(View.VISIBLE);
                new DataManager.fetchData(SearchFragment.this, progressBar).execute(newText);
                return false;
            }

        });


        return view;
    }





//Call List Adapter Passing in Searched Content
    public void updateList(String[][] result) {

        MyListAdapter adapter = new MyListAdapter(getContext(), result); // add data
        searchList.setAdapter(adapter); // Set adapter to ListView

        searchList.setOnItemClickListener((parent, itemView, position, id) -> {
            String[] article = (String[]) parent.getAdapter().getItem(position);

            String title = article[0];
            String url = article[1];
            String section = article[2];

            Intent articleDisplay = new Intent( getContext(), ArticleDisplay.class);
            articleDisplay.putExtra("title", title);
            articleDisplay.putExtra("url", url);
            articleDisplay.putExtra("section", section);

            Log.d("complete", "searchList.setOnItemClickListener");

            startActivity(articleDisplay);
        });

  //add to favourites
        searchList.setOnItemLongClickListener((parent, itemView, position, id) -> {
            String[] article = (String[]) parent.getAdapter().getItem(position);

            String title = article[0];
            String url = article[1];
            String section = article[2];

            try {
                DataManager.addFavourite(getContext(), article);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Toast.makeText(getContext(), getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();

            return true;
        });

    }

//List adapter
    public class MyListAdapter extends BaseAdapter {

        private Context context;
        private String[][] data;

        public MyListAdapter(Context context, String[][] data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data != null ? data.length : 0;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.article_list_layout, parent, false);

            //make a new row:
            if (newView == null) {
                newView = inflater.inflate(R.layout.article_list_layout, parent, false);

            }
            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.webTitle);
            String[] article = data[position];
            tView.setText(article[0]);

            return newView;
        }

    }
}
