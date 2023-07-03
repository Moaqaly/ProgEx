package Home_page_admin;

public class info_List {
    private  int  Parkinglot_nummber;
    private String   Parkinglot_Typ;

    public info_List(int parkinglot_nummber, String parkinglot_Typ) {
        Parkinglot_nummber = parkinglot_nummber;
        Parkinglot_Typ = parkinglot_Typ;
    }

    public int getParkinglot_nummber() {return Parkinglot_nummber;}

    public String getParkinglot_Typ() {return Parkinglot_Typ;}

    public void setParkinglot_nummber(int parkinglot_nummber) {Parkinglot_nummber = parkinglot_nummber;}

    public void setParkinglot_Typ(String parkinglot_Typ) {Parkinglot_Typ = parkinglot_Typ;}
}
