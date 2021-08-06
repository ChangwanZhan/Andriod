package com.example.uscfilms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.uscfilms.modal.CastCardAdapter;
import com.example.uscfilms.modal.HomeScrollAdapter;
import com.example.uscfilms.modal.ReviewAdapter;
import com.example.uscfilms.modal.ReviewScrollAdapter;
import com.example.uscfilms.modal.WatchlistData;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaDetailActivity extends AppCompatActivity {

    ProgressBar loading;
    TextView loadingText;
    ScrollView mediaDetail;

    YouTubePlayerView youTubePlayerView;
    ImageView videoPlaceHolder;
    ImageView watchlistButton;
    TextView title;

    WatchlistData watchlistData;

    LinearLayout overview;
    ReadMoreTextView overviewText;

    LinearLayout genres;
    TextView genresText;

    LinearLayout year;
    TextView yearText;

    LinearLayout cast;
    RecyclerView castRecyclerView;

    LinearLayout review;
    RecyclerView reviewRecyclerView;

    LinearLayout recommend;
    RecyclerView recommendRecyclerView;

    String mediaId;
    String mediaType;
    String mediaName;
    String backdropPath;
    String posterPath;

    Boolean videoFetched = false;
    Boolean detailFetched = false;
    Boolean castFetched = false;
    Boolean reviewFetched = false;
    Boolean recommendedFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_USCFilms);
        super.onCreate(savedInstanceState);
        getInputParams();
        setContentView(R.layout.activity_media_detail);

        getUI();
        watchlistData = new WatchlistData(mediaId, mediaType, mediaName, backdropPath, posterPath);
        MyLibs.setWatchlistButton(getApplicationContext(), watchlistData, this.watchlistButton);
        hideUnfetchedUI();
        fetchData();
        Picasso.get().load(backdropPath).fit().centerCrop().into(videoPlaceHolder);
        title.setText(mediaName);


    }

    private void getInputParams() {
        this.mediaId = getIntent().getStringExtra("ID");
        this.mediaType = getIntent().getStringExtra("MEDIA_TYPE");
        this.mediaName = getIntent().getStringExtra("MEDIA_NAME");
        this.backdropPath = getIntent().getStringExtra("BACKDROP_PATH");
        this.posterPath = getIntent().getStringExtra("POSTER_PATH");
    }

    private void getUI() {
        loading = findViewById(R.id.mediaDetailLoading);
        loadingText = findViewById(R.id.mediaDetailLoadingText);

        mediaDetail = findViewById(R.id.mediaDetailView);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        videoPlaceHolder = findViewById(R.id.videoPlaceholder);
        watchlistButton = findViewById(R.id.addWatchList);
        title = findViewById(R.id.mediaName);

        overview = findViewById(R.id.detailOverview);
        overviewText = findViewById(R.id.detailOverviewText);

        genres = findViewById(R.id.detailGenres);
        genresText = findViewById(R.id.detailGenresText);

        year = findViewById(R.id.detailYear);
        yearText = findViewById(R.id.detailYearText);

        cast = findViewById(R.id.detailCast);
        castRecyclerView = findViewById(R.id.detailCastRecyclerView);

        review = findViewById(R.id.detailReview);
        reviewRecyclerView = findViewById(R.id.detailReviewRecycler);

        recommend = findViewById(R.id.detailRecommended);
        recommendRecyclerView = findViewById(R.id.detailRecommendedRecyclerView);
    }

    private void hideUnfetchedUI() {
        mediaDetail.setVisibility(View.INVISIBLE);
        youTubePlayerView.setVisibility(View.INVISIBLE);
        videoPlaceHolder.setVisibility(View.INVISIBLE);

        overview.setVisibility(View.GONE);
        genres.setVisibility(View.GONE);
        year.setVisibility(View.GONE);
        cast.setVisibility(View.GONE);
        review.setVisibility(View.GONE);
        recommend.setVisibility(View.GONE);
    }

    private void fetchData() {
        fetchYoutubeViedo(getString(R.string.base_url_media) + String.format("/video/%s/%s", mediaType, mediaId));
        fetchDetailData(getString(R.string.base_url_media) + String.format("/detail/%s/%s", mediaType, mediaId));
        Log.d("media activity", getString(R.string.base_url_media) + String.format("/detail/%s/%s", mediaType, mediaId));
        fetchCastData(getString(R.string.base_url_cast) + String.format("/media/%s/%s", mediaType, mediaId));
        fetchReviewData(getString(R.string.base_url_media) + String.format("/review/%s/%s", mediaType, mediaId));
        fetchRecommendedData(getString(R.string.base_url_media) + String.format("/recommended/%s/%s", mediaType, mediaId));
        Log.d("Recommended Url", getString(R.string.base_url_media) + String.format("/recommended/%s/%s", mediaType, mediaId));
    }

    private void fetchYoutubeViedo(String requestUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pass the data by a mutable string builder object
                        StringBuilder sb=new StringBuilder();
                        sb.append(response);
                        try {
                            videoFetched = true;
                            JSONObject data = new JSONObject(sb.toString());
                            String videoId = data.getString("key");
                            Log.d("key", videoId);
                            if (!videoId.equals("")) {
                                setUpYoutubePlayer(videoId);
                            } else {
                                videoPlaceHolder.setVisibility(View.VISIBLE);
                            }
                            fetchFinished();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorResponse",error.toString());
            }
        });

        queue.add(stringRequest);
    }


    private void fetchDetailData(String requestUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pass the data by a mutable string builder object
                        StringBuilder sb=new StringBuilder();
                        sb.append(response);
                        try {
                            detailFetched = true;
                            showDetailData(sb);
                            fetchFinished();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorResponse",error.toString());
            }
        });

        queue.add(stringRequest);
    }

    private void fetchCastData(String requestUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pass the data by a mutable string builder object
                        StringBuilder sb=new StringBuilder();
                        sb.append(response);
                        try {
                            castFetched = true;
                            showCastInfo(sb);
                            fetchFinished();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorResponse",error.toString());
            }
        });

        queue.add(stringRequest);
    }

    private void fetchReviewData(String requestUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pass the data by a mutable string builder object
                        StringBuilder sb=new StringBuilder();
                        sb.append(response);
                        try {
                            reviewFetched = true;
                            showReviewInfo(sb);
                            fetchFinished();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorResponse",error.toString());
            }
        });

        queue.add(stringRequest);
    }


    private void fetchRecommendedData(String requestUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pass the data by a mutable string builder object
                        StringBuilder sb=new StringBuilder();
                        sb.append(response);
                        try {
                            recommendedFetched = true;
                            showRecommendedInfo(sb);
                            fetchFinished();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorResponse",error.toString());
            }
        });

        queue.add(stringRequest);
    }



    private void setUpYoutubePlayer(String videoId) {
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                Log.d("youtube set up", videoId);
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
        youTubePlayerView.setVisibility(View.VISIBLE);
    }

    private void showDetailData(StringBuilder sb) throws JSONException{
        JSONObject data = new JSONObject(sb.toString());

        String overview = data.getString("overview");
        String genres = data.getString("genres");
        String date = data.getString("date");

        if (!overview.equals("")) {
            overviewText.setText(overview);
            this.overview.setVisibility(View.VISIBLE);
        }

        if (!genres.equals("")) {
            genresText.setText(genres);
            this.genres.setVisibility(View.VISIBLE);
        }

        if (!date.equals("")) {
            yearText.setText(date.split("-")[0]);
            this.year.setVisibility(View.VISIBLE);
        }
    }

    private void showCastInfo (StringBuilder sb) throws JSONException {
        JSONArray data = new JSONArray(sb.toString());
        if (data.length() == 0) return;
        CastCardAdapter adapter = new CastCardAdapter(this, data);
        castRecyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
        castRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        ViewGroup.LayoutParams params = cast.getLayoutParams();

        if (data.length() <= 3) {
            params.height = MyLibs.dp2Pixel(this, 210);
        } else {
            params.height = MyLibs.dp2Pixel(this, 360);
        }
        cast.setLayoutParams(params);
        cast.setVisibility(View.VISIBLE);
    }

    private void showReviewInfo (StringBuilder sb) throws JSONException {
        JSONArray data = new JSONArray(sb.toString());
        if (data.length() == 0) return;
        ReviewAdapter adapter = new ReviewAdapter(this, data);
        reviewRecyclerView.setAdapter(adapter);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        review.setVisibility(View.VISIBLE);
    }

    private void showRecommendedInfo (StringBuilder sb) throws JSONException {
        JSONArray data = new JSONArray(sb.toString());
        if (data.length() == 0) return;
        HomeScrollAdapter adapter = new HomeScrollAdapter(this, data, mediaType, "mediaDetail");
        recommendRecyclerView.setAdapter(adapter);
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendRecyclerView.setVisibility(View.VISIBLE);
        recommend.setVisibility(View.VISIBLE);
    }

    public void shareOnFacebook(View view) {
        MyLibs.shareFacebook(this, mediaType, mediaId);
    }

    public void shareOnTwitter(View view) {
        MyLibs.shareTwitter(this, mediaType, mediaId);
    }

    public void clickMediaDetailWatchlistButton(View view) throws JSONException {
        MyLibs.clickWatchlist(getApplication(), watchlistData);
        MyLibs.setWatchlistButton(getApplication(), watchlistData, watchlistButton);
    }

    public void fetchFinished() {
        if (videoFetched && detailFetched && castFetched && reviewFetched && recommendedFetched) {
            loading.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);
            mediaDetail.setVisibility(View.VISIBLE);

        }
    }

}