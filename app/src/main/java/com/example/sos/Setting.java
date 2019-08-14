package com.example.sos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Setting extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String fire_Service_number,ambulance_service_number,police_service_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        EditText  ambulance= (EditText)findViewById(R.id.ambulance_phone_number);
        ambulance_service_number = ambulance.getText().toString();

        EditText  police= (EditText)findViewById(R.id.police_phone_number);
        police_service_number = police.getText().toString();

        EditText  fire= (EditText)findViewById(R.id.fire_phone_number);
       fire_Service_number = fire.getText().toString();

        BottomNavigationView topView = findViewById(R.id.nav_view_top) ;
        topView.setOnNavigationItemSelectedListener(this);

        topView.getMenu() .getItem(2).setChecked(true);



    }



    public void openMainActivity()
    {  Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("fire",fire_Service_number);
        intent.putExtra("ambulance",ambulance_service_number);
        intent.putExtra("police",police_service_number);
        startActivity(intent);
    }
    public void openContect()
    {  Intent intent = new Intent(this,contacts.class);
        startActivity(intent);
    }
    public void openRescueFragment()
    {  Intent intent = new Intent(this,rescuefragment.class);
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
                openContect();
                break;


        }
        return true;
    }
}
