package com.pusatict.getvet.forum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.Katforum;
import com.pusatict.getvet.datalistadapter.ListAdapterKatforum;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.Utils;
import com.pusatict.getvet.tool.tesKoneksiInet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import windyzboy.github.io.customeeditor.CustomEditText;
import windyzboy.github.io.customeeditor.CustomEditText.EventBack;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;


public class ForumCommentAdd extends Activity implements OnAmbilWarnaListener, CompoundButton.OnCheckedChangeListener , AdapterView.OnItemSelectedListener {
    private LinearLayout lnl;
    private CustomEditText customEditor;
    private AmbilWarnaDialog colorPickerDialog;
    private ImageView imgChangeColor;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    private String ba1,thid,urlfoto,uid,id;
    private Bitmap bitmap,bitmap1;
    private TextView text;
    private Button btnklik;
    private tesKoneksiInet koneksiInet;

//    private static String url = "http://10.0.2.2/klinikhewan/inserttext.php";
//    private static String url = "http://192.168.0.104/klinikhewan/inserttext.php";
    private static String url = "http://getsimplebisnis.com/index.php/api/insertcomment";
    private static String URL1="http://getsimplebisnis.com/index.php/api/fotocom";
    private static String url1="http://getsimplebisnis.com/index.php/api/ubahcomment";
    private static String urlubahkat="http://getsimplebisnis.com/index.php/api/threadbysubkat";
    private static String urlinsertusercom="http://getsimplebisnis.com/index.php/api/InsertUserCommend";
    private static String urldelfot="http://getsimplebisnis.com/index.php/api/delfotocom";
    private static String ubahfoto="http://getsimplebisnis.com/index.php/api/ubahfotocom";
    private int selectionStart;
    private int selectionEnd;

    private EventBack eventBack = new EventBack() {

        @Override
        public void close() {
            lnl.setVisibility(View.GONE);
        }

        @Override
        public void show() {
            lnl.setVisibility(View.VISIBLE);
        }
    };
    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (customEditor.isFocused()) {
                lnl.setVisibility(View.VISIBLE);
            }
        }
    };
    JSONParser jsonParser = new JSONParser();
    private EditText edtTitle;
    private ProgressDialog pDialog;
    private ImageButton btnAddPhoto,btnClearPhoto;
    private TextView tvPhoto;
    private ImageView imgPhoto;
    private Toast toast;
    private String fname;
    private ImageButton imgBack,imgHome;
    private TextView textfoto;
    private String strfoto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_comment_add);

        colorPickerDialog = new AmbilWarnaDialog(this, Color.BLACK, this);
        ToggleButton boldToggle = (ToggleButton) findViewById(R.id.btnBold);
        ToggleButton italicsToggle = (ToggleButton) findViewById(R.id.btnItalics);
//        ToggleButton underlinedToggle = (ToggleButton) findViewById(R.id.btnUnderline);
        imgChangeColor = (ImageView) findViewById(R.id.btnChangeTextColor);
        lnl = (LinearLayout) findViewById(R.id.lnlAction);
        lnl.setVisibility(View.GONE);

        customEditor = (CustomEditText) findViewById(R.id.CustomEditor);
        customEditor.setHint(getResources().getString(R.string.hint));
        customEditor.setSingleLine(false);
        customEditor.setMinLines(10);
        customEditor.setBoldToggleButton(boldToggle);
        customEditor.setItalicsToggleButton(italicsToggle);
//        customEditor.setUnderlineToggleButton(underlinedToggle);
        customEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lnl.setVisibility(View.VISIBLE);
                } else {
                    lnl.setVisibility(View.GONE);
                }
            }
        });
        customEditor.setEventBack(eventBack);
        customEditor.setOnClickListener(clickListener);
        imgChangeColor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectionStart = customEditor.getSelectionStart();
                selectionEnd = customEditor.getSelectionEnd();
                colorPickerDialog.show();
            }
        });

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);
        koneksiInet = new tesKoneksiInet(getApplicationContext());
        textfoto=(TextView)findViewById(R.id.tvfoto);
        edtTitle=(EditText)findViewById(R.id.edtTitle);
        // Creating adapter for spinner
        btnAddPhoto=(ImageButton) findViewById(R.id.btnAddPhoto);
        btnClearPhoto=(ImageButton) findViewById(R.id.btnClearPhoto);
