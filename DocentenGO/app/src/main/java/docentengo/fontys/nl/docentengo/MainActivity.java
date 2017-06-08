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
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import Business.APIConnection;
import Business.User;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String secureID;
    RestTemplate client;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        this.client = new RestTemplate();
        client.getMessageConverters().add(new StringHttpMessageConverter());
        Async async = new Async();
        async.execute();


        Button submitButton = (Button)findViewById(R.id.btnSaveName);
        EditText inputField = (EditText)findViewById(R.id.txtInput);
        //determine whether or not it's the app booting up, it can also be redirecting to the enter teacher code screen
        if(!getIntent().hasExtra("BattleMode")){
            initiateHomeScreen(submitButton, inputField);
        }else if(getIntent().hasExtra("BattleMode")){
            initiateBattleScreen(submitButton, inputField);
        }
    }

    //#ToDo Jeroen hier zorgen dat je als je al eens bent ingelogt gelijk doorgaat, in this case no input required
    private void initiateHomeScreen(Button submitButton, final EditText inputField){
        //ifSignedIn call OpenTeacherDex with my ID/Username
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(testInput(inputField.getText().toString())){
                    //#getMyAndroidID
                    String myID = "0";
                    User newUser = new User(myID, inputField.getText().toString());
                    //TODO create user in db
                    OpenTeacherDex(newUser);
                }else{
                    showAlertDialog("Missing input", "please enter a username");
                }
            }
        });
    }

    private void OpenTeacherDex(User currentUser){
        Intent intent = new Intent(getApplicationContext(),TeacherDexActivity.class);
        intent.putExtra("CurrentUser", currentUser);
        startActivity(intent);
        finish();
    }

    private void initiateBattleScreen(Button submitButton, final EditText inputField){
        submitButton.setText("Battle");
        TextView message = (TextView)findViewById(R.id.tvEnterView);
        message.setText("Enter the code of the teacher you found:");

        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!testInput(inputField.getText().toString())){
                    showAlertDialog("Missing input", "please enter the Teacher code.");
                }else if(1==2){
                    //#ToDo contact server and check if the teacher code exists
                    showAlertDialog("Invallid input", "The entered teacher code was not vallid.");
                }else{
                    OpenBattleScreen(inputField.getText().toString());
                }
            }
        });

        Button returnButton = (Button)findViewById(R.id.btnReturnDex);
        returnButton.setVisibility(View.VISIBLE);
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTeacherDex((User)getIntent().getExtras().getSerializable("BattleMode"));
            }
        });
    }

    private void OpenBattleScreen(String teacherCode){
        Intent intent = new Intent(getApplicationContext(),BattleActivity.class);
        intent.putExtra("CurrentUser", getIntent().getExtras().getSerializable("BattleMode"));
        intent.putExtra("TeacherCode", teacherCode);
        startActivity(intent);
        finish();
    }

    private boolean testInput(String stringToTest){
        if(stringToTest.equals(null) || "".equals(stringToTest)){
            return false;
        }
        return true;
    }

    /**
     * Shows an alert dialog
     * @param title of the dialog
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



    private class Async extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected List<User> doInBackground(Void... params) {
            List<User> temp = Arrays.asList(client.getForObject(APIConnection.getAPIConnectionInformationURL()+"user",User[].class));
            return temp;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
          String userID =  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            for(User user : users)
            {
                if(userID.equals(user.getiD()))
                {
                    OpenTeacherDex(user);
                    break;
                }
            }
        }
    }
}
