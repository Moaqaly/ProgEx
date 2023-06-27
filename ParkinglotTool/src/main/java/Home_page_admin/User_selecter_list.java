package Home_page_admin;

public class User_selecter_list {
    private String name_A;
    private boolean details_A;
    private String  Gender_A;

    public User_selecter_list(String name, boolean details, String Gender) {
        this.name_A = name;
        this.details_A = details;
        this.Gender_A = Gender;
    }

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
}
