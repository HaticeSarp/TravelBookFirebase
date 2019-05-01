package com.example.travelbook.Fragment;


import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.travelbook.Adapter.Mekan;
import com.example.travelbook.Adapter.MekanAdapter;
import com.example.travelbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ListView listview;
    private MekanAdapter mekanAdapter;
    private List<String> mekanListesi=new ArrayList<>();
    private List<Image> mekanFoto=new ArrayList<>();
    private DatabaseReference databaseReference,databaseReference1;
    private FirebaseDatabase firebaseDatabase,firebaseDatabase1;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getLayoutInflater().getContext(),
                android.R.layout.simple_list_item_1,mekanListesi);//veri tabanındaki notları çekip buraya atacağız notlar diye dizi oluşturup onu atıcaz burya

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseDatabase1=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("LocationDetails");

        //databaseReference=firebaseDatabase.getReference("LocationDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()) {
                    mekanListesi.add(data.getValue().toString());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listview=(ListView)view.findViewById(R.id.listView);
        listview.setAdapter(adapter);
         listview.setAdapter(mekanAdapter);

        return view;
    }

}
