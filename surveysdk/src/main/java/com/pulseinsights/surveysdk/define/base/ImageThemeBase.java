package com.pulseinsights.surveysdk.define.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pulseinsights.surveysdk.util.MeasureTools;

public abstract class ImageThemeBase extends ViewThemeBase {
    public String horizonAlign = "center";

    public void configImageView(Context context, final ImageView imageView, String imageUrl) {
        if (!imageUrl.isEmpty()) {
            boolean doResize = width > 0 && height > 0;
            int widthPx = MeasureTools.dip2px(context, width);
            int heightPx = MeasureTools.dip2px(context, height);
            RequestOptions requestOptions = doResize
                    ? new RequestOptions().override(widthPx, heightPx) : new RequestOptions();
            Glide.with(context).asBitmap().load(imageUrl).apply(requestOptions).into(
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap,
                                                    Transition<? super Bitmap> transition) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
        applyMargin(context, imageView);
    }

    public void configImageContainer(RelativeLayout container) {
        container.setGravity(getHorizonGravity(horizonAlign));
    }

    public void applyNewStyle(ImageThemeBase newStyle) {
        this.width = newStyle.width;
        this.height = newStyle.height;
        this.margin = newStyle.margin;
        this.horizonAlign = newStyle.horizonAlign;
    }

}