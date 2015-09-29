package ivica.android.pointssimplification;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;


public class NMEA {
	
	interface SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position);
	}
	
	static float Latitude2Decimal(String lat, String NS) {
		float med = Float.parseFloat(lat.substring(2))/60.0f;
		med +=  Float.parseFloat(lat.substring(0, 2));
		if(NS.startsWith("S")) {
			med = -med;
		}
		return med;
	}

	static float Longitude2Decimal(String lon, String WE) {
		float med = Float.parseFloat(lon.substring(3))/60.0f;
		med +=  Float.parseFloat(lon.substring(0, 3));
		if(WE.startsWith("W")) {
			med = -med;
		}
		return med;
	}

	class GPGGA implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			if(tokens[1].length()== 0) return false;
			if(tokens[2].length()== 0) return false;
			if(tokens[3].length()== 0) return false;
			if(tokens[4].length()== 0) return false;
			if(tokens[5].length()== 0) return false;
			if(tokens[6].length()== 0) return false;
			if(tokens[9].length()== 0) return false;
			
			position.time = Float.parseFloat(tokens[1]);
			position.lat = Latitude2Decimal(tokens[2], tokens[3]);
			position.lon = Longitude2Decimal(tokens[4], tokens[5]);
			position.quality = Integer.parseInt(tokens[6]);
			position.altitude = Float.parseFloat(tokens[9]);
			return true;
		}
	}
	
	class GPGGL implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			if(tokens[1].length()== 0) return false;
			if(tokens[2].length()== 0) return false;
			if(tokens[3].length()== 0) return false;
			if(tokens[4].length()== 0) return false;
			if(tokens[5].length()== 0) return false;
			
			position.lat = Latitude2Decimal(tokens[1], tokens[2]);
			position.lon = Longitude2Decimal(tokens[3], tokens[4]);
			position.time = Float.parseFloat(tokens[5]);
			return true;
		}
	}
	
	class GPRMC implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			if(tokens[1].length()== 0) return false;
			if(tokens[3].length()== 0) return false;
			if(tokens[4].length()== 0) return false;
			if(tokens[5].length()== 0) return false;
			if(tokens[6].length()== 0) return false;
			if(tokens[7].length()== 0) return false;
			if(tokens[8].length()== 0) return false;
			
			position.time = Float.parseFloat(tokens[1]);
			position.lat = Latitude2Decimal(tokens[3], tokens[4]);
			position.lon = Longitude2Decimal(tokens[5], tokens[6]);
			position.velocity = Float.parseFloat(tokens[7]);
			position.dir = Float.parseFloat(tokens[8]);
			return true;
		}
	}
	
	class GPVTG implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			if(tokens[3].length()== 0) return false;
			position.dir = Float.parseFloat(tokens[3]);
			return true;
		}
	}
	
	class GPRMZ implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			if(tokens[1].length()== 0) return false;
			position.altitude = Float.parseFloat(tokens[1]);
			return true;
		}
	}
	
	class GPGSA implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position){
			if(Integer.parseInt(tokens[2])==1) return false;
			if(tokens[16].length()== 0) return false;
			
			position.hdop  = Float.parseFloat(tokens[15]);
			return true;
		}
	}
	
	public class GPSPosition {
		public float hdop = -1;
		public float time = 0.0f;
		public float lat = 0.0f;
		public float lon = 0.0f;
		public boolean fixed = false;
		public int quality = 0;
		public float dir = 0.0f;
		public float altitude = 0.0f;
		public float velocity = 0.0f;
		
		public void updatefix() {
			fixed = quality > 0;
		}
		
		@SuppressLint("DefaultLocale")
		public String toString() {
			return String.format("POSITION: lat: %f, lon: %f, time: %f, Q: %d, dir: %f, alt: %f, vel: %f", lat, lon, time, quality, dir, altitude, velocity);
		}
	}
	
	GPSPosition position = new GPSPosition();
	
	private static final Map<String, SentenceParser> sentenceParsers = new HashMap<String, SentenceParser>();
	
    public NMEA() {
    	sentenceParsers.put("GPGGA", new GPGGA());
    	sentenceParsers.put("GPGGL", new GPGGL());
    	sentenceParsers.put("GPRMC", new GPRMC());
    	sentenceParsers.put("GPRMZ", new GPRMZ());
    	sentenceParsers.put("GPGSA", new GPGSA());
    	//only really good GPS devices have this sentence
    	sentenceParsers.put("GPVTG", new GPVTG());
    }
    
	public GPSPosition parse(String line) {
		
		if(line.startsWith("$")) {
			String nmea = line.substring(1);
			String[] tokens = nmea.split(",");
			String type = tokens[0];

			if(sentenceParsers.containsKey(type)) {
				sentenceParsers.get(type).parse(tokens, position);
			}
			position.updatefix();
		}
		
		return position;
	}
}

