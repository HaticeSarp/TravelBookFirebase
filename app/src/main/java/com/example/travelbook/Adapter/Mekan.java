package com.example.travelbook.Adapter;

public class Mekan {
    private int resimId;
    private String mekanadi;
    private String mekanaciklama;

    public Mekan(int resimId, String mekanadi, String mekanaciklama) {
        this.resimId = resimId;
        this.mekanadi = mekanadi;
        this.mekanaciklama = mekanaciklama;
    }

    public int getResimId() {
        return resimId;
    }

    public void setResimId(int resimId) {
        this.resimId = resimId;
    }

    public String getMekanadi() {
        return mekanadi;
    }

    public void setMekanadi(String mekanadi) {
        this.mekanadi = mekanadi;
    }

    public String getMekanaciklama() {
        return mekanaciklama;
    }

    public void setMekanaciklama(String mekanaciklama) {
        this.mekanaciklama = mekanaciklama;
    }

}
