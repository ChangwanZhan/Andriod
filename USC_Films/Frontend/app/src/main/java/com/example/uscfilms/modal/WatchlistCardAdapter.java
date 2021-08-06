package com.example.uscfilms.modal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.MyLibs;
import com.example.uscfilms.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WatchlistCardAdapter extends RecyclerView.Adapter<WatchlistCardAdapter.WatchlistCardViewHolder> {

    public Context context;
    public ArrayList<WatchlistData> watchlistArray;
    public WatchlistCardAdapter adapter;

    public WatchlistCardAdapter(Context ct, ArrayList<WatchlistData> watchlistArray) {
        this.context = ct;
        this.watchlistArray = watchlistArray;
        adapter = this;
    }

    @NonNull
    @Override
    public WatchlistCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.watchlist_card_layout, parent, false);
        return new WatchlistCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistCardViewHolder holder, int position) {
        String cat = watchlistArray.get(position).getMediaType();
        String mediaType = "";
        if (cat.equals("movie")) {
            mediaType = "Movie";
        } else {
            mediaType = "TV";
        }
        holder.mediaCat.setText(mediaType);
        Picasso.get().load(watchlistArray.get(position).getPosterPath()).fit().centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return watchlistArray.size();
    }

    public class WatchlistCardViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView mediaCat;
        public ImageView mediaButton;

        public WatchlistCardViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.listImage);
            mediaCat = itemView.findViewById(R.id.listCat);
            mediaButton = itemView.findViewById(R.id.listButton);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("click card", "onClick: ");
                    int position = getAdapterPosition();
                    String mediaId = watchlistArray.get(position).getMediaId();
                    String mediaType = watchlistArray.get(position).getMediaType();
                    String mediaName = watchlistArray.get(position).getMediaName();
                    String backdropPath = watchlistArray.get(position).getBackdropPath();
                    String posterPath = watchlistArray.get(position).getPosterPath();
                    MyLibs.openMediaDetailActivity(context, mediaId, mediaType, mediaName, backdropPath, posterPath);
                }
            });

            mediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int position = getAdapterPosition();
                        MyLibs.clickWatchlist(context.getApplicationContext(), watchlistArray.get(position));
                        watchlistArray.remove(position);
                        notifyItemRemoved(position);
                        TextView watchListEmpty = (TextView) ((Activity) context).findViewById(R.id.watchlistEmpty);
                        if (watchlistArray.size() == 0) {
                            watchListEmpty.setVisibility(View.VISIBLE);
                        } else {
                            watchListEmpty.setVisibility(View.INVISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
