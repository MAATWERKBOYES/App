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
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import Business.APIConnection;
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

    private void loadTeacherIntoGUI(){
        if(getIntent().hasExtra("selectedTeacher")){
            Person person = (Person)getIntent().getExtras().getSerializable("selectedTeacher");
            ImageView teacherPicture = (ImageView)findViewById(R.id.imgTeacher);
            TextView name = (TextView)findViewById(R.id.txtTeacherName);
            TextView occupation = (TextView)findViewById(R.id.txtTeacherOccupation);
            TextView title = (TextView)findViewById(R.id.txtTeacherTitle);
            TextView present = (TextView)findViewById(R.id.txtTeacherPresent);

            //#ToDo variable image not hardcoded
            teacherPicture.setBackgroundResource(R.drawable.luuk);
            name.setText(person.getDisplayName());
            occupation.setText(person.getDepartment());
            title.setText(person.getPersonalTitle().toString());
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

    private class Async extends AsyncTask<Void, Void, List<Person>> {
        @Override
        protected List<Person> doInBackground(Void... params) {
            //TODO put to textboxes (Future)
            System.out.println("wat.");
            List<Person> temp = Arrays.asList(client.getForObject(APIConnection.getAPIConnectionInformationURL() + "People", Person[].class));
            //TODO Select correct question
            return temp;
        }

        @Override
        protected void onPostExecute(List<Person> persons) {
            super.onPostExecute(persons);
        }
    }
}
