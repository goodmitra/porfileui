package com.namdev.namdevvivah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView register;
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    private String usid, password, resy ,qrid, qrpass;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.userid);
        passwordEditText = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginApp);
        progressBar = findViewById(R.id.progress);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                usid = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                if(!usid.equals("") && !password.equals("")){
                    String url = "https://namdevvivah.com/login.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res", response);
                            resy = response.replaceAll("\\s", "");
                            String[] namesList = resy.split("\\|");
                            String unid = namesList [0];
                            String Status = namesList [1];
                            if (Status.equals("SAP") || resy.equals("AAP")) {
                                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("flag", true);
                                editor.putString("unid", unid);
                                editor.putString("sender", usid);
                                editor.apply();
                                Intent intent = new Intent(Login.this, Allprofile.class);
                                startActivity(intent);
                                finish();
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
                            data.put("usid", usid);
                            data.put("password", password);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }else{
                    Toast.makeText(getApplicationContext(), "Fields can not be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        retrieveUsername();
    }
    private void retrieveUsername() {
        // Get the shared preferences file with the name "MyPrefs"
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        //Boolean check = sharedPreferences.getBoolean("flag",false);
        String savedUsername = sharedPreferences.getString("sender", "");

        if(savedUsername.isEmpty())
        // Display the retrieved username
        {
            Toast.makeText(getApplicationContext(), "Please Login Again", Toast.LENGTH_SHORT).show();
        }
        else {
                Toast.makeText(getApplicationContext(), "WelCome", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Allprofile.class);
                startActivity(intent);
                finish();
        }
    }

}