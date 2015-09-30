package ivica.android.pointssimplification;

public class PointSimplification {
	
	private SpeedFilter speedFilter;
	private KalmanFilter filter;
	private AdaptionToSpeed speedAdaption;
	private SmartListener locationListener;
	
	private double lon;
	private double lat;
	private double lastLon;
	private double lastLat;
	
	private double angle;
	private double lastAngle;
	private double diffAngle;
	
	private double distanceLon;
	private double distanceLat;
	
	private float velocity;
	
	private long time;
	private float acc;
	
	public PointSimplification(SmartListener smListenerClass){
		this.locationListener = smListenerClass;
		speedAdaption = new AdaptionToSpeed();
		speedFilter = new SpeedFilter();
		filter = new KalmanFilter();
	}
	
	public void Update(LocationData data){
		speedFilter.setSpeed(data.velocity);
		this.velocity = speedFilter.getSpeed();
		this.lon = data.lon;
		this.lat = data.lat;
		this.time = data.time;
		this.acc = data.acc;
		
		if(filter.isInitialized){
			LocationData _d = filter.setState(lon, lat, time).update();
		   	this.lon = _d.lon;
		   	this.lat = _d.lat;
		   
		   	distanceLon = data.lon - this.lon;
			distanceLat = data.lat - this.lat;
			angle = Math.atan2(distanceLat, distanceLon) * 180 / Math.PI;
			diffAngle = Math.abs(angle - lastAngle);
			
			float minDist = speedAdaption.getSn(this.velocity, this.acc);
			double currentDistance = distFrom(this.lon, this.lat, this.lastLon, this.lastLat);
			
			if(diffAngle > 4.25 * data.hdop){
				//add this point to the path
				this.lastLon = this.lon;
				this.lastLat = this.lat;
				
				locationListener.OnLocationChanged(data);
			} else if(currentDistance >= minDist && diffAngle >= Preferences.MIN_ANGLE){
				this.lastLon = this.lon;
				this.lastLat = this.lat;
				locationListener.OnLocationChanged(data);
			}
			lastAngle = angle;
		} else {
			filter.initialize(lon, lat);//first need initialization here (starting here)
		}
		
	}
	
	private double distFrom(double lon1, double lat1, double lon2, double lat2) {
		 
		double earthRadius = 6371000; //in meters
		double dLng = Math.toRadians(lon2-lon1);
		double dLat = Math.toRadians(lat2-lat1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		           Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = earthRadius * c;
		
		return dist;
	 }
}
