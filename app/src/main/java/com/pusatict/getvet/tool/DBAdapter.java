package com.pusatict.getvet.tool;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pusatict.getvet.datalistadapter.iklan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bronky on 06/08/2015.
 */
public class DBAdapter {
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "klinik";
    private static final String DATABASE_TABLE = "login";
    private static final String DATABASE_TABLE3 = "iklanbanner";
    private static final int DATABASE_VERSION = 8;
//    private static final String TABLE_CREATE = "create table login (_id integer primary key autoincrement, "
//            + "  logstatus text not null,logemail text not null ,logdokter text not null ,logppshop text not null  ," +
//            "logid text not null,lognama text not null,logkontak text not null,logkota text not null, logsrv text ," +
//            "logprovinsi text)";

    //version 7
//    private static final String TABLE_CREATE = "create table login (_id integer primary key autoincrement, "
//            + "  logstatus text not null,logemail text not null ,logdokter text not null ,logppshop text not null  ," +
//            "logid text not null,lognama text not null,logkontak text not null,logkota text not null, logsrv text ," +
//            "logprovinsi text,logcatken text not null )";

    private static final String TABLE_CREATE = "create table login (_id integer primary key autoincrement, "
            + "  logstatus text not null,logemail text not null ,logdokter text not null ,logppshop text not null  ," +
            "logid text not null,lognama text not null,logkontak text not null,logkota text not null, logsrv text ," +
            "logprovinsi text,logcatken text not null ,lognotifthread text not null,lognotifcomthread text not null)";

    //database versi 2 di bawah ini
/*    private static final String TABLE_CREATE1 = "CREATE TABLE "
            + "iklan (_id INTEGER PRIMARY KEY autoincrement, tglselesai DATE,html"
            + " text, waktumulai DATETIME, waktuselesai DATETIME)";*/

    //database versi 4 di bawah ini
//    private static final String TABLE_CREATE1 = "CREATE TABLE "
//            + "iklan (_id INTEGER PRIMARY KEY autoincrement, tglselesai DATE, html"
//            + " text, waktumulai DATETIME, waktuselesai DATETIME, tikid text, tgltenggang text, "
//            + "tikjudul text, tikhead text,tikaktif text)";
//    database versi 5 di bawah ini
    private static final String TABLE_CREATE1 = "CREATE TABLE "
            + "iklan (_id INTEGER PRIMARY KEY autoincrement, tglselesai DATE, html"
            + " text, waktumulai DATETIME, waktuselesai DATETIME, tikid text, tgltenggang text, "
            + "tikjudul text, tikhead text,tikaktif text,tikorder text)";
    //versi ke 7
    private static final String TABLE_CREATE2 = "CREATE TABLE "
            + "iklanbanner (_id INTEGER PRIMARY KEY autoincrement, poin INTEGER)";
    private static final String TABLE_DROP = "DROP TABLE IF EXISTS login";
    private static final String TABLE_DROP1 = "DROP TABLE IF EXISTS iklan";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_LOGSTATUS = "logstatus";
    public static final String KEY_LOGEMAIL = "logemail";
    public static final String KEY_LOGDOKTER = "logdokter";
    public static final String KEY_LOGPPSHOP = "logppshop";
    public static final String KEY_LOGID= "logID";
    public static final String KEY_LOGNAMA= "lognama";
    public static final String KEY_LOGKONTAK= "logkontak";
    public static final String KEY_LOGKOTA= "logkota";
    public static final String KEY_LOGSRV= "logsrv";
    public static final String KEY_LOGPROVINSI= "logprovinsi";
    //version 6
    public static final String KEY_LOGCATKEN = "logcatken";
    //version 7
    public static final String KEY_LOGNOTIFTHREAD = "lognotifthread";
    public static final String KEY_LOGNOTIFCOMTHREAD = "lognotifcomthread";

