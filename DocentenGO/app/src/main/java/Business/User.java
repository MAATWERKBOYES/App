package Business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Arno on 6/2/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private String imei;
    private String userName;


    //imei                <- fuck off
    //name
    public User(@JsonProperty("name")String userName, @JsonProperty("imei")String id)
    {
        this.userName = userName;
        this.imei = id;
    }

    public String getImei() {
        return imei;
    }

    public String getUserName() {
        return userName;
    }

    
}
