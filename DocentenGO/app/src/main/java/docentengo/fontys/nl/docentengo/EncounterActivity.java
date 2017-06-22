package docentengo.fontys.nl.docentengo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import api.ApiController;
import Business.Person;
import Business.User;

public class EncounterActivity extends AppCompatActivity {
    private ApiController apiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter);
        Button submitButton = (Button) findViewById(R.id.btnEngage);
        EditText inputField = (EditText) findViewById(R.id.txtInput);
        initiateBattleScreen(submitButton, inputField);
        if(getIntent().hasExtra("returnFromBattle")){
            if((boolean)getIntent().getExtras().getSerializable("returnFromBattle")){
                AlertHandler.showAlertDialog(EncounterActivity.this, "Correct!",
                        "You caught a teacher, check your teacherdex to see it's data");
            }else{
                AlertHandler.showAlertDialog(EncounterActivity.this, "Wrong answer!",
                        "Failed to catch the teacher, it ran away");
            }
        }
        apiController = new ApiController();
    }

    private void initiateBattleScreen(Button submitButton, final EditText inputField) {
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputField.getText().toString().isEmpty()) {
                    AlertHandler.showAlertDialog(EncounterActivity.this,
                            "Missing input",
                            "Please enter the Teacher code.");
                } else {
                    GetTeacher getTeacher = new GetTeacher(inputField.getText().toString());
                    getTeacher.execute();
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

    public void loadBattle(Person teacher){
        if(teacher != null){
            Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
            intent.putExtra("CurrentUser", getIntent().getExtras().getSerializable("CurrentUser"));
            intent.putExtra("SelectedTeacher", teacher);
            startActivity(intent);
            finish();
        }else{
            AlertHandler.showAlertDialog(EncounterActivity.this, "No teacher", "Tried to open the battle screen without a teacher");
        }
    }

    private class GetTeacher extends AsyncTask<Void, Void, Person> {
        private String abbreviation;

        public GetTeacher(String abbreviation) {
            this.abbreviation = abbreviation.toLowerCase();
        }

        @Override
        protected Person doInBackground(Void... params) {
            try {
                return apiController.getTeacher(abbreviation);
            } catch (Exception ex) {
                AlertHandler.showErrorDialog(EncounterActivity.this, ex, "Invalid input", "The entered teacher code was invalid.");
                return null;
            }

        }

        @Override
        protected void onPostExecute(Person person) {
            if (person!=null && !person.getDepartment().isEmpty()) {
                if(person.getDepartment().equals("Team S") || person.getDepartment().equals("Team T") ||
                        person.getDepartment().equals("Team M") || person.getDepartment().equals("Team B"))
                    loadBattle(person);
                else
                    AlertHandler.showAlertDialog(EncounterActivity.this, "Invalid input", "The entered teacher code was invalid.");
            }
        }
    }

}

