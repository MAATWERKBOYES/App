package docentengo.fontys.nl.docentengo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import Business.APIConnection;
import Business.Answer;
import Business.DownloadImageTask;
import Business.Person;
import Business.Question;
import Business.User;

public class BattleActivity extends AppCompatActivity {//SelectedTeacher
    RestTemplate client;
    Person selectedTeacher;
    Question selectedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        loadTeacher();
        this.client = new RestTemplate();
        client.getMessageConverters().add(new StringHttpMessageConverter());
        BattleActivity.Async async = new BattleActivity.Async(this);
        async.execute();
    }

    private void loadTeacher(){
        if(getIntent().hasExtra("SelectedTeacher")){
            selectedTeacher = (Person) getIntent().getExtras().getSerializable("SelectedTeacher");
            ImageView picture = (ImageView) findViewById(R.id.imgVTeacherHead);
            if(selectedTeacher != null){
                new DownloadImageTask(picture)
                        .execute(selectedTeacher.getPhoto()+"?access_token="+APIConnection.getPictureToken());
            }else{
                new DownloadImageTask(picture)
                        .execute("https://api.fhict.nl/pictures/i870092.jpg?access_token="+APIConnection.getPictureToken());
            }
        }else if(!getIntent().hasExtra("SelectedTeacher")){
            showAlertDialog("No teacher", "No selected teacher was found");
        }
    }

    private void loadQuestion(Question question){
        try{
            //load the question
            TextView statedQuestion = (TextView)findViewById(R.id.txtQuestion);
            statedQuestion.setText(question.getValue());

            //load the answers
            TableLayout answers = (TableLayout)findViewById(R.id.tblQuestions);
            for(final Answer a : question.getAnswers()){
                TableRow row = new TableRow(BattleActivity.this);
                Button selectButton = new Button(BattleActivity.this);
                selectButton.setText("Go");
                selectButton.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAlertDialog("clicked", a.getId() + ", " + a.getValue());
                    }
                });
                row.addView(selectButton);
                TextView answer = new TextView(BattleActivity.this);
                answer.setText(a.toString());
                row.addView(answer);
                answers.addView(row);
            }
        }catch(Exception ex){
            showAlertDialog("exception loadQuestion", ex.getMessage());
        }
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
//question/department/"name"
    private class Async extends AsyncTask<Void, Void, Question> {
        private final BattleActivity activity;

        public Async(BattleActivity activity)
        {
            this.activity = activity;
        }

        @Override
        protected Question doInBackground(Void... params) {
            System.out.println("Tried getting a question for: " + selectedTeacher.getDisplayName() + " from department: " + selectedTeacher.getDepartment());
            Question temp = client.getForObject(APIConnection.getAPIConnectionInformationURL() + "question/department/" + selectedTeacher.getDepartment(), Question.class);
            return temp;
        }

        @Override
        protected void onPostExecute(Question question) {
            System.out.println("in onPostExecute");
            activity.loadQuestion(question);
        }
    }
}
