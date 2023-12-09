package com.projeto2.env_station_app.Model.DAO;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projeto2.env_station_app.Model.Device;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class DeviceDAO {
    String responseString;

    public interface VolleyCallback {
        void onSuccess(String response) throws JSONException;
        void onError(String error);
    }

    public String register_device(Context context, Device device, String user_id, final DeviceDAO.VolleyCallback callback){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            final String mRequestBody = "{\"station\":[\""+device.getId()+"\"]}";

            StringRequest sr = new StringRequest(Request.Method.PUT, ""+user_id+"", new Response.Listener<String>() {
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
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(sr);
        }catch (Error e) {
            e.printStackTrace();
        }
        return responseString;
    }
}
