package docentengo.fontys.nl.docentengo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import Business.RankingEntry;
import Business.User;
import api.ApiController;

public class RankingActivity extends AppCompatActivity {
    private User signedUser;
    private ListView lvRankings;
    private ApiController apiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        retrieveUser();
        createReturnDexButtonEvent();
        this.apiController = new ApiController();
        lvRankings = (ListView) findViewById(R.id.lvRankings);

        RankingActivity.FillRankingList fillRankingList = new RankingActivity.FillRankingList(this);
        fillRankingList.execute();
    }

    public void setAdapter(List<RankingEntry> rankings)
    {
        Collections.sort(rankings);
        lvRankings = (ListView)findViewById(R.id.lvRankings);
        ArrayAdapter<RankingEntry> adapter = new ArrayAdapter<>(this
                ,android.R.layout.simple_list_item_1
                ,android.R.id.text1
                ,rankings);
        lvRankings.setAdapter(adapter);
    }

    private void retrieveUser(){
        if(getIntent().hasExtra("CurrentUser")){
            signedUser = (User)getIntent().getExtras().getSerializable("CurrentUser");
        }else if(!getIntent().hasExtra("CurrentUser")){
            AlertHandler.showAlertDialog(this,
                    "No user",
                    "There was no logged-in User found.");
        }
    }

    private void createReturnDexButtonEvent(){
        Button returnButton = (Button)findViewById(R.id.btnReturnDex);
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TeacherDexActivity.class);
                intent.putExtra("CurrentUser", signedUser);
                startActivity(intent);
                finish();
            }
        });
    }

    private class FillRankingList extends AsyncTask<Void, Void, List<RankingEntry>> {

        private final RankingActivity activity;

        public FillRankingList(RankingActivity activity)
        {
            this.activity = activity;
            System.out.println("created Async");
        }

        @Override
        protected List<RankingEntry> doInBackground(Void... params) {
            try
            {
                return apiController.getRanking();
            }
            catch(Exception ex)
            {
                AlertHandler.showErrorDialog(RankingActivity.this,
                        ex,
                        "Server connection error",
                        "The application was unable to connect to the server");
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RankingEntry> highscore)
        {
            activity.setAdapter(highscore);
        }
    }
}
