package com.namdev.namdevvivah;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
TextView userName;
TextView mobile;
EditText message;
ImageView send, buttonChoose, buttonUpload,viewImg,backButton,sendImg;;
    ImageView logout,vedio;
    private Massage userAdapter;
    RecyclerView recyclerView;
    ImageView userImg;
    RecyclerView.LayoutManager layoutManager;
    private String sendMsg, sendId, reId,resy,unid;
    private Bitmap bitmap;
    private final int CAMERA_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    ProgressBar progressBar;
    private final ArrayList<Mass> massageList = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatsend);
        userName = findViewById(R.id.username);
        mobile = findViewById(R.id.userMobile);
        message = findViewById(R.id.massageId);
        send = findViewById(R.id.send);
        sendImg = findViewById(R.id.sendItem);
        progressBar = findViewById(R.id.progress);

        buttonUpload = findViewById(R.id.imgCam);
        buttonChoose = findViewById(R.id.imgGal);

        viewImg = findViewById(R.id.imageView);
        viewImg.setVisibility(View.GONE);

        sendImg.setVisibility(View.GONE);

        backButton = findViewById(R.id.back_btn);
        vedio = findViewById(R.id.vedio);
        logout = findViewById(R.id.logout);
        userImg=findViewById(R.id.userImg);
        String imageUrl="https://gpsggc.com/assets/img/user.png";
        Glide.with(this).load(imageUrl).into(userImg);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new Massage(massageList,this);
        recyclerView.setAdapter(userAdapter);
        recyclerView.scrollToPosition(userAdapter.getItemCount() - 1);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        //Boolean check = sharedPreferences.getBoolean("flag",false);
        unid = sharedPreferences.getString("sender", "");
        sendId = sharedPreferences.getString("unid", "");

        Bundle extras = getIntent().getExtras();
        assert extras != null;
       // sendId = "340";
       // reId = "796";
        reId = extras.getString("userId");
        String uName = extras.getString("userName");
        String uMobile= extras.getString("userMobile");
        userName.setText(uName);
        mobile.setText(uMobile);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        vedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(ChatActivity.this, VedioChat.class);
                startActivity(intent);
                finish();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                showFileChooser();
            }
        });
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                sendMsg = message.getText().toString().trim();
                if(!sendMsg.equals("")){
                    String url = "https://namdevvivah.com/sendmsg.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res", response);
                            resy = response.replaceAll("\\s", "");
                            if (resy.equals("OK")) {
                                message.setText("");
                                //fetchUsers();
                                viewImg.setVisibility(View.GONE);
                                fetchUsers(sendId,reId);
                                Toast.makeText(getApplicationContext(), resy, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else if (resy.equals("NO")) {
                                Toast.makeText(getApplicationContext(), resy, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("sender", sendId);
                            data.put("reciver", reId);
                            data.put("msg", sendMsg);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
        sendImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                final String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                sendMsg = message.getText().toString().trim();
                    String url = "https://namdevvivah.com/sendmsg.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res", response);
                            resy = response.replaceAll("\\s", "");
                            if (resy.equals("OK")) {
                                message.setText("");
                                //fetchUsers();
                                viewImg.setVisibility(View.GONE);
                                sendImg.setVisibility(View.GONE);
                                send.setVisibility(View.VISIBLE);
                                fetchUsers(sendId,reId);
                                Toast.makeText(getApplicationContext(), resy, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else if (resy.equals("NO")) {
                                Toast.makeText(getApplicationContext(), resy, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("sender", sendId);
                            data.put("reciver", reId);
                            data.put("msg", sendMsg);
                            data.put("image", encodedImage);

                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

            }
        });
        //fetchUsers();
        fetchUsers(sendId,reId);

        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                resetUsername();
            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                // Getting bitmap from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Setting bitmap to ImageView
                viewImg.setVisibility(View.VISIBLE);
                sendImg.setVisibility(View.VISIBLE);
                send.setVisibility(View.GONE);
                viewImg.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            viewImg.setVisibility(View.VISIBLE);
            sendImg.setVisibility(View.VISIBLE);
            send.setVisibility(View.GONE);
            viewImg.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Sorry Something Wrong", Toast.LENGTH_SHORT).show();
        }
        }
    }
    @SuppressLint("StaticFieldLeak")
    private void fetchUsers(String sendId, String reId) {
        // You need to make a network request to fetch data from your backend API
        // Here you should use libraries like Retrofit, Volley, or OkHttp
        // For simplicity, I'll simulate a network request using a dummy JSON response
       // String url = "https://gpsggc.com/userlist.php?sender=" + query +"&reciver=" +recev;

        // Make a network request to fetch data
        // For simplicity, I'll use AsyncTask, but you should use libraries like Retrofit or Volley for better networking handling
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://namdevvivah.com/msglist.php?sender=" + sendId +"&reciver=" +reId);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(String response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        massageList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                           // String id = jsonObject.getString("id");
                            String id = "56";
                            String name = jsonObject.getString("outgoing_msg_id");
                            String email = jsonObject.getString("incoming_msg_id");
                            String msg = jsonObject.getString("msg");
                            String img = jsonObject.getString("pfile");
                            String type ="";
                            String left ="L";
                            String rtght ="R";
                            if(!img.equals("")){
                                type="1";
                            }else {
                                type="2";
                            }
                            if (name.equals(ChatActivity.this.sendId)) {
                                rtght =msg;
                            }
                            else{
                                left =msg;
                            }
                            Mass mass = new Mass(id, left, rtght,img,type);
                            massageList.add(mass);
                            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                        }
                        userAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(userAdapter.getItemCount() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private void resetUsername() {
        // Get or create shared preferences file with the name "MyPrefs"
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        // Create an editor to modify the shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // reset
        editor.clear();
        // Apply changes
        editor.apply();

        Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}