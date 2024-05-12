package com.example.doctorappointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText firstNameTextView;
    EditText lastNameTextView;
    EditText emailTextView;
    EditText passwordTextView;
    EditText passwordConfirmTextView;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;

    private CollectionReference mPatients;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        firstNameTextView = findViewById(R.id.firstName);
        lastNameTextView = findViewById(R.id.lastName);
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        passwordConfirmTextView = findViewById(R.id.passwordconfirm);

        mAuth = FirebaseAuth.getInstance();

        mFirestore = FirebaseFirestore.getInstance();
        mPatients = mFirestore.collection("Patients");
    }

    public void cancel(View view) {
        finish();
    }

    public void register(View view) {
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        String passwordConfirm = passwordConfirmTextView.getText().toString();
        String firstName = firstNameTextView.getText().toString();
        String lastName = lastNameTextView.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(RegisterActivity.this, "Passwords are not matching!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    //Log.d(LOG_TAG, "User created successfully");
                    Toast.makeText(RegisterActivity.this, "Successfull registration!", Toast.LENGTH_LONG).show();
                    Patient newPatient = new Patient(firstName, lastName, email);
                    mPatients.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(newPatient);
                    startAppointments();
                    finish();
                } else {
                    Log.d(LOG_TAG, "User wasn't created successfully");
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(RegisterActivity.this, "One or all fields are empty!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(RegisterActivity.this, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
        }

    }

    public void startAppointments() {
        Intent intent = new Intent(this, AppointmentListActivity.class);
        startActivity(intent);
    }


}
