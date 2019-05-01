package com.example.travelbook.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment {
    private EditText noteeditText;
    private Button addnoteButton,returnButton;
    private String email="email";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    public AddNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   email= getArguments().getString("useremail");

        View view= inflater.inflate(R.layout.fragment_add_note, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference=firebaseDatabase.getReference().child("Notlar").child(firebaseUser.getUid());//Tablo ismi
        noteeditText=(EditText)view.findViewById(R.id.note_edittext);
        addnoteButton=(Button)view.findViewById(R.id.new_note_button);
        returnButton=(Button)view.findViewById(R.id.new_return_button);


        addnoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    databaseReference.push().setValue(noteeditText.getText().toString());
                    Toast.makeText(getLayoutInflater().getContext(),"Başarılı",Toast.LENGTH_LONG).show();
                    noteeditText.setText("");

                }catch (Exception e){
                    Toast.makeText(getLayoutInflater().getContext(),"Hata oluştu",Toast.LENGTH_LONG).show();

                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteFragment fragment=new NoteFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentFrame,fragment);//yer değiştir tüm frgamentlerin yerleştiği ala ile note fragmenti değiştir,
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
