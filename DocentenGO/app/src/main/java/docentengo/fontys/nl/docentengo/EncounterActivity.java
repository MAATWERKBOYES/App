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

import Business.ApiController;
import Business.Person;
import Business.User;

public class EncounterActivity extends AppCompatActivity {

    private ApiController apiController;
    private Person selectedTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter);
        Button submitButton = (Button) findViewById(R.id.btnEngage);
        EditText inputField = (EditText) findViewById(R.id.txtInput);
        initiateBattleScreen(submitButton, inputField);

        apiController = new ApiController();
    }

    private void initiateBattleScreen(Button submitButton, final EditText inputField) {
        submitButton.setText("Battle");
        TextView message = (TextView) findViewById(R.id.tvEnterView);
        message.setText("Enter the code of the teacher you found:");

        submitButton.setOnClickListener(new Button.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (!testInput(inputField.getText().toString())) {
                                                    showAlertDialog("Missing input", "please enter the Teacher code.");
                                                } else {
                                                    GetTeacher getTeacher = new GetTeacher(inputField.getText().toString());

                                                    if (getTeacher.execute()==null) {
                                                        showAlertDialog("Invallid input", "The entered teacher code was not vallid.");
                                                    } else {
                                                        OpenBattleScreen(inputField.getText().toString());
                                                    }
                                                }
                                            }
                                        }
        );
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
        intent.putExtra("CurrentUser", getIntent().getExtras().getSerializable("BattleMode"));
        intent.putExtra("TeacherCode", teacherCode);
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
    }//user/save


    private class GetTeacher extends AsyncTask<Void, Void, Person> {
        private String abbreviation;
        public GetTeacher(String abbreviation)
        {
            this.abbreviation = abbreviation;
        }
        @Override
        protected Person doInBackground(Void... params) {

            return apiController.getTeacher(abbreviation);
        }

        @Override
        protected void onPostExecute(Person person) {
            selectedTeacher = person;
        }
    }
}