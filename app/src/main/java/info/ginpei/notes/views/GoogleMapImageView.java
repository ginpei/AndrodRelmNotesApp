package info.ginpei.notes.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GoogleMapImageView extends android.support.v7.widget.AppCompatImageView {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 300;
    public static final int THRESHOLD = 10;
    public static final String TAG = "G#GoogleMapImageView";
    private double latitude = -1;
    private double longitude = -1;

    public GoogleMapImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLocation(Activity activity, Location location) {
        double newLatitude = location.getLatitude();
        double newLongitude = location.getLongitude();

        double diff = Math.max(Math.abs(newLatitude - latitude), Math.abs(newLongitude - longitude));
        boolean isNew = latitude < 0;
        if (!isNew && diff < THRESHOLD) {
            Log.d(TAG, "setLocation: Diff is too small to update: " + diff);
            return;
        }

        latitude = newLatitude;
        longitude = newLongitude;

        new Thread(() -> {
            String url = buildImageUrl(latitude, longitude);
            Bitmap bitmap = loadBitmap(url);

            activity.runOnUiThread(() -> {
                setImageBitmap(bitmap);
            });
        }).start();
    }

    @SuppressLint("DefaultLocale")
    private String buildImageUrl(double latitude, double longitude) {
        return String.format(
                "https://maps.googleapis.com/maps/api/staticmap?size=%dx%d&scale=2&markers=%s,%s",
                WIDTH,
                HEIGHT,
                latitude,
                longitude
        );
    }

    @WorkerThread
    @Nullable
    private Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
