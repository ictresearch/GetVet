package com.pusatict.getvet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.NotifyService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ICT on 23/08/2015.
 */
public class SplashScreen extends Activity  {
    //private static int SPLASH_TIME_OUT = 3000
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
//        Intent intent = new Intent(SplashScreen.this, NotifyService.class);
//        SplashScreen.this.startService(intent);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    DBAdapter db = new DBAdapter(SplashScreen.this);
                    db.open();
                    Cursor c=db.getContact(1);
//                    Toast.makeText(getApplicationContext(), " " + c.moveToFirst(), Toast.LENGTH_LONG).show();
                    if (c.moveToFirst()) {
                        if (c.getString(1).equals("1")) {
                            Intent intent = new Intent(SplashScreen.this, coba.class);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(SplashScreen.this, ActivityLogin.class);
                        startActivity(intent);
                    }
                    db.close();
                }
            }
        };

        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private class cektrauserthread extends AsyncTask<String, Void, String> {
        private String success1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String url="http://getsimplebisnis.com/index.php/api/userthreaduid";

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", "1"));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success1 = json.getString("Total");
            }catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
//			super.onPostExecute(s);
//            if(success1.equals("1")) {
//                Log.e("asik", "asik");
//            }
            Log.e("asik", success1);
        }
    }
}
