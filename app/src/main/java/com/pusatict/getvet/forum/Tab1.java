package com.pusatict.getvet.forum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.Katforum;
import com.pusatict.getvet.datalistadapter.ListAdapterKatforum;
import com.pusatict.getvet.datalistadapter.ListAdapterThread;
import com.pusatict.getvet.datalistadapter.thread;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab1 extends Fragment {
    private ListAdapterThread adapter;
    private ListView lvkatforum;
    private ProgressDialog pDialog;
    private List<thread> list;
    private String offset;
    private int posisi=0;
    private JSONParser jsonParser= new JSONParser();
    private thread selectedList;
    private Button btnadd;
    private boolean flag_loading=false;


    private String link,urlgambar,uid,tanggal;
    private int poin,poin2=0,poin1=4;
    private ImageView imgBanner1;
    Calendar c1 = Calendar.getInstance();
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");

//    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_hot, container, false);
//        return v;
//    }
//        btnadd=(Button)v.findViewById(R.id.add);
//        if(posisi==0) {
//            imgBanner1 = (ImageView) v.findViewById(R.id.imgbb1);
//            tanggal = Ftgl.format(c1.getTime());
//            DBAdapter db = new DBAdapter(getActivity());
//            db.open();
//            Cursor c1 = db.getContactbanner(1);
//            if (c1.moveToFirst()) {
//                poin = c1.getInt(1) + 1;
//            }
//            Cursor c = db.getContact(1);
//            if (c.moveToFirst()) {
//                uid = c.getString(5);
//            }
//            db.close();
//        }
        lvkatforum=(ListView)v.findViewById(R.id.lvKatforum);
        list=new ArrayList<thread>();
        adapter = new ListAdapterThread(v.getContext(), list);
//        new selectsubkat().execute("1");
        new selectKategori().execute();
//        if(posisi==0) {
//            new MainActivityAsync1().execute();
//        }
//        btnadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                addlist();
//                new selectKategori().execute();
//            }
//        });
        lvkatforum.setAdapter(adapter);
        fillListView();

//        v.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
//
        return v;
    }
////
    public void fillListView(){

        lvkatforum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList = (thread) lvkatforum.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ForumDetails.class);
                Bundle Parsing = new Bundle();
                Parsing.putString("thid", selectedList.getId().toString());
                Parsing.putString("judul", selectedList.getJudul().toString());
                Parsing.putString("isi", selectedList.getIsi().toString());
                Parsing.putString("uid", selectedList.getUid().toString());
                Parsing.putString("katid", selectedList.getKatid().toString());
                Parsing.putString("date", selectedList.getDate().toString());
                Parsing.putString("namauser", selectedList.getNamauser().toString());
                Parsing.putString("jumcomment", selectedList.getComment().toString());
                Parsing.putString("foto", selectedList.getFoto().toString());
                Parsing.putString("to", "0");
                intent.putExtras(Parsing);
                startActivity(intent);
//                getActivity().finish();
               getActivity().overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }
    private class selectKategori extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
//                String url="http://10.0.2.2/klinikhewan/select_all.php";
//                String url="http://192.168.0.103/klinikhewan/selectkliniklimit";
            String url="http://getsimplebisnis.com/index.php/api/hotthread";
//            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
//            nvp.add(new BasicNameValuePair("offset", offset+""));
//
//            JSONObject json = jsonParser.makeHttpRequest(url, "GET", nvp);
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                JSONArray jsonArray = json.getJSONArray("HT");
                thread thr=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    thr = new thread();
                    thr.setId(obj.getLong("thid"));
                    thr.setJudul(obj.getString("thjudul"));
                    thr.setIsi(obj.getString("thisi"));
                    thr.setUid(obj.getString("uid"));
                    thr.setSpid(obj.getString("spid"));
                    thr.setKatid(obj.getString("katid"));
                    thr.setDate(obj.getString("thtanggalPost"));
                    thr.setComment(obj.getString("com"));
                    thr.setNamauser(obj.getString("unama"));
                    thr.setFoto(obj.getString("thfoto"));
                    adapter.add(thr);
                    Log.d("eror", "data lengt: " + jsonArray.length());
                }
            }catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            adapter.notifyDataSetChanged();
            offset=offset+10;
            if(posisi==0){
                posisi=posisi+1;
            }

//            populateListView();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
    }
    private class MainActivityAsync1 extends AsyncTask<String, Void, String> {

    String succes="0",succes1="0";
    Bitmap bitmap = null;
    int id;
    protected void onPreExecute() {
        super.onPreExecute();
//        pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Finding Data... ");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
//            String jam=waktu();
        String url="http://getsimplebisnis.com/index.php/api/baner";
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("tikorder", poin+""));
        nvp.add(new BasicNameValuePair("tikmulai", tanggal));
        nvp.add(new BasicNameValuePair("uid", uid));
        Log.e("benner", poin + "," + uid+","+tanggal);
        JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);

        try {
            succes1=json.getString("success");
            Log.e("benner", succes1);
            if(succes1.equals("1")){

                JSONArray jsonArray = json.getJSONArray("baner");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    link = (obj.getString("tikhtml"));
                    urlgambar = (obj.getString("tikgambar1"));
                    poin=(obj.getInt("tikorder"));
                    Log.e("benner", link + "," + urlgambar);
                    dowloadgambar(urlgambar);

                }
            }
        }catch (JSONException e) {
            Log.e("asem", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        DBAdapter db=new DBAdapter(getActivity());
//            pDialog.dismiss();
//            db.open();
//            db.updatebanner(1, 0);
//            db.close();
        if(succes1.equals("0")){

            Log.e("benner1", succes1);
            db.open();
            db.updatebanner(1, 0);
            if(poin>1) {
                Cursor c1 = db.getContactbanner(1);
                if (c1.moveToFirst()) {
                    poin = c1.getInt(1) + 1;
                }
                db.close();
                new MainActivityAsync1().execute();
            } else {
                    imgBanner1.setVisibility(View.GONE);
                float scaleDp = getResources().getDisplayMetrics().density;
                int pixelDp10 = (int) (10 * scaleDp + 0.5f);
//                    lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp10);
            }
        }else{
            db.open();
            db.updatebanner(1, poin);
            db.close();
            imgBanner1.setVisibility(View.VISIBLE);
            float scaleDp = getResources().getDisplayMetrics().density;
            int pixelDp10 = (int) (10 * scaleDp + 0.5f);
            int pixelDp = (int) (80 * scaleDp + 0.5f);
//                lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp);
        }
        posisi=posisi+1;
    }
}
    public void dowloadgambar(String URL){
        String imageURL = URL;

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            imgBanner1.setImageBitmap(bitmap);
//            Utils.saveImage(bitmap, fname, coba.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
