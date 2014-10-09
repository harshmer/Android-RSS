package com.example.flipnews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewsViewActivity extends Activity 
{
	NewsItems newsItem;
	WebView newsView;
	ProgressBar loading;
	boolean favouritesFlag, newsListFlag;
	String url;
	String newsFavLink;
	String newsTrendName, displayName;
	ArrayList<String> trendList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_view);
		
		Intent getNewsIntent = getIntent();
		Bundle bundle = getNewsIntent.getExtras();
		
		newsTrendName = bundle.getString("newsTrendName");
		displayName = bundle.getString("displayName");
		trendList = bundle.getStringArrayList("trendList");
		
		
		
		newsListFlag = getNewsIntent.getBooleanExtra("newsListFlag", false);
		favouritesFlag = getNewsIntent.getBooleanExtra("favFlag", false);
		
		if(newsListFlag)
		{
			newsItem = (NewsItems) bundle.getSerializable("NewsItem");
			url = newsItem.getLink();
			//newsListFlag = false;
		}
		
		else if(favouritesFlag)
		{
			newsFavLink = getNewsIntent.getStringExtra("newsFavouriteLink");
			url = newsFavLink;
			//favouritesFlag = false;
		}
		
		
		newsView = (WebView) findViewById(R.id.newsView);
		newsView.getSettings().setBuiltInZoomControls(true);
		newsView.getSettings().setJavaScriptEnabled(true);
		newsView.getSettings().setBuiltInZoomControls(true);
		newsView.getSettings().setUseWideViewPort(true);
		newsView.getSettings().setSupportMultipleWindows(true);
		newsView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		newsView.getSettings().setLoadWithOverviewMode(true);

		loading = (ProgressBar) findViewById(R.id.progressBar1);
		
				
		this.setTitle(url);
		
		newsView.setWebViewClient(new MyWebViewClient());
		newsView.loadUrl(url);
	}
	
	
	
	class MyWebViewClient extends WebViewClient
	{

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			loading.setVisibility(view.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}
		
	}

	
	
	@Override
	public void onBackPressed()
	{
		Intent intent = null;
		if(favouritesFlag){
			intent = new Intent(NewsViewActivity.this, Favourites.class);
			intent.putExtra("displayName", displayName);
			Bundle bundle = new Bundle();
			
			bundle.putString("newsTrendName", newsTrendName);
			bundle.putStringArrayList("TrendList", trendList);
			
			intent.putExtras(bundle);
			startActivity(intent);
			NewsViewActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			
			favouritesFlag = false;
		}
		
		else if(newsListFlag){
			intent = new Intent(NewsViewActivity.this, NewsList.class);
			newsListFlag = false;
			Bundle bundle = new Bundle();
			bundle.putString("newsTrendName", newsTrendName);
			bundle.putString("displayName", displayName);
			
			bundle.putStringArrayList("trendList", trendList);
			
			intent.putExtras(bundle);
			startActivity(intent);
			NewsViewActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		}
		
		
		
	}

}
