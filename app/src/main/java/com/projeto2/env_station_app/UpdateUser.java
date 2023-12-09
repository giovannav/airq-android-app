package com.projeto2.env_station_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projeto2.env_station_app.Model.User;
import com.projeto2.env_station_app.Model.DAO.UserDAO;

import org.json.JSONException;

public class UpdateUser extends AppCompatActivity {
    EditText et_update_name, et_update_surname, et_update_password, et_update_confirm_password;
    Button btn_update_user;
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        String user_id = getIntent().getStringExtra("user_id");

        et_update_name = findViewById(R.id.et_update_name);
        et_update_surname = findViewById(R.id.et_update_surname);
        et_update_password = findViewById(R.id.et_update_password);
        et_update_confirm_password = findViewById(R.id.et_update_confirm_password);
        btn_update_user = findViewById(R.id.btn_update_user);

        btn_update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_update_password.length() > 0 & et_update_password.length() < MIN_PASSWORD_LENGTH) {
                    et_update_password.setError("Senha deve ter no mínimo " + MIN_PASSWORD_LENGTH + " caracteres");
                    return;
                }
                if (et_update_password.length() > 0 & !et_update_password.getText().toString().equals(et_update_confirm_password.getText().toString())){
                    et_update_confirm_password.setError("Senhas não coincidem!");
                    return;
                }
                User user = new User(user_id, et_update_name.getText().toString(), et_update_surname.getText().toString(), null, et_update_password.getText().toString(), null);
                UserDAO userDAO = new UserDAO();
                userDAO.update_user(UpdateUser.this, user, new UserDAO.VolleyCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if (response.equals("200")){
                            Toast.makeText(UpdateUser.this, "Usuário atualizado.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(UpdateUser.this, "Error no cadastro. Verifique os campos.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}