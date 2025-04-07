package com.example.mgi_finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ArticleDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String PREFS_FILE = "com.example.sharedPreferences.Article";
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();


//Get Data
        Intent dataSent = getIntent();
        String title = dataSent.getStringExtra("title");
        String url = dataSent.getStringExtra("url");
        String section = dataSent.getStringExtra("section");

// If new article info is passed, save it
        if (title != null && url != null && section != null) {
            editor.putString("TITLE", title);
            editor.putString("URL", url);
            editor.putString("SECTION", section);
            editor.apply(); // use apply instead of commit for async
        } else {
            // Otherwise, load the most recent saved article
            title = prefs.getString("TITLE", "No title saved");
            url = prefs.getString("URL", "https://example.com");
            section = prefs.getString("SECTION", "Unknown");
        }

    //Set Data
        TextView t = findViewById(R.id.title);
        t.setText(title);

        TextView u = findViewById(R.id.url);
        u.setText(url);
        String finalUrl = url;
        u.setOnClickListener(click -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
            startActivity(browserIntent);
        });

        TextView s = findViewById(R.id.section);
        s.setText(section);



    // end activity
        Button btn = findViewById(R.id.backButton);
        btn.setOnClickListener(click -> {
            finish();
        });

        };



    }
