package docentengo.fontys.nl.docentengo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

import Business.PersonEntry;
import Business.User;
import api.ApiController;

public class MainActivity extends AppCompatActivity {


  private ApiController apiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiController = new ApiController();
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


