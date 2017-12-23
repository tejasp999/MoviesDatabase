package com.example.teja.midterm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by teja on 10/16/17.
 */

public class MovieResults implements Serializable {
    String movieName;
    String overView;
    String release_date;
    Double voteAverage;
    Double popularity;
    Boolean favorite;
    String posterPath;

    public static Comparator<MovieResults> COMPARE_BY_VOTE = new Comparator<MovieResults>() {
        public int compare(MovieResults one, MovieResults other) {
            return one.voteAverage.compareTo(other.voteAverage);
        }
    };

    public static Comparator<MovieResults> COMPARE_BY_POPULARITY = new Comparator<MovieResults>() {
        public int compare(MovieResults one, MovieResults other) {
            return one.popularity.compareTo(other.popularity);
        }
    };
    static public MovieResults createResults(JSONObject jsonObject) throws JSONException {
        MovieResults trackObject = new MovieResults();
        trackObject.setMovieName(jsonObject.getString("original_title"));
        trackObject.setOverView(jsonObject.getString("overview"));
        trackObject.setRelease_date(jsonObject.getString("release_date"));
        trackObject.setVoteAverage(jsonObject.getDouble("vote_average"));
        trackObject.setPopularity(jsonObject.getDouble("popularity"));
        trackObject.setPosterPath(jsonObject.getString("poster_path"));
        trackObject.setFavorite(false);
        return trackObject;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath() {

        return posterPath;
    }
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getOverView() {

        return overView;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Boolean getFavorite() {
        return favorite;
    }
}
