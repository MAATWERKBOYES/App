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
import java.util.List;

/**
 * Created by Jeroe on 9-6-2017.
 */

public class ApiController {

    private RestTemplate client = new RestTemplate();


    public Person getTeacher(String abbreviation)
    {
        return client.getForObject(APIConnection.getAPIConnectionInformationURL() + "people/"+ abbreviation, Person.class);
    }
    public List<User> getAllUser()
    {
        return Arrays.asList(client.getForObject(APIConnection.getAPIConnectionInformationURL() + "user", User[].class));
    }
    public List<Question> getQuestions()
    {
        return Arrays.asList(client.getForObject(APIConnection.getAPIConnectionInformationURL() + "question", Question[].class));
    }
    public User loginUser(String secureID)
    {
        return client.getForObject(APIConnection.getAPIConnectionInformationURL() + "user/" + secureID, User.class);
    }
    public void registerUser(User user)
    {
        ObjectMapper mapper= new ObjectMapper();
        String json="";
        try {
            json = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        client.setInterceptors(Arrays.asList((ClientHttpRequestInterceptor) new JsonHeaderInterceptor()));
        client.postForObject(APIConnection.getAPIConnectionInformationURL() + "user",json,User.class);
    }

    private class JsonHeaderInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().add(HttpHeaders.ACCEPT, "application/json");
            request.getHeaders().remove(HttpHeaders.CONTENT_TYPE);
            request.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");


            return execution.execute(request, body);
        }
    }
}
