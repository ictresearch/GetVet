package com.pusatict.getvet.forum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.Comment;
import com.pusatict.getvet.datalistadapter.ListAdapterComment1;
import com.pusatict.getvet.sponsor.Banner;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.tesKoneksiInet;

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
 * Created by ICT on 31/08/2015.
 */
public class ViewCommentList extends Activity {
    private ListAdapterComment1 adapter;
    private List<Comment> list;
    private Comment selectedList;
    JSONParser jsonParser = new JSONParser();

    private ListView lvVetList;
    private ImageButton imgBack, imgHome, imgAdd;
    private ProgressDialog pDialog;
    private tesKoneksiInet koneksiInet;
    private Toast toast;
    private boolean flag_loading;
    private int offset=0;


    DBAdapter db=new DBAdapter(ViewCommentList.this);
    private String link,urlgambar,uid,tanggal;;
    private int poin,poin2=0,poin1=4;
    private ImageView imgBanner;
    Calendar c1 = Calendar.getInstance();
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_view_comment_list);

        lvVetList = (ListView)findViewById(R.id.lvVetList);
        list= new ArrayList<Comment>();

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);
        imgBanner =(ImageView)findViewById(R.id.imgBanner);
//        imgAdd = (ImageButton)findViewById(R.id.imgAdd);
        tanggal = Ftgl.format(c1.getTime());
        db.open();
        Cursor c1 = db.getContactbanner(1);
        if (c1.moveToFirst()) {
            poin = c1.getInt(1) + 1;
        }
        Cursor c = db.getContact(1);
        if (c.moveToFirst()) {
            uid = c.getString(5);
        }
        db.close();


        adapter = new ListAdapterComment1(getApplicationContext(), list);
        new MainActivityAsync().execute("load");
        lvVetList.setAdapter(adapter);
        new MainActivityAsync1().execute();
        isiBtnClick();
//        fillListView();
    }

    public void isiBtnClick(){
        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCommentList.this, Banner.class);
                Bundle Parsing = new Bundle();
//                Parsing.putString("banner", "http://www.animalrapidtest.com/");
                Parsing.putString("banner", link);
                Parsing.putString("broadcash", "0");
                Parsing.putString("tdid", "");
                intent.putExtras(Parsing);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCommentList.this, forum_main.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        lvVetList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    new MainActivityAsync().execute("load");
                }
                i = i + 1;
                Log.e("roll", "septi" + i);

            }
        }
    });

    }
    public Bitmap dowloadgambar(String URL){
        String imageURL = URL;
        Bitmap bitmap = null;
        if(!URL.equals("null")) {

            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
//            Utils.saveImage(bitmap, fname, coba.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            bitmap=null;
        }
        return bitmap;
    }
    public void fillListView(){
        lvVetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList = (Comment) lvVetList.getAdapter().getItem(position);
                String stv = selectedList.getId().toString();
                if(!stv.equals("0")) {
                    munculDialog(position, stv);
                }
            }
        });
    }

    private void munculDialog(int i,final String txt) {
        final CharSequence[] items = {"Edit", "Delete"};
        selectedList = (Comment) lvVetList.getAdapter().getItem(i);
        AlertDialog.Builder kk = new AlertDialog.Builder(this);
        kk.setTitle(" Choose Action ");
        kk.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Edit")){
                    Intent myintent = new Intent(ViewCommentList.this, ForumCommentAdd.class);
                    Bundle Parsing = new Bundle();
                    Parsing.putString("id", selectedList.getId().toString());
                    Parsing.putString("judul", selectedList.getJudul().toString());
                    Parsing.putString("isi", selectedList.getIsi().toString());
                    Parsing.putString("thid", selectedList.getThid().toString());
                    Parsing.putString("foto",selectedList.getFoto().toString());
                    Parsing.putString("pos", "1");
                    myintent.putExtras(Parsing);
                    startActivity(myintent);
                    finish();
                    overridePendingTransition(R.animator.activity_in, R.animator.activity_out);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewCommentList.this);
                    builder.setMessage("Are you sure want to delete ''"+ selectedList.getJudul().toString() +"'' ?").
                            setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            new MainActivityAsync().execute("delete", txt);
                                    /*Intent myintent = new Intent(VetList.this, VetList.class);
                                    startActivity(myintent);*/
                            startActivity(getIntent());
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        }).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private class MainActivityAsync extends AsyncTask<String, Void, String> {
        DBAdapter db = new DBAdapter(ViewCommentList.this);
        String uid,success,thid;
        Bundle Parsing=getIntent().getExtras();
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewCommentList.this);
            pDialog.setMessage(" Processing... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if(params[0] == "delete"){
                String url="http://getsimplebisnis.com/index.php/api/hapuscomment";
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("comid", params[1]));
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
                try {
                    success = json.getString("hapus");
                }catch (JSONException e) {

                }
            }else{
                thid=Parsing.getString("thid");
                String url="http://getsimplebisnis.com/index.php/api/commentbyid?offset="+offset;
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("thid", thid));
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
                try {
                    success="1";
                    JSONArray jsonArray = json.getJSONArray("list");
                    Comment thr = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        thr = new Comment();
                        thr.setId(obj.getLong("comid"));
                        thr.setJudul(obj.getString("comJudul"));
                        thr.setIsi(obj.getString("comIsi"));
                        thr.setUid(obj.getString("uid"));
                        thr.setSpid(obj.getString("spid"));
                        thr.setThid(obj.getLong("thid"));
                        thr.setDate(obj.getString("comtanggalPost"));
                        thr.setComment("");
                        thr.setNamauser(obj.getString("unama"));
                        thr.setFoto(obj.getString("comfoto"));
                        if(!obj.getString("comfoto").equals("null")){
                            thr.setMfoto(dowloadgambar(obj.getString("comfoto")));
                        }

                        if(!obj.getString("uid").equals("")){
                            flag_loading=false;
                        }
                        Log.e("bitmap",thr.getMfoto()+"");
                        adapter.add(thr);
                    }
                } catch (JSONException e) {
                    Log.e("asem", list.toString());
                    success="0";
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            offset=offset+10;
//            adapter = new ListAdapterComment1(getApplicationContext(), list);
//            lvVetList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    private class MainActivityAsync1 extends AsyncTask<String, Void, String> {

        String succes="0",succes1="0";
        Bitmap bitmap = null;
        int id;
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(forum_main.this);
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
                        dowloadgambar1(urlgambar);

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
                    imgBanner.setVisibility(View.GONE);
                    float scaleDp = getResources().getDisplayMetrics().density;
                    int pixelDp10 = (int) (10 * scaleDp + 0.5f);
//                    lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp10);
                }
            }else{
                db.open();
                db.updatebanner(1, poin);
                db.close();
                imgBanner.setVisibility(View.VISIBLE);
                float scaleDp = getResources().getDisplayMetrics().density;
                int pixelDp10 = (int) (10 * scaleDp + 0.5f);
                int pixelDp = (int) (80 * scaleDp + 0.5f);
//                lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp);
            }

        }
    }
    public void dowloadgambar1(String URL){
        String imageURL = URL;

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            imgBanner.setImageBitmap(bitmap);
//            Utils.saveImage(bitmap, fname, coba.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
