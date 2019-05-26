package com.rapido.youtube_rapido.app.VideoList;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.rapido.youtube_rapido.app.YoutubeApplication;
import com.rapido.youtube_rapido.model.response.ThumbnailData;

public class PicassoImageBindingAdapter {

    @BindingAdapter("app:imageUrl")
    public static void setImageResource(ImageView view, ThumbnailData thumbnailData){
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        if(thumbnailData!=null)
        {
            YoutubeApplication.getInstance().getApplicationComponent().picasso()
                    .load(thumbnailData.getUrl())
                    .into(view);
        }


    }
}
