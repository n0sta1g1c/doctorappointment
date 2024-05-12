package com.example.doctorappointment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditProfileActivity.class.getName();

    private FirebaseUser user;
    private  String uid;
    private FirebaseFirestore mFirestore;
    private CollectionReference mPatients;
    private DocumentReference mdocRef;

    private AlertDialog.Builder builder;


    EditText firstnameEditTV;
    String initFirstName;
    EditText lastnameEditTV;
    String initLastName;
    String initPassword;
    String initTAJNumber;
    EditText emailEditTV;
    String initEmail;
    EditText phonenumberEditTV;
    String initphoneNumber;
    EditText TAJNumberEditTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        builder = new AlertDialog.Builder(this);

        mFirestore = FirebaseFirestore.getInstance();
        mPatients = mFirestore.collection("Patients");

        firstnameEditTV = findViewById(R.id.firstNameEdit);
        lastnameEditTV = findViewById(R.id.lastNameEdit);
        phonenumberEditTV = findViewById(R.id.phoneNumberEdit);
        TAJNumberEditTV = findViewById(R.id.TAJNumberEdit);

        uid = user.getUid();

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
                        firstnameEditTV.setText((CharSequence) document.get("firstName"));
                        lastnameEditTV.setText((CharSequence) document.get("lastName"));
                        phonenumberEditTV.setText((CharSequence) document.get("phoneNumber"));
                        TAJNumberEditTV.setText((CharSequence) document.get("tajnumber"));
                        initFirstName = firstnameEditTV.getText().toString();
                        initLastName = lastnameEditTV.getText().toString();
                        initphoneNumber = phonenumberEditTV.getText().toString();
                        initTAJNumber = TAJNumberEditTV.getText().toString();
                    } else {
                        Log.d(LOG_TAG, "No such document");
                    }
                } else {
                    Log.d(LOG_TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void UpdateData(View view){
        boolean validphonenumber = true;
        boolean validtajnumber = true;
        if(phonenumberEditTV.getText().toString().trim().length() != 12 ){
            Toast.makeText(EditProfileActivity.this, "Invalid phone number!", Toast.LENGTH_LONG).show();
            validphonenumber = false;
            finish();
        }
        if(TAJNumberEditTV.getText().toString().trim().length() != 9){
            Toast.makeText(EditProfileActivity.this, "Invalid TAJ number!", Toast.LENGTH_LONG).show();
            validtajnumber = false;
            finish();
        }
        mdocRef = mPatients.document(user.getUid());
        if (!firstnameEditTV.getText().toString().trim().equals(initFirstName) && !firstnameEditTV.getText().toString().trim().isEmpty()){
            Log.d(LOG_TAG,  "initFirstName->" + initLastName + " firstnameEditTV" + firstnameEditTV.getText());
            mdocRef
                    .update("firstName", firstnameEditTV.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            initFirstName = firstnameEditTV.getText().toString();
                            Toast.makeText(EditProfileActivity.this, "Succesfully updated the first name!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Error updating! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else if (firstnameEditTV.getText().toString().trim().isEmpty()){
            Log.d(LOG_TAG, firstnameEditTV.getText().toString());
            Toast.makeText(EditProfileActivity.this, "First name field cannot be empty!", Toast.LENGTH_LONG).show();
        }
        if (!lastnameEditTV.getText().toString().trim().equals(initLastName) && !lastnameEditTV.getText().toString().trim().isEmpty()){
            mdocRef
                    .update("lastName", lastnameEditTV.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            initLastName = lastnameEditTV.getText().toString();
                            Toast.makeText(EditProfileActivity.this, "Succesfully updated the last name!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Error updating! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else if (lastnameEditTV.getText().toString().trim().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Last name field cannot be empty!", Toast.LENGTH_LONG).show();
        }
        if (validphonenumber && !phonenumberEditTV.getText().toString().trim().equals(initphoneNumber) && !phonenumberEditTV.getText().toString().trim().isEmpty()){
            mdocRef
                    .update("phoneNumber", phonenumberEditTV.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            initphoneNumber = phonenumberEditTV.getText().toString();
                            Toast.makeText(EditProfileActivity.this, "Succesfully updated the phone number!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Error updating! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else if (phonenumberEditTV.getText().toString().trim().isEmpty()){
            Toast.makeText(EditProfileActivity.this, "Phone number field cannot be empty!", Toast.LENGTH_LONG).show();
        }
        if (validtajnumber && !TAJNumberEditTV.getText().toString().trim().equals(initTAJNumber) && !TAJNumberEditTV.getText().toString().trim().isEmpty()){
            mdocRef
                    .update("tajnumber", TAJNumberEditTV.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            initTAJNumber = TAJNumberEditTV.getText().toString();
                            Toast.makeText(EditProfileActivity.this, "Succesfully updated the TAJ number!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Error updating! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else if (TAJNumberEditTV.getText().toString().trim().isEmpty()){
            Toast.makeText(EditProfileActivity.this, "TAj number field cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAccount(View view) {
        builder.setTitle("Warning!").setMessage("Do you wanna delete this account?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPatients.document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(LOG_TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(LOG_TAG, "Error deleting document", e);
                                    }
                                });
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditProfileActivity.this, user.getEmail() + " account has been succesfully deleted!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfileActivity.this, "Error: Cannot delete account! Please try again!", Toast.LENGTH_LONG).show();
                                    }
                                });
                        toMainActivity();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void toMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
