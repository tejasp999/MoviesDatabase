//Midterm
//Prabhakar Teja Seeda
package com.example.teja.midterm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teja on 10/16/17.
 */

public class ListAdapter extends ArrayAdapter<MovieResults> {
    String imageURL = "https://image.tmdb.org/t/p/w154/";
    private Activity context;
    private List<MovieResults> trackResults;
    public List<MovieResults> trackResultsShared = new ArrayList<MovieResults>();
    MainActivity main = new MainActivity();
    public ListAdapter(Activity context, List<MovieResults> resultsList) {
        super(context, R.layout.resultsrow_list, (List<MovieResults>) resultsList);
        this.context = context;
        this.trackResults = resultsList;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.resultsrow_list, null,true);
        Log.d("the povalues are","in adapter" + position);
        final MovieResults movieData = getItem(position);
        TextView name = (TextView) rowView.findViewById(R.id.trackName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.smallImage);
        TextView artistName = (TextView) rowView.findViewById(R.id.artistName);
        name.setText(movieData.getMovieName());
        artistName.setText(movieData.getRelease_date());
        final ImageButton favoriteButton = (ImageButton) rowView.findViewById(R.id.favoriteImage);
        favoriteButton.setId(position);
        if (movieData.getFavorite()==false) {
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
        }else{
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
        }
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteButton.getId()==position && movieData.getFavorite() == false){
                    favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    Toast.makeText(context,"Added to Favorite List",Toast.LENGTH_LONG).show();
                    favoriteButton.setId(position+1);
                    movieData.setFavorite(true);
                    trackResultsShared = getSharedValues();
                    if (trackResultsShared!=null){
                        if(!trackResultsShared.contains(movieData)) {
                            trackResultsShared.add(movieData);
                        }
                        FavoritesActivity.movieResultsList = trackResultsShared;
                        Log.d("Peas","Fell"+MainActivity.movieResultsList);
                    }else {
                        FavoritesActivity.movieResultsList.add(movieData);
                    }
                    storeSharedPreferences(FavoritesActivity.movieResultsList);

                } else {
                    favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    Toast.makeText(context,"Removed from Favorite List",Toast.LENGTH_LONG).show();
                    favoriteButton.setId(position+1);
                    movieData.setFavorite(false);
                    trackResultsShared = getSharedValues();
                    for (MovieResults favTrack:trackResultsShared) {
                        if (favTrack.getRelease_date().equals(movieData.getRelease_date())) {
                            trackResultsShared.remove(favTrack);
                            break;
                        }
                    }
                    for (MovieResults movieTrack:FavoritesActivity.movieResultsList) {
                        if (movieTrack.getRelease_date().equals(movieData.getRelease_date())) {
                            FavoritesActivity.movieResultsList.remove(movieTrack);
                            break;
                        }
                    }
                    //MainActivity.trackResultsList.remove(trackData);
                    storeSharedPreferences(FavoritesActivity.movieResultsList);

                }
                notifyDataSetChanged();
            }
        });
        favoriteButton.setFocusable(false);
        String getPoster = movieData.getPosterPath();
        imageURL = imageURL+getPoster;
        Log.d("The imageURL","is"+imageURL);
        Picasso.with(context).load(imageURL).into(imageView);
        imageURL = "https://image.tmdb.org/t/p/w154/";
        return rowView;
    }
    public List<MovieResults> getSharedValues(){
        SharedPreferences preferences = context.getSharedPreferences("Hello", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("favorite", null);
        Type type = new TypeToken<ArrayList<MovieResults>>() {}.getType();
        ArrayList<MovieResults> arrayList = gson.fromJson(json, type);
        return arrayList;
    }
    public void storeSharedPreferences(List<MovieResults> trackDatas){
        SharedPreferences preferences = context.getSharedPreferences("Hello",Context.MODE_PRIVATE);
        Gson gsonNew = new Gson();
        SharedPreferences.Editor prefEditor = preferences.edit();
        String json2 = gsonNew.toJson(trackDatas);
        prefEditor.putString("favorite",json2);
        prefEditor.commit();
    }

}
