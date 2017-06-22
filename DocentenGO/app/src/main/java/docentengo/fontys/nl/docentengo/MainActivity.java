package docentengo.fontys.nl.docentengo;


//Never forgetti
//http://145.93.96.177:8080/people
//http://145.93.96.177:8080/question

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.springframework.web.client.HttpClientErrorException;

import Business.Person;
import api.ApiController;
import Business.PersonEntry;
import Business.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


  private ApiController apiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiController = new ApiController();
        List<PersonEntry> personEntryList = new ArrayList<>();
        Person person = new Person("1","testleraar","ka","pa","noob","Software",null,null,false);
        Person person2 = new Person("2","testleraar2","ka","pa","noob","Software",null,null,false);
        Person person3 = new Person("3","testleraar3","ka","pa","noob","Software",null,null,false);
        Person person4 = new Person("4","testleraar4","ka","pa","noob","Software",null,null,false);

        personEntryList.add(new PersonEntry(1,person));
        personEntryList.add(new PersonEntry(1,person2));
        personEntryList.add(new PersonEntry(1,person3));
        personEntryList.add(new PersonEntry(1,person4));


        Login Login = new Login();
        Login.execute();

        Button submitButton = (Button) findViewById(R.id.btnSaveName);
        EditText inputField = (EditText) findViewById(R.id.txtInput);
        initiateHomeScreen(submitButton, inputField);
    }

    private void initiateHomeScreen(Button submitButton, final EditText inputField) {
        //ifSignedIn call OpenTeacherDex with my ID/Username
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testInput(inputField.getText().toString())) {
                    //#getMyAndroidID
                    User newUser = new User(inputField.getText().toString(), Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), new ArrayList<PersonEntry>());
                    registerUser save = new registerUser(newUser);
                    save.execute();
                    OpenTeacherDex(newUser);
                } else {
                    AlertHandler.showAlertDialog(MainActivity.this,
                            "Missing input",
                            "Please enter a valid username.");
                }
            }
        });
    }

    private void OpenTeacherDex(User currentUser) {
        Intent intent = new Intent(getApplicationContext(), TeacherDexActivity.class);
        intent.putExtra("CurrentUser", currentUser);
        startActivity(intent);
        finish();
    }

    private boolean testInput(String stringToTest) {
        return !(stringToTest == null || stringToTest.trim().isEmpty());
    }

    private class Login extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            try
            {
                String secureID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                return apiController.loginUser(secureID);
            }
            catch (HttpClientErrorException ex)
            {
                AlertHandler.showErrorDialog(MainActivity.this,
                        ex,
                        "Server connection error",
                        "The application was unable to connect to the server.");
                return null;
            }
        }
        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                OpenTeacherDex(user);
            }
        }

    }

    private class registerUser extends AsyncTask<Void, Void, Void> {
        private User user;
        public registerUser(User user)
        {
            this.user = user;
        }
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                apiController.registerUser(user);
                return null;
            }
            catch (Exception ex)
            {
                AlertHandler.showErrorDialog(MainActivity.this,
                        ex,
                        "Server connection error",
                        "The application was unable to connect to the server");
                return null;
            }

        }
    }
}


