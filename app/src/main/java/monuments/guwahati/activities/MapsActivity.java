package monuments.guwahati.activities;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

import monuments.guwahati.R;
import monuments.guwahati.gps.GMapV2Direction;
import monuments.guwahati.gps.GPSTracker;
import monuments.guwahati.helper.OnTaskCompleted;
import monuments.guwahati.network.InternetConnectionDetector;


public class MapsActivity extends AppCompatActivity implements OnTaskCompleted
{

    GoogleMap mMap;
    GMapV2Direction md;

    LatLng fromPosition;
    LatLng toPosition;

    private  boolean walking = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.quickreturn_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        md = new GMapV2Direction();
        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();


        if (!new InternetConnectionDetector(getApplicationContext()).isConnected())
        {
            Toast.makeText(getApplicationContext(), "Internet connection failure", Toast.LENGTH_SHORT).show();
            return;
        }


        GPSTracker gps = new GPSTracker(MapsActivity.this, this);

        if(gps.canGetLocation()) // check if GPS enabled
        {
            fromPosition = new LatLng(gps.getLatitude() , gps.getLongitude());
            toPosition = new LatLng(26.1664, 91.7055);
        }

        else
        {
            gps.showSettingsAlert();
            return;
        }


        LatLng coordinates = new LatLng(gps.getLatitude() , gps.getLongitude());


        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16));


        mMap.addMarker(new MarkerOptions().position(fromPosition).title("Start"));
        mMap.addMarker(new MarkerOptions().position(toPosition).title("End"));


        new showRoute(walking).execute();


        final ImageButton fabMode = (ImageButton) findViewById(R.id.myfab_main_btn);


        fabMode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                if(walking)
                {
                    walking = false;
                    fabMode.setImageResource(R.drawable.ic_taxi_white_24dp);
                }

                else
                {
                    walking = true;
                    fabMode.setImageResource(R.drawable.ic_walk_white_24dp);
                }

                new showRoute(walking).execute();
            }
        });
    }


    @Override
    public void onTaskCompleted(boolean flag, String message)
    {

    }


    @Override
    public void onTaskCompleted(Location location)
    {

    }


    private class showRoute extends AsyncTask<Void, Void, Document>
    {

        Document doc;
        PolylineOptions rectLine;
        boolean walk = false;


        public showRoute(boolean walk)
        {
            this.walk = walk;
        }


        @Override
        protected Document doInBackground(Void... params)
        {

            if(walk)
            {
                doc = md.getDocument(fromPosition, toPosition, GMapV2Direction.MODE_WALKING);
            }

            else
            {
                doc = md.getDocument(fromPosition, toPosition, GMapV2Direction.MODE_DRIVING);
            }


            Log.v("Duration: ", "" + md.getDurationText(doc));
            Log.v("Duration Value: ", "" + md.getDurationValue(doc));

            Log.v("Distance: ", "" + md.getDistanceText(doc));
            Log.v("Distance Value: ", "" + md.getDistanceValue(doc));

            Log.v("Start Address: ", "" + md.getStartAddress(doc));
            Log.v("End Address: ", "" + md.getEndAddress(doc));

            Log.v("Copy: ", "" + md.getCopyRights(doc));


            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            rectLine = new PolylineOptions().width(10).color(Color.BLUE);


            if(walking)
            {
                rectLine = new PolylineOptions().width(10).color(Color.CYAN);
            }


            for(int i = 0 ; i < directionPoint.size() ; i++)
            {
                rectLine.add(directionPoint.get(i));
            }

            return null;
        }


        @Override
        protected void onPostExecute(Document result)
        {

            try
            {
                mMap.addPolyline(rectLine);
            }

            catch (Exception e)
            {

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}