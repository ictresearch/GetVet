package com.pusatict.getvet.datalistadapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.R;
import com.pusatict.getvet.tool.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bronky on 11/08/2015.
 */

public class ListAdapterComment1 extends BaseAdapter implements Filterable{
    private Context context;
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<Comment> list,filterd;
    private int index;
    private String cari1="",cari2="",cari3="",tgl;
    private Toast toast;
    private ImageView imgvw;
    private String fname;
    private Bitmap bitmap;

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

    public ListAdapterComment1(Context context, List<Comment> list) {
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

    public void decodeFile(String filePath) {
        Log.e("patc", filePath);
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        // Show the Selected Image on ImageView
//        tvPhoto.setVisibility(View.VISIBLE);
//        btnClearPhoto.setVisibility(View.VISIBLE);


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(this.context);
            convertView=inflater.inflate(R.layout.list_itemcomment,null);
        }
        Comment thr =filterd.get(position);
        imgvw=(ImageView)convertView.findViewById(R.id.imgcom);
        if(!thr.getFoto().equals("null")){
            imgvw.setVisibility(View.VISIBLE);
            imgvw.setImageBitmap(thr.getMfoto());
        }else{
            imgvw.setVisibility(View.GONE);

        }

        Spanned htmlAsSpanned = Html.fromHtml(thr.getIsi());
        TextView txtjudul=(TextView)convertView.findViewById(R.id.tvJudul);
        if(thr.getJudul().trim().equals("")){
            txtjudul.setVisibility(View.GONE);
        }else{
            txtjudul.setText(thr.getJudul());
        }
        TextView txtisi=(TextView)convertView.findViewById(R.id.tvIsi);
        txtisi.setText(htmlAsSpanned);
        TextView txtjudulth=(TextView)convertView.findViewById(R.id.tvJudulth);
        if(thr.getComment().trim().equals("")){
            txtjudulth.setVisibility(View.GONE);
        }else{
            txtjudulth.setText("Commented on '"+thr.getComment()+"'");
        }
        TextView txtNama=(TextView)convertView.findViewById(R.id.tvNama);
        txtNama.setText(thr.getNamauser());
        TextView txt_date=(TextView)convertView.findViewById(R.id.tvDate);
        try {
//
            Date tgl1=Ftgl.parse(thr.getDate());
            Log.e("sasd",thr.getDate());
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
