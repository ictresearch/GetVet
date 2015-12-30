package com.pusatict.getvet.datalistadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;

import com.pusatict.getvet.R;
import com.pusatict.getvet.coba;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

public class ListAdapteriklan extends BaseAdapter{
	private Context context;
	private List<iklan> list, filterd;

	public ListAdapteriklan(Context context, List<iklan> list) {
		this.context = context;
		this.list = list;
		this.filterd = this.list;
	}
	
	@Override
	public int getCount() {
		return filterd.size();
	}

	@Override
	public Object getItem(int position) {
		return filterd.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.list_webview, null);
		}
		iklan ikn = filterd.get(position);
		WebView webView=(WebView)convertView.findViewById(R.id.webView);

		/*webView.setInitialScale(1);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);*/
		webView.getSettings().setJavaScriptEnabled(true);

		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.loadDataWithBaseURL("file:///sdcard/dir", ikn.getHtml(), "text/html", "utf-8", "");
		//webView.loadUrl(ikn.getHtml());
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
//		TextView htmlTextView = (TextView)convertView.findViewById(R.id.html_text);
//		htmlTextView.setText(Html.fromHtml(ikn.getHtml()));
		return convertView;
	}
	
//	@Override
//	public Filter getFilter() {
//		maskotaFilter filter = new maskotaFilter();
//		return filter;
//	}
//
//	/** Class filter untuk melakukan filter (pencarian) */
//	private class maskotaFilter extends Filter{
//
//		@Override
//		protected FilterResults performFiltering(CharSequence constraint) {
//			List<maskota> filteredData = new ArrayList<maskota>();
//			FilterResults result = new FilterResults();
//			String filterString = constraint.toString().toLowerCase();
//			for(maskota mhs: list){
//				if(mhs.getKtnama().toLowerCase().contains(filterString)){
//					filteredData.add(mhs);
//				}
//			}
//			result.count = filteredData.size();
//			result.values =  filteredData;
//			return result;
//		}
//
//		@Override
//		protected void publishResults(CharSequence constraint, FilterResults results) {
//			filterd = (List<maskota>) results.values;
//			notifyDataSetChanged();
//		}

//	}
}