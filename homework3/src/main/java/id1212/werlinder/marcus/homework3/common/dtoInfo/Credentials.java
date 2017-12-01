package id1212.werlinder.marcus.homework3.common.dtoInfo;

import java.io.Serializable;

/**
 * The class that holds the necessary information for a user
 */
public class Credentials implements Serializable{
    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUser() {
        return username;
    }

    public String getUserPass() {
        return password;
    }
}
