package Business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeroe on 8-6-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiConnectionInformation {
    public String connectionHost;
    public int connectionPort;

    public String getUrl()
    {
      return "http://"+connectionHost + ":"+connectionPort+"/";
    }

    @JsonCreator
    public ApiConnectionInformation(@JsonProperty("connection_host") String connectionHost, @JsonProperty("connection_port") int connectionPort)
    {
        this.connectionPort = connectionPort;
        this.connectionHost = connectionHost;
    }
}
