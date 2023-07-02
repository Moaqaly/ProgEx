package History;

public class HistoryData {
	private String ParkingSpace;
	private String Date;

	public HistoryData(String p, String d) {
		ParkingSpace = p;
		Date = d;
	}
	
	public String getParkingSpace() {
		return this.ParkingSpace;
	}
	
	public String getDate() {
		return this.Date;
	}
	
	public void set_ParkingSpace(String s) {
		ParkingSpace = s;
	}
	
	public void set_Date(String s) {
		Date = s;
	}
}


