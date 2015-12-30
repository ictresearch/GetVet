package com.pusatict.getvet.datalistadapter;

import android.graphics.Bitmap;

/**
 * Created by bronky on 11/08/2015.
 */
public class Comment {
    private Long id;
    private String judul;
    private String isi;
    private String uid;
    private String spid;
    private Long thid;
    private String date;
    private String Namauser;
    private String Comment;
    private String foto;
    private Bitmap mfoto;

    public Comment(){
        super();
    }

    public Comment(Long id, String judul, String isi) {
        this.id = id;
        this.judul = judul;
        this.isi = isi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public Long getThid() {
        return thid;
    }

    public void setThid(Long thid) {
        this.thid = thid;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNamauser() {
        return Namauser;
    }

    public void setNamauser(String namauser) {
        Namauser = namauser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Bitmap getMfoto() {
        return mfoto;
    }

    public void setMfoto(Bitmap mfoto) {
        this.mfoto = mfoto;
    }
}
