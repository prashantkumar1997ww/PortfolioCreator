package com.example.portfoliocreator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EditPortfolio extends AppCompatActivity {


    ImageButton delete_btn;

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

    Button edit;



    public String[] ListElements = new String[] { };
    public String[] ListElements1 = new String[] { };

    List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));
    ArrayAdapter<String> adapter;


    List<String> ListElementsArrayList1 = new ArrayList<>(Arrays.asList(ListElements1));
    ArrayAdapter<String> adapter1;



    //Shared Preference
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_SITELINK = "sitelink";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "token";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_portfolio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        JSONObject expJson = new JSONObject();
        JSONObject skillJson = new JSONObject();


        delete_btn = findViewById(R.id.del_btn);

        about = findViewById(R.id.edt_about);

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

        // link details
        git = findViewById(R.id.edt_git);
        fb = findViewById(R.id.edt_fb);
        linkedIn = findViewById(R.id.edt_linkedIn);

        edit = findViewById(R.id.btn_edit);


        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        final String[] sToken = {sharedPreferences.getString(KEY_TOKEN, null)};
        final String[] sUsername = {sharedPreferences.getString(KEY_USERNAME, null)};
        Log.d("Tag","token "+ sToken[0]);
        Log.d("Tag","Username "+ sUsername[0]);



        // image picker
        img = (ImageView) findViewById(R.id.img_view);
        btn_add_image = (Button) findViewById(R.id.add_img);

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

             // Delete resume
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(EditPortfolio.this);
                JSONObject delJson  = new JSONObject();
                try
                {
                    delJson.put("x-auth-token", sToken[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url3 ="https://portfolio-v0.herokuapp.com/api/delete_resume";

                JsonObjectRequest delRequest = new JsonObjectRequest(Request.Method.POST, url3, delJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                Log.d("TAG Del Response",response.toString());
                                String msg = null;
                                try {
                                    msg = (String) response.get("msg");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(msg.equals("success"))
                                {
                                    Toast.makeText(EditPortfolio.this,msg, Toast.LENGTH_SHORT).show();
                                    String sitelink = null;
                                    try {
                                        sitelink = (String) response.get("sitelink");
                                        sitelink = " ";

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //link.setText("Link :- "+sitelink);

                                    sharedPreferences =  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_SITELINK,sitelink);
                                    editor.apply();
                                    Intent intent = new Intent(EditPortfolio.this,HomePage.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(EditPortfolio.this,msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditPortfolio.this,HomePage.class);
                                    startActivity(intent);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG Error",error.toString());
                    }
                }){
                };

                delRequest.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        3,
                        3));

                queue.add(delRequest);

            }
        });


        // Get resume data from database
        RequestQueue queue = Volley.newRequestQueue(EditPortfolio.this);
        JSONObject getJson  = new JSONObject();
        try
        {
            getJson.put("x-auth-token", sToken[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url2 ="https://portfolio-v0.herokuapp.com/api/get_resume";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url2, getJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            JSONObject obj = (JSONObject) response.get("obj");
                            Log.d("TAG",response.get("obj").toString());

                            about.setText(obj.get("about").toString());
//                            Log.d("TAG",obj.get("about").toString());


                            JSONObject getskills = (JSONObject) obj.get("skills");
                            Iterator<String> keys = getskills.keys();
                            while(keys.hasNext())
                            {
                                String key = keys.next();
                                ListElementsArrayList.add(key + "\n\n" + getskills.get(key).toString());
                                adapter.notifyDataSetChanged();
//                                Log.d("TAG",key + " : " + getskills.get(key).toString());
                            }


                            JSONObject getexperience = (JSONObject) obj.get("experience");
                            Iterator<String> keys1 = getexperience.keys();
                            while(keys1.hasNext())
                            {
                                String key1 = keys1.next();
                                ListElementsArrayList1.add(key1 + "\n\n" + getexperience.get(key1).toString());
                                adapter1.notifyDataSetChanged();
//                                Log.d("TAG",key1 + " : " + getexperience.get(key1).toString());
                            }


                            JSONObject getcollege = (JSONObject) obj.get("college");
                            clgName.setText(getcollege.get("name").toString());
                            clgLocation.setText(getcollege.get("address").toString());
                            clgDegree.setText(getcollege.get("degree").toString());
                            clgMarks.setText(getcollege.get("marks").toString());
                            clgMarksType.setText(getcollege.get("metric").toString());
                            clgYear.setText(getcollege.get("year").toString());


                            JSONObject get12school = (JSONObject) obj.get("twelth");
                            school12Name.setText(get12school.get("name").toString());
                            board12.setText(get12school.get("degree").toString());
                            year12.setText(get12school.get("year").toString());
                            Location12.setText(get12school.get("address").toString());
                            Marks12.setText(get12school.get("marks").toString());
                            MarksType12.setText(get12school.get("metric").toString());



                            JSONObject get10school = (JSONObject) obj.get("tenth");
                            school10Name.setText(get10school.get("name").toString());
                            board10.setText(get10school.get("degree").toString());
                            year10.setText(get10school.get("year").toString());
                            Location10.setText(get10school.get("address").toString());
                            Marks10.setText(get10school.get("marks").toString());
                            MarksType10.setText(get10school.get("metric").toString());


                            JSONObject getlink = (JSONObject) obj.get("links");
                            git.setText(getlink.get("github").toString());
                            fb.setText(getlink.get("facebook").toString());
                            linkedIn.setText(getlink.get("linkedin").toString());

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG Error",error.toString());
            }
        }){
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
        };

        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                3,
                3));

        queue.add(getRequest);


        edit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                //Progress Bar
                progressDialog = new ProgressDialog(EditPortfolio.this);
                progressDialog.setMessage("Please wait while your portfolio is being created, it might take some time due to server downtime");
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource
                        (android.R.color.transparent);

                ListElementsArrayList.forEach((s) -> {
//                    Log.d("Tag",s);
                    String skill,desc;
                    skill = s.split("\n")[0];
                    desc = s.split("\n")[2];
                    try {
                        skillJson.put(skill,desc);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                ListElementsArrayList1.forEach((s) -> {
//                    Log.d("Tag",s);
                    String job_type,detail;
                    job_type = s.split("\n")[0];
                    detail = s.split("\n")[2];
                    try {
                        expJson.put(job_type,detail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                JSONObject mainJson  = new JSONObject();
                try {
                    mainJson.put("image",image2);
                    mainJson.put("about",about.getText().toString());

                    mainJson.put("skills",skillJson);


                    mainJson.put("experience",expJson);


                    mainJson.put("x-auth-token", sToken[0]);


                    JSONObject college = new JSONObject();
                    college.put("name",clgName.getText().toString());
                    college.put("address",clgLocation.getText().toString());
                    college.put("degree",clgDegree.getText().toString());
                    college.put("marks",clgMarks.getText().toString());
                    college.put("metric",clgMarksType.getText().toString());
                    college.put("year",clgYear.getText().toString());
                    mainJson.put("college",college);

                    JSONObject school12 = new JSONObject();
                    school12.put("name",school12Name.getText().toString());
                    school12.put("address",Location12.getText().toString());
                    school12.put("degree",board12.getText().toString());
                    school12.put("marks",Marks12.getText().toString());
                    school12.put("metric",MarksType12.getText().toString());
                    school12.put("year",year12.getText().toString());
                    mainJson.put("twelth",school12);


                    JSONObject school10 = new JSONObject();
                    school10.put("name",school10Name.getText().toString());
                    school10.put("address",Location10.getText().toString());
                    school10.put("degree",board10.getText().toString());
                    school10.put("marks",Marks10.getText().toString());
                    school10.put("metric",MarksType10.getText().toString());
                    school10.put("year",year10.getText().toString());
                    mainJson.put("tenth",school10);

                    JSONObject link = new JSONObject();
                    link.put("github",git.getText().toString());
                    link.put("facebook",fb.getText().toString());
                    link.put("linkedin",linkedIn.getText().toString());
                    mainJson.put("links",link);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url ="https://portfolio-v0.herokuapp.com/api/edit_resume";

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
                                    Toast.makeText(EditPortfolio.this,sitelink, Toast.LENGTH_SHORT).show();
                                    //link.setText("Link :- "+sitelink);
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(EditPortfolio.this,ProfilePage.class);
                                    startActivity(intent);
                                    sharedPreferences =  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_SITELINK,sitelink);
                                    editor.apply();
                                }
                                else{
                                    Toast.makeText(EditPortfolio.this,msg, Toast.LENGTH_SHORT).show();
                                }
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
                        3,
                        3));


                queue.add(jsonRequest);

            }
        });



        // Technical Skills
        et = (EditText) findViewById(R.id.ed_text);
        lt = (EditText) findViewById(R.id.ed_level);
        addbt = (Button) findViewById(R.id.btn_addData);
        lv = (ListView) findViewById(R.id.listView_lv);

        adapter= new ArrayAdapter<>
                (EditPortfolio.this, R.layout.activity_list_view,  R.id.title,ListElementsArrayList);


        lv.setAdapter(adapter);


        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    skillJson.put(et.getText().toString(),lt.getText().toString());
                    ListElementsArrayList.add(et.getText().toString()+ "\n\n" +lt.getText().toString());
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // Work Experiences
        et1 = (EditText) findViewById(R.id.ed_text1);
        lt1 = (EditText) findViewById(R.id.ed_level1);
        addbt1 = (Button) findViewById(R.id.btn_addData1);
        lv1 = (ListView) findViewById(R.id.listView_lv1);

        adapter1= new ArrayAdapter<>
                (EditPortfolio.this, R.layout.activity_list_view,  R.id.title,ListElementsArrayList1);



        lv1.setAdapter(adapter1);

        addbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    expJson.put(et1.getText().toString(),lt1.getText().toString());
                    ListElementsArrayList1.add(et1.getText().toString()+ "\n\n" +lt1.getText().toString());
                    adapter1.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }




    public void deleteCurrent(View v){
        ViewGroup prnt = (ViewGroup) v.getParent();
        ViewGroup superParent = (ViewGroup) prnt.getParent().getParent();
        TextView txtv = (TextView) prnt.getChildAt(0);
        if(superParent.getId() == R.id.listView_lv){
            ListElementsArrayList.remove(txtv.getText().toString());
            adapter.notifyDataSetChanged();
        }
        else{
            ListElementsArrayList1.remove(txtv.getText().toString());
            adapter1.notifyDataSetChanged();
        }
        Log.d("Delete Method", "deleteCurrent: "+txtv.getText().toString());
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_editor, menu);
//        return true;
//    }




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