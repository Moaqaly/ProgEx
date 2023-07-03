package Home_page_admin;

import Add_User.tableUser;

public class User_selecter_list {
    private String name_A;
    private boolean details_A;
    private String  Gender_A;
    private String typ;

    public User_selecter_list(String name, boolean details, String Gender,String typ ) {
        this.name_A = name;
        this.details_A = details;
        this.Gender_A = Gender;
        this.typ = typ;
    }

    public String getTyp() {return typ;}

    public void setTyp(String typ) {this.typ = typ;}

    public String getName_A() {
        return name_A;
    }

    public void setName_A(String name) {
        this.name_A = name;
    }

    public boolean getDetails_A() {
        return details_A;
    }

    public String getGender_A() {
        return Gender_A;
    }

    public void setDetails_A(Boolean details_A) {
        this.details_A = details_A;
    }

    public void setGender_A(String gender_A) {
        Gender_A = gender_A;
    }

    public boolean isAdmin() {
        tableUser typ = new tableUser();
        return typ.isAdmin;
    }

    public int getID() {
        tableUser user = new tableUser(); // Create an instance of tableUser
        return user.getId(); // Call getId() on the instance
    }
}
