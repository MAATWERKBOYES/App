package docentengo.fontys.nl.docentengo;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.web.client.HttpClientErrorException;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.List;

import Business.PersonEntry;
import Business.User;
import api.ApiController;

public class TeacherDexActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "BeaconSearch";

    private User signedUser;
    private ListView lvTeacherDex;
    private ListView lvNearbyTeachers;
    private BeaconManager beaconManager;
    private boolean bound = false;

    private int ACCES_FINE_LOCATION_PERMISSION_CODE = 69;

    private ApiController apiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dex);
        lvTeacherDex = (ListView)findViewById(R.id.lvTeacherDex);

        lvTeacherDex = (ListView) findViewById(R.id.lvTeacherDex);

        //Check if device supports BLE
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //If not disable the nearby teacher textView, listView en button.
            lvNearbyTeachers = (ListView) findViewById(R.id.lvNearbyTeachers);
            lvNearbyTeachers.setVisibility(View.GONE);
            TextView tvNearbyTeachers = (TextView) findViewById(R.id.tvNearbyTeachers);
            tvNearbyTeachers.setVisibility(View.GONE);
            Button btnSearchNearbyTeachers = (Button) findViewById(R.id.btnSearchNearbyTeachers);
            btnSearchNearbyTeachers.setVisibility(View.GONE);
        } else {
            //If it does support BLE hide listview

            //Do not show listview yet because user must first press the button
            ListView lvNearbyTeachers = (ListView) findViewById(R.id.lvNearbyTeachers);
            lvNearbyTeachers.setVisibility(View.GONE);

            lvTeacherDex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PersonEntry entry = (PersonEntry) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(getApplicationContext(), TeacherInfoActivity.class);
                    intent.putExtra("selectedTeacher", entry);
                    intent.putExtra("CurrentUser", signedUser);
                    startActivity(intent);
                    finish();
                }
            });
        }


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
                Intent intent = new Intent(getApplicationContext(), TeacherInfoActivity.class);
                intent.putExtra("selectedTeacher", entry);
                intent.putExtra("CurrentUser", signedUser);
                startActivity(intent);
                finish();
            }
        });
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();
    }

    private void retrieveUser() {
        if (getIntent().hasExtra("CurrentUser")) {
            signedUser = (User) getIntent().getExtras().getSerializable("CurrentUser");
        } else if (!getIntent().hasExtra("CurrentUser")) {
            AlertHandler.showAlertDialog(this,
                    "No user",
                    "There was no logged-in User found.");
        }
    }

    private void setPersonalDexName() {
        TextView devName = (TextView) findViewById(R.id.tvDexName);
        String username = signedUser.getName();
        if ("sS".indexOf(username.substring(username.length() - 1)) > -1) {
            devName.setText(signedUser.getName() + "' Dex");
        } else {
            devName.setText(signedUser.getName() + "'s Dex");
        }
    }

    private void createEnterCodeButtonEvent() {
        Button navigateBattleCodeScreen = (Button) findViewById(R.id.btnCode);
        navigateBattleCodeScreen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EncounterActivity.class);
                intent.putExtra("CurrentUser", signedUser);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createRankingsButtonEvent() {
        Button btnNavigateRanking = (Button) findViewById(R.id.btnRanking);
        btnNavigateRanking.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
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
    //Not in on click listener made for button because of request permission cannot be called from there.
    public void onSearchClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCES_FINE_LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == ACCES_FINE_LOCATION_PERMISSION_CODE) {
            //If BLE permission is granted remove button, show listView and start search for Beacons.
            lvNearbyTeachers = (ListView) findViewById(R.id.lvNearbyTeachers);
            lvNearbyTeachers.setVisibility(View.VISIBLE);
            Button btnSearchNearbyTeachers = (Button) findViewById(R.id.btnSearchNearbyTeachers);
            btnSearchNearbyTeachers.setVisibility(View.GONE);

            BeaconManager.setAndroidLScanningDisabled(true);
            beaconManager = BeaconManager.getInstanceForApplication(this);//Sets auto beacon layout : m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25

            //Only use this if the beacon layout is different from default.
            beaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")); //Deze shit moet nog aangepast worden naar onze beacon settings
            beaconManager.bind(this);

            bound = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            beaconManager.unbind(this);
            bound = false;
        }

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "Beacon detected, UniqueID: " + region.getUniqueId());
                getTeachersFromBeacon(region.getUniqueId());
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see the beacon with UniqueID: " + region.getUniqueId());
            }

            //I think there is no use for this one yet.
            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, "exception", e);
        }
    }

    //Beetje prototype want kan nog niet docenten koppelen aan beacons.
    private void getTeachersFromBeacon(String uniqueId) {
        //Go to database and get teachers that are with the uniqueID
        Log.i("Kutzooi", "ALLE FATOES ZIJN LELIJKSDKBFDKJJFSDNGFXNGFNDGJNFDGNJI");
        //Use getTeachers from signedUser as a test because I have no other teachers.
        final List<PersonEntry> teacherList = signedUser.getTeachers();
        for (PersonEntry p : teacherList) {
            Log.i("Teacher", p.getTeacher().getGivenName());
        }

        this.runOnUiThread(new Runnable() {
            public void run() {
                ArrayAdapter<PersonEntry> adapter = new ArrayAdapter<>(TeacherDexActivity.this
                        , android.R.layout.simple_list_item_1
                        , android.R.id.text1
                        , teacherList);

                lvTeacherDex.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();
                Log.i("UIRunnable", "LIJST ISNV DGTRHYRMJHTKHNFLAAR FATOE");
            }
        });


    }
}
