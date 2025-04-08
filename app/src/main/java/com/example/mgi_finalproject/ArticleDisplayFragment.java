package com.example.mgi_finalproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ArticleDisplayFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_display, container, false);
        Log.d("DEBUG", "View Inflated");

        String PREFS_FILE = "com.example.sharedPreferences.Article";
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();


//Get Data
        Bundle dataSent =  getArguments();
        Log.d("DEBUG", "dataSent: " + dataSent);
        String title;
        String url;
        String section;

        if (dataSent != null){
            //if new data, load it
            title = dataSent.getString("title");
            url = dataSent.getString("url");
            section = dataSent.getString("section");

            editor.putString("TITLE", title);
            editor.putString("URL", url);
            editor.putString("SECTION", section);
            editor.apply();
            Log.d("Debug","editor applied");
        } else {
            // Otherwise, load the most recent saved article
            title = prefs.getString("TITLE", "No title saved");
            url = prefs.getString("URL", "https://example.com");
            section = prefs.getString("SECTION", "Unknown");
            Log.d("Debug","Loaded saved article from Shared");
        }

        //Set Data
        TextView t = view.findViewById(R.id.title);
        t.setText(title);

        TextView u = view.findViewById(R.id.url);
        u.setText(url);
        String finalUrl = url;
        u.setOnClickListener(click -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
            startActivity(browserIntent);
        });

        TextView s = view.findViewById(R.id.section);
        s.setText(section);



        // end activity
        Button btn = view.findViewById(R.id.backButton);
        btn.setOnClickListener(click -> {
            getActivity().finish();
        });


        return view;
    }



}
