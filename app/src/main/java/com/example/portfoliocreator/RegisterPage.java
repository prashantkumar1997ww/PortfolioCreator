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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    EditText email,password,name;
    Button btn_save;


    SharedPreferences sharedPreferences;
//    SharedPreferences sharedPreferences1;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        name = (EditText) findViewById(R.id.edt_name);
        email = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_password);
        btn_save = (Button) findViewById(R.id.btn_save);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("TAG!", "check: ");
                String sName    = name.getText().toString();
                String sEmail    = email.getText().toString();
                String sPassword = password.getText().toString();

                if(sEmail.isEmpty() && sPassword.isEmpty() && sName.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Data", Toast.LENGTH_SHORT).show();
                }

                else if(sName.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Name", Toast.LENGTH_SHORT).show();
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

                    RequestQueue queue = Volley.newRequestQueue(RegisterPage.this);
                    String url ="https://portfolio-v0.herokuapp.com/storeuser";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //String res = "{\"msg\":\"User already exists\"}";
                                    //String res = "{\"msg\":\"success\",\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoicGthMTIzQGdtYWlsLmNvbSIsIm5hbWUiOiJWaWNreSIsInR5cGUiOiJjdXN0b21lciJ9LCJpYXQiOjE1OTQzNzYyNjYsImV4cCI6MTU5NDczNjI2Nn0.W9E16QUlDfmeZT-bqII-BwYQWkDbOTZicL7Vo85xQ-0\"}";
                                    String[] msg = response.split("\"");
                                    String msg1 = msg[3];
                                    if(msg1 == "User already exists")
                                    {
                                        Toast.makeText(RegisterPage.this,msg1,Toast.LENGTH_SHORT).show();
                                    }
                                    if(msg1 == "success")
                                    {
                                        String msg2 = msg[3]+ "\n" +msg[7];
                                        Toast.makeText(RegisterPage.this,msg2,Toast.LENGTH_SHORT).show();
                                    }
//                                    String msg2 = msg[3]+ "\n" +msg[7];
//                                    Toast.makeText(RegisterPage.this,msg2,Toast.LENGTH_SHORT).show();

                                    Log.d("TAG", "onResponse: " + response);
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
                            map.put("name",sName);
                            map.put("username",sEmail);
                            map.put("password",sPassword);


                            return map;
                        }

//                    @Override
//                    public String getBodyContentType() {
//                        return "multipart";
//                    }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(stringRequest);
                    sharedPreferences =  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_EMAIL,sEmail);
                    editor.putString(KEY_PASSWORD,sPassword);
                    editor.apply();
                    Intent intent = new Intent(RegisterPage.this,HomePage.class);
                    startActivity(intent);
                    //Toast.makeText(RegisterPage.this,"Login Successful", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
