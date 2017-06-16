package docentengo.fontys.nl.docentengo;

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

import Business.User;
import api.APIConnection;
import Business.Answer;
import api.ApiController;
import api.DownloadImageTask;
import Business.Person;
import Business.Question;

public class BattleActivity extends AppCompatActivity {
    private User loggedUser;
    private Person selectedTeacher;
    private ApiController apiController;
    private Question selectedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        if(getIntent().hasExtra("CurrentUser")) {
            loggedUser = (User)getIntent().getExtras().getSerializable("CurrentUser");
        }else{
            AlertHandler.showAlertDialog(BattleActivity.this, "Pageload error!",
                    "Loaded the page without a logged in user");
        }
        loadTeacher();
        createRunButtonEvent();
        apiController = new ApiController();
        BattleActivity.GetQuestions getQuestion = new BattleActivity.GetQuestions(this);
        getQuestion.execute();
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
                        .execute("https://api.fhict.nl/pictures/i870092.jpg?access_token="+ APIConnection.getPictureToken());
            }
        }else if(!getIntent().hasExtra("SelectedTeacher")){
            AlertHandler.showAlertDialog(this, "No teacher found", "No selected teacher was found");
        }
    }

    private void createRunButtonEvent(){
        Button returnDex = (Button)findViewById(R.id.btnReturn);
        returnDex.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToEncounterScreen();
            }
        });
    }

    private void returnToEncounterScreen(){
        Intent intent = new Intent(getApplicationContext(),EncounterActivity.class);
        intent.putExtra("CurrentUser", loggedUser);
        startActivity(intent);
        finish();
    }

    private void loadQuestion(Question question){
        try{
            selectedQuestion = question;

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
                        answerQuestion(a);
                        AlertHandler.showAlertDialog(BattleActivity.this, "Clicked!",
                                "Id: " + a.getId() + ";\n" +
                                        "Value: "+ a.getValue());
                    }
                });
                row.addView(selectButton);
                TextView answer = new TextView(BattleActivity.this);
                answer.setText(a.toString());
                row.addView(answer);
                answers.addView(row);
            }
        }catch(Exception ex){
            AlertHandler.showErrorDialog(this, ex, "Failed to load question.");
        }
    }


    public void answerQuestion(Answer a){
        if(a.isCorrect()){
            //correct answer
            BattleActivity.AnswerQuestionTask async = new BattleActivity.AnswerQuestionTask(loggedUser.getImei(), selectedTeacher.getId());
            async.execute();
        }else{
            //incorrect
            AlertHandler.showAlertDialog(BattleActivity.this, "Wrong answer!",
                    selectedTeacher.getSurName() + " got away safely.");
        }
        returnToEncounterScreen();
    }

    //question/department/"name"
    private class GetQuestions extends AsyncTask<Void, Void, Question> {
        private final BattleActivity activity;

        public GetQuestions(BattleActivity activity)
        {
            this.activity = activity;
        }

        @Override
        protected Question doInBackground(Void... params) {
            return apiController.getQuestionsFromTeacher(selectedTeacher);
        }

        @Override
        protected void onPostExecute(Question question) {
            activity.loadQuestion(question);
        }
    }//user/'userID' post request met id teacher
    //userID, QuestionID, request naar addperson(teacherID, userID)
    private class AnswerQuestionTask extends AsyncTask<Void, Void, Void> {
        private final String userId;
        private final String teacherId;


        public AnswerQuestionTask(String userId, String teacherId)
        {
            this.userId = userId;
            this.teacherId = teacherId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                apiController.answerQuestion(userId, teacherId);
            }catch (Exception ex){
                AlertHandler.showErrorDialog(BattleActivity.this, ex, "Connection error", "Failed to sent your answer to the server");
            }
            return null;
        }
    }
}
