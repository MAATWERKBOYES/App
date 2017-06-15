package business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Arno on 6/2/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private String imei;
    private String name;
    private List<PersonEntry> teachers;


    //imei                <- fuck off
    //name
    @JsonCreator
    public User(@JsonProperty("name")String userName, @JsonProperty("imei")String id, @JsonProperty("teachers")List<PersonEntry> teachers)
    {
        this.name = userName;
        this.imei = id;
        this.teachers = teachers;
    }

    public String getImei() {
        return imei;
    }

    public String getName() {
        return name;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonEntry> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<PersonEntry> teachers) {
        this.teachers = teachers;
    }
}
