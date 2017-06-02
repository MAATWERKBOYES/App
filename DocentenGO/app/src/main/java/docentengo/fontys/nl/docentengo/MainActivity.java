package docentengo.fontys.nl.docentengo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submitButton = (Button)findViewById(R.id.btnSaveName);
        EditText inputField = (EditText)findViewById(R.id.txtInput);
        //determine whether or not it's the app booting up, it can also be redirecting to the enter teacher code screen
        if(!getIntent().hasExtra("BattleMode")){
            initiateHomeScreen(submitButton, inputField);
        }else if(getIntent().hasExtra("BattleMode")){
            initiateBattleScreen(submitButton, inputField);
        }
    }

    //#ToDo Jeroen hier zorgen dat je als je al eens bent ingelogt gelijk doorgaat, in this case no input required
    private void initiateHomeScreen(Button submitButton, final EditText inputField){
        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(testInput(inputField.getText().toString())){
                    OpenTeacherDex(inputField.getText().toString());
                }else{
                    showAlertDialog("Missing input", "please enter a username");
                }
            }
        });
    }

    private void OpenTeacherDex(String userName){
        Intent intent = new Intent(getApplicationContext(),TeacherDexActivity.class);
        intent.putExtra("UserName", userName);
        startActivity(intent);
        finish();
    }

    private void initiateBattleScreen(Button submitButton, final EditText inputField){
        submitButton.setText("Battle");
        inputField.setText("Enter the code of the teacher you found");
        TextView textView = (TextView)findViewById(R.id.tvEnterView);

        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!testInput(inputField.getText().toString())){
                    showAlertDialog("Missing input", "please enter the Teacher code.");
                }else if(1==2){
                    //#ToDo contact server and check if the teacher code exists
                    showAlertDialog("Invallid input", "The entered teacher code was not vallid.");
                }else{
                    OpenBattleScreen(inputField.getText().toString());
                }
            }
        });

    }

    private void OpenBattleScreen(String teacherCode){
        Intent intent = new Intent(getApplicationContext(),BattleActivity.class);
        intent.putExtra("UserName", (String)getIntent().getExtras().getSerializable("BattleMode"));
        intent.putExtra("TeacherCode", teacherCode);
        startActivity(intent);
        finish();
    }

    private boolean testInput(String stringToTest){
        if(stringToTest.equals(null) || "".equals(stringToTest)){
            return false;
        }
        return true;
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
