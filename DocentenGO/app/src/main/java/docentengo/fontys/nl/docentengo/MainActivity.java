package docentengo.fontys.nl.docentengo;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String secureID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secureID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        APIConnection connection = new APIConnection();
       List<Person> People = connection.getPeople();

    //http://145.93.96.177:8080/people
    }


}
