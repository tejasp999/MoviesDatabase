//Midterm
//Prabhakar Teja Seeda
package com.example.teja.midterm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;

public class DetailsActivity extends AppCompatActivity {
    MovieResults results;
    TextView t1,t2,t3,t4;
    ImageView imageView;
    String imageURL = "https://image.tmdb.org/t/p/w342/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getIntent().getExtras() != null) {
            results = (MovieResults) getIntent().getExtras().getSerializable(MainActivity.TRACK_RESULTS);
            t1 = (TextView) findViewById(R.id.titleTextView);
            t1.setText(results.getMovieName());
            t2 = (TextView) findViewById(R.id.overViewText);
            t2.setText(results.getOverView());
            t3 = (TextView) findViewById(R.id.releaseDateText);
            t3.setText(results.getRelease_date());
            t4 = (TextView) findViewById(R.id.ratingText);
            t4.setText(results.getVoteAverage().toString());
            imageView = (ImageView) findViewById(R.id.largeImageView);
            imageURL=imageURL+results.getPosterPath();
            Picasso.with(this).load(imageURL).into(imageView);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.homeButton:
                Intent i = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.sortRate:
                return true;
            case R.id.sortPopular:
                return true;
            case R.id.actQuit:
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                return true;
        }
        return true;
    }
}
