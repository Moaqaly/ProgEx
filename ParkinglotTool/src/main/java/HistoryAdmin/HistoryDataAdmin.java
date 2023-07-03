package HistoryAdmin;

public class HistoryDataAdmin {
	private String ParkingSpace;
	private String Date;
	private String Username;

	public HistoryDataAdmin(String p, String d, String u) {
		ParkingSpace = p;
		Date = d;
		Username = u;
	}
	
	public String getParkingSpace() {
		return this.ParkingSpace;
	}
	
	public String getDate() {
		return this.Date;
	}
	
	public String getUsername() {
		return this.Username;
	}
	
	public void set_ParkingSpace(String s) {
		ParkingSpace = s;
	}
	
	public void set_Date(String s) {
		Date = s;
	}


}


