package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Business.Person;
import Business.Question;
import Business.User;
import Business.RankingEntry;

import static api.APIConnection.*;
import static java.util.Collections.singletonList;

/**
 * Created by Jeroe on 9-6-2017.
 */

public class ApiController {

    private RestTemplate client;

    public ApiController() {
        client = new RestTemplate();
        SimpleClientHttpRequestFactory s = new SimpleClientHttpRequestFactory();
        s.setReadTimeout(5000);
        s.setConnectTimeout(2000);
    }

    public Question getQuestionsFromTeacher(Person person) throws HttpClientErrorException {
        return client.getForObject(getAPIConnectionInformationURL() + "question/department/" + person.getDepartment(), Question.class);
    }

    public Person getTeacher(String abbreviation) throws HttpClientErrorException {
        return client.getForObject(getAPIConnectionInformationURL() + "people/" + abbreviation.toLowerCase(), Person.class);
    }

    public List<User> getAllUser() throws HttpClientErrorException {
        return Arrays.asList(client.getForObject(getAPIConnectionInformationURL() + "user", User[].class));
    }

    public List<Question> getQuestions() throws HttpClientErrorException {
        return Arrays.asList(client.getForObject(getAPIConnectionInformationURL() + "question", Question[].class));
    }

    public User loginUser(String secureID) throws HttpClientErrorException {
        return client.getForObject(getAPIConnectionInformationURL() + "user/" + secureID, User.class);
    }

    public List<RankingEntry> getRanking() throws HttpClientErrorException {
        return Arrays.asList(client.getForObject(getAPIConnectionInformationURL() + "user/ranking", RankingEntry[].class));
    }

    public void registerUser(User user) throws HttpClientErrorException {
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        String url = getAPIConnectionInformationURL() + "user";
        client.postForObject(url, request, User.class);
    }
    //Connect too users/'userID' and post the tearcherId
    public void answerQuestion (String userId, String teacherId) throws HttpClientErrorException{
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("personId", teacherId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
        client.postForEntity(getAPIConnectionInformationURL() + "user/" + userId, request, String.class);
    }
}
