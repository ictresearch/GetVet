package com.pusatict.getvet.datalistadapter;

/**
 * Created by bronky on 11/08/2015.
 */
public class thread {
    private Long id;
    private String judul;
    private String isi;
    private String uid;
    private String spid;
    private String katid;
    private String date;
    private String Namauser;
    private String Comment;
    private String foto;

    public thread(){
        super();
    }

    public thread(Long id, String judul, String isi) {
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

    public String getKatid() {
        return katid;
    }

    public void setKatid(String katid) {
        this.katid = katid;
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
}
