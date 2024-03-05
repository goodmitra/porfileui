package com.namdev.namdevvivah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserList userAdapter;
    EditText searchInput;
    TextView userTitle;
    ImageButton searchButton;
    ImageView close,profile;
    LinearLayout searchBar,search;
    ImageButton backButton;
    private String usid,unid;
    ProgressBar progressBar;
    private final ArrayList<User> searchList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.setAnimation(R.raw.startup);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Chat.class);
                startActivity(intent);
                finish();
            }
        },1000);*/
        scheduleAlarm();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserList(searchList,this);
        recyclerView.setAdapter(userAdapter);
        searchInput = findViewById(R.id.seach_username_input);
       // searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        userTitle = findViewById(R.id.userTitle);
        progressBar = findViewById(R.id.progress);
        search = findViewById(R.id.search);
        close = findViewById(R.id.close);
        searchBar = findViewById(R.id.searchBar);
        searchBar.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        //Boolean check = sharedPreferences.getBoolean("flag",false);
        unid = sharedPreferences.getString("unid", "");
        usid = sharedPreferences.getString("sender", "");
        userTitle.setText(usid);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
            }
        });
     /*  profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                searchInput.setText("");
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                progressBar.setVisibility(View.VISIBLE);
                fetchUsers(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }

        });
        fetchUsers(null);
    }
    private void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the alarm to trigger at approximately 24-hour intervals
        long alarmTriggerTime = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTriggerTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    @SuppressLint("StaticFieldLeak")
    private void fetchUsers(String query) {
        // You need to make a network request to fetch data from your backend API
        // Here you should use libraries like Retrofit, Volley, or OkHttp
        // For simplicity, I'll simulate a network request using a dummy JSON response
        String url = "https://namdevvivah.com/userlist.php?query=" + query;

        // Make a network request to fetch data
        // For simplicity, I'll use AsyncTask, but you should use libraries like Retrofit or Volley for better networking handling
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://namdevvivah.com/userlist.php?query=" + query);
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
                        searchList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("uname");
                            String email = jsonObject.getString("mobile");
                            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                            if(!unid.equals(id)){
                                User user = new User(id, name, email);
                                searchList.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}