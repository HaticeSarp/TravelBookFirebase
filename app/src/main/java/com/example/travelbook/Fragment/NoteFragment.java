package com.example.travelbook.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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
public class NoteFragment extends Fragment {

    private ImageView imageView;
    private ListView listView;
    private List<String> notlar=new ArrayList<>();
    String id;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    //activite lerde onCreate Fragmentlarda onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_note,container,false);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getLayoutInflater().getContext(),
                android.R.layout.simple_list_item_1,notlar);//veri tabanındaki notları çekip buraya atacağız notlar diye dizi oluşturup onu atıcaz burya




        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        id=firebaseUser.getUid();
        databaseReference=firebaseDatabase.getReference("Notlar").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override//yeni not ekleyince çalışır
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()) {

                    notlar.add(data.getValue().toString());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageView=(ImageView)view.findViewById(R.id.not_ekle_git);//frgamentlarda view üzerinden findviewı çekiyoruz
        listView=(ListView)view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteFragment fragment=new AddNoteFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentFrame,fragment);//yer değiştir tüm frgamentlerin yerleştiği ala ile note fragmenti değiştir,
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
