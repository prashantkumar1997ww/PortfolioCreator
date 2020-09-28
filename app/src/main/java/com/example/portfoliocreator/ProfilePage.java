package com.example.portfoliocreator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilePage extends AppCompatActivity {

    //Shared Preference
    TextView username,name,link;
    Button logout;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SITELINK = "sitelink";
    //private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Shared Preference
        username = (TextView) findViewById(R.id.txt_username);
        name = (TextView) findViewById(R.id.txt_name);
        link = (TextView) findViewById(R.id.txt_link);
        logout = (Button) findViewById(R.id.btn_signout);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        final String sName = sharedPreferences.getString(KEY_NAME,null);
        final String sUsername = sharedPreferences.getString(KEY_USERNAME,null);
        final String sLink = sharedPreferences.getString(KEY_SITELINK,null);
        //final String sToken = sharedPreferences.getString(KEY_TOKEN,null);

        if(sUsername != null || sName != null || sLink != null)
        {
            username.setText("Username : "+sUsername);
            name.setText("Welcome,\n"+sName);
            link.setText("Site : "+sLink);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Toast.makeText(ProfilePage.this,"LogOut Successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilePage.this, HomePage.class);
                startActivity(intent);
            }
        });
    }
}