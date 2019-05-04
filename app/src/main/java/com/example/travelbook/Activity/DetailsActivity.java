
package com.example.travelbook.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelbook.Adapter.Mekan;
import com.example.travelbook.Model.DetailsModel;
import com.example.travelbook.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    public class DetailsActivity extends AppCompatActivity {
        private ImageView kamera, lokasyonresim;
        private TextView kameratextView;
        private EditText mekanaciklama, mekanadi;
        private Button kamerayaBaglan, kaydet;
        private Uri imageUri;
        private ListView listview;

        private String imagelink="";

        private DetailsModel dmodel;

        private FirebaseStorage firebaseStorage;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReference;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
        private StorageReference storageRef;
        private FirebaseStorage storage;
        private String userid=null,baslik,aciklama;
        List<Mekan> mekanListesi = new ArrayList<>();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_details);

            mekanListesi.add(new Mekan(R.drawable.foto1, "", ""));


            kamera = (ImageView) findViewById(R.id.kamerayaBaglan);
            lokasyonresim = (ImageView) findViewById(R.id.sehirFotografi);
            mekanadi = (EditText) findViewById(R.id.mekanadi);
            mekanaciklama = (EditText) findViewById(R.id.mekanaciklama);
            kamerayaBaglan = (Button) findViewById(R.id.kamerayabaglanButton);
            kaydet = (Button) findViewById(R.id.kaydetButton);
            kameratextView = (TextView) findViewById(R.id.textView2);

            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            firebaseStorage=FirebaseStorage.getInstance();
            storage=FirebaseStorage.getInstance();
            storageRef=storage.getReference();

            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseUser=firebaseAuth.getCurrentUser();
            databaseReference=firebaseDatabase.getReference().child("LocationsDetails");//Tablo ismi

            kamerayaBaglan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "select picture"), 0);//0->fotoraf çek 1ise video çek şeklinde key verdil

                }
            });


        }
        public void onActivityResult(int requestCode,int resultCode, @Nullable Intent data)
            {
                super.onActivityResult(requestCode, resultCode, data);
                switch (requestCode) {
                    case 0:

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                            lokasyonresim.setImageBitmap(bitmap);
                            imageUri = data.getData();
                             kaydet();
                            kamera.setVisibility(View.GONE);
                            kameratextView.setVisibility(View.GONE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                }
            }


        public void kaydet() {
            if (imageUri != null) {
                userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final StorageReference imagePath = storageRef.child(userid).child("images/"+(imageUri.getLastPathSegment()));

                imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getLayoutInflater().getContext(), "!", Toast.LENGTH_LONG).show();
                    }
                });
//resim url alma
                imagePath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return imagePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downUri = task.getResult();
                            imagelink=downUri.toString();
                        }
                    }
                });
            }
        }

        public void onClick(View view) {
            baslik=mekanadi.getText().toString();
            aciklama=mekanaciklama.getText().toString();
            insertDetails(baslik,aciklama);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        public void insertDetails(String baslik, String aciklama)
        {
            try {
                String id = databaseReference.push().getKey();
                dmodel = new DetailsModel(id, baslik, aciklama,imagelink);
                databaseReference.child(id).setValue(dmodel);
                Toast.makeText(getLayoutInflater().getContext(), "Kaydedildi", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(getLayoutInflater().getContext(), "Problem...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

