package com.example.travelbook.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelbook.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeProfileFragment extends Fragment {
    private ImageView profilephoto;
    private Button fotokaydet,fotosec;
    private int SELECT_IMAGE=1;
    private Uri imageUri;
    private String userid=null;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    public ChangeProfileFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_change_profile, container, false);
        profilephoto=(ImageView)view.findViewById(R.id.profile_foto_imageView);
        fotokaydet=(Button)view.findViewById(R.id.fotografkaydet_button);
        fotosec=(Button)view.findViewById(R.id.fotografsec_button);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference().child("profilFotograflari");//fotograflar için oluşturduğumuz klasör

        fotosec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select picture"),SELECT_IMAGE);
            }
        });


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        profilephoto.setImageBitmap(bitmap);
                        imageUri=data.getData();
                        kaydet();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void kaydet(){
        if(imageUri!=null){
            userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference imagePath=storageReference.child(userid).child(imageUri.getLastPathSegment());

            imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getLayoutInflater().getContext(),"Kaydedildi!",Toast.LENGTH_LONG).show();

                }
            });
        }
    }


}
