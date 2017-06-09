package Business;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import docentengo.fontys.nl.docentengo.MainActivity;

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

    public static String getPictureToken(){
        return apiURL.getPictureToken();
    }

}
