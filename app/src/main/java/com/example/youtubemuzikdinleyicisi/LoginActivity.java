package com.example.youtubemuzikdinleyicisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtubemuzikdinleyicisi.Entity.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.xml.transform.Source;

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private EditText inputUsername;
    private EditText inputPassword;
    private MaterialButton btnLogin;
    private TextView linkSignup;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        initComponents();

        if (!sharedPreferences.getString("username", "defValue").equals("defValue")) {
            Intent intent = new Intent(LoginActivity.this, GenresActivity.class);
            startActivity(intent);
        }

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        registerEventHandlers();

    }

    private void initComponents() {
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        linkSignup = findViewById(R.id.linkSignup);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
    }

    private void registerEventHandlers() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = inputUsername.getText().toString();
                final String password = inputPassword.getText().toString();

                //DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
                dbRef.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Toast.makeText(getApplicationContext(),dataSnapshot.getValue(),Toast.LENGTH_LONG).show();
                        //String a = (String)dataSnapshot.getValue();
                        boolean loginSuccess = false;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            User user = ds.getValue(User.class);
                            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();

                                loginSuccess = true;

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", user.getName());
                                editor.putString("surname", user.getSurname());
                                editor.putString("username", user.getUsername());
                                editor.putString("password", user.getPassword());
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, GenresActivity.class);
                                startActivity(intent);
                            }
                        }
                        if (!loginSuccess)
                            Toast.makeText(getApplicationContext(), "Login error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}
