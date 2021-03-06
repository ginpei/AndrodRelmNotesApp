package info.ginpei.notes.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class BaseLocationActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    public static final String TAG = "G#BaseLocationActivity";
    protected LocationManager locationManager;
    private LocationListener listener;
    private boolean locationCheckEnabled = true;

    // vvvvvvvv


    public boolean isLocationCheckEnabled() {
        return locationCheckEnabled;
    }

    public void setLocationCheckEnabled(boolean locationCheckEnabled) {
        this.locationCheckEnabled = locationCheckEnabled;

        if (locationCheckEnabled) {
            startListeningLocation();
        } else {
            stopListeningLocation();
        }
    }

    // ^^^^^^^^

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startListeningLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopListeningLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // call again so that getting location should success now
                    startListeningLocation();
                } else {
                    onDeniedLocationPermission();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startListeningLocation() {
        if (!isLocationCheckEnabled()) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        stopListeningLocation();

        long minTime = 0;
        float minDistance = 1;
        listener = getLocationListener();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                listener
        );

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                listener
        );
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Log.d(TAG, "requestLocationPermission: Shouldn't request permission.");
        }
    }

    @NonNull
    private LocationListener getLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                BaseLocationActivity.this.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
    }

    private void stopListeningLocation() {
        if (locationManager != null && listener != null) {
            locationManager.removeUpdates(listener);
            listener = null;
        }
    }

    protected boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void onDeniedLocationPermission() {
        Toast.makeText(this, "Please allow location access.", Toast.LENGTH_LONG).show();
    }

    protected void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location);
    }
}