//        tvPhoto=(TextView) findViewById(R.id.tvPhoto);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        btnAddPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btnClearPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                tvPhoto.setVisibility(View.GONE);
                textfoto.setVisibility(View.GONE);
                btnClearPhoto.setVisibility(View.GONE);

                imgPhoto.setImageResource(R.drawable.picture_100_blue);
                float scaleDp = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (50*scaleDp + 0.5f);
                imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                ba1=null;
            }
        });
        setawal();
        setBtnSave();
    }
    public void setawal(){
        Bundle Parsing=getIntent().getExtras();
        if(Parsing.getString("pos").equals("1")){
            edtTitle.setText(Parsing.getString("judul"));
            customEditor.setTextHTML(Parsing.getString("isi"));
            Log.e("foto", Parsing.getString("foto"));
//            deleteGambar("k");
            if(!Parsing.getString("foto").equals("null")){
                new DownloadImage().execute(Parsing.getString("foto"));
//                new DownloadImage().execute(Parsing.getString("foto"));

            }else{
                imgPhoto.setImageResource(R.drawable.picture_100_blue);
                float scaleDp = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (50*scaleDp + 0.5f);
                imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                ba1=null;
            }
        }
    }
    public void setBtnSave(){

        btnklik=(Button)findViewById(R.id.btnSave);
        btnklik.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                text.setText(c);
                Bundle Parsing = getIntent().getExtras();
                if (customEditor.getText().toString().equals("")) {

                    toast = Toast.makeText(getApplicationContext(), " Please fill in all fields. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    if (Parsing.getString("pos").equals("1")) {
                        if (koneksiInet.isNetworkAvailable()) {
                            new updateThread().execute("update");
                        } else {
                            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        if (koneksiInet.isNetworkAvailable()) {
                            if (ba1 == null) {
                                urlfoto = "null";
                                new tambahcom().execute();
                            } else {
                                new uploadToServer2().execute();
                                new tambahcom().execute();
                            }
                        } else {
                            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
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
                Intent intent = new Intent(ForumCommentAdd.this, forum_main.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
//    @Override
//    public void onBackPressed() {
//        /*Intent intent = new Intent(VetAdd.this, VetList.class);
//        startActivity(intent);
//        finish();*/
//
//        Intent intent = new Intent(ForumCommentAdd.this, ForumList.class);
//        startActivity(intent);
//        finish();
//        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
//    }
    public void decodeFile(String filePath) {
        Log.e("patc",filePath);
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
        btnClearPhoto.setVisibility(View.VISIBLE);

        imgPhoto.setImageBitmap(bitmap);
        float scaleDp = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (2*scaleDp + 0.5f);
        imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int si=0;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1){
                try {
                    Uri selectedImage = data.getData();
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    String[] filePathColumn = { MediaStore.MediaColumns.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                    cursor.moveToFirst();
                    picturePath = cursor.getString(columnIndex);
                    strfoto=picturePath.substring(picturePath.lastIndexOf("/") + 1);
                    textfoto.setVisibility(View.VISIBLE);
                    lnl.setVisibility(View.GONE);
                    textfoto.setText(strfoto);
                    cursor.close();

                    decodeFile(picturePath);

                    // Image location URL
                    Log.e("path", "----------------" + picturePath);

                    // Image
                    Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
//                    bm.compress(Bitmap.CompressFormat.JPEG, 94, bao);
                    bm.compress(Bitmap.CompressFormat.JPEG, 40, bao);
                    byte[] ba = bao.toByteArray();
                    ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
                    si=ba.length/1024;
                    Log.e("base64", "-----" + ba1);
                }catch (Exception e){
                    toast = Toast.makeText(getApplicationContext(), "File not allowed, Please select another picture.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    tvPhoto.setVisibility(View.GONE);
                    btnClearPhoto.setVisibility(View.GONE);

                    imgPhoto.setImageResource(R.drawable.picture_100_blue);
                    float scaleDp = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (50*scaleDp + 0.5f);
                    imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                    ba1=null;
                }
//                if(si>512){
//                    tvPhoto.setVisibility(View.GONE);
//                    btnClearPhoto.setVisibility(View.GONE);
//
//                    imgPhoto.setImageResource(R.drawable.picture_100_blue);
//                    float scaleDp = getResources().getDisplayMetrics().density;
//                    int dpAsPixels = (int) (50*scaleDp + 0.5f);
//                    imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
//                    ba1=null;
//                    toast = Toast.makeText(getApplicationContext(), "maaf foto terlalu besar",
//                            Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
            }
        }
    }


    @Override
    public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

    }

    @Override
    public void onOk(AmbilWarnaDialog dialog, int color) {
        customEditor.setColor(color, selectionStart, selectionEnd);
        imgChangeColor.setBackgroundColor(color);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class tambahcom extends AsyncTask<String,String ,String> {

        String success,isi;
        Bundle Parsing=getIntent().getExtras();
        DBAdapter db=new DBAdapter(ForumCommentAdd.this);
        private String success1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForumCommentAdd.this);
            pDialog.setMessage(" Saving data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            thid=Parsing.getString("thid");
            db.open();
            Cursor c=db.getContact(1);
            if (c.moveToFirst()) {
                uid=c.getString(5);
            }
            db.close();
            isi=customEditor.getTextHTML();
//            Log.e("asik",strnama);
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", uid));
            nvp.add(new BasicNameValuePair("comJudul", edtTitle.getText().toString().trim()));
            nvp.add(new BasicNameValuePair("comIsi", isi));
            nvp.add(new BasicNameValuePair("thid", thid));
            nvp.add(new BasicNameValuePair("comfoto", urlfoto));


            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("success");
                Log.e("s",success);

            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Error",
//                        Toast.LENGTH_LONG).show();
            }
            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", uid));
            nvp.add(new BasicNameValuePair("thid", thid));

            json = jsonParser.makeHttpRequest(urlinsertusercom, "POST", nvp);
            try {
                success1 = json.getString("insert");
                Log.e("sa1",success1);

            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Error",
//                        Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (success.equals("1")) {
                onBackPressed();
            }
        }
    }

    public class uploadToServer2 extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(ForumCommentAdd.this);
        //        Bundle Parsing=getIntent().getExtras();
//        String id=Parsing.getString("id");
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            //Wahyu Gatel
//            nvp.add(new BasicNameValuePair("klid",id));
            nvp.add(new BasicNameValuePair("image", ba1));
            nvp.add(new BasicNameValuePair("filename", System.currentTimeMillis() + ".jpg"));

//            nvp.add(new BasicNameValuePair("base64", ba1));
//            nvp.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            JSONObject json = jsonParser.makeHttpRequest(URL1, "POST", nvp);
            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//                httppost.setEntity(new UrlEncodedFormEntity(nvp));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
                urlfoto=json.getString("url");
//                urlfoto=json.getString("url");

                Log.v("log_tag", "In the try Loop" + urlfoto);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }
    public class updateThread extends AsyncTask<String,String ,String> {

        //        String url="http://getsimplebisnis.com/index.php/api/ubahklinik";
        String strNama,strAlamat,strKontak,strPelayanan,strJadwal,strKota,success;
        DBAdapter db=new DBAdapter(ForumCommentAdd.this);
        Bundle Parsing=getIntent().getExtras();
        private String isi;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForumCommentAdd.this);
            pDialog.setMessage(" Saving data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            id=Parsing.getString("id");
            isi=customEditor.getTextHTML();

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("comid", id));
            nvp.add(new BasicNameValuePair("comJudul", edtTitle.getText().toString().trim()));
            nvp.add(new BasicNameValuePair("comIsi", isi));
//            nvp.add(new BasicNameValuePair("katid", katid));

            JSONObject json = jsonParser.makeHttpRequest(url1, "POST", nvp);
            try {
                success = json.getString("ubah");

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(ba1!=null) {
                new uploadToServer().execute();
                toast = Toast.makeText(getApplicationContext(), " Your comment has been successfully updated ",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }else{
                new uploadToServer3().execute();
//                pDialog.dismiss();
//                onBackPressed();
            }
//            pDialog.dismiss();
//            if (success.equals("1")) {
//                toast = Toast.makeText(getApplicationContext(), " Your vet clinic has been successfully updated ",
//                        Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            }

        }
    }
    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(ForumCommentAdd.this);
        Bundle Parsing=getIntent().getExtras();
        String id=Parsing.getString("id");
        String namafoto=Parsing.getString("foto").trim();
        String success,success1;
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(ForumCommentAdd.this);
            pDialog.setMessage("Wait image uploading!");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            namafoto=namafoto.replace("http://www.getsimplebisnis.com/Download/getvet/thread/comment/","");
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            Log.e("jah",namafoto);
            JSONObject json;
//            if(!namafoto.equals("null")){

            nvp.add(new BasicNameValuePair("name",namafoto));


            json = jsonParser.makeHttpRequest(urldelfot, "POST", nvp);
            try {
                success1=json.getString("hapus");
            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }

            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("comid", id));
            nvp.add(new BasicNameValuePair("image", ba1));
            if (namafoto.equals("null")) {
                nvp.add(new BasicNameValuePair("filename", System.currentTimeMillis() + ".jpg"));
            } else {
                nvp.add(new BasicNameValuePair("filename", namafoto));
            }

            json = jsonParser.makeHttpRequest(ubahfoto, "POST", nvp);
            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//                httppost.setEntity(new UrlEncodedFormEntity(nvp));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
                success = json.getString("ubah");

//                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }

            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (success.equals("1")) {
            toast = Toast.makeText(getApplicationContext(), " Your comment has been successfully updated ",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

//            }
            onBackPressed();
            pDialog.dismiss();
        }
    }
    public class uploadToServer3 extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(ForumCommentAdd.this);
        Bundle Parsing=getIntent().getExtras();
        String id=Parsing.getString("id");
        String namafoto=Parsing.getString("foto").trim();
        String success,success1;
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(ForumCommentAdd.this);
            pDialog.setMessage("Wait image uploading!");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            namafoto=namafoto.replace("http://www.getsimplebisnis.com/Download/getvet/thread/comment/","");
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

            Log.e("jah",namafoto);
            JSONObject json;
            if(!namafoto.equals("null")) {

                nvp.add(new BasicNameValuePair("name", namafoto));

                String delete = "http://www.getsimplebisnis.com/index.php/api/delfotocom";
                json = jsonParser.makeHttpRequest(delete, "POST", nvp);
                try {
                    success1 = json.getString("hapus");
                } catch (Exception e) {
                    Log.v("log_tag", "Error in http connection " + e.toString());
                }
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (success.equals("1")) {
            toast = Toast.makeText(getApplicationContext(), " Your comment has been successfully updated ",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

//            }
            onBackPressed();
            pDialog.dismiss();
        }
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForumCommentAdd.this);
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
                InputStream input = new URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] ba = bao.toByteArray();
                ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Utils.saveImage1(result, fname, ForumCommentAdd.this);
            decodeFile(ForumCommentAdd.this.getFilesDir().toString() + "/dir/k.jpg");
            pDialog.dismiss();
        }
    }
}
