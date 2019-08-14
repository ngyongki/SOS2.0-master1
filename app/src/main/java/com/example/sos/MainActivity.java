package com.example.sos;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {
    Dialog myDialog;
    Button helpButton;

    String fire_number,police_number,ambulance_number,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view );
        BottomNavigationView topView = findViewById(R.id.nav_view_top) ;

        navView.setOnNavigationItemSelectedListener(this);
        topView.setOnNavigationItemSelectedListener(this);
        myDialog = new Dialog(this );

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fire_number = extras.getString("fire");
            police_number = extras.getString("police");
            ambulance_number = extras.getString("ambulance");
            email=extras.getString("email");
        }

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.siren);
        helpButton = (Button) findViewById(R.id.helpButton);


        helpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //sendSMS();
                if (mp.isPlaying()) {
                    mp.pause();
                } else
                    mp.start();

            }

        });
    }

    private void sendSMS() {


        ArrayList<contacts> recipient = new ArrayList<contacts>();
        GPStracker g = new GPStracker(this.getApplicationContext());
        Location l = g.getLocation();
        if (l != null){
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            String message="http://maps.google.com/maps?saddr="+lat+","+lon;
            java.util.ArrayList<contacts> phoneNo = recipient;
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(String.valueOf(phoneNo), null, message, null,null);
        }
                /*Create the intent.
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                // Set the data for the intent as the phone number.
                smsIntent.setData(Uri.parse(phoneNo));
                // Add the message (sms) with the key ("sms_body").
                smsIntent.putExtra("sms_body", message);
                // If package resolves (target app installed), send intent.
                if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Log.e(TAG, "Can't resolve app for ACTION_SENDTO Intent");
                }*/
    }
    public void openMainActivity()
    {  Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void openContact()
    {  Intent intent = new Intent(this,contacts.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    public void openSetting()
    {  Intent intent = new Intent(this,Setting.class);
        startActivity(intent);
    }

    public void openRescueFragment()

    {  Intent intent = new Intent(this,rescuefragment.class);
        intent.putExtra("fire",fire_number);
        intent.putExtra("police",police_number);
        intent.putExtra("ambulance",fire_number);
        startActivity(intent);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.navigation_home :

                openMainActivity();
                break;
            case R.id.navigation_rescue:
                openRescueFragment();
                break;
            case R.id.navigation_contacts:
               openContact();
                break;
            case R.id.navigation_setting:
                openSetting();
                break;


        }
        return true;
}


}
