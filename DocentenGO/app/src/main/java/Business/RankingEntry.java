package Business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Arno on 6/16/2017.
 */

public class RankingEntry implements Comparable<RankingEntry>, Serializable{
    @JsonProperty("value")
    private Double value;
    @JsonProperty("username")
    private String username;

    @JsonCreator
    public RankingEntry(@JsonProperty("value")Double value, @JsonProperty("username")String username)
    {
        this.value = value;
        this.username = username;
    }

    public Double getValue() {
        return value;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int compareTo(RankingEntry object){
        return Double.compare(object.getValue(), this.getValue());
    }

    private String getOneDecimal(Double target){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(target);
    }

    @Override
    public String toString(){
        return getOneDecimal(value) + " " + username;
    }
}
