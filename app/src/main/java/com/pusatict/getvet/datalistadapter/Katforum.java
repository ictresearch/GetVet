package com.pusatict.getvet.datalistadapter;

/**
 * Created by bronky on 11/08/2015.
 */
public class Katforum {
    private Integer id;
    private String nama;
    private String jumlah;


    public  Katforum(){
        super();
    }

    public Katforum(Integer id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
