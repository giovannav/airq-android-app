package com.projeto2.env_station_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projeto2.env_station_app.Model.User;
import com.projeto2.env_station_app.Model.DAO.UserDAO;
import com.projeto2.env_station_app.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_login;
    EditText et_login_email, et_login_password;
    TextView btn_create_account;
    JSONObject jsonObject;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());

        btn_create_account = findViewById(R.id.btn_create_account);
        btn_login = findViewById(R.id.btn_login);
        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(null, null, et_login_email.getText().toString(), et_login_password.getText().toString(), null);
                UserDAO userDAO = new UserDAO();

                userDAO.login(MainActivity.this, user, new UserDAO.VolleyCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        jsonObject = new JSONObject(response);

                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String message = jsonObject.getString("message");
                            JSONArray stationArray = jsonObject.getJSONArray("station");
                            List<String> station = new ArrayList<>();
                            for (int i = 0; i < stationArray.length(); i++) {
                                station.add(stationArray.getString(i));
                            }
                            user.setId(id);
                            user.setName(name);
                            if (station.size()>0){
                                user.setDevice(station);
                            }

                        if (message.equals("Login successful")){
                                sessionManager.createLoginSession(user);
                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error and response code
                        if (error.equals("401")){
                            Toast.makeText(MainActivity.this, "E-mail ou senha inválidos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}