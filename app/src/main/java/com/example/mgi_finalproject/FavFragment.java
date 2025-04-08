package com.example.mgi_finalproject;

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
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.Objects;


public class FavFragment extends Fragment {

    ListView savedList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        savedList = view.findViewById(R.id.search_list);

        String [][] articles;

        try {
            articles = DataManager.getFavourite(requireContext());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        MyListAdapter adapter = new MyListAdapter(getContext(), articles); // add data
        savedList.setAdapter(adapter); // Set adapter to ListView

        savedList.setOnItemClickListener((parent, itemView, position, id) -> {
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

        //remove from favourites
        savedList.setOnItemLongClickListener((parent, itemView, position, id) -> {
            String[] article = (String[]) parent.getAdapter().getItem(position);

            String m = getResources().getString(R.string.snack_message);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content), m, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar snackbar1 = Snackbar.make(getActivity().findViewById(android.R.id.content), "Article Saved", Snackbar.LENGTH_SHORT);

                            try {
                                DataManager.addFavourite(getContext(), article);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            String [][] newArticles;
                            try {
                                newArticles = DataManager.getFavourite(requireContext());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            MyListAdapter adapter2 = new MyListAdapter(getContext(), newArticles); // add data
                            savedList.setAdapter(adapter2);
                            snackbar1.show();
                        }
                    });

            try {
                DataManager.deleteFavourite(getContext(), position);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String [][] newArticles;
            try {
                newArticles = DataManager.getFavourite(requireContext());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            MyListAdapter adapter2 = new MyListAdapter(getContext(), newArticles); // add data
            savedList.setAdapter(adapter2);
            snackbar.show();
            return true;
        });


        return view;

    }



    //List adapter
    class MyListAdapter extends BaseAdapter {

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