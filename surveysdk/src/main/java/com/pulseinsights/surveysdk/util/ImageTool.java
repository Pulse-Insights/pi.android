package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LeoChao on 16/6/3.
 */
public class ImageTool {

    public static Bitmap readBitMap(Context context, int resId) {
        return readBitMap(context, resId, -1, -1);
    }


    public static Bitmap readBitMap(Context context, int resId,
                                    int containerWidthInDip, int containerHeightInDip) {


        Bitmap rtBitmap = null;

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inSampleSize = 1;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        if (containerWidthInDip > 0 && containerHeightInDip > 0) {

            int resWidth = MeasureTools.dip2px(context, containerWidthInDip);
            int resHeight = MeasureTools.dip2px(context, containerHeightInDip);


            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = true;
            Bitmap bitmap =
                    BitmapFactory.decodeResource(context.getResources(), resId, dimensions);
            int height = dimensions.outHeight;
            int width = dimensions.outWidth;

            if ((height >= resHeight) && (width >= resWidth)) {
                int tmpVector = Math.max(height / resHeight, width / resWidth);
                if (tmpVector > 1) {
                    opt.inSampleSize = tmpVector;
                } else {
                    opt.inSampleSize = 1;
                }

            }
        }


        try {
            InputStream is = context.getResources().openRawResource(resId);
            rtBitmap = BitmapFactory.decodeStream(is, null, opt);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rtBitmap;
    }

    public static Bitmap decodeResource(Resources res, int id) {
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, dimensions);
        int height = dimensions.outHeight;
        int width = dimensions.outWidth;


        Bitmap bitmapOut = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmapOut = BitmapFactory.decodeResource(res, id, options);
                //Log.d(TAG_LOG, "Decoded successfully for sampleSize " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
                // If an OutOfMemoryError occurred,
                // we continue with for loop and next inSampleSize value
                //Log.e(TAG_LOG, "outOfMemoryError while reading file for sampleSize "
                // + options.inSampleSize+ " retrying with higher value");
            }
        }
        return bitmapOut;
    }

}
