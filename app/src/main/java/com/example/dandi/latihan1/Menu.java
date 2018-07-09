package com.example.dandi.latihan1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        gpsTracker = new GPSTracker(Menu.this);

        Button per = (Button)findViewById(R.id.perum);
        Button tel = (Button)findViewById(R.id.telkom);
        Button kam = (Button)findViewById(R.id.kampus);
        Button mylok = (Button)findViewById(R.id.mylocation);


        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, MapsActivityTelkom.class);
                startActivity(i);
            }
        });

        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, MapsActivityPerum.class);
                startActivity(i);
            }
        });

        kam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, MapsActivityKampus.class);
                startActivity(i);
            }
        });

        mylok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*Intent i = new Intent(Menu.this, MapsActivityMyloc.class);
                startActivity(i);*/

//                String lat = String.valueOf(gpsTracker.getLatitude());
//                String longi = String.valueOf(gpsTracker.getLongitude());
//                if (lat==null){
//                   Toast.makeText(Menu.this, "likasi tidak ditemukan",Toast.LENGTH_LONG).show();
//                }else
//                Toast.makeText(Menu.this, "Latitude = "+lat+"\n Longitude= "+longi,Toast.LENGTH_LONG).show();
            }
        });
    }
}
