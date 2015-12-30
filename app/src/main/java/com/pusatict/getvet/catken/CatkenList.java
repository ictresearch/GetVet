package com.pusatict.getvet.catken;

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

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.ListAdapterCatKen;
import com.pusatict.getvet.datalistadapter.ListAdapterPetShop;
import com.pusatict.getvet.datalistadapter.CatKen;
import com.pusatict.getvet.shop.ShopAdd;
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
public class CatkenList extends Activity {
    private ListAdapterCatKen adapter;
    private List<CatKen> list;
    private CatKen selectedList;
    JSONParser jsonParser = new JSONParser();

    private ListView lvVetList;
    private ImageButton imgBack, imgHome, imgAdd;
    private ProgressDialog pDialog;
    private tesKoneksiInet koneksiInet;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catken_list);

        lvVetList = (ListView)findViewById(R.id.lvVetList);
        list= new ArrayList<CatKen>();

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
                Intent intent = new Intent(CatkenList.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatkenList.this, CatkenAdd.class);
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
                selectedList = (CatKen) lvVetList.getAdapter().getItem(position);
                String stv = selectedList.getId().toString();
                if(!stv.equals("0")) {
                    munculDialog(position, stv);
                }
            }
        });
    }

    private void munculDialog(int i,final String txt) {
        final CharSequence[] items = {"Edit", "Delete"};
        selectedList = (CatKen) lvVetList.getAdapter().getItem(i);
        AlertDialog.Builder kk = new AlertDialog.Builder(this);
        kk.setTitle(" Choose Action ");
        kk.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Edit")){
                    Intent myintent = new Intent(CatkenList.this, CatkenAdd.class);
                    Bundle Parsing = new Bundle();
                    Parsing.putString("id", selectedList.getId().toString());
                    Parsing.putString("nama", selectedList.getNama().toString());
                    Parsing.putString("alamat", selectedList.getAlamat().toString());
                    Parsing.putString("kota", selectedList.getKota().toString());
                    Parsing.putString("kontak", selectedList.getDetail().toString());
                    Parsing.putString("jasa", selectedList.getPelayanan().toString());
                    Parsing.putString("jadwal", selectedList.getJadwal().toString());
                    Parsing.putString("foto",selectedList.getFoto().toString());
                    Parsing.putString("pos", "1");
                    myintent.putExtras(Parsing);
                    startActivity(myintent);
                    finish();
                    overridePendingTransition(R.animator.activity_in, R.animator.activity_out);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CatkenList.this);
                    builder.setMessage("Are you sure want to delete ''"+ selectedList.getNama().toString() +"'' ?").
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

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private class MainActivityAsync extends AsyncTask<String, Void, String> {
        DBAdapter db = new DBAdapter(CatkenList.this);
        String uid,success;
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CatkenList.this);
            pDialog.setMessage(" Processing... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if(params[0] == "delete"){
                String url="http://getsimplebisnis.com/index.php/api/hapuscatken";
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("ckid", params[1]));
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
                try {
                    success = json.getString("hapuscatken");
                }catch (JSONException e) {

                }
            }else{
                db.open();
                Cursor c=db.getContact(1);
                if (c.moveToFirst()) {
                    uid=c.getString(5);
                }
                db.close();
                String url="http://getsimplebisnis.com/index.php/api/catkenid";
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("uid", uid));
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
                try {
                    success="1";
                    JSONArray jsonArray = json.getJSONArray("catken");
                    CatKen shop = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        shop = new CatKen();
                        shop.setId(obj.getInt("ckid"));
                        shop.setNama(obj.getString("cknama"));
                        shop.setAlamat(obj.getString("ckalamat"));
                        shop.setDetail(obj.getString("ckkontak"));
                        shop.setKota(obj.getString("ckkota"));
                        shop.setPelayanan(obj.getString("ckjasa"));
                        shop.setJadwal(obj.getString("ckjadwal"));
                        shop.setNamaDok(obj.getString("unama"));
                        shop.setFoto(obj.getString("ckfoto"));
                        list.add(shop);
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
            adapter = new ListAdapterCatKen(getApplicationContext(), list);
            lvVetList.setAdapter(adapter);
        }
    }
}
