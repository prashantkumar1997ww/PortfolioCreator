package com.example.portfoliocreator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    //Progress Bar
    ProgressDialog progressDialog;

    Button btn_login;
    EditText loginUsername,loginPassword;
    TextView txtRegister;


    //  SharedPreferences
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Progress Bar
                progressDialog = new ProgressDialog(LoginPage.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final String[] sToken = new String[1];
                String sUsername    = loginUsername.getText().toString();
                String sPassword = loginPassword.getText().toString();

                if(sUsername.isEmpty() && sPassword.isEmpty())
                {

                    Toast.makeText(LoginPage.this,"Enter Data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


                else if(sUsername.isEmpty())
                {

                    Toast.makeText(LoginPage.this,"Enter Username", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(sPassword.isEmpty())
                {

                    Toast.makeText(LoginPage.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {

                    RequestQueue queue = Volley.newRequestQueue(LoginPage.this);
                    String url ="https://ad1f06dcf937.ngrok.io/loginUser";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String[] msg = response.split("\"");
                                    String msg1 = msg[3];
                                    sToken[0] = msg[7];
                                    String sName = msg[11];

                                    Log.d("TAG0", response);
                                    Log.d("TAG", sName);
                                    if(msg1.equals("success"))
                                    {
                                        sharedPreferences =  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(KEY_USERNAME,sUsername);
                                        editor.putString(KEY_NAME,sName);
                                        editor.putString(KEY_PASSWORD,sPassword);
                                        editor.putString(KEY_TOKEN,sToken[0]);
                                        editor.apply();

                                        Toast.makeText(LoginPage.this,msg1,Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(LoginPage.this,HomePage.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Log.d("TAG", msg1);
                                        Toast.makeText(LoginPage.this,msg1,Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(LoginPage.this,LoginPage.class);
                                        startActivity(intent);
                                    }
                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("TAG", "onResponse: " + error.toString());
                                }
                            }
                    ){
                        @Override
                        protected Map<String, String> getParams()  {
                            Map<String,String> map = new HashMap<String,String>();
                            map.put("username",sUsername);
                            map.put("password",sPassword);

                            return map;
                        }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(stringRequest);
                }

            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginPage.this,RegisterPage.class);
                startActivity(intent);
            }
        });

    }
}