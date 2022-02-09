package com.ecocovoit.ecocovoit;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecocovoit.ecocovoit.communication.comhandlers.ConnexionHandler;
import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.ecocovoit.ecocovoit.map.OsmPathFollow;
import com.ecocovoit.ecocovoit.map.view.IconOnMap;
import com.ecocovoit.ecocovoit.map.view.OsmMapWebView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String url = "http://192.168.43.251:8080";

    private static final String TAG = "Mr Sauvage";

    private OsmMapWebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = findViewById(R.id.layout_main);
        Button btnMap = findViewById(R.id.btnMap_main);

        final List<Location> path = new ArrayList<>();
        path.add(new Location(2.001, 48.001, 0));
        path.add(new Location(2.002, 48.002, 0));
        path.add(new Location(2.004, 48.003, 0));
        path.add(new Location(2.008, 48.004, 0));
        path.add(new Location(2.010, 48.005, 0));

        List<IconOnMap> icons = new ArrayList<>();
        List<Location> iconLocations1 = new ArrayList<>();
        iconLocations1.add(new Location(2.0, 48.0, 0));
        iconLocations1.add(new Location(2.001, 48.001, 0));
        iconLocations1.add(new Location(2.002, 48.002, 0));
        iconLocations1.add(new Location(2.003, 48.003, 0));
        List<Location> iconLocations2 = new ArrayList<>();
        iconLocations2.add(new Location(2.000, 48.001, 0));
        iconLocations2.add(new Location(2.001, 48.002, 0));
        iconLocations2.add(new Location(2.002, 48.003, 0));
        iconLocations2.add(new Location(2.003, 48.004, 0));
        icons.add(new IconOnMap("https://i.postimg.cc/FRYXnqJH/logo.png", 30, 30, iconLocations1));
        icons.add(new IconOnMap("https://i.postimg.cc/0N55kchp/Screenshot-20210208-133200.jpg", 30, 90, iconLocations2));

        view = new OsmMapWebView(
                this,
                path,
                new Location(2.0, 48.0, 0),
                icons);
        layout.addView(view);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.showCurrentLocation();
            }
        });
    }

    private void followPath(final List<Location> path) {
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "request position : GRANTED");
            perfromPathFollow(path);
        }
        else {
            Log.i(TAG, "Ask permission");
            ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if (result) {
                                Log.i(TAG, "request position : GRANTED");
                                perfromPathFollow(path);
                            } else {
                                Log.i(TAG, "request position : DENIED");
                            }
                        }
                    });
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void perfromPathFollow(List<Location> path) {
        OsmPathFollow pathFollow = new OsmPathFollow(this, path, new OsmPathFollow.LocationFollowerListener() {
            @Override
            public boolean onLocationUpdate(double lon, double lat) {
                Log.i(TAG, "onLocationUpdate: ("+lon+", "+lat+")");
                //view.moveTo(lon, lat);
                view.setCurrentLocation(new Location(lat, lon, 0));
                return true;
            }

            @Override
            public boolean isLocationReached(double lon, double lat, Location location) {
                return false;
            }

            @Override
            public boolean onPathLocationReached(List<Location> path, int locationIndex) {
                return false;
            }
        }, false);
        pathFollow.startFollowing();
    }

    @Override
    protected void onResume() {
        super.onResume();

        view.reload();
    }

    private void testConnectRequest() {
        ConnexionHandler handler = new ConnexionHandler(
                this,
                url
        );
        handler.connect(
                "LOGIN_GHISLAIN",
                "PASSWORD_GHISLAIN",
                true,
                new ConnexionHandler.ConnexionOperationListener() {
                    @Override
                    public void onWrongPassword() {
                        Toast.makeText(MainActivity.this, "Bad password", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "wrong password");
                    }

                    @Override
                    public void onUnknownLogin() {
                        Toast.makeText(MainActivity.this, "unknown login", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "unknown login");
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "success");
                    }

                    @Override
                    public void onOtherError(String message) {
                        Toast.makeText(MainActivity.this, "Error : "+message, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "other error : "+message);
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(MainActivity.this, "Fail : "+message, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "fail : "+message);
                    }

                    @Override
                    public void onBadResponseFromServer() {
                        Toast.makeText(MainActivity.this, "Bad response from server", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Bad response from server");
                    }
                }
        );
    }
}