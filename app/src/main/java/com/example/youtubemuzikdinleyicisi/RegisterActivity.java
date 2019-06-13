package com.example.youtubemuzikdinleyicisi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.youtubemuzikdinleyicisi.Entity.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputName;
    private EditText inputSurname;
    private MaterialButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register);

        initComponents();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        RegisterEventHandlers();
    }

    private void initComponents() {
        inputUsername = findViewById(R.id.rinputUsername);
        inputPassword = findViewById(R.id.rinputPassword);
        inputName = findViewById(R.id.rinputName);
        inputSurname = findViewById(R.id.rinputSurname);
        btnRegister = findViewById(R.id.rbtnRegister);
    }

    private void RegisterEventHandlers() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(inputName.getText().toString(), inputSurname.getText().toString()
                        , inputUsername.getText().toString(), inputPassword.getText().toString());

                dbRef.child("Users").push().setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getApplicationContext(), "You have been registered successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "An error has been occured.", Toast.LENGTH_LONG).show();
                            databaseError.getCode();
                        }
                    }
                });

            }
        });
    }


}
