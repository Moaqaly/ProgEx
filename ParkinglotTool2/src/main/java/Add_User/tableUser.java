package Add_User;

public class tableUser {
    public  boolean isAdmin;
    public  int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String gender;
    private boolean details;
    private String userTypeID;

    public tableUser(int id, String name, String email, String username, String password, String gender, boolean details, boolean isAdmin, String userTypeID) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.details = details;
        this.isAdmin = isAdmin;
        this.userTypeID = userTypeID;
    }

    public tableUser(int id, String name, String email, String username, String gender, boolean details, String userTypeID) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.details = details;
        this.userTypeID = userTypeID;
    }

    public tableUser() {

    }

    public void setId(int id) {
        this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDetails(boolean details) {
        this.details = details;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setUserTypeID(String userTypeID) {
        this.userTypeID = userTypeID;
    }

    public int getId() {
        return this.id;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public boolean isDetails() {
        return details;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUserTypeID() {
        return userTypeID;
    }




}
