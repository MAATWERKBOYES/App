package docentengo.fontys.nl.docentengo;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import Business.PersonEntry;
import Business.User;
import api.ApiController;

public class TeacherDexActivity extends AppCompatActivity {
    private User signedUser;
    private ListView lvTeacherDex;
    private ApiController apiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dex);
        lvTeacherDex = (ListView)findViewById(R.id.lvTeacherDex);
        System.out.println("Loaded page");
        retrieveUser();
        System.out.println("got the user");
        setPersonalDexName();
        System.out.println("set the dex name");

        apiController = new ApiController();
        GetUpdatedUser updateUser = new GetUpdatedUser();
        updateUser.execute();

        createEnterCodeButtonEvent();
        createRankingsButtonEvent();
    }

    public void setAdapter(List<PersonEntry> teacherList)
    {
        PictureListAdapter adapter = new
                PictureListAdapter(TeacherDexActivity.this, teacherList);
        lvTeacherDex.setAdapter(adapter);
        lvTeacherDex.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PersonEntry entry = (PersonEntry) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(),TeacherInfoActivity.class);
                intent.putExtra("selectedTeacher",entry);
                intent.putExtra("CurrentUser", signedUser);
                startActivity(intent);
                finish();
            }
        });
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();
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

    private void setPersonalDexName(){
        TextView devName = (TextView)findViewById(R.id.tvDexName);
        String username = signedUser.getName();
        if("sS".indexOf(username.substring(username.length() - 1)) > -1 ){
            devName.setText(signedUser.getName() + "' Dex");
        }else{
            devName.setText(signedUser.getName() + "'s Dex");
        }
    }

    private void createEnterCodeButtonEvent(){
        Button navigateBattleCodeScreen = (Button)findViewById(R.id.btnCode);
        navigateBattleCodeScreen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EncounterActivity.class);
                intent.putExtra("CurrentUser", signedUser);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createRankingsButtonEvent(){
        Button btnNavigateRanking = (Button)findViewById(R.id.btnRanking);
        btnNavigateRanking.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RankingActivity.class);
                intent.putExtra("CurrentUser", signedUser);
                startActivity(intent);
                finish();
            }
        });
    }

    private class GetUpdatedUser extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            try {
                return apiController.loginUser(signedUser.getImei());
            } catch (HttpClientErrorException ex) {
                AlertHandler.showErrorDialog(TeacherDexActivity.this,
                        ex,
                        "Server connection error",
                        "The application was unable to connect to the server.");
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                signedUser = user;
                setAdapter(signedUser.getTeachers());
            }
        }
    }
}
