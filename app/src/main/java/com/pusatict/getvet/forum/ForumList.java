package com.pusatict.getvet.forum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.pusatict.getvet.R;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ICT on 31/08/2015.
 */
public class ForumList extends Activity {
    private ListAdapterThread adapter;
    private List<thread> list;
    private thread selectedList;
    JSONParser jsonParser = new JSONParser();

    private ListView lvVetList;
    private ImageButton imgBack, imgHome, imgAdd;
    private ProgressDialog pDialog;
    private tesKoneksiInet koneksiInet;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_list);

        lvVetList = (ListView)findViewById(R.id.lvVetList);
        list= new ArrayList<thread>();

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);
        imgAdd = (ImageButton)findViewById(R.id.imgAdd);

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
                Intent intent = new Intent(ForumList.this, forum_main.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumList.this, ForumAdd.class);
                Bundle Parsing = new Bundle();
                Parsing.putString("pos", "0");
                intent.putExtras(Parsing);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

    }

    public void fillListView(){
        lvVetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList = (thread) lvVetList.getAdapter().getItem(position);
                String stv = selectedList.getId().toString();
                Log.e("itemstok",stv);

                if (!stv.equals("0")) {
                    Log.e("itemstok",stv);
                    munculDialog(position, stv);
                }
            }
        });
    }

    private void munculDialog(int i,final String txt) {
        final CharSequence[] items = {"Edit", "Delete"};
        selectedList = (thread) lvVetList.getAdapter().getItem(i);
        AlertDialog.Builder kk = new AlertDialog.Builder(this);
        kk.setTitle(" Choose Action ");
        kk.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Edit")){
                    Intent myintent = new Intent(ForumList.this, ForumAdd.class);
                    Bundle Parsing = new Bundle();
                    Parsing.putString("id", selectedList.getId().toString());
                    Parsing.putString("judul", selectedList.getJudul().toString());
                    Parsing.putString("isi", selectedList.getIsi().toString());
                    Parsing.putString("katid", selectedList.getKatid().toString());
                    Parsing.putString("foto",selectedList.getFoto().toString());
                    Parsing.putString("pos", "1");
                    myintent.putExtras(Parsing);
                    startActivity(myintent);
                    finish();
                    overridePendingTransition(R.animator.activity_in, R.animator.activity_out);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForumList.this);
                    builder.setMessage("Are you sure want to delete ''"+ selectedList.getJudul().toString() +"'' ?").
                            setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            new MainActivityAsync().execute("delete", txt);
//                                    /*Intent myintent = new Intent(VetList.this, VetList.class);
//                                    startActivity(myintent);*/
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

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private class MainActivityAsync extends AsyncTask<String, Void, String> {
        DBAdapter db = new DBAdapter(ForumList.this);
        String uid,success;
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForumList.this);
            pDialog.setMessage(" Processing... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if(params[0] == "delete"){
                String url="http://getsimplebisnis.com/index.php/api/delthread";
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("thid", params[1]));
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
                try {
                    success = json.getString("delthread");
                }catch (JSONException e) {

                }
            }else{
                db.open();
                Cursor c=db.getContact(1);
                if (c.moveToFirst()) {
                    uid=c.getString(5);
                }
                db.close();
                String url="http://getsimplebisnis.com/index.php/api/threadid";
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("uid", uid));
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
                try {
                    success="1";
                    JSONArray jsonArray = json.getJSONArray("list");
                    thread thr = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
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
            adapter = new ListAdapterThread(getApplicationContext(), list);
            lvVetList.setAdapter(adapter);
        }
    }
}
