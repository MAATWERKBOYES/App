package Business;

/**
 * Created by Jeroe on 2-6-2017.
 */

public class Answer {
    private String value;
    private int id;

    public Answer(){}

    public Answer(String value, int id) {
        this.value = value;
        this.id = id;
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



}
