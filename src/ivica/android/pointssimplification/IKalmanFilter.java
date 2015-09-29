package ivica.android.pointssimplification;

public interface IKalmanFilter {
	
	public void configurate();
	
	/*
	 * Time Update (prediction) method. Here we update the values for Xk and Pk to be used in the next step.
	 * Xk^=Xk-1
	 * Pk^=Pk-1 or (Pk^ = Pk-1 + Q), where Q is process noise and also a constant.
	 */
	public void predict();
	
	/*
	 * Measurement Update (correction) method.
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
	 */
	public LocationData update();
	
	/*
	 * Set the current state;
	 */
	public KalmanFilter setState(double lon, double lat);
	
	/*
	 * Get the last state;
	 */
	public double[] getState();
	
	/*
	 * Set the random noise estimation from the out of fuzzy logic.
	 */
	public KalmanFilter setNoise(double R);
	
}
