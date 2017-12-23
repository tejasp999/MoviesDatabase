//Midterm
//Prabhakar Teja Seeda
package com.example.teja.midterm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    ListView list;
    ListAdapter adapter;
    ArrayList<MovieResults> arrayList,checkArrayList = new ArrayList<MovieResults>();
    public static List<MovieResults> movieResultsList = new ArrayList<MovieResults>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Hello",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("favorite", "");
        Type type = new TypeToken<ArrayList<MovieResults>>() {}.getType();
        arrayList = gson.fromJson(json, type);
        Log.d("the value on resume","id"+arrayList);
        movieResultsList = arrayList;
        if (movieResultsList!=null && !movieResultsList.isEmpty()){

            adapter = new ListAdapter(this, arrayList);
            list = (ListView) findViewById(R.id.resultsListView);
            list.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
                    MovieResults movieResults = movieResultsList.get(position);
                    Intent i = new Intent(FavoritesActivity.this, DetailsActivity.class);
                    i.putExtra(MainActivity.TRACK_RESULTS, (Serializable) movieResults);
                    startActivity(i);
                }
            });
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.homeButton:
                Intent i = new Intent(FavoritesActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.sortRate:
                arrayList = getData();
                if (arrayList!=null&&!arrayList.isEmpty()) {
                    Collections.sort(checkArrayList, MovieResults.COMPARE_BY_VOTE);
                    setListData(checkArrayList);
                }
                return true;
            case R.id.sortPopular:
                arrayList = getData();
                if (arrayList!=null&&!arrayList.isEmpty()) {
                    Collections.sort(arrayList, MovieResults.COMPARE_BY_POPULARITY);
                    setListData(arrayList);
                }
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
    public void setListData(final List<MovieResults> setData){
        if (setData!=null && !setData.isEmpty()) {
            adapter = new ListAdapter(this, setData);
            list = (ListView) findViewById(R.id.favoriteMainView);
            list.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
            //saveSharedPreferences(movieResultsList1);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieResults movieResults = setData.get(position);
                    Intent i = new Intent(FavoritesActivity.this, DetailsActivity.class);
                    i.putExtra(MainActivity.TRACK_RESULTS, (Serializable) movieResults);
                    startActivity(i);
                }
            });
        }
    }
    public ArrayList<MovieResults> getData(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Hello",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("favorite", "");
        Type type = new TypeToken<ArrayList<MovieResults>>() {}.getType();
        arrayList = gson.fromJson(json, type);
        return arrayList;
    }
}
