package docentengo.fontys.nl.docentengo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import Business.User;

public class TeacherDexActivity extends AppCompatActivity {
    private User signedUser;
    private final String URL = "http://145.93.96.177:8080/";
    RestTemplate client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dex);
        System.out.println("Loaded page");
        retrieveUser();
        System.out.println("got the user");
        setPersonalDexName();
        System.out.println("set the dex name");
        //#ToDo load the TearcherDex content(for this user)

        this.client = new RestTemplate();
        client.getMessageConverters().add(new StringHttpMessageConverter());
        TeacherDexActivity.Async async = new TeacherDexActivity.Async();
        async.execute();

        createEnterCodeButtonEvent();
        createRankingsButtonEvent();
    }

    private void retrieveUser(){
        if(getIntent().hasExtra("CurrentUser")){
            signedUser = (User)getIntent().getExtras().getSerializable("CurrentUser");
        }else if(!getIntent().hasExtra("CurrentUser")){
            showAlertDialog("No user", "There was no logged in User found.");
        }
    }

    private void setPersonalDexName(){
        TextView devName = (TextView)findViewById(R.id.tvDexName);
        String username = signedUser.getUserName();
        if("sS".indexOf(username.substring(username.length() - 1)) > -1 ){
            devName.setText(signedUser.getUserName() + "' Dex");
        }else{
            devName.setText(signedUser.getUserName() + "'s Dex");
        }
    }

    private void createEnterCodeButtonEvent(){
        Button navigateBattleCodeScreen = (Button)findViewById(R.id.btnCode);
        navigateBattleCodeScreen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("BattleMode", signedUser);
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
    private class Async extends AsyncTask<Void, Void, List<Person>> {
        @Override
        protected List<Person> doInBackground(Void... params) {
            //TODO put to textboxes (Future)
            System.out.println("wat.");
            List<Person> temp = Arrays.asList(client.getForObject(URL + "Person", Person[].class));
            //TODO Select correct question
            return temp;
        }
    }
}
