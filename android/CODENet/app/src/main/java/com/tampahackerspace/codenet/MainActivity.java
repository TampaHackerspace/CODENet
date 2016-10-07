package com.tampahackerspace.codenet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.google.wrapper.MyLocationOverlay;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {

    /*
    public class CustomResourceProxy extends DefaultResourceProxyImpl {

        private final Context mContext;
        public CustomResourceProxy(Context pContext) {
            super(pContext);
            mContext = pContext;
        }

        @Override
        public Bitmap getBitmap(final bitmap pResId) {
            switch (pResId){
                case person:
                    //your image goes here!!!
                    return BitmapFactory.decodeResource(mContext.getResources(),org.osmdroid.example.R.drawable.sfgpuci);
            }
            return super.getBitmap(pResId);
        }

        @Override
        public Drawable getDrawable(final bitmap pResId) {
            switch (pResId){
                case person:
                    return mContext.getResources().getDrawable(org.osmdroid.example.R.drawable.sfgpuci);
            }
            return super.getDrawable(pResId);
        }
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request permissions to support Android Marshmallow and above devices
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }

        //important! set your user agent to prevent getting banned from the osm servers
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);


        ResourceProxy mResourceProxy;
        //mResourceProxy = new CustomResourceProxy(getApplicationContext());
        mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        //final RelativeLayout rl = new RelativeLayout(this);
        //this.mOsmv = new MapView(this,mResourceProxy);

        MapView map = (MapView) findViewById(R.id.map);
        //map.setTileSource(TileSourceFactory.MAPNIK);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setTilesScaledToDpi(true);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(17);

        //GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        GeoPoint startPoint = new GeoPoint(27.953641,-82.5266934);
        mapController.setCenter(startPoint);

        GeoPoint ptAirport = new GeoPoint(27.977330, -82.535052);
        GeoPoint ptHackerspace = new GeoPoint(27.954442, -82.527487);
        GeoPoint ptMall = new GeoPoint(27.963240, -82.517957);
        GeoPoint ptGreenThree = new GeoPoint(27.958900, -82.525316);
        GeoPoint ptRedOne = new GeoPoint(27.953543, -82.528260);

        //your items
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        OverlayItem overlayItem;
        Drawable newMarker;
        overlayItem = new OverlayItem("CODENet", "Airport", ptAirport); // Lat/Lon decimal degrees
        newMarker = this.getDrawable(R.drawable.raspi);
        overlayItem.setMarker(newMarker);
        items.add(overlayItem);

        overlayItem = new OverlayItem("CODENet", "Market Square", ptMall); // Lat/Lon decimal degrees
        newMarker = this.getDrawable(R.drawable.raspi);
        overlayItem.setMarker(newMarker);
        items.add(overlayItem);

        overlayItem = new OverlayItem("CODENet", "Compound", ptHackerspace); // Lat/Lon decimal degrees
        newMarker = this.getDrawable(R.drawable.raspi);
        overlayItem.setMarker(newMarker);
        items.add(overlayItem);

        overlayItem = new OverlayItem("Operater", "Red One", ptRedOne); // Lat/Lon decimal degrees
        newMarker = this.getDrawable(R.drawable.android);
        overlayItem.setMarker(newMarker);
        items.add(overlayItem);

        overlayItem = new OverlayItem("Operater", "Green Three", ptGreenThree); // Lat/Lon decimal degrees
        newMarker = this.getDrawable(R.drawable.android);
        overlayItem.setMarker(newMarker);
        items.add(overlayItem);

        PathOverlay myPath = new PathOverlay(Color.RED, this);
        myPath.addPoint(ptMall);
        myPath.addPoint(ptAirport);
        myPath.addPoint(ptHackerspace);
        myPath.addPoint(ptMall);
        map.getOverlays().add(myPath);


//the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, mResourceProxy);
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);


        // See if we can start the SDR
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("iqsrc://-a 127.0.0.1 -p 1234 -n 1"));
        startActivityForResult(intent, 1234);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1234) {
            Log.i("SDR", "got request that didn't match");
            return; // This is the requestCode that was used with startActivityForResult
        }
        if (resultCode == RESULT_OK) {
            Log.i("SDR","RESULT OK");
            // Connection with device has been opened and the rtl-tcp server is running. You are now responsible for connecting.
            //int[] supportedTcpCommands = data.getIntArrayExtra("supportedTcpCommands");
            //startTcp(supportedTcpCommands); // Start your TCP client, see section below
        } else {
            // err_info from RTL2832U:
            String[] rtlsdrErrInfo = {
                    "permission_denied",
                    "root_required",
                    "no_devices_found",
                    "unknown_error",
                    "replug",
                    "already_running"};

            int errorId = -1;
            int exceptionCode = 0;
            String detailedDescription = null;
            if(data != null) {
                errorId = data.getIntExtra("marto.rtl_tcp_andro.RtlTcpExceptionId", -1);
                exceptionCode = data.getIntExtra("detailed_exception_code", 0);
                detailedDescription = data.getStringExtra("detailed_exception_message");
            }
            String errorMsg = "ERROR NOT SPECIFIED";
            if(errorId >= 0 && errorId < rtlsdrErrInfo.length)
                errorMsg = rtlsdrErrInfo[errorId];

            Log.e("SDR", "onActivityResult: RTL2832U driver returned with error: " + errorMsg + " ("+errorId+")"
                    + (detailedDescription != null ? ": " + detailedDescription + " (" + exceptionCode + ")" : ""));


            Log.i("SDR","RESULT NOT OK");
            // something went wrong, and the driver failed to start
            String errmsg = data.getStringExtra("detailed_exception_message");
            //showErrorMessage(errmsg); // Show the user why something went wrong
        }
    }



    // START PERMISSION CHECK
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        String message = "osmdroid permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nLocation to show user location.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nStorage access to store map tiles.";
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } // else: We already have permissions, so handle as normal
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (location && storage) {
                    // All Permissions Granted
                    Toast.makeText(MainActivity.this, "All permissions granted", Toast.LENGTH_SHORT).show();
                } else if (location) {
                    Toast.makeText(this, "Storage permission is required to store map tiles to reduce data usage and for offline usage.", Toast.LENGTH_LONG).show();
                } else if (storage) {
                    Toast.makeText(this, "Location permission is required to show the user's location on map.", Toast.LENGTH_LONG).show();
                } else { // !location && !storage case
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Storage permission is required to store map tiles to reduce data usage and for offline usage." +
                            "\nLocation permission is required to show the user's location on map.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // END PERMISSION CHECK
}
