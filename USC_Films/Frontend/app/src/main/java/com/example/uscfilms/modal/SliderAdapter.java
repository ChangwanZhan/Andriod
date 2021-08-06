package com.example.uscfilms.modal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.uscfilms.MyLibs;
import com.example.uscfilms.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<SliderData> mSliderItems;
    private final Context context;

    // Constructor
    public SliderAdapter(Context context, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getPosterPath())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getPosterPath())
                .transform(new jp.wasabeef.glide.transformations.BlurTransformation(25, 3))
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLibs.openMediaDetailActivity(context, sliderItem.getMediaId(), sliderItem.getMediaType(), sliderItem.getMediaName(),sliderItem.getBackdropPath(), sliderItem.getPosterPath());
            }
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return Math.min(6, mSliderItems.size());
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;
        ImageView image;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            image = itemView.findViewById(R.id.myimage2);
            this.itemView = itemView;

        }
    }
}