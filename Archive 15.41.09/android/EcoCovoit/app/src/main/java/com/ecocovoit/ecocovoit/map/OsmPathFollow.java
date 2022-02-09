package com.ecocovoit.ecocovoit.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**This class role is to notify a listener about device location updates.
 * The updates are looked up in a separated thread and the notification if send to
 * the ThreadUI for potential update of the UI.*/
public class OsmPathFollow {

    public static final String TAG = "Mr Sauvage";

    private List<Location> path;

    /**This field allow us to ask for device location updates.*/
    private FusedLocationProviderClient locationProviderClient;

    /**The current listener of the location.*/
    private LocationFollowerListener locationFollowerListener;

    /**The handler to execute UI code on device location update.*/
    private HandlerPathFunctionToUIThread handler;

    /**If true, we ask device location periodically.
     * If false, we are notified only on device location change*/
    private final boolean periodic;

    /**The period of periodic location requests.
     * Can be changed dynamically during asker thread execution.*/
    private static long INTERVAL_IN_MILLIS = 1000;

    public OsmPathFollow(Context context, List<Location> path, LocationFollowerListener locationFollowerListener, boolean periodic) {
        this.path = path;
        this.periodic = periodic;
        this.locationFollowerListener = locationFollowerListener;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if(periodic) {
            handler = new HandlerPathFunctionToUIThread(locationFollowerListener);
        }
        else {handler = null;}
    }

    /**Start the thread which will ask for device location updates.*/
    public void startFollowing() {
        Log.i(TAG, "startFollowing");
        followThread.start();
    }

    /**Stop the thread asking for device location updates.*/
    public void forceStopFollowing() {
        Log.i(TAG, "forceStopFollowing");
        followThread.interrupt();
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public static void setIntervalInMillis(long intervalInMillis) {
        INTERVAL_IN_MILLIS = intervalInMillis;
    }

    public static long getIntervalInMillis() {
        return INTERVAL_IN_MILLIS;
    }

    /**The class defining the handler for UI code.*/
    static class HandlerPathFunctionToUIThread extends Handler {

        /**Message send by the asker thread on device location update.*/
        public static final int MESSAGE_ON_LOCATION_UPDATE = 1;
        /**Message send by the asker thread when the device location match a location of the path.*/
        public static final int MESSAGE_ON_PATH_LOCATION_REACHED = 2;

        /**The listener containing the ui code to be executed.*/
        private LocationFollowerListener locationFollowerListener;

        /**The asker thread read this variable at each device location update.
         * If it's false, then it will stop requesting device location and return.*/
        private boolean keepListening;

        public HandlerPathFunctionToUIThread(
                LocationFollowerListener locationFollowerListener) {
            this.locationFollowerListener = locationFollowerListener;
            this.keepListening = true;
        }

        public static class LocationReachMessageData {
            public List<Location> path;
            public int currentLocationTarget;
            public LocationReachMessageData(List<Location> path, int currentLocationTarget) {
                this.path = path;
                this.currentLocationTarget = currentLocationTarget;
            }
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_ON_LOCATION_UPDATE:
                    Location location = (Location) msg.obj;
                    keepListening = locationFollowerListener.onLocationUpdate(
                            location.getLongitude(), location.getLatitude());
                    break;
                case MESSAGE_ON_PATH_LOCATION_REACHED:
                    LocationReachMessageData data = (LocationReachMessageData) msg.obj;
                    keepListening = locationFollowerListener.onPathLocationReached(data.path, data.currentLocationTarget);
            }
        }
    }

    /**The famous asker thread. it role is to request device location,
     * Whether periodically or request to be notified of the new location at location chnges.*/
    private Thread followThread = new Thread(){

        private int currentLocationTarget;

        @Override
        public void run() {
            super.run();
            currentLocationTarget = 0;
            request();
        }

        private void request() {
            Log.i(TAG, "request position");
            if(periodic) {
                requestLocationPeriodicaly();
            }
            else {
                requestLocationUpdate();
            }
        }

        /**Request to be notified of device location change.*/
        @SuppressLint("MissingPermission")
        private void requestLocationUpdate() {
            locationProviderClient.requestLocationUpdates(new LocationRequest(), new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    double lon = locationResult.getLastLocation().getLongitude();
                    double lat = locationResult.getLastLocation().getLatitude();

                    boolean keep = locationFollowerListener.onLocationUpdate(
                            lon, lat);

                    if(locationFollowerListener.isLocationReached(lon, lat, path.get(currentLocationTarget))) {
                        keep = locationFollowerListener.onPathLocationReached(path, currentLocationTarget);
                        currentLocationTarget++;
                    }

                    if(!keep) {
                        locationProviderClient.removeLocationUpdates(this);
                    }
                }
            }, Looper.getMainLooper());
        }

        /**Request the location after each {@link OsmPathFollow#INTERVAL_IN_MILLIS} milliseconds.*/
        @SuppressLint("MissingPermission")
        private void requestLocationPeriodicaly() {
            locationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        Log.i(TAG, "request position : Success");
                        double lon = location.getLongitude();
                        double lat = location.getLatitude();

                        Message messageLocationUpdate = handler.obtainMessage(
                                HandlerPathFunctionToUIThread.MESSAGE_ON_LOCATION_UPDATE,
                                new Location(lat, lon, 0));
                        messageLocationUpdate.sendToTarget();

                        if(locationFollowerListener.isLocationReached(lon, lat, path.get(currentLocationTarget))) {
                            Message messageLocationReached = handler.obtainMessage(
                                    HandlerPathFunctionToUIThread.MESSAGE_ON_PATH_LOCATION_REACHED,
                                    new HandlerPathFunctionToUIThread.LocationReachMessageData(path, currentLocationTarget));
                            messageLocationReached.sendToTarget();

                            currentLocationTarget++;
                        }

                        try {
                            sleep(INTERVAL_IN_MILLIS);
                            if(handler.keepListening) {
                                request();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "request position fail : "+e.getMessage());
                    }
                });
        }
    };

    public interface LocationFollowerListener {
        boolean onLocationUpdate(double lon, double lat);
        boolean isLocationReached(double lon, double lat, Location location);
        boolean onPathLocationReached(List<Location> path, int locationIndex);
    }
}
