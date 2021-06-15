package data.classes;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String firstName;
    @Expose
    private String date;
    @Expose
    private String lastName;
    @Expose
    private int phoneNumber;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String firstName, String date, String lastName, int phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.date = date;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
