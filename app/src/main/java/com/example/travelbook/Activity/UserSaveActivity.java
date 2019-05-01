package com.example.travelbook.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.travelbook.Activity.LoginActivity;
import com.example.travelbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class UserSaveActivity extends AppCompatActivity {

    private EditText parolaEdittext,parolatekrarEdittext,emailEdittext;
    private Button kayitButton;
    private String email,parola,parolatekrar;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_save);

        parolaEdittext = (EditText) findViewById(R.id.parolaedittext);
        parolatekrarEdittext = (EditText) findViewById(R.id.parolatekraredittext);
        emailEdittext = (EditText) findViewById(R.id.emailedittext);
        kayitButton = (Button) findViewById(R.id.kayitbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);//progress bar gözüksün


        firebaseAuth = FirebaseAuth.getInstance();//firebase nesnesi oluşturduk

    }
    public void kayitOl(View view) {
        progressBar.setVisibility(View.VISIBLE);//progress bar gözüksün

        email=emailEdittext.getText().toString();
        parola=parolaEdittext.getText().toString();
        parolatekrar=parolatekrarEdittext.getText().toString();
        if(email.isEmpty()){
            emailEdittext.setError("Email gerekli lütfen email giriniz");
            emailEdittext.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdittext.setError("Bir email adresi giriniz!");
            emailEdittext.requestFocus();
            return;
        }
        if (parola.isEmpty()){
            parolaEdittext.setError("Parola gerekli!");
            parolaEdittext.requestFocus();
            return;
        }
        if(parola.length()<6){
            parolaEdittext.setError("parola uzunluğu minimum 6 olmalıdır!");
            parolaEdittext.requestFocus();//Hatalı alana cursor gider
            return;
        }
        if(!parolatekrar.equals(parola)){
            parolatekrarEdittext.setError("Parolalar aynı olmalıdır");
            parolatekrarEdittext.requestFocus();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Başarılı", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(getApplicationContext(),"Böyle bir kayıt bulunmaktadır",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Başarısız"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}
