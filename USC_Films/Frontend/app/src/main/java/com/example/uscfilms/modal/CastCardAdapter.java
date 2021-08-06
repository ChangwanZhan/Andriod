package com.example.uscfilms.modal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastCardAdapter extends RecyclerView.Adapter<CastCardAdapter.CastCardViewHolder> {

    public Context context;
    public JSONArray castInfo;

    public CastCardAdapter(Context ct, JSONArray castInfo) {
        this.context = ct;
        this.castInfo = castInfo;
    }

    @NonNull
    @Override
    public CastCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cast_card_layout, parent, false);
        return new CastCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastCardViewHolder holder, int position) {
        try {
            JSONObject obj = castInfo.getJSONObject(position);
            Picasso.get().load(obj.getString("profile_path")).fit().centerCrop().into(holder.castImg);
            holder.nameText.setText(obj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(6, this.castInfo.length());
    }


    public class CastCardViewHolder extends RecyclerView.ViewHolder {
        CircleImageView castImg;
        TextView nameText;

        public CastCardViewHolder(@NonNull View itemView) {
            super(itemView);
            castImg = itemView.findViewById(R.id.castImg);
            nameText = itemView.findViewById(R.id.castName);
        }
    }
}
