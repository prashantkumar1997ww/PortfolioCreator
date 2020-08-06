package com.example.portfoliocreator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

    //Progress Bar
    ProgressDialog progressDialog;

    EditText username,password,name;
    Button btn_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);


        name = (EditText) findViewById(R.id.edt_name);
        username = (EditText) findViewById(R.id.edt_username);
        password = (EditText) findViewById(R.id.edt_password);
        btn_save = (Button) findViewById(R.id.btn_save);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Progress Bar
                progressDialog = new ProgressDialog(RegisterPage.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );


                String sName    = name.getText().toString();
                String sUsername    = username.getText().toString();
                String sPassword = password.getText().toString();


                if(sUsername.isEmpty() && sPassword.isEmpty() && sName.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                else if(sName.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Name", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                else if(sUsername.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Username", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(sPassword.isEmpty())
                {

                    Toast.makeText(RegisterPage.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {

                    RequestQueue queue = Volley.newRequestQueue(RegisterPage.this);
                    String url ="https://portfolio-v0.herokuapp.com/registerUser";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String[] msg = response.split("\"");
                                    String msg1 = msg[3];
                                    Log.d("TAG0", response);
                                    Log.d("TAG", msg1);
                                    if(msg1.equals("success"))
                                    {

                                        progressDialog.dismiss();
                                        Intent intent = new Intent(RegisterPage.this,HomePage.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterPage.this,msg1,Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
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
                            map.put("name",sName);
                            map.put("username",sUsername);
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
//                    sharedPreferences =  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(KEY_EMAIL,sEmail);
//                    editor.putString(KEY_PASSWORD,sPassword);
//                    editor.putString(KEY_TOKEN, sToken[0]);
//                   // Log.d("Tag","token "+sToken[0]);
//                    editor.apply();

//                    Intent intent = new Intent(RegisterPage.this,HomePage.class);
//                    startActivity(intent);
                    //Toast.makeText(RegisterPage.this,"Login Successful", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
