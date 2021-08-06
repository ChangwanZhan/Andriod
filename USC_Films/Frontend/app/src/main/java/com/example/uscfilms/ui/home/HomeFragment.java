package com.example.uscfilms.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.uscfilms.R;
import com.example.uscfilms.modal.HomeScrollAdapter;
import com.example.uscfilms.modal.SliderAdapter;
import com.example.uscfilms.modal.SliderData;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private View view;

    private ConstraintLayout homeBar;
    private ScrollView homeContent;

    private ProgressBar loading;
    private TextView loadingText;

    private SliderView sliderView;
    private RecyclerView topRatedRecyclerView;
    private RecyclerView popularRecyclerView;

    public String TMDBUrl = "https://www.themoviedb.org/";

    private Boolean tvSlide = false;
    private Boolean movieSlide = false;
    private Boolean tvTopRated = false;
    private Boolean movieTopRated = false;
    private Boolean tvPopular = false;
    private Boolean moviePopular = false;

    private ArrayList<SliderData> tvSliderDataArrayList = new ArrayList<>();
    private ArrayList<SliderData> movieSliderDataArrayList = new ArrayList<>();

    private JSONArray tvTopRatedData;
    private JSONArray movieTopRatedData;
    private JSONArray tvPopularData;
    private JSONArray moviePopularData;

//    private JSONObject tvObj = new JSONObject();
//    private String[] localHomeKeys = {"trending", "top-rated", "popular"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        view = inflater.inflate(R.layout.fragment_home, container, false);

        homeBar = view.findViewById(R.id.homeBar);
        homeContent = view.findViewById(R.id.homeContentScroll);

        loading = view.findViewById(R.id.homeLoading);
        loadingText = view.findViewById(R.id.homeLoadingText);

        homeBar.setVisibility(View.INVISIBLE);
        homeContent.setVisibility(View.INVISIBLE);

        sliderView = view.findViewById(R.id.slider);
        topRatedRecyclerView = view.findViewById(R.id.topRatedRecyclerView);
        popularRecyclerView = view.findViewById(R.id.popularRecyclerView);
        Button movieButton = (Button) view.findViewById(R.id.movie);
        movieButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    showMovieData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button tvButton = (Button) view.findViewById(R.id.tv);
        tvButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    showTVData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView TMDB = (TextView) view.findViewById(R.id.TMDBLink);
        TMDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TMDBUrl));
                startActivity(browserIntent);
            }
        });

        fetchData("movie");
        fetchData("tv");
        return view;
    }


    private void fetchData(String cat) {
        fetchSlideData(getString(R.string.base_url_media) + "currently-playing/" + cat, cat);
        fetchScrollData(getString(R.string.base_url_media) + "top-rated/" + cat, topRatedRecyclerView, cat, "topRated");
        fetchScrollData(getString(R.string.base_url_media) + "popular/" + cat, popularRecyclerView, cat, "popular");
    }


    private void fetchSlideData(String requestUrl, String cat) {
        sliderView.setVisibility(View.INVISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (cat.equals("movie")) {
                            movieSlide = true;
                        } else {
                            tvSlide = true;
                        }
                        // pass the data by a mutable string builder object
                        StringBuilder sb=new StringBuilder();
                        sb.append(response);
                        try {
                            getSlideDetails(sb, cat);
                            fetchFinished();
//                            showSlideDetails(sb, cat);
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

    private void getSlideDetails (StringBuilder sb, String cat) throws JSONException {
        // we are creating array list for storing our image urls.
        JSONArray data = new JSONArray(sb.toString());
        for (int i = 0 ; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            if (cat.equals("movie")) {
                movieSliderDataArrayList.add(new SliderData(obj.getString("poster_path"), obj.getString("id"), cat, obj.getString("name"), obj.getString("backdrop_path")));
            } else {
                tvSliderDataArrayList.add(new SliderData(obj.getString("poster_path"), obj.getString("id"), cat, obj.getString("name"), obj.getString("backdrop_path")));
            }
        }
    }

    private void showSlideDetails (String cat) throws JSONException {
        SliderAdapter adapter = new SliderAdapter(this.getActivity(), movieSliderDataArrayList);

        // passing this array list inside our adapter class.
        if (cat.equals("tv")) {
            adapter = new SliderAdapter(this.getActivity(), tvSliderDataArrayList);
        }
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);

        sliderView.setAutoCycle(true);

        sliderView.startAutoCycle();
        sliderView.setVisibility(View.VISIBLE);

    }

    private void fetchScrollData(String requestUrl, RecyclerView recyclerView, String cat, String type) {
        recyclerView.setVisibility(View.INVISIBLE);
//        recyclerView.removeAllViews();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringBuilder sb=new StringBuilder();
                        sb.append(response);
                        try {
                            if (cat.equals("movie")) {
                                if (type.equals("topRated")) {
                                    movieTopRated = true;
                                    movieTopRatedData = getScrollDetail(sb);
                                } else {
                                    moviePopular = true;
                                    moviePopularData = getScrollDetail(sb);
                                }
                            } else {
                                if (type.equals("topRated")) {
                                    tvTopRated = true;
                                    tvTopRatedData = getScrollDetail(sb);
                                } else {
                                    tvPopular = true;
                                    tvPopularData = getScrollDetail(sb);
                                }
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

    private JSONArray getScrollDetail(StringBuilder sb) throws JSONException {
        JSONArray data = new JSONArray(sb.toString());
        return data;
    }


    private void showScrollDetails (RecyclerView recyclerView, String cat, JSONArray data) throws JSONException {
        HomeScrollAdapter adapter = new HomeScrollAdapter(this.getActivity(), data, cat, "home");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void fetchFinished() throws JSONException {
        if (tvSlide && movieSlide && tvTopRated && movieTopRated && tvPopular && moviePopular) {
            showMovieData();
            loading.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);
            homeBar.setVisibility(View.VISIBLE);
            homeContent.setVisibility(View.VISIBLE);
        }
    }

    private void showMovieData() throws JSONException {
        showSlideDetails("movie");
        showScrollDetails(topRatedRecyclerView, "movie", movieTopRatedData);
        showScrollDetails(popularRecyclerView, "movie", moviePopularData);
    }

    private void showTVData() throws JSONException {
        showSlideDetails("tv");
        showScrollDetails(topRatedRecyclerView, "tv", tvTopRatedData);
        showScrollDetails(popularRecyclerView, "tv", tvPopularData);
    }

}