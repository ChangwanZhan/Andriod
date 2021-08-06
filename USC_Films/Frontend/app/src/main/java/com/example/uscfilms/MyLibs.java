package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uscfilms.modal.WatchlistData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyLibs {
    public static void openMediaDetailActivity(Context context, String id, String mediaType, String mediaName, String backdropPath, String posterPath) {
        Intent intent = new Intent(context, MediaDetailActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("MEDIA_TYPE", mediaType);
        intent.putExtra("MEDIA_NAME", mediaName);
        intent.putExtra("BACKDROP_PATH", backdropPath);
        intent.putExtra("POSTER_PATH", posterPath);
        context.startActivity(intent);
    }

    public static void shareFacebook(Context context, String mediaType, String id) {
        String facebookShareUrl = "https://www.facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/%s/%s";
        String url = String.format(facebookShareUrl, mediaType, id);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void shareTwitter(Context context, String mediaType, String id) {
        String twitterShareUrl = "https://twitter.com/intent/tweet?url=https://www.themoviedb.org/%s/%s?language=en-US";
        String url = String.format(twitterShareUrl, mediaType, id);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static int dp2Pixel(Context context, int dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public static void addToWatchList(Context context, WatchlistData data) throws JSONException {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        Set<String> visited = getWatchlistVisited(pref);
        List<String> visitedSeq = getWatchlistVisitedSeq(pref);

        SharedPreferences.Editor editor = pref.edit();

        String id = getWatchlistDataId(data);

        if (visited.contains(id)) {
            return;
        }
        visited.add(id);
        visitedSeq.add(id);
        System.out.println(visitedSeq);

        editor.putString(id, watchlistData2String(data));

        editor.putStringSet("visited", visited);
        editor.putString("seq", String.join(" ", visitedSeq));

        editor.apply();
        Log.d("watchlist data", pref.getString("seq", null));
    }


    public static void removeFromWatchList(Context context, WatchlistData data) throws JSONException {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        Set<String> visited = getWatchlistVisited(pref);
        List<String> visitedSeq = getWatchlistVisitedSeq(pref);

        SharedPreferences.Editor editor = pref.edit();

        String id = getWatchlistDataId(data);
        if (!visited.contains(id)) {
            return;
        }
        visited.remove(id);
        visitedSeq.remove(id);
        System.out.println(visitedSeq);

        editor.remove(id);
        editor.putStringSet("visited", visited);
        editor.putString("seq", String.join(" ", visitedSeq));

        editor.apply();
        Log.d("watchlist data", pref.getString("seq", null));
    }

    public static ArrayList<WatchlistData> getWatchList(Context context) throws JSONException {
        ArrayList<WatchlistData> watchlist = new ArrayList<>();

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        List<String> visitedSeq = getWatchlistVisitedSeq(pref);

        for (String id: visitedSeq) {
            if (id.equals("")) continue;
            WatchlistData data = string2watchlistData(pref.getString(id, null));
            watchlist.add(data);
        }

        return watchlist;
    }
    

    public static String getWatchlistDataId(WatchlistData data) {
        return data.getMediaType() + "," + data.getMediaId();
    }

    public static String watchlistData2String(WatchlistData data) throws JSONException {
        JSONObject watchListJS = new JSONObject();
        watchListJS.put("mediaId", data.getMediaId());
        watchListJS.put("mediaType", data.getMediaType());
        watchListJS.put("mediaName", data.getMediaName());
        watchListJS.put("backdropPath", data.getBackdropPath());
        watchListJS.put("posterPath", data.getPosterPath());
        return watchListJS.toString();
    }

    public static WatchlistData string2watchlistData(String s) throws JSONException {
        JSONObject data = new JSONObject(s);
        String id = data.getString("mediaId");
        String mediaType = data.getString("mediaType");
        String mediaName = data.getString("mediaName");
        String backdropPath = data.getString("backdropPath");
        String posterPath = data.getString("posterPath");
        return new WatchlistData(id, mediaType, mediaName, backdropPath, posterPath);
    }

    public static void UpdateWatchlistOrder(Context context, ArrayList<WatchlistData> watchlist) {
        List<String> visitedSeq = new ArrayList<>();
        for (WatchlistData data : watchlist) {
            String id = getWatchlistDataId(data);
            visitedSeq.add(id);
        }

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("seq", String.join(" ", visitedSeq));
        editor.apply();
    }

    public static Set<String> getWatchlistVisited(SharedPreferences pref) {
        Set<String> visited = new HashSet<>();

        if (pref.contains("visited")) {
            visited = pref.getStringSet("visited", null);
        }

        return visited;
    }

    public static ArrayList<String> getWatchlistVisitedSeq(SharedPreferences pref) {
        ArrayList<String> visitedSeq = new ArrayList<>();

        if (pref.contains("seq")) {
            visitedSeq = new ArrayList<String>(Arrays.asList(pref.getString("seq", null).split(" ")));
        }

        return visitedSeq;
    }

    public static String getWatchlistHint(Context context, WatchlistData data) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        Set<String> visited = getWatchlistVisited(pref);
        String id = getWatchlistDataId(data);
        if (visited.contains(id)) {
            return "Remove from Watchlist";
        } else {
            return "Add to Watchlist";
        }
    }

    public static void setWatchlistButton(Context context, WatchlistData data, ImageView button) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        Set<String> visited = getWatchlistVisited(pref);
        String id = getWatchlistDataId(data);
        if (visited.contains(id)) {
            button.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
        } else {
            button.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
        }


    }

    public static void clickWatchlist(Context context, WatchlistData data) throws JSONException {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        Set<String> visited = getWatchlistVisited(pref);
        String id = getWatchlistDataId(data);
        if (!visited.contains(id)) {
            addToWatchList(context, data);
            Toast.makeText(context, data.getMediaName() + " was added to Watchlist", Toast.LENGTH_SHORT).show();
        } else {
            removeFromWatchList(context, data);
            Toast.makeText(context, data.getMediaName() + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
        }
    }

}
