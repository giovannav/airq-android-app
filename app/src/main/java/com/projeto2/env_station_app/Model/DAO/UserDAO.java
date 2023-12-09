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
import com.projeto2.env_station_app.Model.User;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class UserDAO {

    public interface VolleyCallback {
        void onSuccess(String response) throws JSONException;
        void onError(String error);
    }

    public void registration(Context context, User user, final VolleyCallback callback){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            final String mRequestBody = return_user(user);

            StringRequest sr = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
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
                    String responseString = "";
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
    }
    public void get_user(Context context, String user_id, final VolleyCallback callback) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest sr = new StringRequest(Request.Method.GET, ""+user_id+"",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                callback.onSuccess(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
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
            queue.add(sr);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public void login(Context context, User user, final VolleyCallback callback) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            final String mRequestBody = return_login(user);

            StringRequest sr = new StringRequest(Request.Method.POST, "",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                callback.onSuccess(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
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
            queue.add(sr);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public String return_login(User user){
        String obj_user = "{\"email\":\""+user.getEmail()+"\",\"password\":\""+user.getPassword()+"\"}";
        return obj_user;
    }

    public String return_user(User user){
        String obj_user = "{\"name\":\""+user.getName()+"\",\"surname\":\""+user.getSurname()+"\",\"email\":\""+user.getEmail()+"\",\"password\":\""+user.getPassword()+"\",\"confirmed_email\":\"false\",\"birthday\":\"2000-04-28\",\"station\":[]}";
        return obj_user;
    }

    public void update_user(Context context, User user, final UserDAO.VolleyCallback callback){
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest sr = new StringRequest(Request.Method.PUT, ""+user.getId()+"?name="+user.getName()+"&surname="+user.getSurname()+"&password="+user.getPassword()+"", new Response.Listener<String>() {
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
    }
}


