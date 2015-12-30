package com.pusatict.getvet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pusatict.getvet.datalistadapter.ListAdapteriklan;
import com.pusatict.getvet.datalistadapter.iklan;

import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.Utils;
import com.pusatict.getvet.tool.tesKoneksiInet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by bronky on 20/08/2015.
 */
public class coba extends Activity {
    String fname="data1";
    String fname1="data1";
    public static Bitmap image;
    private List<iklan> list;
    public static ListAdapteriklan adapter;
    private ListView listview;
    String html=new String();
    private String gambar1,gambar2,judul,head,order;
    tesKoneksiInet koneksiInet;
    String tglselesai,tanggal,tanggal1,tgltenggang;
    DBAdapter db=new DBAdapter(coba.this);
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    int jumlahdata;
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");
    private ProgressDialog pDialog;
    private Tracker mTracker;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coba);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        koneksiInet = new tesKoneksiInet(getApplicationContext());
        html="iklan";
        tanggal=Ftgl.format(c1.getTime());
        listview=(ListView) findViewById(R.id.listView);
        list= new ArrayList<iklan>();

        db.open();
        Cursor c = db.getAllContact1();
        if (c.moveToFirst()) {
            do {
                tanggal1 = c.getString(1);
                try {
                    Date Localdate = Ftgl.parse(tanggal1);
                    Date Mobiledate = Ftgl.parse(tanggal);
                    if (Localdate.before(Mobiledate)) {
                        Date Localdate1 = Ftgl.parse(c.getString(3));
                        db.updateiklan(c.getInt(0),"0");
                        if(Localdate1.before(Mobiledate)) {
                            fname1 = "iklan" + c.getString(2) + "1";
                            Log.e("asik", c.getString(2));
                            deleteGambar(fname1);
                            fname1 = "iklan" + c.getString(2) + "2";
                            deleteGambar(fname1);
                            db.deleteContact(c.getInt(0));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }

        db.close();
        setOnClickbtn();
        if(koneksiInet.isNetworkAvailable()) {
            new MainActivityAsync().execute();

        }else{
            db.open();
            list=db.getAllToDos(waktu());
            adapter = new ListAdapteriklan(getApplicationContext(), list);
            listview.setAdapter(adapter);
            db.close();
        }
//        mTracker.setScreenName("Home");
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }
    public void deleteGambar(String name){
        String patch=coba.this.getFilesDir()+"/dir";
        File dir = new File(patch);

        new File(dir,name+".jpg").delete();
    }
    public void setOnClickbtn(){
        ImageButton btnclost=(ImageButton)findViewById(R.id.imgClose);
        btnclost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(coba.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
                finish();
            }
        });
    }
    public void dowloadgambar(String URL){
        String imageURL = URL;

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            Utils.saveImage(bitmap, fname, coba.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(coba.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        finish();
    }
    public String waktu(){
        Date date1=c1.getTime();
        int hours=date1.getHours();
        String hours1;
        int menit=date1.getMinutes();
        String menit1;
        if(hours<10){
            hours1="0"+hours;
        }else{
            hours1=""+hours;
        }
        if(menit<10){
            menit1="0"+menit;
        }else{
            menit1=""+menit;
        }
        String jam=hours1+":"+menit1+":00";
        return jam;
    }
    private class MainActivityAsync extends AsyncTask<String, Void, String> {

        String mulaijam,selesaijam;
        Bitmap bitmap = null;
        int id;
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(coba.this);
//            pDialog.setMessage("Finding Data... ");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String jam=waktu();
            String url="http://getsimplebisnis.com/index.php/api/iklan?jam="+jam+"&tgl="+tanggal;
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);

            iklan ikn=null;
            try {
                JSONArray jsonArray = json.getJSONArray("iklan");
                for(int i = 0; i < jsonArray.length(); i++){

                    JSONObject obj = jsonArray.getJSONObject(i);
                    id=obj.getInt("id");
                    html = obj.getString("html");
                    gambar1=obj.getString("gambar1");
                    gambar2=obj.getString("gambar2");
                    mulaijam=obj.getString("jam_mulai");
                    selesaijam=obj.getString("jam_selesai");
                    judul=obj.getString("judul");
                    head=obj.getString("head");
                    tglselesai=obj.getString("selesai");
                    order=obj.getString("order");
                    if(!gambar1.equals("null")){
                        fname="iklan"+id+"1";
                        dowloadgambar(gambar1);
                        html=html.replace("<img src=imag1>","<center><img src=imag1 width=\"100%\"></center>");
                        html=html.replace("imag1", "\"file://"+coba.this.getFilesDir().toString()+"/dir/iklan"+id+"1.jpg\" ");
                    }
                    if(!gambar1.equals("null")){
                        fname="iklan"+id+"2";
                        dowloadgambar(gambar2);
                        html=html.replace("<img src=imag2>","<center><img src=imag2 width=\"100%\"></center>");
                        html=html.replace("imag2", "\"file://"+coba.this.getFilesDir().toString()+"/dir/iklan"+id+"2.jpg\"");
                    }
                    Log.e("as", html);
                    ikn = new iklan();
                    ikn.setHtml(html);
                    list.add(ikn);
                    if(!html.equals("iklan")){
                        db.open();
                        int jml=db.getCountTikid(""+id);
                        try {
                            c2.setTime(Ftgl.parse(tglselesai));
                            c2.add(Calendar.DATE, 3);
                            tgltenggang=Ftgl.format(c2.getTime());

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(jml<=0){
                            db.insertCustomer1(tglselesai, html, mulaijam,selesaijam,""+id,tgltenggang,judul,head,"1",order);
                        }else{
                            db.updateiklanall(""+id,tglselesai, html, mulaijam,selesaijam,tgltenggang,judul,head,"1",order);
                        }
                        db.close();
                    }
                }
            }catch (JSONException e) {
                Log.e("asem",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pDialog.dismiss();
            if(!html.equals("iklan")) {
                adapter = new ListAdapteriklan(getApplicationContext(), list);
                listview.setAdapter(adapter);
            }
        }
    }
}