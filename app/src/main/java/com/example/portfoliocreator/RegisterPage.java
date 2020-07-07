package com.example.portfoliocreator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {

    EditText email,password;
    Button btn_save;


    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        email = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_password);
        btn_save = (Button) findViewById(R.id.btn_save);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("TAG!", "check: ");
                String sEmail    = email.getText().toString();
                String sPassword = password.getText().toString();

                if(sEmail.isEmpty() && sPassword.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Data", Toast.LENGTH_SHORT).show();
                }

                else if(sEmail.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(sPassword.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sharedPreferences =  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_EMAIL,sEmail);
                    editor.putString(KEY_PASSWORD,sPassword);
                    editor.apply();
                    Intent intent = new Intent(RegisterPage.this,HomePage.class);
                    startActivity(intent);
                    Toast.makeText(RegisterPage.this,"Login Successful", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
