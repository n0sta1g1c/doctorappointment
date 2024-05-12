package com.example.doctorappointment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getName();

    private FirebaseUser user;

    private FirebaseFirestore mFirestore;

    private CollectionReference mPatients;

    private DocumentReference mdocRef;


    TextView firstnameTV;
    TextView lastnameTV;
    TextView emailTV;
    TextView phonenumberTV;
    TextView TAJNumberTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mFirestore = FirebaseFirestore.getInstance();
        mPatients = mFirestore.collection("Patients");

        firstnameTV = findViewById(R.id.firstName);
        lastnameTV = findViewById(R.id.lastName);
        emailTV = findViewById(R.id.email);
        phonenumberTV = findViewById(R.id.phoneNumber);
        TAJNumberTV = findViewById(R.id.TAJNumber);
        //firstnameTV.setText();
        readDocument(user.getUid());
    }

    public void readDocument(String uid) {
        mdocRef = mPatients.document(uid);

        mdocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        firstnameTV.setText((CharSequence) document.get("firstName"));
                        lastnameTV.setText((CharSequence) document.get("lastName"));
                        emailTV.setText((CharSequence) document.get("email"));
                        phonenumberTV.setText((CharSequence) document.get("phoneNumber"));
                        TAJNumberTV.setText((CharSequence) document.get("tajnumber"));
                    } else {
                        Log.d(LOG_TAG, "No such document");
                    }
                } else {
                    Log.d(LOG_TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void StartEditProfile(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        readDocument(user.getUid());
    }

    @Override
    protected void onResume() {
        super.onResume();
        readDocument(user.getUid());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        readDocument(user.getUid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back_btn){
            finish();
            return true;
        }
        if (id == R.id.about_btn){
            Toast.makeText(this, "About selected!", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.log_out_btn){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
            return true;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
