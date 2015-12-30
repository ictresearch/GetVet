package com.pusatict.getvet.datalistadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.R;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bronky on 11/08/2015.
 */

public class ListAdapterComment extends BaseAdapter implements Filterable{
    private Context context;
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<Comment> list,filterd;
    private int index;
    private String cari1="",cari2="",cari3="",tgl;
    private Toast toast;
    private ImageButton imageico;

    public void setCari1(String cari1,String cari2,String cari3) {
        if(cari1.equals("")){
//            cari1="ict2015";
            cari1="";
        }
        if(cari2.equals("")){
//            cari2="ict2015";
            cari2="";
        }
        if(cari3.equals("")){
//            cari3="ict2015";
            cari3="";
        }
        this.cari1 = cari1;
        this.cari2 = cari2;
        this.cari3 = cari3;
    }

    public int getIndex() {
        return index;
    }
    public void add(Comment T) {
        list.add(T);
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public ListAdapterComment(Context context, List<Comment> list) {
        this.context=context;
        this.list=list;
        this.filterd=this.list;
    }

    @Override
    public int getCount() {
        return filterd.size();
    }

    @Override
    public Object getItem(int position) {
        return filterd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void dowloadgambar(String URL){
        String imageURL = URL;

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            imageico.setImageBitmap(bitmap);
//            Utils.saveImage(bitmap, fname, coba.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(this.context);
            convertView=inflater.inflate(R.layout.list_itemforum,null);
        }
        Comment thr =filterd.get(position);
//        TextView textnama_dokter=(TextView)convertView.findViewById(R.id.nama_dokter);
//        textnama_dokter.setText(thr.getJudul());
//        imageico=(ImageButton)convertView.findViewById(R.id.imgIco);
//        Log.e("foto",thr.getFoto()+"");
//        dowloadgambar(thr.getFoto());
        Spanned htmlAsSpanned = Html.fromHtml(thr.getIsi());
        TextView txtjudul=(TextView)convertView.findViewById(R.id.tvJudul);
        txtjudul.setText(thr.getJudul());
        TextView txtisi=(TextView)convertView.findViewById(R.id.tvisi);
        txtisi.setText(htmlAsSpanned);
//
//        WebView webView=(WebView)convertView.findViewById(R.id.tvIsi);
//        webView.getSettings().setJavaScriptEnabled(true);
//
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.loadDataWithBaseURL("file:///sdcard/dir", thr.getIsi(), "text/html", "utf-8", "");
//        //webView.loadUrl(ikn.getHtml());
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
////
        TextView textdetail_dokter=(TextView)convertView.findViewById(R.id.tvNama);
        textdetail_dokter.setText(thr.getNamauser());
        TextView txt_Com=(TextView)convertView.findViewById(R.id.tvComment);
        txt_Com.setText("");
        TextView txt_date=(TextView)convertView.findViewById(R.id.tvDate);
        try {
            Log.e("sasd",thr.getDate());
            Date tgl1=Ftgl.parse(thr.getDate());
            tgl=Ftgl.format(tgl1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txt_date.setText(tgl);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        MahasiswaFilter filter = new MahasiswaFilter();
        filter.setI(index);
        return filter;
    }

    /** Class filter untuk melakukan filter (pencarian) */
    private class MahasiswaFilter extends Filter {
        public int i;

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Comment> filteredData = new ArrayList<Comment>();
            FilterResults result = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            for(Comment mhs: list){
                //ori
                /*String Nama=mhs.getNama()+""+mhs.getNamaDok();
                String Alamat=mhs.getAlamat()+""+mhs.getKota();
                String Layanan=mhs.getPelayanan();
                if(Nama.toLowerCase().contains(cari1)&&Alamat.toLowerCase().contains(cari2)&&Layanan.toLowerCase().contains(cari3)){
                    filteredData.add(mhs);
                }*/
                //gabung
//                String Nama =mhs.getNama();
//                String NamaDok = mhs.getNamaDok();
//                String Alamat =mhs.getAlamat();
//                String Kota =mhs.getKota();
//                String Layanan =mhs.getPelayanan();
//                if((Nama.toLowerCase().contains(cari1)||NamaDok.toLowerCase().contains(cari1))&&
//                        (Alamat.toLowerCase().contains(cari2)||Kota.toLowerCase().contains(cari2))&&
//                        (Layanan.toLowerCase().contains(cari3))){
//                    filteredData.add(mhs);
//                }
                //sercing or
//                if(Nama.toLowerCase().contains(cari1)||Alamat.toLowerCase().contains(cari2)||Layanan.toLowerCase().contains(cari3)){
//                    filteredData.add(mhs);
//                }

            }
            result.count = filteredData.size();
            result.values =  filteredData;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterd = (List<Comment>) results.values;

            toast = Toast.makeText(context, "About " + filterd.size() + " results",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            notifyDataSetChanged();
        }

    }


}
