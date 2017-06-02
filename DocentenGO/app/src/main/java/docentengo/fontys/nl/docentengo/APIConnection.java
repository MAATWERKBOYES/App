package docentengo.fontys.nl.docentengo;



import android.os.AsyncTask;
import android.provider.Contacts;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jeroe on 2-6-2017.
 */

public class APIConnection
{
    private final String URL = "http://145.93.96.177:8080/";
    RestTemplate client;
    private List<Person> people;


    public APIConnection() {
        this.client = new RestTemplate();
        client.getMessageConverters().add(new StringHttpMessageConverter());

    }

    public List<Person> getPeople()
    {
        return people;
    }

    public void getFromServer()
    {
        people = Arrays.asList(client.getForObject(URL+"people",Person[].class));
    }

    private class Async extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }

}
