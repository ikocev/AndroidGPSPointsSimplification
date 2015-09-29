package ivica.android.pointssimplification;

/*
 * Overview: IIR Speed Filter
 */
public class SpeedFilter {
	private float speed;
	private float speed_last;
	
	public SpeedFilter(){
		speed_last = -1;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		if(speed_last == -1) {
			this.speed = speed;
			this.speed_last = speed;
		}
		this.speed = Preferences.ALPHA * speed_last + (1 - Preferences.ALPHA) * speed;
		this.speed_last = speed;
	}
	
	
	
}
