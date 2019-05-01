package com.example.travelbook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbook.Adapter.Mekan;
import com.example.travelbook.R;

import java.util.List;

public class MekanAdapter extends BaseAdapter {
    List<Mekan> mekanList;
    LayoutInflater layoutInflater;

    public MekanAdapter(LayoutInflater layoutInflater, List<Mekan> mekanListesi) {
        this.mekanList = mekanListesi;
        this.layoutInflater = layoutInflater;
    }


    @Override
    public int getCount() {
        return mekanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mekanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mekanView= layoutInflater.inflate(R.layout.satir,null);
        ImageView sehirfotografi=mekanView.findViewById(R.id.sehirFotografi);//satır.xml deki sehir fotografını cekiyoruz
        TextView sehiradi=mekanView.findViewById(R.id.mekanadi);
        TextView sehiraciklama=mekanView.findViewById(R.id.mekanaciklama);


        sehirfotografi.setImageResource(mekanList.get(position).getResimId());//ilk satırdaki resmi alıcak
        sehiradi.setText(mekanList.get(position).getMekanadi());
        sehiraciklama.setText(mekanList.get(position).getMekanaciklama());//elimizdeki listeden satir.xmle doldurma işlemi yapıyoruz
        return mekanView;
    }
}
