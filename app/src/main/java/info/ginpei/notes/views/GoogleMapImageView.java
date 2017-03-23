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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GoogleMapImageView extends android.support.v7.widget.AppCompatImageView {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 300;
    private Location location;

    public GoogleMapImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLocation(Activity activity, Location location) {
        this.location = location;

        new Thread(() -> {
            String url = buildImageUrl(location);
            Bitmap bitmap = loadBitmap(url);

            activity.runOnUiThread(() -> {
                setImageBitmap(bitmap);
            });
        }).start();
    }

    @SuppressLint("DefaultLocale")
    private String buildImageUrl(Location location) {
        return String.format(
                "https://maps.googleapis.com/maps/api/staticmap?size=%dx%d&scale=2&markers=%s,%s",
                WIDTH,
                HEIGHT,
                location.getLatitude(),
                location.getLongitude()
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
