//Midterm
//Prabhakar Teja Seeda
package com.example.teja.midterm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public static List<MovieResults> movieResultsList = new ArrayList<MovieResults>();
    String trackName, artistName;
    String s = "",finalURL = "";
    public static List<MovieResults> movieResultsList1 = new ArrayList<MovieResults>();
    ListView list;
    ListAdapter adapter;
    final static String URL = "url",TRACK_RESULTS = "trackresults";
    String movieURL = "https://api.themoviedb.org/3/search/movie?api_key=cec572c1304c2e5b12907b4ee5244e2c&page=1&query=";
    String baseURL = "https://ws.audioscrobbler.com/2.0/?format=json&method=track.search&api_key=bdebb829c13038655ef949113ad7daab&limit=20&track=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Search Movies");
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.ratingSort:
                if (!movieResultsList1.isEmpty() && movieResultsList1!=null){
                    Collections.sort(movieResultsList1, MovieResults.COMPARE_BY_VOTE);
                    setListData(movieResultsList1);
                }
                return true;
            case R.id.popularitySort:
                if (!movieResultsList1.isEmpty() && movieResultsList1!=null){
                    Collections.sort(movieResultsList1, MovieResults.COMPARE_BY_POPULARITY);
                    setListData(movieResultsList1);
                }
                return true;
            case R.id.favoriteDisplay:
                Intent i = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(i);
                return true;
            case R.id.QuitButton:
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                return true;
        }
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Hello",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("favorite", "");
        Type type = new TypeToken<ArrayList<MovieResults>>() {}.getType();
        ArrayList<MovieResults> arrayList = gson.fromJson(json, type);
        Log.d("the value on resume","id"+arrayList);
//        if (movieResultsList.isEmpty() && arrayList!=null) {
//            movieResultsList = arrayList;
//            adapter = new ListAdapter(this, movieResultsList);
//            list = (ListView) findViewById(R.id.favoriteMainView);
//            list.setAdapter(adapter);
//            adapter.setNotifyOnChange(true);
//            //saveSharedPreferences(movieResultsList);
//        }else {
//            adapter = new ListAdapter(this, movieResultsList);
//            list = (ListView) findViewById(R.id.favoriteMainView);
//            list.setAdapter(adapter);
//            adapter.setNotifyOnChange(true);
//            //saveSharedPreferences(movieResultsList);
//        }
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
//                MovieResults movieResults = movieResultsList1.get(position);
//                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
//                i.putExtra(TRACK_RESULTS, (Serializable) movieResults);
//                startActivity(i);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void setListData(List<MovieResults> setData){
        movieResultsList1 = setData;
        adapter = new ListAdapter(this, movieResultsList1);
        list = (ListView) findViewById(R.id.favoriteMainView);
        list.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        //saveSharedPreferences(movieResultsList1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
                MovieResults movieResults = movieResultsList1.get(position);
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra(TRACK_RESULTS, (Serializable) movieResults);
                startActivity(i);
            }
        });
    }
    public void saveSharedPreferences(List<MovieResults> resultsList){
        SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("Hello", MODE_PRIVATE);
        Gson gsonNew = new Gson();
        SharedPreferences.Editor prefEditor = preferences2.edit();
        String json2 = gsonNew.toJson(resultsList);
        prefEditor.putString("favorite", json2);
        prefEditor.commit();
    }

    public String getEncodedUrl() throws UnsupportedEncodingException {
        EditText getTrack = (EditText) findViewById(R.id.editText);
        String val="";
        s = (String) getTrack.getText().toString().trim();
        if(!s.isEmpty()) {
            val = URLEncoder.encode(s, "UTF-8");
        }
        else
        {
            Toast.makeText(this,"Enter text to search",Toast.LENGTH_LONG).show();
        }
        return movieURL+val;

    }
    public void searchFunction(View view) throws UnsupportedEncodingException{
        finalURL = getEncodedUrl();
        Log.d("The url is","Hello"+finalURL);
        new GetTrackAsyncTask().execute(finalURL);
    }
    public List<MovieResults> getSharedValues(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Hello", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("favorite", null);
        Type type = new TypeToken<ArrayList<MovieResults>>() {}.getType();
        ArrayList<MovieResults> arrayList = gson.fromJson(json, type);
        return arrayList;
    }
    private class GetTrackAsyncTask extends AsyncTask<String, Integer ,ArrayList<MovieResults>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieResults> trackResults) {
            Log.d("The trackresults in ","postexecute is " + trackResults);
            if (trackResults!=null) {
                int totalTrackCount = trackResults.size();
                super.onPostExecute(trackResults);
                if (totalTrackCount > 0) {
//                    ArrayList<MovieResults> arrayList = getSharedValues();
//                    if (!arrayList.isEmpty() && arrayList!=null) {
//                        for (MovieResults favTrack : trackResults) {
//                            if (favTrack.getRelease_date().equals(movieData.getRelease_date())) {
//                                trackResultsShared.remove(favTrack);
//                                break;
//                            }
//                        }
//                    }
                    setListData(trackResults);
                } else {
                    Toast.makeText(MainActivity.this, "No Movie data found", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MovieResults> doInBackground(String... params) {
            try {
                java.net.URL url = new URL(params[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Log.d("the response","code is " + connection.getResponseCode());
                int statusCode = connection.getResponseCode();
                if(statusCode== HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = reader.readLine();
                    while(line!=null){
                        stringBuilder.append(line);
                        line = reader.readLine();
                    }
                    return parseTrackResult(stringBuilder.toString());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<MovieResults> parseTrackResult(String in) throws JSONException {
            ArrayList<MovieResults> trackResults = new ArrayList<>();
            JSONObject rootObject = new JSONObject(in);
            JSONArray trackArray = rootObject.getJSONArray("results");
            for(int i=0;i<trackArray.length();i++){
                JSONObject trackJSONObject = trackArray.getJSONObject(i);
                trackResults.add(MovieResults.createResults(trackJSONObject));
            }
            return trackResults;
        }
    }
}
