package Business;

import java.io.Serializable;

/**
 * Created by Arno on 6/2/2017.
 */

public class User implements Serializable {
    private String iD;
    private String userName;

    public User(String iD, String userName) {
        this.iD = iD;
        this.userName = userName;
    }

    public String getiD() {
        return iD;
    }

    public String getUserName() {
        return userName;
    }
}
