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

public class Registration extends AppCompatActivity {
    Button btn_goto_login, btn_register;
    EditText et_name, et_surname, et_email, et_password, et_confirm_password;
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn_goto_login = findViewById(R.id.btn_goto_login);
        btn_register = findViewById(R.id.btn_register);
        et_name = findViewById(R.id.et_name);
        et_surname = findViewById(R.id.et_surname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_name.length() <= 0){
                    et_name.setError("Nome não pode ficar vazio!");
                    return;
                }
                if(et_surname.length() <= 0){
                    et_surname.setError("Sobrenome não pode ficar vazio!");
                    return;
                }
                if(et_email.length() <= 0){
                    et_email.setError("E-mail não pode ficar vazio@");
                    return;
                }
                if (et_password.length() < MIN_PASSWORD_LENGTH) {
                    et_password.setError("Senha deve ter no mínimo " + MIN_PASSWORD_LENGTH + " caracteres");
                    return;
                }
                if (!et_password.getText().toString().equals(et_confirm_password.getText().toString())){
                    et_confirm_password.setError("Senhas não coincidem!");
                    return;
                }

                User user = new User(et_name.getText().toString(), et_surname.getText().toString(), et_email.getText().toString(), et_password.getText().toString(), null);
                UserDAO userDAO = new UserDAO();
                userDAO.registration(Registration.this, user, new UserDAO.VolleyCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if (response.equals("200")){
                            Toast.makeText(Registration.this, "Usuário cadastrado.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onError(String error) {
                        if (error.equals("400")){
                            Toast.makeText(Registration.this, "E-mail já cadastrado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btn_goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}