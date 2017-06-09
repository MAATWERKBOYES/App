package docentengo.fontys.nl.docentengo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import Business.ApiController;
import Business.Question;

public class BattleActivity extends AppCompatActivity {

private ApiController apiController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        apiController = new ApiController();
        GetQuestions getQuestions = new GetQuestions();
        getQuestions.execute();
    }



    private class GetQuestions extends AsyncTask<Void, Void, List<Question>> {
        @Override
        protected List<Question> doInBackground(Void... params) {
            //TODO put to textboxes (Future)
            //TODO Select correct question
            return apiController.getQuestions();
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            super.onPostExecute(questions);
        }
    }
}
