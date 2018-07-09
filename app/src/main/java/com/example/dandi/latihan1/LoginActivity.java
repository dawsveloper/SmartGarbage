package com.example.dandi.latihan1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                // sebuah perintah
                Toast.makeText(getBaseContext(),"Smart Garbage",Toast.LENGTH_LONG).show();
                Intent bukaMainActivity = new Intent(this,MainActivity.class);
                startActivity(bukaMainActivity);
                break;
        }
    }
}
