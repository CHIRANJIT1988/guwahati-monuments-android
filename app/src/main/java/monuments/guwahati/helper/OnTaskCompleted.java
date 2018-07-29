package monuments.guwahati.helper;


import android.location.Location;

public interface OnTaskCompleted
{

	void onTaskCompleted(boolean b, String action);
	void onTaskCompleted(Location location);
}