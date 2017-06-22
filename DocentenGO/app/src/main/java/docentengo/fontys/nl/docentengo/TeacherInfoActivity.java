package docentengo.fontys.nl.docentengo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.springframework.web.client.RestTemplate;

import Business.PersonEntry;
import api.APIConnection;
import api.DownloadImageTask;
import Business.Person;
import Business.User;

public class TeacherInfoActivity extends AppCompatActivity {

    RestTemplate client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        createButtonReturnDexEvent();
        loadTeacherIntoGUI();
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

    private void loadTeacherIntoGUI(){
        if(getIntent().hasExtra("selectedTeacher")){
            PersonEntry person = (PersonEntry)getIntent().getExtras().getSerializable("selectedTeacher");
            TextView name = (TextView)findViewById(R.id.txtTeacherName);
            TextView occupation = (TextView)findViewById(R.id.txtTeacherOccupation);
            TextView level = (TextView)findViewById(R.id.txtTeacherLevel);
            TextView present = (TextView)findViewById(R.id.txtTeacherPresent);
            ImageView picture = (ImageView) findViewById(R.id.imgTeacher);

            if(person.getTeacher().getPhoto() != null){
                System.out.println(person.getTeacher().getPhoto());
                new DownloadImageTask((ImageView) findViewById(R.id.imgTeacher))
                        .execute(person.getTeacher().getPhoto()+"?access_token="+APIConnection.getPictureToken());
            }else{
                new DownloadImageTask(picture)
                        .execute("https://api.fhict.nl/pictures/i870092.jpg?access_token="+APIConnection.getPictureToken());
            }
            name.setText(person.getTeacher().getDisplayName());
            occupation.setText(person.getTeacher().getDepartment());
            level.setText(Integer.toString(person.getLevel()));
            if(person.getTeacher().getPresent()){
                present.setText(person.getTeacher().getGivenName() + " is roaming the Fontys");
            }else{
                present.setText(person.getTeacher().getGivenName() + " not present anymore");
            }
        }else if(!getIntent().hasExtra("CurrentUser")){
            AlertHandler.showAlertDialog(
                    this,
                    "No teacher",
                    "The screen was loaded without there being a selected teacher");
        }
    }

}
