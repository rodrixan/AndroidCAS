package es.uam.eps.tfg.cas.android.examples.criminalintent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.text.format.DateFormat;

import java.util.Date;


public class Utils {

    public static final String APP_PATH = "es.uam.eps.tfg.cas.android.examples.criminalintent";
    public static final String APP_LOG_TAG = "criminalintent";

    public static String formatDateToString(final Date date) {
        final DateFormat df = new DateFormat();
        return df.format("EEEE, MMM dd, yyyy", date).toString();
    }

    public static Bitmap getScaledBitmap(final String path, final Activity activity) {
        final Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    private static Bitmap getScaledBitmap(final String path, final int destWidth, final int destHeight) {
        final float[] srcDimens = getSrcDimensions(path);

        final int inSampleSize = getScaleDown(srcDimens, destWidth, destHeight);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }


    private static float[] getSrcDimensions(final String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        return new float[]{options.outWidth, options.outHeight};
    }

    private static int getScaleDown(final float[] srcDimens, final int destWidth, final int destHeight) {
        int scale = 1;
        final float srcWidth = srcDimens[0];
        final float srcHeight = srcDimens[1];

        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                scale = Math.round(srcHeight / destHeight);
            } else {
                scale = Math.round(srcWidth / destWidth);
            }
        }

        return scale;
    }
}
