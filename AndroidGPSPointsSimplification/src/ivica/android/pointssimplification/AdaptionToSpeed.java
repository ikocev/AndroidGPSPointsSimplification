package ivica.android.pointssimplification;


public class AdaptionToSpeed {
	
	private float Sn;

	public float getSn() {
		return Sn;
	}

	public float getSn(float speed, float accuracy) {
		if(speed > 100){
			this.Sn = Preferences.S_MAX;
		} else {			
			this.Sn = accuracy + (Preferences.S_MAX - accuracy)/100 * speed;
		}
		return this.Sn;
	}
	
}
