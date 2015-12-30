package com.pusatict.getvet.forum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ICT on 29/08/2015.
 */
public class ForumDetails extends Activity {
    private ImageButton imgBack, imgHome;
    private TextView tvNamaUser,tvIsi,tvDate,tvjudul;
    private EditText edtComment;
    private String fname,uid,thid;
    private ProgressDialog pDialog;
    private Bitmap bitmap;
    private ImageView imgPhoto;
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Toast toast;
    private Button btnviewcom;
    JSONParser jsonParser = new JSONParser();
    DBAdapter db=new DBAdapter(ForumDetails.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_details);

        imgBack = (ImageButton) findViewById(R.id.imgBack);
        imgHome = (ImageButton) findViewById(R.id.imgHome);
        tvIsi=(TextView)findViewById(R.id.tvIsi);
        tvNamaUser=(TextView)findViewById(R.id.tvNamaUser);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvjudul=(TextView)findViewById(R.id.tvJudul);
        imgPhoto=(ImageView)findViewById(R.id.imgKlinik);
        edtComment=(EditText)findViewById(R.id.edtComment);
        btnviewcom=(Button)findViewById(R.id.btnviewCom);
        db.open();
        Cursor c = db.getContact(1);
        if (c.moveToFirst()) {
            uid = c.getString(5);
        }
        db.close();
        new updateuserthread().execute();
        setdetailklinik();
        isiBtnClick();
    }
    public void setdetailklinik(){
        Bundle Parsing=getIntent().getExtras();
        tvNamaUser.setText(Parsing.getString("namauser"));
        try {

            Date tgl1=Ftgl.parse(Parsing.getString("date"));
            String tgl=Ftgl.format(tgl1);
            tvDate.setText(tgl+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvjudul.setText(Parsing.getString("judul"));
        Spanned htmlAsSpanned = Html.fromHtml(Parsing.getString("isi"));
        tvIsi.setText(htmlAsSpanned);
//        Log.e("fuk",Parsing.getString("klfoto"));
        if(!Parsing.getString("foto").equals("null")){
            new DownloadImage().execute(Parsing.getString("foto"));
        }else{
            imgPhoto.setVisibility(View.GONE);
        }

        thid=Parsing.getString("thid");
    }
    public void isiBtnClick(){
//        tvPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//                cm.setText(tvPhone.getText());
//                toast = Toast.makeText(getApplicationContext(), "Copied to clipboard",
//                        Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            }
//        });
//        btncall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String toDial="tel:"+tvPhone.getText().toString();
//                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toDial)));
//            }
//        });
        btnviewcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumDetails.this, ViewCommentList.class);
                Bundle Parsing = new Bundle();
                Log.e("thid",thid);
                Parsing.putString("thid", thid);
                intent.putExtras(Parsing);
                startActivity(intent);
            }
        });
        edtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumDetails.this, ForumCommentAdd.class);
                Bundle Parsing = new Bundle();
                Parsing.putString("pos","0");
                Parsing.putString("thid", thid);
                intent.putExtras(Parsing);
                startActivity(intent);
//                    finish();

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
                Intent intent = new Intent(ForumDetails.this, forum_main.class);
                startActivity(intent);
                finish();

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Bundle Parsing=getIntent().getExtras();
        if(Parsing.getString("to").equals("1")){
            Intent intent = new Intent(ForumDetails.this, forum_main.class);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
//            Intent intent = new Intent(ForumDetails.this, Tab3_1.class);
//            Parsing.putString("katnama", Parsing.getString("katnama"));
//            intent.putExtras(Parsing);
//            startActivity(intent);
//            finish();
        }

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(ForumDetails.this);
//            pDialog.setMessage(" Processing... ");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            fname="k";
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Utils.saveImage1(result, fname, ForumDetails.this);
            decodeFile(ForumDetails.this.getFilesDir().toString() + "/dir/k.jpg");
//            pDialog.dismiss();
        }
    }
    public void decodeFile(String filePath) {
        Log.e("patc", filePath);
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        // Show the Selected Image on ImageView
//        tvPhoto.setVisibility(View.VISIBLE);
//        btnClearPhoto.setVisibility(View.VISIBLE);

        imgPhoto.setImageBitmap(bitmap);
        float scaleDp = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (2*scaleDp + 0.5f);
        imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
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
}
