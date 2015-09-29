package ivica.android.pointssimplification;
/*
 * Usage:
 * KalmanFilter filter = new KalmanFilter();
 * filter.setNoise(0.4).setState(32.32323, 21.43434).Update().getState();
 */
public class KalmanFilter implements IKalmanFilter {
	/*
	 * 0 element is the longitude
	 * 1st element is the latitude
	 */
	private double[] Xk;
	/*
	 * 0 element is the longitude
	 * 1st element is the latitude
	 */
	private double[] Zk;
	
	private double Kk;
	private double Pk;
	private double R;
	private double Q;
	public boolean isInitialized = false;;
	private double[] distanceXk = new double[2];
	//private double angle = 0;
	//private double lastAngle = 0;
	//private double diffAngle = 0;
	//private double hdop = 1.5;
	
	LocationData data;
	/*
	 * 0 element is the longitude
	 * 1st element is the latitude
	 */
	private double[] Xk_last;
	private double Pk_last;
	
	public KalmanFilter(){
		this.configurate();
		data = new LocationData();
	}
	
	public void initialize(double lon, double lat){
		Xk[0] = lon;
		Xk[1] = lat;
		this.isInitialized = true;
	}
	
	public boolean IsInitialized(){
		return this.isInitialized;
	}
	
	@Override
	public void configurate() {
		Xk = new double[2];
		Zk = new double[2];
		Kk = 0;
		Pk = 1;
		R = 1;
		Q = 1;
		
		distanceXk[0] = 0;
		distanceXk[1] = 0;
		
		Xk_last = new double[2];
		Xk_last[0] = 0;
		Xk_last[1] = 0;
		Pk_last = 1;
	}
	
	/*
	 * Time Update (prediction) method. Here we update the values for Xk and Pk to be used in the next step.
	 * Xk^=Xk-1
	 * Pk^=Pk-1 or (Pk^ = Pk-1 + Q), where Q is process noise and also a constant.
	 */
	@Override
	public void predict() {
		Xk_last[0] = Xk[0];
		Xk_last[1] = Xk[1];
		Pk_last = Pk + Q;
	}
	
	/*
	 * Measurement Update (correction) method.
	 * 
	 * Note: First it is called predict method.
	 * 
	 * Kk = Pk^ / (Pk^ + R)
	 * Xk = Xk^ + Kk(Zk-Xk^)
	 * Pk = (1 - Kk)Pk^
	 * 
	 * (^) - symbol represent the values from the last step.
	 * R - random noise in the environment. Greater then zero. Calculated form fuzzy logic.
	 * P = prior error covariance.
	 * Xk - prior estimate.
	 * Zk - measurement value.
	 * Kk - Kalman gain
	 * 
	 */
	@Override
	public LocationData update() {
		this.predict();
		
		Kk = Pk_last / (Pk_last + R);
		
		Xk[0] = Xk_last[0] + Kk*(Zk[0]-Xk_last[0]);
		Xk[1] = Xk_last[1] + Kk*(Zk[1]-Xk_last[1]);
		
		Pk = (1 - Kk) * Pk_last;
		
		data = new LocationData();
		data.lon = Xk[0];
		data.lat = Xk[1];
		return data;
	}

	@Override
	public KalmanFilter setState(double lon, double lat) {
		Zk[0] = lon;
		Zk[1] = lat;
		return this;
	}
	
	public KalmanFilter setState(double lon, double lat, long lTime) {
		Zk[0] = lon;
		Zk[1] = lat;
		//this.locationTime = lTime;
		return this;
	}
	
	/*
	 * [0] - longitude, [1] - latitude
	 * @see com.ivica.gpstracl.KalmanFilterInterface#getState()
	 */
	@Override
	public double[] getState() {
		double[] obj = new double[2];
		obj[0] = Xk[0];
		obj[1] = Xk[1];
		return Xk;
	}

	@Override
	public KalmanFilter setNoise(double R) {
		this.R = R;
		return this;
	}
}
