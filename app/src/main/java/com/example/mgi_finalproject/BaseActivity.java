package com.example.mgi_finalproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        String activityName = this.getClass().getSimpleName();
        String versionName = "";
        try {
            versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Set the title in the format: "Activity Name - vX.X"
        getSupportActionBar().setTitle(activityName + " - v" + versionName);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent searchPage = new Intent( this, SearchActivity.class);
        Intent favPage = new Intent( this, FavActivity.class);
        Intent lastArticle = new Intent( this, ArticleDisplay.class);
        Intent homepage = new Intent( this, MainActivity.class);

        if (id == R.id.nav_search){
            startActivity(searchPage);

        } else if (id == R.id.nav_fav && !(this instanceof FavActivity)) {
            startActivity(favPage);
        } else if (id == R.id.nav_last ) {
            startActivity(lastArticle);
        } else if (id == R.id.nav_home ) {
            startActivity(homepage);
    }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.help) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            String m = "";
            if (this instanceof SearchActivity) {
                m = getString(R.string.sAlert);

            } else if (this instanceof FavActivity) {
                m = getString(R.string.fAlert);
            } else if (this instanceof ArticleDisplay) {
                m = getString(R.string.dAlert);
            };

            alertDialogBuilder.setMessage(m);
            alertDialogBuilder.create().show();

        }

        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tb_menu, menu);
        return true;
    }
}