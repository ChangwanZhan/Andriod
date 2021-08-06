package com.example.uscfilms.ui.watchlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.MyLibs;
import com.example.uscfilms.R;
import com.example.uscfilms.modal.WatchlistCardAdapter;
import com.example.uscfilms.modal.WatchlistData;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class WatchlistFragment extends Fragment {

    private WatchlistViewModel watchlistViewModel;

    ArrayList<WatchlistData> watchlistData;
    TextView watchlistEmpty;
    RecyclerView watchlist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        watchlistViewModel =
                new ViewModelProvider(this).get(WatchlistViewModel.class);
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);


        watchlistEmpty = view.findViewById(R.id.watchlistEmpty);

        WatchlistCardAdapter adapter = null;
        try {
            watchlistData = MyLibs.getWatchList(getActivity().getApplicationContext());
            if (watchlistData.size() == 0) {
                watchlistEmpty.setVisibility(View.VISIBLE);
            } else {
                watchlistEmpty.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new WatchlistCardAdapter(getActivity(), watchlistData);

        watchlist = view.findViewById(R.id.watchlist);

        watchlist.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
        watchlist.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(watchlist);

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(watchlistData, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            MyLibs.UpdateWatchlistOrder(getActivity().getApplicationContext(), watchlistData);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    @Override
    public void onResume() {
        try {
            watchlistData = MyLibs.getWatchList(getActivity().getApplicationContext());
            WatchlistCardAdapter adapter = new WatchlistCardAdapter(getActivity(), watchlistData);
            watchlist.setAdapter(adapter);
            if (watchlistData.size() == 0) {
                watchlistEmpty.setVisibility(View.VISIBLE);
            } else {
                watchlistEmpty.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

}