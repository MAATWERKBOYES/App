package business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeroe on 2-6-2017.
 */

public class Answer {
    private String value;
    private int id;
    private boolean correct;

    @JsonCreator
    public Answer(@JsonProperty("value") String value,
                  @JsonProperty("id") int id,
                  @JsonProperty("isCorrect") boolean correct) {
        this.value = value;
        this.id = id;
        this.correct = correct;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString(){
        return value;
    }
}
