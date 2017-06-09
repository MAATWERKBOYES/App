package docentengo.fontys.nl.docentengo;


//Never forgetti
//http://145.93.96.177:8080/people
//http://145.93.96.177:8080/question

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Business.ApiController;
import Business.PersonEntry;
import Business.User;

import java.util.ArrayList;

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

    //#ToDo Jeroen hier zorgen dat je als je al eens bent ingelogt gelijk doorgaat, in this case no input required
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
                    showAlertDialog("Missing input", "please enter a username");
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

    /**
     * Shows an alert dialog
     *
     * @param title   of the dialog
     * @param message of the dialog
     */
    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private class Login extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            String secureID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            return apiController.loginUser(secureID);
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
            apiController.registerUser(user);
            return null;
        }
    }
}


