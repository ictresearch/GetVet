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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.catken.CatkenAdd;
import com.pusatict.getvet.datalistadapter.CatKen;
import com.pusatict.getvet.datalistadapter.Comment;
import com.pusatict.getvet.datalistadapter.ListAdapterCatKen;
import com.pusatict.getvet.datalistadapter.ListAdapterComment;
import com.pusatict.getvet.datalistadapter.ListAdapterComment1;
import com.pusatict.getvet.datalistadapter.ListAdapterKatforum;
import com.pusatict.getvet.datalistadapter.ListAdapterThread;
import com.pusatict.getvet.datalistadapter.thread;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.tesKoneksiInet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ICT on 31/08/2015.
 */
public class CommentList extends Activity {
    private ListAdapterComment1 adapter;
    private List<Comment> list;
    private Comment selectedList;
    JSONParser jsonParser = new JSONParser();

    private ListView lvVetList;
    private ImageButton imgBack, imgHome, imgAdd;
    private ProgressDialog pDialog;
    private tesKoneksiInet koneksiInet;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_comment_list);

        lvVetList = (ListView)findViewById(R.id.lvVetList);
        list= new ArrayList<Comment>();

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);
//        imgAdd = (ImageButton)findViewById(R.id.imgAdd);

        isiBtnClick();
        new MainActivityAsync().execute("load");
        fillListView();
    }

    public void isiBtnClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentList.this, forum_main.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

//        imgAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CommentList.this, CatkenAdd.class);
//                Bundle Parsing = new Bundle();
//                Parsing.putString("pos", "0");
//                intent.putExtras(Parsing);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
//            }
//        });

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
        final CharSequence[] items = {"Edit", "Delete","Go to the thread"};
        selectedList = (Comment) lvVetList.getAdapter().getItem(i);
        AlertDialog.Builder kk = new AlertDialog.Builder(this);
        kk.setTitle(" Choose Action ");
        kk.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Edit")){
                    Intent myintent = new Intent(CommentList.this, ForumCommentAdd.class);
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

                }else if(items[item].equals("Delete")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentList.this);
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
                }else{
                    new selectThreade().execute(selectedList.getThid().toString());
                }
            }
        }).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private class MainActivityAsync extends AsyncTask<String, Void, String> {
        DBAdapter db = new DBAdapter(CommentList.this);
        String uid,success;
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CommentList.this);
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
                db.open();
                Cursor c=db.getContact(1);
                if (c.moveToFirst()) {
                    uid=c.getString(5);
                }
                db.close();
                String url="http://getsimplebisnis.com/index.php/api/commentbyuid";
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("uid", uid));
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
                        thr.setComment(obj.getString("thjudul"));
                        thr.setNamauser(obj.getString("unama"));
                        thr.setFoto(obj.getString("comfoto"));
                        if(!obj.getString("comfoto").equals("null")){
                            thr.setMfoto(dowloadgambar(obj.getString("comfoto")));
//                            Log.e("bitmap",thr.getMfoto()+"");
                        }
                        list.add(thr);
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
            adapter = new ListAdapterComment1(getApplicationContext(), list);
            lvVetList.setAdapter(adapter);
        }
    }
    private class selectThreade extends AsyncTask<String, Void, String> {
        String uid,success,thjudul,thisi,thuid,thspid,thkatid,thtanggalpost,thcom,thunama,thfoto;
        private long thid;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CommentList.this);
            pDialog.setMessage(" Processing... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
                String url="http://getsimplebisnis.com/index.php/api/thread";
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("thid", params[0]));
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
                try {
                    success="1";
                    JSONArray jsonArray = json.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        thid=(obj.getLong("thid"));
                        thjudul=(obj.getString("thjudul"));
                        thisi=(obj.getString("thisi"));
                        thuid=(obj.getString("uid"));
                        thspid=(obj.getString("spid"));
                        thkatid=(obj.getString("katid"));
                        thtanggalpost=(obj.getString("thtanggalPost"));
                        thcom=(obj.getString("com"));
                        thunama=(obj.getString("unama"));
                        thfoto=(obj.getString("thfoto"));
                    }
                } catch (JSONException e) {
                    success="0";
                }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            Intent intent = new Intent(CommentList.this, ForumDetails.class);
            Bundle Parsing = new Bundle();
            Parsing.putString("thid",thid+"");
            Parsing.putString("judul", thjudul);
            Parsing.putString("isi", thisi);
            Parsing.putString("uid", thuid);
            Parsing.putString("katid", thkatid);
            Parsing.putString("date", thtanggalpost);
            Parsing.putString("namauser", thunama);
            Parsing.putString("jumcomment", thcom);
            Parsing.putString("foto", thfoto);
            Parsing.putString("to", "0");
            intent.putExtras(Parsing);
            startActivity(intent);
            finish();
            overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        }
    }
}
