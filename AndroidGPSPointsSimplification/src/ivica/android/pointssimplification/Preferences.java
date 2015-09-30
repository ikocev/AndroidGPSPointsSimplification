package ivica.android.pointssimplification;

public class Preferences {
	public final static int HDOP_APPROXIMATION_FACTOR = 4;
	
	public static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
    public static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	
	/**
	 * valid range for azimuth angles.
	 */
	public static final float AZIMUTH_MIN = 0;
	public static final float AZIMUTH_MAX = 360;
	public static final float AZIMUTH_INVALID = -1;
	
	public static final long MULTIPLY_FIX = 10000000000l;
	public static final float DEG_FILTERING = 0.95f;
	
	public static final float ALPHA = 0.85f;
	public static final int S_MAX = 65;

	public static final double MIN_ANGLE = 2;
	
	
	
	public static final String INTENT_ACTION = "com.ivica.gpstracl";
}
