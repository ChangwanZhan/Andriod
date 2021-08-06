package com.example.uscfilms.modal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.MyLibs;
import com.example.uscfilms.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewScrollAdapter extends RecyclerView.Adapter<ReviewScrollAdapter.ReviewScrollViewHolder> {

    public Context context;
    public JSONArray mediaInfo;
    public String mediaType;

    public ReviewScrollAdapter(Context ct, JSONArray mediaInfo, String cat) {
        this.context = ct;
        this.mediaInfo = mediaInfo;
        this.mediaType = cat;
    }

    @NonNull
    @Override
    public ReviewScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.detail_card_layout, parent, false);
        return new ReviewScrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewScrollViewHolder holder, int position) {
        try {
            JSONObject obj = mediaInfo.getJSONObject(position);
            Picasso.get().load(obj.getString("poster_path")).into(holder.image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(10, mediaInfo.length());
    }

    public class ReviewScrollViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public ReviewScrollViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.detailCardImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
//                    Intent intent = new Intent(context, MediaDetailActivity.class);

                    String id = "";
                    String mediaName = "";
                    String backdropPath = "";
                    String posterPath = "";

                    try {
                        id = getMediaId(position);
                        mediaName = getMediaName(position);
                        backdropPath = getBackdropPath(position);
                        posterPath = getPosterPath(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MyLibs.openMediaDetailActivity(context, id, mediaType, mediaName, backdropPath, posterPath);
                }
            });

        }
    }

    private String getMediaId(int position) throws JSONException {
        return mediaInfo.getJSONObject(position).getString("id");
    }

    private String getMediaName(int position) throws JSONException {
        return mediaInfo.getJSONObject(position).getString("name");
    }

    private String getBackdropPath(int position) throws JSONException {
        return mediaInfo.getJSONObject(position).getString("backdrop_path");
    }

    private String getPosterPath(int position) throws JSONException {
        return mediaInfo.getJSONObject(position).getString("poster_path");
    }

}
