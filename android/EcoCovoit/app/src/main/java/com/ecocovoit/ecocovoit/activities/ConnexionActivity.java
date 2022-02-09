package com.ecocovoit.ecocovoit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ecocovoit.ecocovoit.R;

public class ConnexionActivity extends AppCompatActivity {

    private EditText EditTextAdresseEmail;
    private EditText EditTextMotDePasse;
    private Button btnConnexion;
    private Button btn_connexionactivity_createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        btn_connexionactivity_createAccount = findViewById(R.id.btn_connexionactivity_createAccount);
        btn_connexionactivity_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        CreateAccountActivity.class));

            }
        });


    }
}