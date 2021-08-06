package com.example.uscfilms.modal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.MediaDetailActivity;
import com.example.uscfilms.MyLibs;
import com.example.uscfilms.R;
import com.example.uscfilms.ReviewActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public Context context;
    public JSONArray reviewInfo;


    public ReviewAdapter(Context ct, JSONArray reviewInfo) {
        this.context = ct;
        this.reviewInfo = reviewInfo;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        try {
            String info = getReviewInfo(position);
            String rate = getRate(position);
            String content = getReviewContent(position);

            holder.reviewInfoText.setText(info);
            holder.rate.setText(rate);
            holder.reviewContentText.setText(content);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(3, this.reviewInfo.length());
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView reviewInfoText;
        TextView rate;
        TextView reviewContentText;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewInfoText = itemView.findViewById(R.id.reviewInfo);
            rate = itemView.findViewById(R.id.reviewScore);
            reviewContentText = itemView.findViewById(R.id.reviewContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int position = getAdapterPosition();
                        String info = getReviewInfo(position);
                        String rate = getRate(position);
                        String content = getReviewContent(position);

                        openReviewActivity(context, info, rate, content);

                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }

                }
            });


        }

    }

    private String getReviewInfo(int position) throws JSONException, ParseException {
        JSONObject obj = reviewInfo.getJSONObject(position);
        String author = obj.getString("author");
        if (author.equals("")) {
            author = "anynomous user";
        }
        String dateStr = obj.getString("created_at").split("T")[0];

        Date date =new SimpleDateFormat("yyyy-mm-dd").parse(dateStr);
        DateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
        dateStr = dateFormat.format(date);

        return String.format("by %s on %s", author, dateStr);
    }

    private String getRate(int position) throws JSONException {
        JSONObject obj = reviewInfo.getJSONObject(position);
        Integer rate = obj.getInt("rating");
        return  String.format("%d/5", rate/2);
    }

    private String getReviewContent(int position)  throws JSONException {
        JSONObject obj = reviewInfo.getJSONObject(position);
        return obj.getString("content");
    }

    private void openReviewActivity(Context context, String info, String rate, String content) {
        Intent intent = new Intent(context, ReviewActivity.class);
        intent.putExtra("REVIEW_INFO", info);
        intent.putExtra("REVIEW_RATE", rate);
        intent.putExtra("REVIEW_CONTENT", content);
        context.startActivity(intent);
    }

}
