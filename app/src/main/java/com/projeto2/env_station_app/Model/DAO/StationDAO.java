package com.projeto2.env_station_app.Model.DAO;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;


public class StationDAO {
    String inputFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    String outputFormat = "HH:mm";

    SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
    SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);

    public interface VolleyCallback {
        void onSuccess(String response) throws JSONException;
        void onError(String error);
    }

    public void retrieve_station_data(Context context, String station_id, final StationDAO.VolleyCallback callback) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = ""+station_id;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                callback.onSuccess(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage = "";
                    if (error.networkResponse != null) {
                        errorMessage = String.valueOf(error.networkResponse.statusCode);
                    }
                    callback.onError(errorMessage);
                }

            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null && response.data != null) {
                        try {
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(stringRequest);
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}
