package com.example.doctorsappassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.android.volley.toolbox.Volley;
import com.example.doctorsappassignment.databinding.ActivityDashboardBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        req();

    }
    private void req(){
        binding.dashProgress.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, MainActivity.toGetUrl ,null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.dashProgress.setVisibility(View.GONE);
                        Log.d("DashStatusCheck",response.toString());
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.getString("d"));
                            binding.txtName.setText(jsonObject1.getString("name"));
                        } catch (Exception e) {
                            binding.dashProgress.setVisibility(View.GONE);
                            Toast.makeText(DashboardActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> {
                    binding.dashProgress.setVisibility(View.GONE);
                    Toast.makeText(DashboardActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

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