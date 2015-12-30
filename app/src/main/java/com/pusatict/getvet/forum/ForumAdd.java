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
import com.pusatict.getvet.catken.CatkenList;
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


public class ForumAdd extends Activity implements OnAmbilWarnaListener, CompoundButton.OnCheckedChangeListener , AdapterView.OnItemSelectedListener {
    private LinearLayout lnl;
    private CustomEditText customEditor;
    private AmbilWarnaDialog colorPickerDialog;
    private ImageView imgChangeColor;
    String picturePath ,strfoto;
    Uri selectedImage;
    Bitmap photo;
    private String ba1,katid,urlfoto,uid,id;
    private Bitmap bitmap,bitmap1;
    private TextView textfoto;
    private Button btnklik;
    private Katforum selectedList;
    private tesKoneksiInet koneksiInet;

//    private static String url = "http://10.0.2.2/klinikhewan/inserttext.php";
//    private static String url = "http://192.168.0.104/klinikhewan/inserttext.php";
    private static String url = "http://getsimplebisnis.com/index.php/api/postthread";
    //upload foto
    private static String URL1="http://getsimplebisnis.com/index.php/api/thfoto";
    //null
    private static String url1="http://getsimplebisnis.com/index.php/api/ubahthread";
    private static String urlinsertusercom="http://getsimplebisnis.com/index.php/api/InsertUserCommend";
    private static String urlubahkat="http://getsimplebisnis.com/index.php/api/threadbysubkat";
    //delete foto
    private static String urldelfot="http://getsimplebisnis.com/index.php/api/thdelfoto";

    private static String ubahfoto="http://getsimplebisnis.com/index.php/api/thubahfoto";
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
    private AutoCompleteTextView auto;
    private ProgressDialog pDialog;
    private ListAdapterKatforum adapter;
    private List<Katforum> list;
    List<String> categories;
    private CheckBox HKburung,HKkucing,HKanjing,HKReptile,HKfish,HKexotic,HKequine,HKother;
    private int gakadacheckHK=0;
    private String burung="0",kucing="0",anjing="0",Reptile="0",Fish="0",exotic="0",equine="0",other="0";
    private Spinner spinner;
    private ArrayAdapter<String> dataAdapter;
    private ImageButton btnAddPhoto,btnClearPhoto;
    private ImageButton imgBack, imgHome, imgAdd;
    private TextView tvPhoto;
    private ImageView imgPhoto;
    private Toast toast;
    private String fname;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_add);

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

        koneksiInet = new tesKoneksiInet(getApplicationContext());
        spinner = (Spinner) findViewById(R.id.spinner);
//
//        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        list = new ArrayList<Katforum>();
        categories = new ArrayList<String>();
        setCheckBox();
        new selectKategori().execute();
        textfoto=(TextView)findViewById(R.id.tvfoto);
        edtTitle=(EditText)findViewById(R.id.edtTitle);
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
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
                textfoto.setVisibility(View.GONE);
                btnClearPhoto.setVisibility(View.GONE);

                imgPhoto.setImageResource(R.drawable.picture_100_blue);
                float scaleDp = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (50*scaleDp + 0.5f);
                imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
