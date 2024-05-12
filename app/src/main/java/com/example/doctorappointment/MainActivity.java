package com.example.doctorappointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int RC_SIGN_IN = 123;
    private static final int SECRET_KEY = 99;

    EditText emailET;
    EditText passwordET;
    Button loginButtonBTN;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        loginButtonBTN = findViewById(R.id.loginButton);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        // Log.i(LOG_TAG, "Bejelentkezett: " + userName + ", jelszó: " + password);
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    //Log.d(LOG_TAG, "User loged in successfully");
                    startAppointments();
                } else {
                    Log.d(LOG_TAG, "User log in fail");
                    Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(MainActivity.this, "One or both fields are empty!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
        }

    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void startAppointments() {
        Intent intent = new Intent(this, AppointmentListActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();

        emailET.setText(null);
        passwordET.setText(null);

        //Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        emailET.setText(null);
        passwordET.setText(null);

        //Log.i(LOG_TAG, "onResume");
    }
}