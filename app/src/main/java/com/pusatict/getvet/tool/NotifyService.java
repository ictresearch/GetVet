package com.pusatict.getvet.tool;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.Comment;
import com.pusatict.getvet.datalistadapter.dokter;
import com.pusatict.getvet.datalistadapter.thread;
import com.pusatict.getvet.forum.ForumDetails;
import com.pusatict.getvet.forum.forum_main;
import com.pusatict.getvet.sponsor.Banner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotifyService extends Service {

	final static String ACTION = "NotifyServiceAction";
	final static String STOP_SERVICE = "";
	final static int RQS_STOP_SERVICE = 0;

	NotifyServiceReceiver notifyServiceReceiver;

	private static int MY_NOTIFICATION_ID=1;
	private NotificationManager notificationManager;
	private Notification myNotification;
	private NotificationManager notificationManager1;
	private Notification myNotification1;
	//	NotificationManager[] notificationManager1 = new NotificationManager[100];
//	Notification[] myNotification1 = new Notification[100];
	private final String myBlog = "http://android-er.blogspot.com/";
	private String uid,threads,comthreade;
	private static Timer time= new Timer();
	private static Timer time1= new Timer();
	private Context ctx;
	private int stopinsert=0;
	JSONParser jsonParser = new JSONParser();
	private tesKoneksiInet koneksiInet;
	Calendar c1 = Calendar.getInstance();
	SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");
	private String tanggal;
	DBAdapter db = new DBAdapter(NotifyService.this);
	DBAdapter db1 = new DBAdapter(NotifyService.this);
	private Toast toast;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		notifyServiceReceiver = new NotifyServiceReceiver();
		koneksiInet = new tesKoneksiInet(getApplicationContext());
		ctx=this;
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		time.scheduleAtFixedRate(new mainTask(),0,14400000);
		time1.scheduleAtFixedRate(new mainTaskBrod(),0,1800000);
//		time.scheduleAtFixedRate(new mainTask(),0,10000);
//		time1.scheduleAtFixedRate(new mainTaskBrod(),0,20000);
////		Thread th=new Thread() {
//			@Override
//			public void run() {
//
//				int a = 0;
//				try {
//					while (a < 3) {
//						Thread th1 = null;
//						th1.sleep(100);
//						th1=new Thread() {
//							@Override
//							public void run() {
//								Log.e("asik", "asik");
//							}
//						};
//
//						a++;
//					}
//				} catch (InterruptedException s) {
//				}
//			}
//		};
//		th.start();
		return super.onStartCommand(intent, flags, startId);
	}
	private class mainTask extends TimerTask
	{
		public void run()
		{
//			toast = Toast.makeText(getApplicationContext(), "threade "+c1.getTime().getMinutes(),
//					Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.show();
//			Toast.makeText(getBaseContext(), "threade "+c1.getTime().getMinutes(),
//					Toast.LENGTH_LONG).show();
			Log.e("taggal tok","asike threade");
			db.open();
			Cursor c=db.getContact(1);
			if (c.moveToFirst()) {
				uid=c.getString(5);
				threads=c.getString(12);
				comthreade=c.getString(13);
				if(koneksiInet.isNetworkAvailable()) {
					if (threads.equals("1")) {
						new MainActivityAsync().execute();
					}
					if (comthreade.equals("1")) {
						new usercommend().execute();
					}
				}
			}
			db.close();

		}
	}
	private class mainTaskBrod extends TimerTask {
		public void run() {

//			toast = Toast.makeText(getApplicationContext(), "threade "+c1.getTime().getMinutes(),
//					Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.show();
			Log.e("taggal tok","asike");
			tanggal=Ftgl.format(c1.getTime());
//			Log.e("taggal tok",tanggal);
			db1.open();
			Cursor c=db1.getContact(1);
			if (c.moveToFirst()) {
				uid=c.getString(5);
				threads=c.getString(12);
				comthreade=c.getString(13);
				if(koneksiInet.isNetworkAvailable()) {
					new broadcast().execute();
				}
			}
			db1.close();
//			new broadcast().execute();
		}
	}
	private class MainActivityAsync extends AsyncTask<String, Void, String> {
		private String success1,success3;
		private Long success;
		private Long success2;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			String url="http://getsimplebisnis.com/index.php/api/userthreaduid";

			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("uid", uid));

			JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
			try {
				success3 = json.getString("insert");
//				success1 = json.getString("Total");
				JSONArray jsonArray = json.getJSONArray("userthread");
					for(int i = 0; i < jsonArray.length(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);
						success = (obj.getLong("utjumlah"));
						success2 = (obj.getLong("Total"));
					}
//					Log.e("asik", success2 - success + ","+success1+","+success3);
			} catch (JSONException e){
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s)
		{
//			super.onPostExecute(s);

			if(!success3.equals("1"))
			{
				if ((success2 - success) != 0)
				{
//					Log.e("asik", success2 - success + "");
					IntentFilter intentFilter = new IntentFilter();
					intentFilter.addAction(ACTION);
					registerReceiver(notifyServiceReceiver, intentFilter);

					// Send Notification
					notificationManager =
							(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					myNotification = new Notification(R.drawable.vet_notif3,
							"GetVet",
							System.currentTimeMillis());
					Context context = getApplicationContext();
					String notificationTitle = "GetVet";
					String notificationText = success2 - success + " New Thread";
//					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
					Intent myIntent = new Intent(getBaseContext(), forum_main.class);
					PendingIntent pendingIntent
							= PendingIntent.getActivity(getBaseContext(),
							0, myIntent,
							Intent.FLAG_ACTIVITY_NEW_TASK);
					myNotification.defaults |= Notification.DEFAULT_SOUND;
					myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

					myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
					notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
//			MY_NOTIFICATION_ID++;
				}
			}
		}
	}
	private class usercommend extends AsyncTask<String, Void, String> {
		private String success1,success3;
		private Long success,success2,hasil;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			String url="http://getsimplebisnis.com/index.php/api/usercommenduid";

			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("uid", uid));

			JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
			try {
				success3 = json.getString("insert");
//				success1 = json.getString("Total");
//				Log.e("asikcom",success3);
				thread thr=null;
				if(!success3.equals("1")) {
					JSONArray jsonArray = json.getJSONArray("usercommend");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);
						thr = new thread();
						success = (obj.getLong("ucjml"));
						success2 = (obj.getLong("Total"));
						thr.setId(obj.getLong("thid"));
						thr.setJudul(obj.getString("thjudul"));
						thr.setIsi(obj.getString("thisi"));
						thr.setUid(obj.getString("uid"));
						thr.setSpid(obj.getString("spid"));
						thr.setKatid(obj.getString("katid"));
						thr.setDate(obj.getString("thtanggalPost"));
//						thr.setComment(obj.getString("com"));
						thr.setNamauser(obj.getString("unama"));
						thr.setFoto(obj.getString("thfoto"));
						hasil=success2-success;
						if ((hasil) != 0) {
							notifcommend(thr,hasil,i);
						}
					}
				}
			}catch (JSONException e) {
				Log.e("asikcom",e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
		}
	}
	private class broadcast extends AsyncTask<String, Void, String> {
		private String success3,judul,head,htmlbroad;
		private int idbroad;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			String url="http://getsimplebisnis.com/index.php/api/SelectBroadcast";

			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("uid", uid));
			nvp.add(new BasicNameValuePair("tgl", tanggal));

			JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
			try {
				success3 = json.getString("succes");
//				success1 = json.getString("Total");
				if(success3.equals("1")) {
					JSONArray jsonArray = json.getJSONArray("broadcast");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);
						idbroad = (obj.getInt("tdid"));
						judul = (obj.getString("tikjudul"));
						head = (obj.getString("tikhead"));
						htmlbroad = (obj.getString("tikhtml"));
//					Log.e("asik", success2 - success + ","+success1+","+success3);
					}
				}
			}catch (JSONException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
//			super.onPostExecute(s);
//			Log.e("asik", success1+",");
			if(success3.equals("1")) {
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(ACTION);
				registerReceiver(notifyServiceReceiver, intentFilter);

				// Send Notification
				notificationManager =
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				myNotification = new Notification(R.drawable.vet_notif3,
						"GetVet",
						System.currentTimeMillis());
				Context context = getApplicationContext();
				String notificationTitle = judul;
				String notificationText = head;
//					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
				Intent intent = new Intent(getBaseContext(), Banner.class);
				Bundle Parsing = new Bundle();
//                Parsing.putString("banner", "http://www.animalrapidtest.com/");
				Parsing.putString("banner", htmlbroad);
				Parsing.putString("broadcash", "1");
				Parsing.putString("tdid", idbroad+"");
				intent.putExtras(Parsing);
				PendingIntent pendingIntent
						= PendingIntent.getActivity(getBaseContext(),
						idbroad, intent,
						PendingIntent.FLAG_CANCEL_CURRENT);
				myNotification.defaults |= Notification.DEFAULT_SOUND;
				myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

				myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
				notificationManager.notify(idbroad, myNotification);
//				MY_NOTIFICATION_ID++;
			}
		}
	}
	public void notifcommend(thread selectedList,Long selisi,int i){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);

