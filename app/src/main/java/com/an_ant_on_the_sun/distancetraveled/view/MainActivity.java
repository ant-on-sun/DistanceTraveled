package com.an_ant_on_the_sun.distancetraveled.view;

import android.content.Intent;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.an_ant_on_the_sun.distancetraveled.BuildConfig;
import com.an_ant_on_the_sun.distancetraveled.R;
import com.an_ant_on_the_sun.distancetraveled.controller.CalculateLengthOfTheRoute;
import com.an_ant_on_the_sun.distancetraveled.controller.GPSTracker;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectDragListener;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import com.an_ant_on_the_sun.distancetraveled.model.PointsOfTheRoute;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements InputListener, MapObjectTapListener {
    private static final String TAG = "DistanceTraveled";

    private static final String KEY_RESULT = "RESULT";

    private final String MAPKIT_API_KEY = "My Mapkit API key";
    private Point INITIAL_LOCATION_POINT = new Point(48.70939, 44.45183);
    private final float VERY_SMALL_NUMBER = 0.001f;
    private LocationListener locationListener;
    // GPSTracker class
    private GPSTracker gps;
    private MapView mMapView;
    private Map mMap;
    private MapObjectCollection mapObjects;
    private PointsOfTheRoute pointsOfTheRoute = PointsOfTheRoute.getInstance();
    private List<Point> pointList = pointsOfTheRoute.getPointList();
    private Polyline polyline;
    private ImageProvider placemarkIcon;
    private ImageProvider userLocationIcon;
    private int result;
    private float zoomValue = 12.0f;

    private TextView textViewInfo;
    private TextView textViewLengthOfRoute;
    private Button buttonNewRoute;
    private Button buttonCalculate;
    private Button buttonAbout;
    private Button buttonAddPlacemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapview);
        textViewInfo = findViewById(R.id.textViewInfo);
        textViewLengthOfRoute = findViewById(R.id.textViewLengthOfRoute);
        buttonNewRoute = findViewById(R.id.buttonNewRoute);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonAbout = findViewById(R.id.buttonAbout);
        buttonAddPlacemark = findViewById(R.id.buttonAddPlacemark);
        placemarkIcon = ImageProvider.fromResource(this, R.drawable.mark);
        userLocationIcon = ImageProvider.fromResource(this, R.drawable.user_location);

        textViewLengthOfRoute.setText("");
        mMap = mMapView.getMap();

        // create class GPSTracker object
        gps = new GPSTracker(this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            INITIAL_LOCATION_POINT = new Point (latitude, longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        mMap.addInputListener(new InputListener() {
            @Override
            public void onMapTap(Map map, Point point) {

            }

            @Override
            public void onMapLongTap(Map map, Point point) {
                pointList.add(point);
                updateObjectsOnTheMap();
            }
        });

        mMap.move(
                new CameraPosition(INITIAL_LOCATION_POINT,zoomValue, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        mapObjects = mMap.getMapObjects().addCollection();

        if (savedInstanceState != null) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "In MainActivity, savedInstanceState is not null");
            }
            result = savedInstanceState.getInt(KEY_RESULT);
            if (result > 0){
                textViewLengthOfRoute.setText(getString(R.string.route_length,
                        String.valueOf(result)));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
        MapKitFactory.getInstance().onStart();

        updateObjectsOnTheMap();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onMapTap(Map map, Point point) {

    }

    @Override
    public void onMapLongTap(Map map, Point point) {
        pointList.add(point);
        updateObjectsOnTheMap();
    }

    @Override
    public boolean onMapObjectTap(MapObject mapObject, Point point) {
        return false;
    }

    public void onNewRouteButtonClick(View view){
        //mapObjects.clear();
        pointList.clear();
        updateObjectsOnTheMap();
        result = 0;
        textViewLengthOfRoute.setText("");
    }

    public void onCalculateButtonClick(View view){
        result = CalculateLengthOfTheRoute.calculateIt();
        textViewLengthOfRoute.setText(getString(R.string.route_length, String.valueOf(result)));
    }

    public void onAboutButtonClick(View view){
        Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intentAbout);
    }

    public void onAddPlacemarkButtonClick (View view){
        Log.d(TAG, "In MainActivity, addPlacemark check for output");
        Point point = mMap.getCameraPosition().getTarget();
        pointList.add(point);
        updateObjectsOnTheMap();
    }

    public void onZoomInButtonClick (View view){
        if (zoomValue + VERY_SMALL_NUMBER < mMap.getMaxZoom()){
            zoomValue = zoomValue + 1.0f;
        }
        mMap.move(new CameraPosition(mMap.getCameraPosition().getTarget(),
                        zoomValue, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    public void onZoomOutButtonClick (View view){
        if (zoomValue - VERY_SMALL_NUMBER > mMap.getMinZoom()){
            zoomValue = zoomValue - 1.0f;
        }
        mMap.move(new CameraPosition(mMap.getCameraPosition().getTarget(),
                        zoomValue, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
    }

    private void updateObjectsOnTheMap(){
        mapObjects.clear();
        for (int i = 0; i < pointList.size(); i++){
            PlacemarkMapObject mark = mapObjects.addPlacemark(pointList.get(i));
            mark.setOpacity(0.5f);
            mark.setIcon(placemarkIcon);
            mark.setDraggable(true);
            if (BuildConfig.DEBUG){
                Log.d(TAG, "Mark [" + i +"] lat = " + mark.getGeometry().getLatitude()
                        + "; lon = " + mark.getGeometry().getLongitude());
            }
            final int indexOfMark = i;
            mark.addTapListener(new MapObjectTapListener() {
                @Override
                public boolean onMapObjectTap(MapObject mapObject, Point point) {
                    pointList.remove(indexOfMark);
                    if (BuildConfig.DEBUG){
                        Log.d(TAG, "in onMapObjectTap indexOfMark = " + indexOfMark);
                    }
                    updateObjectsOnTheMap();
                    return true;
                }
            });
            mark.setDragListener(new MapObjectDragListener() {
                @Override
                public void onMapObjectDragStart(MapObject mapObject) {
                }

                @Override
                public void onMapObjectDrag(MapObject mapObject, Point point) {

                }

                @Override
                public void onMapObjectDragEnd(MapObject mapObject) {
                    PlacemarkMapObject mark = (PlacemarkMapObject) mapObject;
                    Point point = mark.getGeometry();
                    if (BuildConfig.DEBUG){
                        Log.d(TAG, "In MainActivity. On end drag: point lat = "
                                + point.getLatitude() + "; lon = " + point.getLongitude());
                    }
                    pointList.set(indexOfMark, point);
                    updateObjectsOnTheMap();
                }
            });
        }
        //Drawing polyline
        polyline = new Polyline(pointList);
        mapObjects.addPolyline(polyline);
        //Mark user location
        if (gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Point pointUserLocation = new Point (latitude,longitude);
            PlacemarkMapObject markUserLocation = mapObjects.addPlacemark(pointUserLocation);
            markUserLocation.setIcon(userLocationIcon);
            markUserLocation.setOpacity(0.65f);
            markUserLocation.setDraggable(false);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_RESULT, result);
    }

}
