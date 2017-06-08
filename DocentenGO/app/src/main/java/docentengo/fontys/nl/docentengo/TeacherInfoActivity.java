package docentengo.fontys.nl.docentengo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import Business.User;

public class TeacherInfoActivity extends AppCompatActivity {

    private final String URL = "http://145.93.96.177:8080/";
    RestTemplate client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        createButtonReturnDexEvent();

        this.client = new RestTemplate();
        client.getMessageConverters().add(new StringHttpMessageConverter());
        TeacherInfoActivity.Async async = new TeacherInfoActivity.Async();
        async.execute();
    }

    private void createButtonReturnDexEvent(){
        Button returnButton = (Button)findViewById(R.id.btnReturnDex);
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TeacherDexActivity.class);
                intent.putExtra("CurrentUser", (User)getIntent().getExtras().getSerializable("CurrentUser"));
                startActivity(intent);
                finish();
            }
        });
    }

    private class Async extends AsyncTask<Void, Void, List<Person>> {
        @Override
        protected List<Person> doInBackground(Void... params) {
            //TODO put to textboxes (Future)
            System.out.println("wat.");
            List<Person> temp = Arrays.asList(client.getForObject(URL + "People", Person[].class));
            //TODO Select correct question
            return temp;
        }
    }
}
