
package com.example.travelbook.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelbook.Adapter.Mekan;
import com.example.travelbook.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
        private StorageReference storageReference;
        private FirebaseStorage firebaseStorage;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReference;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
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

            firebaseStorage=FirebaseStorage.getInstance();
            storageReference=firebaseStorage.getReference().child("fotograflar");//fotograflar için oluşturduğumuz klasör
            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseUser=firebaseAuth.getCurrentUser();
            databaseReference=firebaseDatabase.getReference().child("LocationsDetails").child(firebaseUser.getUid());//Tablo ismi

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
                StorageReference imagePath = storageReference.child(userid).child(imageUri.getLastPathSegment());


                imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getLayoutInflater().getContext(), "!", Toast.LENGTH_LONG).show();


                    }
                });
            }
        }

        public void onClick(View view) {
            baslik=mekanadi.getText().toString();
            aciklama=mekanaciklama.getText().toString();
            databaseReference.push().setValue(baslik);
            databaseReference.push().setValue(aciklama);
            Toast.makeText(getLayoutInflater().getContext(),"Kaydedildi",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);


        }
    }