//                tvPhoto.setText("Add image...");
                ba1=null;
            }
        });

        setBtnSave();
    }
    public void setBtnSave(){

        btnklik=(Button)findViewById(R.id.btnSave);
        btnklik.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                text.setText(c);
                Bundle Parsing=getIntent().getExtras();
                if(edtTitle.getText().toString().equals("")||customEditor.getText().toString().equals("")||gakadacheckHK==0){

                    toast = Toast.makeText(getApplicationContext(), " Please fill in all fields. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    if (Parsing.getString("pos").equals("1")) {
                        if(koneksiInet.isNetworkAvailable()) {
                            new updateThread().execute("update");
                        }else{
                            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }else{
                        if(koneksiInet.isNetworkAvailable()) {
                            if(ba1==null) {
                                urlfoto="null";
                                new tambahthread().execute();
                            }else{
                                new uploadToServer2().execute();
                                new tambahthread().execute();
                            }
                        }else{
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
                Intent intent = new Intent(ForumAdd.this, forum_main.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(VetAdd.this, VetList.class);
        startActivity(intent);
        finish();*/

        Intent intent = new Intent(ForumAdd.this, ForumList.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
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
                    Log.e("filepat", selectedImage + "");
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
//                    tvPhoto.setVisibility(View.GONE);
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
    public void setCheckBox(){
        HKburung=(CheckBox) findViewById(R.id.chbBurung);
        HKburung.setOnCheckedChangeListener(this);
        HKkucing=(CheckBox) findViewById(R.id.chbKucing);
        HKkucing.setOnCheckedChangeListener(this);
        HKanjing=(CheckBox) findViewById(R.id.chbAnjing);
        HKanjing.setOnCheckedChangeListener(this);
        HKReptile=(CheckBox) findViewById(R.id.chbReptile);
        HKReptile.setOnCheckedChangeListener(this);
        HKfish=(CheckBox) findViewById(R.id.chbfish);
        HKfish.setOnCheckedChangeListener(this);
        HKother=(CheckBox) findViewById(R.id.chbOther);
        HKother.setOnCheckedChangeListener(this);
        HKequine=(CheckBox) findViewById(R.id.chbEquine);
        HKequine.setOnCheckedChangeListener(this);
        HKexotic=(CheckBox) findViewById(R.id.chbExotic);
        HKexotic.setOnCheckedChangeListener(this);
    }
    public void checkincheckbox(){
        if(burung.equals("1")){
            HKburung.setChecked(true);
        }
        if(anjing.equals("1")){
            HKanjing.setChecked(true);
        }
        if(kucing.equals("1")){
            HKkucing.setChecked(true);
        }
        if(Fish.equals("1")){
            HKfish.setChecked(true);
        }
        if(Reptile.equals("1")){
            HKReptile.setChecked(true);
        }
        if(other.equals("1")){
            HKother.setChecked(true);
        }
        if(equine.equals("1")){
            HKequine.setChecked(true);
        }
        if(exotic.equals("1")){
            HKexotic.setChecked(true);
        }
    }
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.chbBurung:
                if(isChecked){
                    burung="1";
                    gakadacheckHK++;
                }else {
                    burung="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbAnjing:
                if(isChecked){
                    anjing="1";
                    gakadacheckHK++;
                }else {
                    anjing="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbKucing:
                if(isChecked){
                    kucing="1";
                    gakadacheckHK++;
                }else {
                    kucing="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbReptile:
                if(isChecked){
                    Reptile="1";
                    gakadacheckHK++;
                }else {
                    Reptile="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbOther:
                if(isChecked){
                    other="1";
                    gakadacheckHK++;
                }else {
                    other="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbfish:
                if(isChecked){
                    Fish="1";
                    gakadacheckHK++;
                }else {
                    Fish="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbEquine:
                if(isChecked){
                    equine="1";
                    gakadacheckHK++;
                }else {
                    equine="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbExotic:
                if(isChecked){
                    exotic="1";
                    gakadacheckHK++;
                }else {
                    exotic="0";
                    gakadacheckHK--;
                }
                break;

        }
    }
    @Override
    public void onCancel(AmbilWarnaDialog dialog) {

    }

    @Override
    public void onOk(AmbilWarnaDialog dialog, int color) {
        customEditor.setColor(color, selectionStart, selectionEnd);
        imgChangeColor.setBackgroundColor(color);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String item = parent.getItemAtPosition(position).toString();
        selectedList = (Katforum) adapter.getItem(position);
//        selectedList = (Katforum) parent.getAdapter().getItem(position);
        String stv = selectedList.getId().toString();
        katid= selectedList.getId().toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + katid+","+stv, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class selectKategori extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForumAdd.this);
            pDialog.setMessage("Finding Data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            Bundle Parsing=getIntent().getExtras();
            id=Parsing.getString("id");
//                String url="http://10.0.2.2/klinikhewan/select_all.php";
//                String url="http://192.168.0.103/klinikhewan/selectkliniklimit";
            String url="http://getsimplebisnis.com/index.php/api/katforum";
//            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
//            nvp.add(new BasicNameValuePair("offset", offset+""));
//
//            JSONObject json = jsonParser.makeHttpRequest(url, "GET", nvp);
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);

            try {
                JSONArray jsonArray = json.getJSONArray("katForum");
                Katforum dok=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    dok = new Katforum();
                    dok.setId(obj.getInt("katid"));
                    dok.setNama(obj.getString("katNama"));
                    Log.e("tes", dok.getNama());
                    list.add(dok);
                    categories.add(obj.getString("katNama"));
                }
            }catch (JSONException e) {

            }
            String url1="http://getsimplebisnis.com/index.php/api/thsubkategori";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("thid", id));
            json = jsonParser.makeHttpRequest(url1, "POST", nvp);
//            Log.e("amin",id);
            try {
                JSONArray jsonArray = json.getJSONArray("subkategori");
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String katnama=obj.getString("tsknama");
                    Log.e("amin",katnama+" "+id);
                    if(katnama.equals("CAT")){
                        kucing="1";
                    }
                    if(katnama.equals("DOG")){
                        anjing="1";
                    }
                    if(katnama.equals("FISH")){
                        Fish="1";
                    }
                    if(katnama.equals("OTHERS")){
                        other="1";
                    }
                    if(katnama.equals("REPTILES AND AMPHIBIANS")){
                        Reptile="1";
                    }
                    if(katnama.equals("BIRDS")){
                        burung="1";
                    }
                    if(katnama.equals("EXOTIC ANIMALS")){
                        exotic="1";
                    }
                    if(katnama.equals("EQUINE AND BIG MAMMALS")){
                        equine="1";
                    }
                }

            }catch (JSONException e) {
                Log.e("eror",e.toString());
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            checkincheckbox();
//            dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categories);
            dataAdapter = new ArrayAdapter<String>(ForumAdd.this, R.layout.listspinner ,categories);
//
//            // Drop down layout style - list view with radio button
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter = new ListAdapterKatforum(getApplicationContext(), list);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.setDropDownViewResource(R.layout.listdown);
//        spinner.setAdapter(adapter);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
//            customEditor.setTextHTML(stremail);
//            if (success.equals("1")) {
//              Toast.makeText(getApplicationContext(), "Berhasil",
//                        Toast.LENGTH_LONG).show();
//            }
            Bundle Parsing=getIntent().getExtras();
            if(Parsing.getString("pos").equals("1")){
                edtTitle.setText(Parsing.getString("judul"));
                customEditor.setTextHTML(Parsing.getString("isi"));
                spinner.setSelection(Integer.parseInt(Parsing.getString("katid")) - 1);
                if(!Parsing.getString("foto").equals("null")){
                    strfoto=Parsing.getString("foto");
                    strfoto=strfoto.replace("http://www.getsimplebisnis.com/Download/getvet/thread/","");
                    textfoto.setText(strfoto);
                }
                Log.e("foto", Parsing.getString("foto"));
                if(!Parsing.getString("foto").equals("null")){
                    new DownloadImage().execute(Parsing.getString("foto"));
                }else{
                    imgPhoto.setImageResource(R.drawable.picture_100_blue);
                    float scaleDp = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (50 * scaleDp + 0.5f);
                    imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                    ba1=null;
                }
            }
        }
    }
    public class tambahthread extends AsyncTask<String,String ,String> {

        String success,isi,thid,success1;
        DBAdapter db=new DBAdapter(ForumAdd.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForumAdd.this);
            pDialog.setMessage(" Saving data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            db.open();
            Cursor c=db.getContact(1);
            if (c.moveToFirst()) {
                uid=c.getString(5);
            }
            db.close();
            isi=customEditor.getTextHTML();
            thid=System.currentTimeMillis()+"";
//            Log.e("asik",strnama);
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", uid));
            nvp.add(new BasicNameValuePair("thjudul", edtTitle.getText().toString().trim()));
            nvp.add(new BasicNameValuePair("thisi", isi));
            nvp.add(new BasicNameValuePair("katid", katid));
            nvp.add(new BasicNameValuePair("thid", thid));
            nvp.add(new BasicNameValuePair("thfoto", urlfoto));
            nvp.add(new BasicNameValuePair("DOG", anjing));
            nvp.add(new BasicNameValuePair("BIRDS", burung));
            nvp.add(new BasicNameValuePair("CAT", kucing));
            nvp.add(new BasicNameValuePair("REPTILES", Reptile));
            nvp.add(new BasicNameValuePair("FISH", Fish));
            nvp.add(new BasicNameValuePair("OTHERS", other));
            nvp.add(new BasicNameValuePair("EXOTIC", exotic));
            nvp.add(new BasicNameValuePair("EQUINE", equine));
            Log.e("septi",anjing+""+burung+""+kucing+""+Reptile+""+Fish+""+other+""+exotic+""+equine);

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

        private ProgressDialog pd = new ProgressDialog(ForumAdd.this);
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
        DBAdapter db=new DBAdapter(ForumAdd.this);
        Bundle Parsing=getIntent().getExtras();
        private String isi;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForumAdd.this);
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
            nvp.add(new BasicNameValuePair("thid", id));
            nvp.add(new BasicNameValuePair("thjudul", edtTitle.getText().toString().trim()));
            nvp.add(new BasicNameValuePair("thisi", isi));
            nvp.add(new BasicNameValuePair("katid", katid));

            JSONObject json = jsonParser.makeHttpRequest(url1, "POST", nvp);
            try {
                success = json.getString("ubahThread");

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();
            }
            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("thid", id));
            nvp.add(new BasicNameValuePair("DOG", anjing));
            nvp.add(new BasicNameValuePair("BIRDS", burung));
            nvp.add(new BasicNameValuePair("CAT", kucing));
            nvp.add(new BasicNameValuePair("REPTILES", Reptile));
            nvp.add(new BasicNameValuePair("FISH", Fish));
            nvp.add(new BasicNameValuePair("OTHERS", other));
            nvp.add(new BasicNameValuePair("EXOTIC", exotic));
            nvp.add(new BasicNameValuePair("EQUINE", equine));

            json = jsonParser.makeHttpRequest(urlubahkat, "POST", nvp);
            try {
                success = json.getString("hapuskategori");

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
                toast = Toast.makeText(getApplicationContext(), " Your thread has been successfully updated ",
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

        private ProgressDialog pd = new ProgressDialog(ForumAdd.this);
        Bundle Parsing=getIntent().getExtras();
        String id=Parsing.getString("id");
        String namafoto=Parsing.getString("foto").trim();
        String success,success1;
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(ForumAdd.this);
            pDialog.setMessage("Wait image uploading!");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            namafoto=namafoto.replace("http://www.getsimplebisnis.com/Download/getvet/thread/","");
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            Log.e("jah",namafoto);
            JSONObject json;
//            if(!namafoto.equals("null")){

            nvp.add(new BasicNameValuePair("name",namafoto));


            json = jsonParser.makeHttpRequest(urldelfot, "POST", nvp);
            try {
                success1=json.getString("delfoto");
            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }

            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("thid", id));
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
                success = json.getString("ubahFoto");

//                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }

            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (success.equals("1")) {
            toast = Toast.makeText(getApplicationContext(), " Your thread has been successfully updated ",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

//            }
            onBackPressed();
            pDialog.dismiss();
        }
    }
    public class uploadToServer3 extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(ForumAdd.this);
        Bundle Parsing=getIntent().getExtras();
        String id=Parsing.getString("id");
        String namafoto=Parsing.getString("foto").trim();
        String success,success1;
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(ForumAdd.this);
            pDialog.setMessage("Wait image uploading!");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            namafoto=namafoto.replace("http://www.getsimplebisnis.com/Download/getvet/thread/","");
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            Log.e("jah",namafoto);
            JSONObject json;
            if(!namafoto.equals("null")) {

                nvp.add(new BasicNameValuePair("name", namafoto));

                String delete = "http://www.getsimplebisnis.com/index.php/api/thdelfoto";
                json = jsonParser.makeHttpRequest(delete, "POST", nvp);
                try {
                    success1 = json.getString("delfoto");
                } catch (Exception e) {
                    Log.v("log_tag", "Error in http connection " + e.toString());
                }
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (success.equals("1")) {
            toast = Toast.makeText(getApplicationContext(), " Your thread has been successfully updated ",
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
            pDialog = new ProgressDialog(ForumAdd.this);
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
            Utils.saveImage1(result, fname, ForumAdd.this);
            decodeFile(ForumAdd.this.getFilesDir().toString() + "/dir/k.jpg");
            pDialog.dismiss();
        }
    }
}
