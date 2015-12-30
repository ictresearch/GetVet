package com.pusatict.getvet;

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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pusatict.getvet.catken.CatkenSearch;
import com.pusatict.getvet.datalistadapter.ListAdapteriklan;
import com.pusatict.getvet.datalistadapter.iklan;
import com.pusatict.getvet.forum.forum_main;
import com.pusatict.getvet.klinik.VetSearch;
import com.pusatict.getvet.shop.ShopSearch;
import com.pusatict.getvet.sponsor.Banner;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.Utils;
import com.pusatict.getvet.tool.tesKoneksiInet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    private TextView tvMenuVet,tvMenuPet,tvMenuForum,tvMenuGet,tvMenuCatken;
    private ImageButton btnMenuVet,btnMenuPet,btnMenuForum,btnMenuGet,btnSetting,btnMenuCatken;
    private ImageView imgBanner;
    private tesKoneksiInet koneksiInet;
    private Toast toast;
    private Tracker mTracker;
    private LinearLayout lyMain;
    DBAdapter db=new DBAdapter(MainActivity.this);
    private ProgressDialog pDialog;
    String tanggal;
    Calendar c1 = Calendar.getInstance();
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");
    JSONParser jsonParser = new JSONParser();
    private String link,urlgambar,uid;
    private int poin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        koneksiInet = new tesKoneksiInet(getApplicationContext());
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

//        mTracker.setScreenName("Home1");
//        mTracker.send(new HitBuilders.AppViewBuilder().build());
        tvMenuVet = (TextView)findViewById(R.id.tvmenuvet);
        tvMenuPet = (TextView)findViewById(R.id.tvmenupet);
        tvMenuCatken = (TextView)findViewById(R.id.tvmenucatken);
        tvMenuForum = (TextView)findViewById(R.id.tvmenuforum);
        tvMenuGet = (TextView)findViewById(R.id.tvmenuget);

        btnMenuVet = (ImageButton)findViewById(R.id.imgmenuvet);
        btnMenuPet = (ImageButton)findViewById(R.id.imgmenupet);
        btnMenuCatken = (ImageButton)findViewById(R.id.imgmenucatken);
        btnMenuForum = (ImageButton)findViewById(R.id.imgmenuforum);
        btnMenuGet = (ImageButton)findViewById(R.id.imgmenuget);
        btnSetting = (ImageButton)findViewById(R.id.imgSetting);
        imgBanner =(ImageView)findViewById(R.id.imgBanner);

        lyMain =(LinearLayout)findViewById(R.id.lymain);
        //lyMain.setPadding(0, 0, 0, 0);

//        if(koneksiInet.isNetworkAvailable()) {

        isiBtnClick();
//        }else{
//            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
//                    Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
        tanggal=Ftgl.format(c1.getTime());

        db.open();
        int jumlah=db.getCountbanner();
        Log.e("jum", jumlah + "");
        if(jumlah<=0){
            db.insertbanner(1,0);
        }
        Cursor c1 = db.getContactbanner(1);
        if (c1.moveToFirst()) {
            poin=c1.getInt(1)+1;
        }
        Cursor c = db.getContact(1);
        if (c.moveToFirst()) {
            uid=c.getString(5);
        }
        db.close();
        if(koneksiInet.isNetworkAvailable()) {
            new MainActivityAsync().execute();
        }else{
            imgBanner.setVisibility(View.GONE);
            lyMain.setPadding(0, 0, 0, 0);
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
            imgBanner.setImageBitmap(bitmap);
//            Utils.saveImage(bitmap, fname, coba.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void isiBtnClick(){
        tvMenuVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, VetSearch.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnMenuVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, VetSearch.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvMenuPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, ShopSearch.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnMenuPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, ShopSearch.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvMenuCatken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, CatkenSearch.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnMenuCatken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, CatkenSearch.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvMenuForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, forum_main.class);
                    startActivity(intent);
                    finishAffinity();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnMenuForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, forum_main.class);
                    startActivity(intent);
                    finish();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivitySetting.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Banner.class);
                Bundle Parsing = new Bundle();
//                Parsing.putString("banner", "http://www.animalrapidtest.com/");
                Parsing.putString("banner", link);
                Parsing.putString("broadcash", "0");
                Parsing.putString("tdid", "");
                intent.putExtras(Parsing);
                startActivity(intent);
                //finish();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        keluar();
//        super.onBackPressed();
//        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private void keluar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to exit?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                        finish();
                        finishAffinity();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        }).show();
    }
    private class MainActivityAsync extends AsyncTask<String, Void, String> {

        String succes="0",succes1="0";
        Bitmap bitmap = null;
        int id;
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(MainActivity.this);
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
                    Log.e("benner", link + "," + urlgambar+","+poin);
                    dowloadgambar(urlgambar);

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
//            db.open();
//            db.updatebanner(1, 0);
//            db.close();
            if(succes1.equals("0")){
                    Log.e("benner1", ""+poin);
                    db.open();
                    db.updatebanner(1, 0);
                if(poin>1) {
                    Cursor c1 = db.getContactbanner(1);
                    if (c1.moveToFirst()) {
                        poin = c1.getInt(1) + 1;
                    }
                    db.close();
                    new MainActivityAsync().execute();
                }else {
                    imgBanner.setVisibility(View.GONE);
                    lyMain.setPadding(0, 0, 0, 0);
                }
            }else{
                db.open();
                db.updatebanner(1, poin);
                db.close();
                imgBanner.setVisibility(View.VISIBLE);
                float scaleDp = getResources().getDisplayMetrics().density;
                int pixelDp = (int) (70 * scaleDp + 0.5f);
                lyMain.setPadding(0, 0, 0, pixelDp);
            }
        }
    }
}
