package com.pusatict.getvet.forum;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pusatict.getvet.AnalyticsApplication;
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
public class Tab2 extends Fragment {
    private ListAdapterThread adapter;
    private ListView lvkatforum;
    private ProgressDialog pDialog2;
    private List<thread> list;
    private int offset;
    private int posisi=0;
    private JSONParser jsonParser= new JSONParser();
    private thread selectedList;
    private boolean flag_loading=false;
    private Button btnCari;
    private EditText etxCari;

    private String link,urlgambar,uid,tanggal;
    private int poin,poin2=0,poin1=4;
    private ImageView imgBanner;
    Calendar c1 = Calendar.getInstance();
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");
    int i=0;
    private Tracker mTracker;

    //
//    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_recent, container, false);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
//        return v;
//    }
//        DBAdapter db=new DBAdapter(getActivity());
//        imgBanner =(ImageView)v.findViewById(R.id.imgBanner);
//        tanggal=Ftgl.format(c1.getTime());
//        db.open();
//        Cursor c1 = db.getContactbanner(1);
//        if (c1.moveToFirst()) {
//            poin=c1.getInt(1)+1;
//        }
//        Cursor c = db.getContact(1);
//        if (c.moveToFirst()) {
//            uid=c.getString(5);
//        }
//        db.close();
        lvkatforum=(ListView)v.findViewById(R.id.lvKatforum);
        btnCari=(Button)v.findViewById(R.id.btnCari);
        etxCari=(EditText)v.findViewById(R.id.etxCari);
        list=new ArrayList<thread>();
        adapter = new ListAdapterThread(v.getContext(), list);
//        new selectsubkat().execute("1");
        new selectKategori().execute();
//        if(i==1){
//            new MainActivityAsync1().execute();
//            i++;
//        }

        lvkatforum.setAdapter(adapter);
        lvkatforum.setOnScrollListener(new AbsListView.OnScrollListener() {
            int i = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        Log.e("roll", offset + "");
                        new selectKategori().execute();
                    }
                    i = i + 1;
                    Log.e("roll", "septi" + i);

                }
            }
        });
//        v.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                    return true;
//                }
//                return false;
//            }
//        });
        fillListView();
        return v;
    }
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
        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offset=0;
                list=new ArrayList<thread>();
                adapter = new ListAdapterThread(getActivity(), list);
                new selectKategori().execute();
                lvkatforum.setAdapter(adapter);
                Log.e("btnCari", etxCari.getText().toString());
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Search forum recent")
                        .setAction(etxCari.getText().toString())
                        .build());
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {

                }
            }
        });
    }
    private class selectKategori extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog2 = new ProgressDialog(getActivity());
//            pDialog2.setMessage("2");
//            pDialog2.setIndeterminate(true);
//            pDialog2.setCancelable(false);
//            pDialog2.show();
        }
        @Override
        protected String doInBackground(String... params) {
//                String url="http://10.0.2.2/klinikhewan/select_all.php";
//                String url="http://192.168.0.103/klinikhewan/selectkliniklimit";
            String url="http://getsimplebisnis.com/index.php/api/threadrecent";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("offset", offset+""));
            nvp.add(new BasicNameValuePair("cari", etxCari.getText().toString().trim()+""));
//
            JSONObject json = jsonParser.makeHttpRequest(url, "GET", nvp);
//            JSONParser jParser = new JSONParser();
//            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                JSONArray jsonArray = json.getJSONArray("RC");
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
                    Log.e("septi",obj.getString("uid"));
                    if(!obj.getString("uid").equals("")){
                        flag_loading=false;
                    }
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
//            pDialog2.dismiss();

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
//   private class MainActivityAsync1 extends AsyncTask<String, Void, String> {
//
//    String succes="0",succes1="0";
//    Bitmap bitmap = null;
//    int id;
//    protected void onPreExecute() {
//        super.onPreExecute();
////        pDialog2 = new ProgressDialog(getActivity());
////            pDialog2.setMessage("Finding Data... ");
////            pDialog2.setIndeterminate(true);
////            pDialog2.setCancelable(false);
////            pDialog2.show();
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
////            String jam=waktu();
//        String url="http://getsimplebisnis.com/index.php/api/baner";
//        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
//        nvp = new ArrayList<NameValuePair>();
//        nvp.add(new BasicNameValuePair("tikorder", poin+""));
//        nvp.add(new BasicNameValuePair("tikmulai", tanggal));
//        nvp.add(new BasicNameValuePair("uid", uid));
//        Log.e("benner", poin + "," + uid+","+tanggal);
//        JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
//
//        try {
//            succes1=json.getString("success");
//            Log.e("benner", succes1);
//            if(succes1.equals("1")){
//
//                JSONArray jsonArray = json.getJSONArray("baner");
//                for(int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject obj = jsonArray.getJSONObject(i);
//                    link = (obj.getString("tikhtml"));
//                    urlgambar = (obj.getString("tikgambar1"));
//                    poin=(obj.getInt("tikorder"));
//                    Log.e("benner", link + "," + urlgambar);
//                    dowloadgambar(urlgambar);
//
//                }
//            }
//        }catch (JSONException e) {
//            Log.e("asem", e.toString());
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        DBAdapter db=new DBAdapter(getActivity());
////            pDialog2.dismiss();
////            db.open();
////            db.updatebanner(1, 0);
////            db.close();
//        if(succes1.equals("0")){
//
//            Log.e("benner1", succes1);
//            db.open();
//            db.updatebanner(1, 0);
//            if(poin>1) {
//                Cursor c1 = db.getContactbanner(1);
//                if (c1.moveToFirst()) {
//                    poin = c1.getInt(1) + 1;
//                }
//                db.close();
//                new MainActivityAsync1().execute();
//            } else {
//                imgBanner.setVisibility(View.GONE);
//                float scaleDp = getResources().getDisplayMetrics().density;
//                int pixelDp10 = (int) (10 * scaleDp + 0.5f);
////                    lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp10);
//            }
//        }else{
//            db.open();
//            db.updatebanner(1, poin);
//            db.close();
//            imgBanner.setVisibility(View.VISIBLE);
//            float scaleDp = getResources().getDisplayMetrics().density;
//            int pixelDp10 = (int) (10 * scaleDp + 0.5f);
//            int pixelDp = (int) (80 * scaleDp + 0.5f);
////                lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp);
//        }
//
//    }
//}
//    public void dowloadgambar(String URL){
//        String imageURL = URL;
//
//        Bitmap bitmap = null;
//        try {
//            // Download Image from URL
//            InputStream input = new java.net.URL(imageURL).openStream();
//            // Decode Bitmap
//            bitmap = BitmapFactory.decodeStream(input);
//            imgBanner.setImageBitmap(bitmap);
////            Utils.saveImage(bitmap, fname, coba.this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}