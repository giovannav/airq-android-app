package com.projeto2.env_station_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projeto2.env_station_app.Model.Device;
import com.projeto2.env_station_app.Model.DAO.DeviceDAO;
import com.projeto2.env_station_app.Session.SessionManager;

import org.json.JSONException;

public class RegisterDevice extends AppCompatActivity {
    Button btn_register_device;
    EditText et_device_code;

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);

        sessionManager = new SessionManager(getApplicationContext());
        String user_id = getIntent().getStringExtra("user_id");

        btn_register_device = findViewById(R.id.btn_register_device);
        et_device_code = findViewById(R.id.et_device_code);

        btn_register_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device = new Device(et_device_code.getText().toString(), null);
                DeviceDAO deviceDAO = new DeviceDAO();

                deviceDAO.register_device(RegisterDevice.this, device, user_id, new DeviceDAO.VolleyCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if (response.equals("200")){
                            Toast.makeText(RegisterDevice.this, "Dispositivo cadastrado.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onError(String error) {
                        if (error.equals("404")){
                            Toast.makeText(RegisterDevice.this, "Dispositivo não existe. Insira um código diferente.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}