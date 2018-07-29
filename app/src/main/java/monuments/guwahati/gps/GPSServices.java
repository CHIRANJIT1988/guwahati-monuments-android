package monuments.guwahati.gps;


import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GPSServices
{

    public static String getDirection(LatLng my_latlong,LatLng frnd_latlong)
    {

        double my_lat=my_latlong.latitude;
        double my_long=my_latlong.longitude;


        double frnd_lat=frnd_latlong.latitude;
        double frnd_long=frnd_latlong.longitude;


        double radians=getAtan2((frnd_long-my_long),(frnd_lat-my_lat));
        double compassReading = radians * (180 / Math.PI);


        String[] coordNames = {"North", "North-East", "East", "South-East", "South", "South-West", "West", "North-West", "North"};
        int coordIndex = (int) Math.round(compassReading / 45);

        if (coordIndex < 0)
        {
            coordIndex = coordIndex + 8;
        }

        return coordNames[coordIndex]; // returns the coordinate value
    }


    private static double getAtan2(double longi,double lat)
    {
        return Math.atan2(longi, lat);
    }


    public static String getDistance(LatLng my_latlong,LatLng frnd_latlong)
    {

        Location l1 = new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);


        Location l2 = new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);


        float distance = Math.round(l1.distanceTo(l2));
        String dist = distance + " M";

        if(distance > 1000.0f)
        {
            distance = distance/1000.0f;
            dist = distance + " KM";
        }

        return dist;
    }
}