    public static final String KEY1_ROWID = "_id";
    public static final String KEY1_TGLSELESAI = "tglselesai";
    public static final String KEY1_HTML = "html";
    public static final String KEY1_WAKTUMULAI = "waktumulai";
    public static final String KEY1_WAKTUSELESAI = "waktuselesai";
    //version 4
    public static final String KEY1_TIKID = "tikid";
    public static final String KEY1_TIKTGLTENGGANG = "tgltenggang";
    public static final String KEY1_TIKJUDUL = "tikjudul";
    public static final String KEY1_TIKHEAD = "tikhead";
    public static final String KEY1_TIKAKTIF = "tikaktif";
    //version 5
    public static final String KEY1_TIKORDER = "tikorder";
    public static final String KEY2_ROWID = "_id";
    public static final String KEY2_TIKPOIN = "poin";

    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseHelper(this.context);
    }



    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);
            db.execSQL(TABLE_CREATE1);
            db.execSQL(TABLE_CREATE2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            Log.e("version",oldVersion+","+newVersion);
            //version database 4
            if (newVersion > oldVersion) {
                //db.execSQL(TABLE_CREATE2);
                db.execSQL("ALTER TABLE login ADD COLUMN lognotifthread text DEFAULT 1");
                db.execSQL("ALTER TABLE login ADD COLUMN lognotifcomthread text DEFAULT 1");
//                db.execSQL("ALTER TABLE login ADD COLUMN logcatken text DEFAULT 0");
//                db.execSQL("ALTER TABLE iklan ADD COLUMN tgltenggang text");
//                db.execSQL("ALTER TABLE iklan ADD COLUMN tikjudul text");
//                db.execSQL("ALTER TABLE iklan ADD COLUMN tikaktif text");
               // db.execSQL(TABLE_DROP1);
                //db.execSQL(TABLE_CREATE1);
            }
        }
    }
    public DBAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
    public long insertCustomer(long rowid,String logstatus, String logemail,String logdokter,String logppshop,String logcatken
            ,String logid ,String lognama,String logkontak,String logkota,String logsrv,String logprovinsi,String lognotifthread,
                               String lognotifcomthread) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, rowid);
        initialValues.put(KEY_LOGSTATUS, logstatus);
        initialValues.put(KEY_LOGEMAIL, logemail);
        initialValues.put(KEY_LOGDOKTER, logdokter);
        initialValues.put(KEY_LOGPPSHOP, logppshop);
        initialValues.put(KEY_LOGID, logid);
        initialValues.put(KEY_LOGNAMA, lognama);
        initialValues.put(KEY_LOGKONTAK, logkontak);
        initialValues.put(KEY_LOGKOTA, logkota);
        initialValues.put(KEY_LOGSRV, logsrv);
        initialValues.put(KEY_LOGPROVINSI, logprovinsi);
        initialValues.put(KEY_LOGCATKEN, logcatken);
        initialValues.put(KEY_LOGNOTIFTHREAD, lognotifthread);
        initialValues.put(KEY_LOGNOTIFCOMTHREAD, lognotifcomthread);
        return db.insert("login", null, initialValues);
    }
    public Cursor getAllContact() {
        return db.query("login", new String[]{
                KEY_ROWID, KEY_LOGSTATUS, KEY_LOGEMAIL, KEY_LOGDOKTER, KEY_LOGPPSHOP,KEY_LOGCATKEN, KEY_LOGID,
                KEY_LOGNAMA, KEY_LOGKONTAK, KEY_LOGKOTA, KEY_LOGSRV, KEY_LOGPROVINSI,KEY_LOGNOTIFTHREAD,KEY_LOGNOTIFCOMTHREAD
        }, null, null, null, null, null);
    }
    public boolean deleteContactLogin(long rowId) {
        return db.delete("login", KEY1_ROWID + "=" + rowId, null) > 0;
    }
    public Cursor getContact(long id)throws SQLException{
        Cursor contact=db.query(true,DATABASE_TABLE,new String[]{
                KEY_ROWID, KEY_LOGSTATUS, KEY_LOGEMAIL, KEY_LOGDOKTER, KEY_LOGPPSHOP,KEY_LOGID,
                        KEY_LOGNAMA,KEY_LOGKONTAK,KEY_LOGKOTA, KEY_LOGSRV,KEY_LOGPROVINSI,
                        KEY_LOGCATKEN,KEY_LOGNOTIFTHREAD,KEY_LOGNOTIFCOMTHREAD},KEY_ROWID+"="+id,
                null,null,null,null,null);
        if (contact != null) {
            contact.moveToFirst();
        }
        return contact;
    }

    public boolean updateprofil(long rowId,String logemail,String logdokter,String logppshop,String logcatken
            ,String lognama,String logkontak,String logkota,String logsrv,String logprovinsi) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LOGEMAIL, logemail);
        initialValues.put(KEY_LOGDOKTER, logdokter);
        initialValues.put(KEY_LOGPPSHOP, logppshop);
        initialValues.put(KEY_LOGCATKEN, logcatken);
        initialValues.put(KEY_LOGNAMA, lognama);
        initialValues.put(KEY_LOGKONTAK, logkontak);
        initialValues.put(KEY_LOGKOTA, logkota);
        initialValues.put(KEY_LOGSRV, logsrv);
        initialValues.put(KEY_LOGPROVINSI, logprovinsi);
        return db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean updatenotif(long rowId,String lognotifthread) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LOGNOTIFTHREAD, lognotifthread);
        return db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean updatenotifcom(long rowId,String lognotifcomthread) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LOGNOTIFCOMTHREAD, lognotifcomthread);
        return db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //METODE TABLE KE 2
    //insert table iklan
    public long insertCustomer1(String tglselesai, String html,String waktumulai,String waktuselesai,
                                String tikid,String tiktgltengggang,String tikjudul,String tikhead,String tikaktif,String tikorder) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY1_TGLSELESAI, tglselesai);
        initialValues.put(KEY1_HTML, html);
        initialValues.put(KEY1_WAKTUMULAI, waktumulai);
        initialValues.put(KEY1_WAKTUSELESAI, waktuselesai);
        initialValues.put(KEY1_TIKID, tikid);
        initialValues.put(KEY1_TIKTGLTENGGANG, tiktgltengggang);
        initialValues.put(KEY1_TIKJUDUL, tikjudul);
        initialValues.put(KEY1_TIKHEAD, tikhead);
        initialValues.put(KEY1_TIKAKTIF, tikaktif);
        initialValues.put(KEY1_TIKORDER, tikorder);
        return db.insert("iklan", null, initialValues);
    }
    //update tikaktif table iklan
    public boolean updateiklan(long rowId,String tikaktif) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY1_TIKAKTIF, tikaktif);
        return db.update("iklan", initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public boolean updateiklanall(String tikid,String tglselesai, String html,String waktumulai,String waktuselesai,
                                  String tiktgltengggang,String tikjudul,String tikhead,String tikaktif,String tikorder) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY1_TGLSELESAI, tglselesai);
        initialValues.put(KEY1_HTML, html);
        initialValues.put(KEY1_WAKTUMULAI, waktumulai);
        initialValues.put(KEY1_WAKTUSELESAI, waktuselesai);
        initialValues.put(KEY1_TIKTGLTENGGANG, tiktgltengggang);
        initialValues.put(KEY1_TIKJUDUL, tikjudul);
        initialValues.put(KEY1_TIKHEAD, tikhead);
        initialValues.put(KEY1_TIKAKTIF, tikaktif);
        initialValues.put(KEY1_TIKORDER, tikorder);
        return db.update("iklan", initialValues, KEY1_TIKID + "=" + tikid, null) > 0;
    }

    public boolean deleteContact(long rowId) {
        return db.delete("iklan", KEY1_ROWID + "=" + rowId, null) > 0;
    }
    public Cursor getAllContact1() {
        return db.query("iklan", new String[]{
                KEY1_ROWID,KEY1_TGLSELESAI,KEY1_TIKID,KEY1_TIKTGLTENGGANG
        }, null, null, null, null, null);
    }

    public List<iklan> getAllToDos(String waktu) {
        List<iklan> todos = new ArrayList<iklan>();
        String selectQuery = "SELECT  * FROM iklan where '"+waktu+"' BETWEEN `waktumulai` AND `waktuselesai` " +
                "AND tikaktif ='1'";
//          String selectQuery = "SELECT  * FROM iklan ";

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                iklan td = new iklan();
                td.setTglselesai(c.getString(c.getColumnIndex("tglselesai")));
                td.setHtml(c.getString(c.getColumnIndex("html")));
                td.setMulaijam(c.getString(c.getColumnIndex("waktumulai")));
                td.setSelesaijam(c.getString(c.getColumnIndex("waktuselesai")));
                Log.e("asok", waktu + "" + td.getSelesaijam() + "," + td.getMulaijam());
                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }
    public List<iklan> getAllToDos1() {
        List<iklan> todos = new ArrayList<iklan>();
//        String selectQuery = "SELECT  * FROM iklan where tikaktif ='0'";
//          String selectQuery = "SELECT  * FROM iklan ORDER BY tikid DESC";
        String selectQuery = "SELECT  * FROM iklan ORDER BY tglselesai DESC,tikorder ASC,tikid ASC";

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                iklan td = new iklan();
                td.setTikid(c.getString(c.getColumnIndex("tikid")));
                td.setTglselesai(c.getString(c.getColumnIndex("tglselesai")));
                td.setHtml(c.getString(c.getColumnIndex("html")));
                td.setMulaijam(c.getString(c.getColumnIndex("waktumulai")));
                td.setSelesaijam(c.getString(c.getColumnIndex("waktuselesai")));
                td.setJudul(c.getString(c.getColumnIndex("tikjudul")));
                td.setHead(c.getString(c.getColumnIndex("tikhead")));
//                Log.e("asok", td.getSelesaijam() + "," + td.getMulaijam());
                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }
    public int getCountTikid(String id) {
        String countQuery = "SELECT  * FROM iklan where "+KEY1_TIKID +"="+id;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public int getCount() {
        String countQuery = "SELECT * FROM iklan";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // table ke 3
    public int getCountbanner() {
        String countQuery = "SELECT * FROM iklanbanner";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public long insertbanner(long rowid,int poin) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY2_ROWID, rowid);
        initialValues.put(KEY2_TIKPOIN, poin);
        return db.insert("iklanbanner", null, initialValues);
    }
    public boolean updatebanner(long rowid,int poin) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY2_ROWID, rowid);
        initialValues.put(KEY2_TIKPOIN, poin);
        return db.update("iklanbanner", initialValues, KEY2_ROWID + "=" + rowid, null) > 0;
    }
    public Cursor getContactbanner(long id)throws SQLException{
        Cursor contact=db.query(true,DATABASE_TABLE3,new String[]{
                        KEY2_ROWID, KEY2_TIKPOIN},KEY2_ROWID+"="+id,
                null,null,null,null,null);
        if (contact != null) {
            contact.moveToFirst();
        }
        return contact;
    }
}