package ivica.android.pointssimplification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public abstract class SmartListener 
	   extends Service 
	   implements LocationListener, GpsStatus.Listener, GpsStatus.NmeaListener {
	
	public LocationManager lmgr;
	public float HDOP=1.5f;
	public int satellitesTotal;
	public int satellitesUsed;
	public PointSimplification algorithm;
	public LocationData data;
	
	public void StartListening(){
		lmgr = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, Preferences.MINIMUM_TIME_BETWEEN_UPDATES, Preferences.MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
		lmgr.addGpsStatusListener(this);
		lmgr.addNmeaListener(this);
		algorithm = new PointSimplification(this);
		data = new LocationData();
	}
	
	@Override
	public void onCreate() {
		this.StartListening();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		lmgr.removeUpdates(this);
		super.onDestroy();
	}
	
	@Override
	public void onLocationChanged(Location location) {
		data.time = location.getTime();
		data.lat = location.getLatitude();
		data.acc = location.getAccuracy();
		data.lon = location.getLongitude();
		data.velocity = location.getSpeed();
		data.hdop = HDOP;
		data.satTotal = satellitesTotal;
		data.satUsed = satellitesUsed;
		algorithm.Update(data);
	}
	
	@Override
	public void onGpsStatusChanged(int event) {
		if (lmgr != null) {
			GpsStatus status = lmgr.getGpsStatus(null);
			// number of satellites
			int newSatTotal = 0;
			int newSatUsed = 0;
			
			for(GpsSatellite sat : status.getSatellites()) {
				newSatTotal++;
				if (sat.usedInFix()) {
					newSatUsed++;
				}
			}
			satellitesTotal = newSatTotal;
			satellitesUsed = newSatUsed;
		}
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		NMEA nmea1 = new NMEA();
		float hdop = nmea1.parse(nmea).hdop;
		if(hdop != -1){
			this.HDOP = hdop;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
	
	public abstract void OnLocationChanged(LocationData location);
}
