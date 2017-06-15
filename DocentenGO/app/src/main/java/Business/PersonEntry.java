package Business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Jeroe on 9-6-2017.
 */

public class PersonEntry implements Serializable{
    private Person teacher;
    private int level;

    @JsonCreator
    public PersonEntry(@JsonProperty("level")int level, @JsonProperty("person")Person person)
    {
        this.teacher = person;
        this.level = level;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return teacher.toString();
    }
}
