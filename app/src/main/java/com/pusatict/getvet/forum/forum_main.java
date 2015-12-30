package com.pusatict.getvet.forum;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.analytics.Tracker;
import com.pusatict.getvet.ActivitySetting;
import com.pusatict.getvet.AnalyticsApplication;
import com.pusatict.getvet.R;
import com.pusatict.getvet.sponsor.Banner;
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
public class forum_main extends ActionBarActivity {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Recent Threads","Hot Threads","Category Threads"};
    int Numboftabs =3;
    private ImageButton btnSetting,btnRefresh;

    DBAdapter db=new DBAdapter(forum_main.this);
    private String link,urlgambar,uid,tanggal;;
    private int poin,poin2=0,poin1=4;
    private ImageView imgBanner;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    Calendar c1 = Calendar.getInstance();
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_main);

//        poin2 = pager.getCurrentItem();
//        Log.e("poin", poin2 + "" + position);
////        if (poin2 != poin1) {
        imgBanner =(ImageView)findViewById(R.id.imgBanner);
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
        new updateuserthread().execute();
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getSystemService(ns);
        nMgr.cancel(1);
        db.close();
//        new MainActivityAsync1().execute();
//            poin1 = poin2;
//        }
        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        btnSetting = (ImageButton)findViewById(R.id.imgSetting);
        btnRefresh = (ImageButton)findViewById(R.id.imgRef);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                poin2 = pager.getCurrentItem();
                if(poin2==2){
                    Log.e("asik",poin2+"");
                }
                if (poin2 != poin1) {
//                    imgBanner =(ImageView)findViewById(R.id.imgBanner);
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

                    new MainActivityAsync1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    poin1 = poin2;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        isiBtnClick();

    }
    public void isiBtnClick(){
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forum_main.this, ForumSetting.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forum_main.this, forum_main.class);
                startActivity(intent);
                finish();
//                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forum_main.this, Banner.class);
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
    public void onBackPressed() {

//        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    public class updateuserthread extends AsyncTask<String,String ,String> {

        String url="http://getsimplebisnis.com/index.php/api/UpdateTraUserThread";
        String url1="http://getsimplebisnis.com/index.php/api/totalthread";
        String url2="http://getsimplebisnis.com/index.php/api/UpdateJmlUserCommend";
        String total, success3,success4;

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url1);
            try {
                total= json.getString("Total");
            }catch (JSONException e) {

            }
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", uid));
            nvp.add(new BasicNameValuePair("utjum", total));

            json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success3 = json.getString("update");

            } catch (Exception e) {
        /*        Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();*/
            }

            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", uid));

            json = jsonParser.makeHttpRequest(url2, "POST", nvp);
            try {
                success4 = json.getString("update");
                Log.e("asa",success4);

            } catch (Exception e) {
        /*        Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();*/
            }
            return null;
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
}