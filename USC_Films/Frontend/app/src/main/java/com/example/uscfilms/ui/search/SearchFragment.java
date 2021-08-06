package com.example.uscfilms.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.uscfilms.R;
import com.example.uscfilms.modal.ReviewAdapter;
import com.example.uscfilms.modal.SearchCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setQueryHint("Search movies and TV");
        searchView.requestFocusFromTouch();

        String requestUrl = getString(R.string.base_url_search);

        RecyclerView searchMedia = view.findViewById(R.id.searchMedia);

        TextView noSearchResult = view.findViewById(R.id.noSearchResult);

        noSearchResult.setVisibility(View.INVISIBLE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl+newText,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // pass the data by a mutable string builder object
                                StringBuilder sb=new StringBuilder();
                                sb.append(response);
                                try {
                                    JSONArray searchRes = new JSONArray(sb.toString());
                                    if (searchRes.length() == 0) {
                                        noSearchResult.setVisibility(View.VISIBLE);
                                        searchMedia.setVisibility(View.INVISIBLE);
                                    } else {
                                        noSearchResult.setVisibility(View.INVISIBLE);
                                        searchMedia.setVisibility(View.VISIBLE);
                                        SearchCardAdapter adapter = new SearchCardAdapter(getActivity(), searchRes);
                                        searchMedia.setAdapter(adapter);
                                        searchMedia.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    }
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

                return false;
            }
        });


        return view;
    }
}