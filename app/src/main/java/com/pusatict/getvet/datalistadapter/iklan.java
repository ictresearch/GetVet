package com.pusatict.getvet.datalistadapter;

public class iklan {
	private String tikid;
	private String tglselesai;
	private String html;
	private String mulaijam;
	private String selesaijam;
	private String judul;
	private String head;
	private String order;
	public  iklan(){
		super();
	}

	public iklan(String tikid,String tglselesai, String html, String mulaijam, String selesaijam, String judul, String head) {
		this.tikid = tikid;
		this.tglselesai = tglselesai;
		this.html = html;
		this.mulaijam = mulaijam;
		this.selesaijam = selesaijam;
		this.judul = judul;
		this.head = head;
	}

	public String getTikid() {
		return tikid;
	}

	public void setTikid(String tikid) {
		this.tikid = tikid;
	}

	public String getTglselesai() {
		return tglselesai;
	}

	public void setTglselesai(String tglselesai) {
		this.tglselesai = tglselesai;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getMulaijam() {
		return mulaijam;
	}

	public void setMulaijam(String mulaijam) {
		this.mulaijam = mulaijam;
	}

	public String getSelesaijam() {
		return selesaijam;
	}

	public void setSelesaijam(String selesaijam) {
		this.selesaijam = selesaijam;
	}

	public String getJudul() {
		return judul;
	}

	public void setJudul(String judul) {
		this.judul = judul;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}