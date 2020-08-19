package com.example.portfoliocreator;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    EditText about;
    EditText clgName, clgDegree, clgYear, clgLocation, clgMarks, clgMarksType;
    EditText school10Name, board10, year10, Location10, Marks10, MarksType10;
    EditText school12Name, board12, year12, Location12, Marks12, MarksType12;
    EditText git, fb, linkedIn;

    Button create;



    String[] ListElements = new String[] { };
    String[] ListElements1 = new String[] { };

    //Shared Preference
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_SITELINK = "sitelink";
    private static final String KEY_TOKEN = "token";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        JSONObject expJson = new JSONObject();
        JSONObject skillJson = new JSONObject();

        about = (EditText) findViewById(R.id.edt_about);

        //Clg Details
        clgName = findViewById(R.id.edt_clgName);
        clgDegree = findViewById(R.id.edt_clgDegree);
        clgYear = findViewById(R.id.edt_clgYear);
        clgLocation = findViewById(R.id.edt_clgLocation);
        clgMarks = findViewById(R.id.edt_clgMarks);
        clgMarksType = findViewById(R.id.edt_clgMarks_type);

        // 10th School Details
        school10Name = findViewById(R.id.edt_10SchoolName);
        board10 = findViewById(R.id.edt_10Board);
        year10 = findViewById(R.id.edt_10Year);
        Location10 = findViewById(R.id.edt_10Location);
        Marks10 = findViewById(R.id.edt_10Marks);
        MarksType10 = findViewById(R.id.edt_10Marks_type);

        // 12th School Details
        school12Name = findViewById(R.id.edt_12SchoolName);
        board12 = findViewById(R.id.edt_12Board);
        year12 = findViewById(R.id.edt_12Year);
        Location12 = findViewById(R.id.edt_12Location);
        Marks12 = findViewById(R.id.edt_12Marks);
        MarksType12 = findViewById(R.id.edt_12Marks_type);

        git = findViewById(R.id.edt_git);
        fb = findViewById(R.id.edt_fb);
        linkedIn = findViewById(R.id.edt_linkedIn);

        create = findViewById(R.id.btn_create);


        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        final String sToken = sharedPreferences.getString(KEY_TOKEN,null);
        Log.d("Tag","token "+sToken);



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

                JSONObject mainJson  = new JSONObject();

                try {
                    mainJson.put("image",image2);
                    mainJson.put("about",about.getText().toString());

                    mainJson.put("skills",skillJson);
                    mainJson.put("experience",expJson);
                    mainJson.put("x-auth-token", sToken);

                    JSONObject college = new JSONObject();
                    college.put("name",clgName.getText().toString());
                    college.put("address",clgDegree.getText().toString());
                    college.put("marks",clgYear.getText().toString());
                    mainJson.put("college",college);

                    JSONObject school12 = new JSONObject();
                    school12.put("name",school12Name.getText().toString());
                    school12.put("address",board12.getText().toString());
                    school12.put("marks",year12.getText().toString());
                    mainJson.put("twelth",school12);


                    JSONObject school10 = new JSONObject();
                    school10.put("name",school10Name.getText().toString());
                    school10.put("address",board10.getText().toString());
                    school10.put("marks",year10.getText().toString());
                    mainJson.put("tenth",school10);

                    JSONObject link = new JSONObject();
                    link.put("github",git.getText().toString());
                    link.put("facebook",fb.getText().toString());
                    link.put("linkedin",linkedIn.getText().toString());
                    mainJson.put("links",link);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://portfolio-v0.herokuapp.com/storeresume";

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, mainJson,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String msg = null;
                        try {
                            msg = (String) response.get("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(msg.equals("success"))
                        {
                            String sitelink = null;
                            try {
                                sitelink = (String) response.get("sitelink");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivity.this,sitelink, Toast.LENGTH_SHORT).show();
                            //link.setText("Link :- "+sitelink);

                            sharedPreferences =  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_SITELINK,sitelink);
                            editor.apply();
                        }
                        else{
                            Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        Log.d("TAG",response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG",error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                queue.add(jsonRequest);

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
                try {
                    skillJson.put(et.getText().toString(),lt.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                try {
                    expJson.put(et1.getText().toString(),lt1.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListElementsArrayList1.add(et1.getText().toString()+ "\n" +lt1.getText().toString());
                adapter1.notifyDataSetChanged();
            }
        });


    }

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
