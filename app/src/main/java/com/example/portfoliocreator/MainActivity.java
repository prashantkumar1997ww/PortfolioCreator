package com.example.portfoliocreator;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //Progress Bar
    ProgressDialog progressDialog;

    // Image picker
    ImageView img;
    Button btn_add_image;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    String image2;

    //main activity
    EditText et,lt,et1,lt1;
    Button addbt,addbt1;
    ListView lv,lv1;

    EditText about, clgName, clgDegree, clgYear, school10Name, board10, year10, school12Name, board12, year12;
    EditText git, fb, linkedIn;

    Button create;



    String[] ListElements = new String[] { };
    //String[] ListDesc = new String[] {};
    String[] ListElements1 = new String[] { };
    //String[] ListDesc1 = new String[] {};


    //Shared Preference
    TextView emailId,password;
    TextView token;
    Button logout;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        about = (EditText) findViewById(R.id.edt_about);

        //Clg Details
        clgName = (EditText) findViewById(R.id.edt_clgName);
        clgDegree = (EditText) findViewById(R.id.edt_clgDegree);
        clgYear = (EditText) findViewById(R.id.edt_clgYear);

        // 10th School Details
        school10Name = (EditText) findViewById(R.id.edt_10SchoolName);
        board10 = (EditText) findViewById(R.id.edt_10Board);
        year10 = (EditText) findViewById(R.id.edt_10Year);

        // 12th School Details
        school12Name = (EditText) findViewById(R.id.edt_12SchoolName);
        board12 = (EditText) findViewById(R.id.edt_12Board);
        year12 = (EditText) findViewById(R.id.edt_12Year);

        git = (EditText) findViewById(R.id.edt_git);
        fb = (EditText) findViewById(R.id.edt_fb);
        linkedIn = (EditText) findViewById(R.id.edt_linkedIn);

        create = (Button) findViewById(R.id.btn_create);


        //Shared Preference
        emailId = (TextView) findViewById(R.id.txt_emailId);
        password = (TextView) findViewById(R.id.txt_password);
        token = (TextView) findViewById(R.id.txt_token);
        logout = (Button) findViewById(R.id.btn_logout);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        final String sEmail = sharedPreferences.getString(KEY_EMAIL,null);
        final String sPassword = sharedPreferences.getString(KEY_PASSWORD,null);

        if(sEmail != null || sPassword != null)
        {
            emailId.setText("Email ID = "+sEmail);
            password.setText("Password = "+sPassword);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Toast.makeText(MainActivity.this,"LogOut Successfully",Toast.LENGTH_SHORT).show();
            }
        });


        // image picker
        img = (ImageView) findViewById(R.id.img_view);
        btn_add_image = (Button) findViewById(R.id.add_img);

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Progress Bar
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                Log.d("TAG", "imgStr : "+ image2);

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://27b143a40c5b.ngrok.io/storeresume";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                String[] sToken = response.split(" ");
                                token.setText("Token :- "+sToken);
//                                for (String a : sToken)
//                                    System.out.println(a);

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
                        map.put("image",image2);
                        map.put("about",about.getText().toString());
                        map.put("collegename",clgName.getText().toString());
                        map.put("collegeaddr",clgDegree.getText().toString());
                        map.put("collegemarks",clgYear.getText().toString());

                        map.put("twelthname",school12Name.getText().toString());
                        map.put("twelthaddr",board12.getText().toString());
                        map.put("twelthmarks",year12.getText().toString());

                        map.put("tenthname",school10Name.getText().toString());
                        map.put("tenthaddr",board10.getText().toString());
                        map.put("tenthmarks",year10.getText().toString());

                        Map<String,String> nestmap = new HashMap<String, String>();
                        nestmap.put("github",git.getText().toString());
                        nestmap.put("facebook",fb.getText().toString());
                        nestmap.put("linkedin",linkedIn.getText().toString());

//                        map.put("links",nestmap);


//                                about,
//                                skillname,skilldesc,
//                                tenthname,tenthaddr,tenthmarks,
//                                twelthname,twelthaddr,twelthmarks,
//                                collegename,collegeaddr,collegemarks,
//                                links


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
            }

        });



        // Technical Skills
        et = (EditText) findViewById(R.id.ed_text);
        lt = (EditText) findViewById(R.id.ed_level);
        addbt = (Button) findViewById(R.id.btn_addData);
        lv = (ListView) findViewById(R.id.listView_lv);



        final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (MainActivity.this, android.R.layout.simple_list_item_1, ListElementsArrayList);
        lv.setAdapter(adapter);


        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListElementsArrayList.add(et.getText().toString()+ "\n" +lt.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });




        // Work Experiences
        et1 = (EditText) findViewById(R.id.ed_text1);
        lt1 = (EditText) findViewById(R.id.ed_level1);
        addbt1 = (Button) findViewById(R.id.btn_addData1);
        lv1 = (ListView) findViewById(R.id.listView_lv1);

        final List<String> ListElementsArrayList1 = new ArrayList<>(Arrays.asList(ListElements1));
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<>
                (MainActivity.this, android.R.layout.simple_list_item_1, ListElementsArrayList1);
        lv1.setAdapter(adapter1);

        addbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListElementsArrayList1.add(et1.getText().toString());
                ListElementsArrayList1.add(lt1.getText().toString());
                adapter1.notifyDataSetChanged();
            }
        });


    }

//    @Override
//    public void onBackPressed() {
//        progressDialog.dismiss();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }



    public void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            img.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Bitmap lsBitmap = null;
                lsBitmap = bitmap;
                image2 = getStringImage(lsBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
