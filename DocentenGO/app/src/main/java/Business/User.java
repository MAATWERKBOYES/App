package Business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Arno on 6/2/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private String iD;
    private String userName;

    public User(String iD, String userName) {
        this.iD = iD;
        this.userName = userName;
    }
    //imei                <- fuck off
    //name
    public User(@JsonProperty("name")String userName)
    {
        this.userName = userName;
        iD = "";
    }

    public String getiD() {
        return iD;
    }

    public String getUserName() {
        return userName;
    }
}
