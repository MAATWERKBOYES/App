package docentengo.fontys.nl.docentengo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

import Business.APIConnection;
import Business.Person;
import Business.Question;
import Business.User;

public class EncounterActivity extends AppCompatActivity {
    RestTemplate client;
    private Person foundTeacher;
    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter);
        Button submitButton = (Button) findViewById(R.id.btnEngage);
        EditText inputField = (EditText) findViewById(R.id.txtInput);
        initiateBattleScreen(submitButton, inputField);
    }

    private void initiateBattleScreen(Button submitButton, final EditText inputField) {
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!testInput(inputField.getText().toString())) {
                    showAlertDialog("Missing input", "please enter the Teacher code.");
                }else{
                    input = inputField.getText().toString();
                    client = new RestTemplate();
                    client.getMessageConverters().add(new StringHttpMessageConverter());
                    EncounterActivity.Async async = new EncounterActivity.Async(EncounterActivity.this);
                    async.execute();
                }
            }
        });
        Button returnButton = (Button) findViewById(R.id.btnReturnDex);
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTeacherDex((User) getIntent().getExtras().getSerializable("CurrentUser"));
            }
        });
    }
    private void OpenTeacherDex(User currentUser) {
        Intent intent = new Intent(getApplicationContext(), TeacherDexActivity.class);
        intent.putExtra("CurrentUser", currentUser);
        startActivity(intent);
        finish();
    }

    private void OpenBattleScreen(String teacherCode) {
        Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
        intent.putExtra("CurrentUser", getIntent().getExtras().getSerializable("CurrentUser"));
        intent.putExtra("TeacherCode", teacherCode);
        startActivity(intent);
        finish();
    }

    private boolean testInput(String stringToTest) {
        if (stringToTest.equals(null) || "".equals(stringToTest)) {
            return false;
        }
        return true;
    }

    public void loadBattle(Person teacher){
        if(teacher != null){
            Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
            intent.putExtra("CurrentUser", getIntent().getExtras().getSerializable("CurrentUser"));
            intent.putExtra("SelectedTeacher", teacher);
            startActivity(intent);
            finish();
        }else{
            showAlertDialog("no Teacher", "Tried opening battle activity without a teacher");
        }
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
    }//user/save
    //people/"abvr"
    private class Async extends AsyncTask<Void, Void, Person> {
        private final EncounterActivity activity;

        public Async(EncounterActivity activity)
        {
            this.activity = activity;
        }

        @Override
        protected Person doInBackground(Void... params) {
            System.out.println("Tried getting a question for: " );
            try{
                Person temp = client.getForObject(APIConnection.getAPIConnectionInformationURL() + "people/" + input.toLowerCase(), Person.class);
                return temp;
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Person foundTeacher) {
            System.out.println("in onPostExecute");
            if(foundTeacher == null){
                activity.showAlertDialog("Teacher not found", "There was no teacher associated with the entered code.");
            }else{
                activity.loadBattle(foundTeacher);
            }

        }
    }
}
