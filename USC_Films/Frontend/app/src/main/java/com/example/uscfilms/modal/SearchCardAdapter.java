package com.example.uscfilms.modal;

import android.content.Context;
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

public class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.SearchCardViewHolder> {

    public Context context;
    public JSONArray mediaInfo;

    public SearchCardAdapter(Context ct, JSONArray mediaInfo) {
        this.context = ct;
        this.mediaInfo = mediaInfo;
    }

    @NonNull
    @Override
    public SearchCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_card_layout, parent, false);
        return new SearchCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCardViewHolder holder, int position) {
        try {
            JSONObject obj = mediaInfo.getJSONObject(position);

            String mediaType = obj.getString("media_type");
            String mediaName = obj.getString("name");
            String mediaYear = obj.getString("year");
            Double mediaVote = obj.getDouble("vote_average");
            String backdropPath = obj.getString("backdrop_path");
            String mediaId = obj.getString("id");
            String posterPath = obj.getString("poster_path");

            String mediaInfo = "";
            if (!mediaYear.equals("")) {
                mediaInfo = String.format("%s(%s)", mediaType.toUpperCase(), mediaYear);
            } else {
                mediaInfo = mediaType.toUpperCase();
            }

            Picasso.get().load(backdropPath).fit().centerCrop().into(holder.image);
            holder.mediaScore.setText(String.format("%.1f", mediaVote/2));
//            holder.mediaName.setText(mediaName.toUpperCase());
            holder.mediaName.setText(mediaName);
            holder.mediaInfo.setText(mediaInfo);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLibs.openMediaDetailActivity(context, mediaId, mediaType.toLowerCase(), mediaName, backdropPath, posterPath);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(20, mediaInfo.length());
    }

    public class SearchCardViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView mediaInfo;
        public TextView mediaName;
        public TextView mediaScore;

        public SearchCardViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.searchMediaImage);
            mediaInfo = itemView.findViewById(R.id.searchMediaInfo);
            mediaName = itemView.findViewById(R.id.searchMediaName);
            mediaScore = itemView.findViewById(R.id.searchMediaScore);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });

        }
    }


}
