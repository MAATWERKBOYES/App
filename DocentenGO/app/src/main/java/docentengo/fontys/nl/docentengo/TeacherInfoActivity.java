package docentengo.fontys.nl.docentengo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.springframework.web.client.RestTemplate;

import api.APIConnection;
import api.DownloadImageTask;
import business.Person;
import business.User;

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
            Person person = (Person)getIntent().getExtras().getSerializable("selectedTeacher");
            TextView name = (TextView)findViewById(R.id.txtTeacherName);
            TextView occupation = (TextView)findViewById(R.id.txtTeacherOccupation);
            TextView title = (TextView)findViewById(R.id.txtTeacherTitle);
            TextView present = (TextView)findViewById(R.id.txtTeacherPresent);
            ImageView picture = (ImageView) findViewById(R.id.imgTeacher);

            if(person.getPhoto() != null){
                System.out.println(person.getPhoto());
                new DownloadImageTask((ImageView) findViewById(R.id.imgTeacher))
                        .execute(person.getPhoto()+"?access_token="+APIConnection.getPictureToken());
            }else{
                new DownloadImageTask(picture)
                        .execute("https://api.fhict.nl/pictures/i870092.jpg?access_token="+APIConnection.getPictureToken());
            }
            name.setText(person.getDisplayName());
            occupation.setText(person.getDepartment());
            if(person.getPersonalTitle() != null){
                title.setText(person.getPersonalTitle().toString());
            }
            if(person.getPresent()){
                present.setText("A wild " + person.getSurName() + " appeared...");
            }else{
                present.setText(person.getSurName() + " got away safely");
            }
        }else if(!getIntent().hasExtra("CurrentUser")){
            showAlertDialog("No teacher", "The screen was loaded without there being a selected teacher");
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


}
