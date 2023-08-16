package com.example.doctorsappassignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doctorsappassignment.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static String toGetUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //for notification
        setSupportActionBar(binding.doctToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.doctToolbar.setTitle("");
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req();
            }
        });

    }
    private void req(){
        binding.mainProgress.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", binding.etName.getText().toString());
            jsonObject.put("email", binding.etEmailId.getText().toString());
            jsonObject.put("gender", "M");
            jsonObject.put("practice_frm_month", binding.etMonths.getText().toString());
            jsonObject.put("practice_frm_year", binding.etYears.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://199.192.26.248:8000/sap/opu/odata/sap/ZCDS_TEST_REGISTER_CDS/ZCDS_TEST_REGISTER" ,jsonObject,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.mainProgress.setVisibility(View.GONE);
                        Log.d("MainStatusCheck",response.toString());
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.getString("d"));
                            Log.d("MainStatusCheck",jsonObject1.toString());
                            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("__metadata"));
                            toGetUrl = jsonObject2.getString("uri");
                            startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                        } catch (Exception e) {
                            binding.mainProgress.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> {
                    binding.mainProgress.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("X-Requested-With", "X");
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}