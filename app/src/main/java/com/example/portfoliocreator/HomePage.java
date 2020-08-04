package com.example.portfoliocreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    CardView create,profile,edit;


    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        create = (CardView) findViewById(R.id.crd_create);
        profile = (CardView) findViewById(R.id.crd_profile);
        edit = (CardView) findViewById(R.id.crd_edit);



        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String sUsername = sharedPreferences.getString(KEY_USERNAME,null);
        final String sPassword = sharedPreferences.getString(KEY_PASSWORD,null);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sUsername != null && sPassword != null)
                {
                    Intent intent = new Intent(HomePage.this,MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(HomePage.this,"First Login", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(sUsername != null && sPassword != null)
//                {
//                    Intent intent = new Intent(HomePage.this,MainActivity.class);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Toast.makeText(HomePage.this,"First Register and Create", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,LoginPage.class);
                startActivity(intent);
            }
        });
    }

}