//		Log.e("Asike",selectedList.getId()+"");
		// Send Notification
		notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		myNotification = new Notification(R.drawable.vet_notif3,
				"GetVet",
				System.currentTimeMillis());
		Context context = getApplicationContext();
		String notificationTitle = "GetVet";
		String notificationText = selisi+" New Comment";
//					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
		Intent myIntent = new Intent(getBaseContext(), ForumDetails.class);
		Bundle Parsing = new Bundle();
		Parsing.putString("thid", selectedList.getId().toString());
		Parsing.putString("judul", selectedList.getJudul().toString());
		Parsing.putString("isi", selectedList.getIsi().toString());
		Parsing.putString("uid", selectedList.getUid().toString());
		Parsing.putString("katid", selectedList.getKatid().toString());
		Parsing.putString("date", selectedList.getDate().toString());
		Parsing.putString("namauser", selectedList.getNamauser().toString());
//		Parsing.putString("jumcomment", selectedList.getComment().toString());
		Parsing.putString("foto", selectedList.getFoto().toString());
		Parsing.putString("to", "1");
		myIntent.putExtras(Parsing);
		PendingIntent pendingIntent
				= PendingIntent.getActivity(getBaseContext(),
				selectedList.getId().intValue(), myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
		myNotification.defaults |= Notification.DEFAULT_SOUND;
		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
		notificationManager.notify(selectedList.getId().intValue(), myNotification);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(notifyServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class NotifyServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int rqs = arg1.getIntExtra("RQS", 0);
			Log.e("rqs",rqs+"");
			if (rqs == RQS_STOP_SERVICE){
				stopSelf();
			}
		}
	}

}
