package com.pusatict.getvet.datalistadapter;

/**
 * Created by bronky on 11/08/2015.
 */
public class PetShop {
    private Integer id;
    private String nama;
    private String alamat;
    private String detail;
    private String kota;
    private String pelayanan;
    private String jadwal;
    private String NamaDok;
    private String foto;

    public PetShop(){
        super();
    }

    public PetShop(Integer id, String nama, String alamat, String detail) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.detail = detail;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNamaDok() {
        return NamaDok;
    }

    public void setNamaDok(String namaDok) {
        NamaDok = namaDok;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getPelayanan() {
        return pelayanan;
    }

    public void setPelayanan(String pelayanan) {
        this.pelayanan = pelayanan;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }
}
