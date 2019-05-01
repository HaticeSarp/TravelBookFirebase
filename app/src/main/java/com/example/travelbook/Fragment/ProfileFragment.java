package com.example.travelbook.Fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    ImageView profileUserInstagram,profildegistirmeImageView;
    private  OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        View profileView=inflater.inflate(R.layout.fragment_profile,container,false);
        profildegistirmeImageView=(ImageView)profileView.findViewById(R.id.profile_degistirme_imageView);

        profileUserInstagram=(ImageView)profileView.findViewById(R.id.profile_user_instagram);

        profileUserInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramSayfasi();
            }
        });

        profildegistirmeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProfileFragment fragment=new ChangeProfileFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentFrame,fragment);//yer değiştir tüm frgamentlerin yerleştiği ala ile note fragmenti değiştir,
                fragmentTransaction.commit();
            }
        });
        return profileView;



    }
    public void instagramSayfasi(){
        Uri uri=Uri.parse("https://www.instagram.com/gezlist/");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        //  intent.setPackage("com.instagram.android");
        try{
            startActivity(intent);
            //startActivity();
        }catch(ActivityNotFoundException e){
            Toast.makeText(getLayoutInflater().getContext(),"Sayfa bulunamadı ",Toast.LENGTH_LONG).show();
        }

    }


    public void onButtonPressed(Uri uri){
        if(mListener!=null){
            mListener.onFragmentInteraction(uri);
        }
    }
    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Uri uri);
    }

}
