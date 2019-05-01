package com.example.travelbook.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText parolaEdittext,emailEdittext;
    private Button kayitButton;
    private String email,parola;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parolaEdittext = (EditText) findViewById(R.id.parolaedittext);
        emailEdittext = (EditText) findViewById(R.id.emailedittext);
        kayitButton = (Button) findViewById(R.id.kayitbutton);

        firebaseAuth = FirebaseAuth.getInstance();//firebase nesnesi oluşturduk
    }

    public void kayitOl(View view) {

        Intent intent = new Intent(getApplicationContext(), UserSaveActivity.class);
        startActivity(intent);
    }
    public void girisYap(View view) {
        String email = emailEdittext.getText().toString();
        String parola = parolaEdittext.getText().toString();

        if (email.isEmpty()) {
            emailEdittext.setError("Email gerekli lütfen email giriniz");
            emailEdittext.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdittext.setError("Bir email adresi giriniz!");
            emailEdittext.requestFocus();
            return;
        }
        if (parola.isEmpty()) {
            parolaEdittext.setError("Parola gerekli!");
            parolaEdittext.requestFocus();
            return;
        }

        if (parola.length() < 6) {
            parolaEdittext.setError("parola uzunluğu minimum 6 olmalıdır!");
            parolaEdittext.requestFocus();//Hatalı alana cursor gider
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Başarısız" + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

