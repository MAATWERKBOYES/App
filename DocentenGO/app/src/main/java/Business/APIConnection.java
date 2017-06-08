package Business;


import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * Created by Jeroe on 2-6-2017.
 */
public final class APIConnection {
    private static final String WEB_PANEL_URL = "https://i341553.iris.fhict.nl/docentgo/";
    private static ApiConnectionInformation apiURL;

    private APIConnection() {

    }

    public static void initApiUrl() {
        apiURL = new RestTemplate().getForObject(WEB_PANEL_URL + "connection_config.json",ApiConnectionInformation.class);
    }

    public static String getAPIConnectionInformationURL() {
        if(apiURL == null)
        {
            initApiUrl();
        }
        return apiURL.getUrl();
    }
}
