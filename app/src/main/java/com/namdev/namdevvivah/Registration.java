package com.namdev.namdevvivah;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Registration extends AppCompatActivity {
    TextView login;
    EditText usernameEditText, passwordEditText,etMobile,etReenterPassword,userName;
    Button registerButton;
    ProgressBar progressBar;
    private TextView tvStatus;
    private EditText dateEditText;
    private Calendar calendar;
    private Spinner genderSpinner,jobSpinner,salarySpinner;
    private Spinner stateSpinner, districtSpinner;
    private final List<String> stateNames = new ArrayList<>();
    private boolean responseReceived = false;
    private final List<String> districtNames = new ArrayList<>();
  // private final List<Map<String, Object>> districtNames = new ArrayList<>();
    private String state,city;
    private String name, mobile, password, resy, userOk,cheMob,dob,Gen,inCome,setJob,selectedSalary,selectedJob,selectedGender;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressBar = findViewById(R.id.progress);
        login = findViewById(R.id.login);
        usernameEditText = findViewById(R.id.etName);
        passwordEditText = findViewById(R.id.etPassword);
        registerButton = findViewById(R.id.btnRegister);
        tvStatus = findViewById(R.id.tvStatus);
        etMobile = findViewById(R.id.etMobile);
        userName = findViewById(R.id.userName);

        dateEditText = findViewById(R.id.dateEdit);
        calendar = Calendar.getInstance();
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Registration.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                updateLabel();
            }
        });


        genderSpinner = findViewById(R.id.genderSpinner);
        jobSpinner = findViewById(R.id.jobSpinner);
        salarySpinner = findViewById(R.id.salarySpinner);


        // Define an array of genders
        List<String> genders = new ArrayList<String>();
        genders.add("Gender");
        genders.add("MALE");
        genders.add("FEMALE");

        // Define an array of job
        List<String> job = new ArrayList<String>();
        job.add("Work_In");
        job.add("None");
        job.add("Private");
        job.add("Government");
        job.add("Business");
        job.add("Defence");
        job.add("Self Employed");

        // Define an array of salary
        List<String> salary = new ArrayList<String>();
        salary.add("Monthly_Income");
        salary.add("None");
        salary.add("10000");
        salary.add("15000");
        salary.add("20000");
        salary.add("25000");
        salary.add("35000");
        salary.add("50000");
        salary.add("75000");
        salary.add("100000");
        salary.add("200000");

        userName.addTextChangedListener(new TextWatcher() {
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

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    progressBar.setVisibility(View.VISIBLE);
                    validateMobileNumber(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<String> jobAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, job);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(jobAdapter);

        ArrayAdapter<String> salaryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, salary);
        salaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salarySpinner.setAdapter(salaryAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                // Retrieve the selected gender
                selectedGender = (String) adapterView.getItemAtPosition(position);
                genderSpinner.setSelection(position);
                // You can use the selectedGender variable here as needed
                // For example, you can display a toast message with the selected gender

                Toast.makeText(Registration.this, "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no item selected event if needed
            }
        });

        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                // Retrieve the selected gender
                selectedJob = (String) adapterView.getItemAtPosition(position);
                jobSpinner.setSelection(position);
                // You can use the selectedGender variable here as needed
                // For example, you can display a toast message with the selected gender
                Toast.makeText(Registration.this, "Work In: " + selectedJob, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no item selected event if needed
            }
        });
        // Set a listener to handle item selection
        salarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                // Retrieve the selected gender
                selectedSalary = (String) adapterView.getItemAtPosition(position);
                salarySpinner.setSelection(position);
                // You can use the selectedGender variable here as needed
                // For example, you can display a toast message with the selected gender
                Toast.makeText(Registration.this, "Monthly Income: " + selectedSalary, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no item selected event if needed
            }
        });

        stateSpinner = findViewById(R.id.stateSpinner);
        districtSpinner = findViewById(R.id.districtSpinner);

        // Fetch data from API
        fetchState();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister();
            }
        });
    }


    private void validateMobileNumber(String mobileNumber) {
        if (TextUtils.isEmpty(mobileNumber)) {
            etMobile.setBackgroundResource(R.drawable.edittext_background);
            Toast.makeText(getApplicationContext(), "Mobile number can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        // Define your regex pattern for mobile number validation
        String mobilePattern = "^[0-9]{10}$"; // Simple pattern for 10-digit numbers
        if (!mobileNumber.matches(mobilePattern)) {
            etMobile.setBackgroundResource(R.drawable.edittext_background);
            Toast.makeText(getApplicationContext(), "Invalid mobile number", Toast.LENGTH_SHORT).show();
        }
        else {
            String url = "https://namdevvivah.com/checkmobile.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Timber.tag("res").d(response);
                    cheMob = response.replaceAll("\\s", "");
                    if (cheMob.equals("99")) {
                        //Toast.makeText(getApplicationContext(), response+" /No", Toast.LENGTH_SHORT).show();
                        etMobile.setBackgroundResource(R.drawable.edittext_background);
                        registerButton.setEnabled(false);
                    }
                    else {
                        if (cheMob.equals(mobileNumber)) {
                            Toast.makeText(getApplicationContext(), cheMob + " /yes", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            etMobile.setBackgroundResource(R.drawable.edittext_background_focused);
                            registerButton.setEnabled(true);
                        }
                        else{
                            etMobile.setBackgroundResource(R.drawable.edittext_background);
                            registerButton.setEnabled(false);
                        }
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
                    data.put("mobile", mobileNumber);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }
    private void fetchUsers(String query) {
        String url = "https://namdevvivah.com/getusername.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Timber.tag("res").d(response);
                resy = response.replaceAll("\\s", "");
                if (resy.equals("NO")) {
                    //Toast.makeText(getApplicationContext(), response+" /No", Toast.LENGTH_SHORT).show();
                    userName.setBackgroundResource(R.drawable.edittext_background);
                    responseReceived = false;
                    registerButton.setEnabled(false);
                }
                else {
                    if (resy.equals(query)) {
                        Toast.makeText(getApplicationContext(), resy + " /yes", Toast.LENGTH_SHORT).show();
                        //usernameEditText.setText(resy);
                        progressBar.setVisibility(View.GONE);
                        // usernameEditText.setSelection(usernameEditText.getText().length());
                        userName.setBackgroundResource(R.drawable.edittext_background_focused);
                        responseReceived = true;
                        registerButton.setEnabled(true);
                        // etMobile.requestFocus();
                    }
                    else {
                        userName.setBackgroundResource(R.drawable.edittext_background);
                        responseReceived = false;
                        registerButton.setEnabled(false);
                    }
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
                data.put("get_username", query);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @SuppressLint("StaticFieldLeak")
    private void fetchState() {
        // Make API call to fetch states and districts
        // Example: Use Volley or Retrofit library to make HTTP requests
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://namdevvivah.com/ajxstatecity.php?state=11");
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject stateObject = jsonArray.getJSONObject(i);
                            stateNames.add(stateObject.getString("s_name"));
                        }
                        // Populate stateSpinner with stateNames
                        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_item, stateNames);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpinner.setAdapter(stateAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
            // Set listener for state spinner to fetch districts based on selected state
            stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    districtNames.clear();
                    stateSpinner.setSelection(position);
                    int selectedStateId = stateSpinner.getSelectedItemPosition() + 1;
                    fetchDistricts(selectedStateId);
                    Toast.makeText(getApplicationContext(), selectedStateId+"State", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getApplicationContext(), "Select State", Toast.LENGTH_SHORT).show();
                }
            });

    }
    @SuppressLint("StaticFieldLeak")
    private void fetchDistricts(int stateId) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://namdevvivah.com/ajxstatecity.php?city="+stateId);
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject distObject = jsonArray.getJSONObject(i);
                            districtNames.add(distObject.getString("distname"));
                        }

                        // Populate districtSpinner with districtNames
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_item, districtNames);
                        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        districtSpinner.setAdapter(districtAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtSpinner.setSelection(position);
                city = (String) parent.getItemAtPosition(position);
                int selectedDistrictId = districtSpinner.getSelectedItemPosition() + 1;
                Toast.makeText(getApplicationContext(), selectedDistrictId+"City", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Select City", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void insertUserProfile() {
        // Get selected state and district IDs
        int selectedStateId = stateSpinner.getSelectedItemPosition() + 1; // Add 1 because index starts from 0
        int selectedDistrictId = districtSpinner.getSelectedItemPosition() + 1; // Add 1 because index starts from 0

        // Display success message or error message based on API response
        Toast.makeText(this, selectedStateId+"User profile inserted successfully"+selectedDistrictId, Toast.LENGTH_SHORT).show();
    }
    public void showDatePickerDialog(View v) {
        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        dateEditText.setSingleLine(true);
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(calendar.getTime()));
        dateEditText.clearFocus();
    }

    public void btnRegister() {
        progressBar.setVisibility(View.VISIBLE);
        name = usernameEditText.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        userOk=userName.getText().toString().trim();

        dob = dateEditText.getText().toString().trim();
        Gen = selectedGender.trim();
        setJob = selectedJob.trim();
        inCome=selectedSalary.trim();

        if(Gen.equals("Gender")){
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            genderSpinner.setBackgroundResource(R.drawable.edittext_background);
        }
        else if(setJob.equals("Work_In")){
            Toast.makeText(this, "Please Select Work In", Toast.LENGTH_SHORT).show();
            jobSpinner.setBackgroundResource(R.drawable.edittext_background);

        }
        else if(inCome.equals("Monthly_Income")){
            Toast.makeText(this, "Please Select Monthly_Income", Toast.LENGTH_SHORT).show();
            salarySpinner.setBackgroundResource(R.drawable.edittext_background);
        }
        else if(!name.equals("") && !mobile.equals("") && !password.equals("") && !userOk.equals("")){
            genderSpinner.setBackgroundResource(R.drawable.edittext_background_focused);
            jobSpinner.setBackgroundResource(R.drawable.edittext_background_focused);
            salarySpinner.setBackgroundResource(R.drawable.edittext_background_focused);
            usernameEditText.setBackgroundResource(R.drawable.edittext_background_focused);
            etMobile.setBackgroundResource(R.drawable.edittext_background_focused);
            passwordEditText.setBackgroundResource(R.drawable.edittext_background_focused);
            userName.setBackgroundResource(R.drawable.edittext_background_focused);
            stateSpinner.setBackgroundResource(R.drawable.edittext_background_focused);
            districtSpinner.setBackgroundResource(R.drawable.edittext_background_focused);
            dateEditText.setBackgroundResource(R.drawable.edittext_background_focused);
            int selectedStateId = stateSpinner.getSelectedItemPosition() + 1; // Add 1 because index starts from 0
            int selectedDistrictId = districtSpinner.getSelectedItemPosition() + 1;
            String url = "https://namdevvivah.com/appregister.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(String response) {
                    resy = response.replaceAll("\\s", "");
                    if (resy.equals("YES")) {
                        progressBar.setVisibility(View.GONE);
                        tvStatus.setText("Successfully registered.");
                        registerButton.setClickable(false);
                        Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else if (resy.equals("NO")) {
                        tvStatus.setText("Something went wrong!"+resy); }
                    else {
                        tvStatus.setText("not registered."+resy);
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
                    data.put("username", name);
                    data.put("mobile", mobile);
                    data.put("password", password);
                    data.put("userid", userOk);
                    data.put("dob", dob);
                    data.put("gender", Gen);
                    data.put("work", setJob);
                    data.put("inCome", inCome);
                    data.put("state", String.valueOf(selectedStateId));
                    data.put("city", city);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
