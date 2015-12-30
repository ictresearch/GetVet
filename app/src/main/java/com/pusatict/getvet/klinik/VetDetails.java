package com.pusatict.getvet.klinik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.tool.Utils;

import java.io.InputStream;

/**
 * Created by ICT on 29/08/2015.
 */
public class VetDetails extends Activity {
    private ImageButton imgBack, imgHome;
    private TextView tvNamaKlinik,tvJasa,tvAlamat,tvDokter,tvPhone,tvJadwal;
    private String fname;
    private ProgressDialog pDialog;
    private Bitmap bitmap;
    private ImageView imgPhoto;
    private Button btnSms,btncall;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vet_details);

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);

        tvNamaKlinik=(TextView)findViewById(R.id.tvNamaKlinik);
        tvJasa=(TextView)findViewById(R.id.tvJasa);
        tvAlamat=(TextView)findViewById(R.id.tvAlamat);
        tvDokter=(TextView)findViewById(R.id.tvDokter);
        tvPhone=(TextView)findViewById(R.id.tvPhone);
        tvJadwal=(TextView)findViewById(R.id.tvJadwal);
        imgPhoto=(ImageView)findViewById(R.id.imgKlinik);
        btncall=(Button)findViewById(R.id.btnCall);
        btnSms=(Button)findViewById(R.id.btnSms);
        setdetailklinik();
        isiBtnClick();
    }
    public void setdetailklinik(){
        Bundle Parsing=getIntent().getExtras();
        tvNamaKlinik.setText(Parsing.getString("nama"));
        tvJasa.setText(Parsing.getString("jasa"));
        tvAlamat.setText(Parsing.getString("alamat")+","+Parsing.getString("kota"));
        tvDokter.setText(Parsing.getString("namadok"));
        tvPhone.setText(Parsing.getString("kontak"));
        tvJadwal.setText(Parsing.getString("jadwal"));
        Log.e("fuk", Parsing.getString("klfoto"));
        if(!Parsing.getString("klfoto").equals("null")){
            new DownloadImage().execute(Parsing.getString("klfoto"));
        }else{
            imgPhoto.setImageResource(R.drawable.picture_100_blue);
            float scaleDp = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (50*scaleDp + 0.5f);
            imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        }
    }
    public void isiBtnClick(){
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tvPhone.getText());
                toast = Toast.makeText(getApplicationContext(), "Copied to clipboard",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toDial="tel:"+tvPhone.getText().toString();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toDial)));
            }
        });
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri =Uri.parse("smsto:"+tvPhone.getText().toString());

                Intent smsIntent=new Intent(Intent.ACTION_SENDTO,uri);
                smsIntent.putExtra("sms_body","");
                try {
                    startActivity(smsIntent);
                }catch (Exception e){

                }
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
                Intent intent = new Intent(VetDetails.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VetDetails.this);
            pDialog.setMessage(" Processing... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
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
            Utils.saveImage1(result, fname, VetDetails.this);
            decodeFile(VetDetails.this.getFilesDir().toString() + "/dir/k.jpg");
            pDialog.dismiss();
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
}
