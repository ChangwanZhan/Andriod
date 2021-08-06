package com.example.uscfilms.modal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.MainActivity;
import com.example.uscfilms.MediaDetailActivity;
import com.example.uscfilms.MyLibs;
import com.example.uscfilms.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeScrollAdapter extends RecyclerView.Adapter<HomeScrollAdapter.HomeScrollViewHolder> {

    public Context context;
    public Context appContext;
    public JSONArray mediaInfo;
    public String mediaType;
    public String activity;

    private final String TMDBUrl = "https://www.themoviedb.org/%s/%s";

    public HomeScrollAdapter(Context ct, JSONArray mediaInfo, String cat, String activity) {
        this.context = ct;
        this.mediaInfo = mediaInfo;
        this.mediaType = cat;
        this.appContext = this.context.getApplicationContext();
        this.activity = activity;
    }

    @NonNull
    @Override
    public HomeScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_card_layout, parent, false);
        return new HomeScrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeScrollViewHolder holder, int position) {
        try {
            JSONObject obj = mediaInfo.getJSONObject(position);
            Picasso.get().load(obj.getString("poster_path")).into(holder.image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(6, mediaInfo.length());
    }

    public class HomeScrollViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView menuButton;

        public HomeScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.homeCardImg);
            menuButton = itemView.findViewById(R.id.menuButton);

            if (activity.equals("mediaDetail")) {
                image.setForeground(null);
                menuButton.setVisibility(View.GONE);
            }

            image.setOnClickListener(new View.OnClickListener() {
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

            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    WatchlistData data = null;
                    try {
                        data = new WatchlistData(getMediaId(getAdapterPosition()), mediaType, getMediaName(position), getBackdropPath(position), getPosterPath(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String watchlistHint = MyLibs.getWatchlistHint(appContext, data);
                    PopupMenu popupMenu = new PopupMenu(context, menuButton);

                    // Inflating popup menu from popup_menu.xml file
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.getMenu().getItem(3).setTitle(watchlistHint);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            String id = "";
                            String mediaName = "";

                            try {
                               id = getMediaId(position);
                                mediaName = getMediaName(position);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (item.getItemId() == R.id.menuTMDB) {
                                String url = String.format(TMDBUrl, mediaType, id);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                context.startActivity(browserIntent);
                            } else if (item.getItemId() == R.id.menuFacebook) {
                                MyLibs.shareFacebook(context, mediaType, id);
                            } else if (item.getItemId() == R.id.menuTwitter) {
                                MyLibs.shareTwitter(context, mediaType, id);
                            } else if (item.getItemId() == R.id.menuWatchlist) {
//                                Toast.makeText(context, "You Clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                try {
                                    WatchlistData data = new WatchlistData(getMediaId(getAdapterPosition()), mediaType, getMediaName(position), getBackdropPath(position), getPosterPath(position));
                                    MyLibs.clickWatchlist(appContext, data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            return true;
                        }
                    });
                    // Showing the popup menu
                    popupMenu.show();

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
