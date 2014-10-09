package com.example.flipnews;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;



public class NewsList extends Activity 
{
	String newsTrendName;
	AssetDatabaseOpenHelper open;
	SQLiteDatabase db;
	List<NewsItems> newsItems;
	NewsItems newsItem;
	ListView newsList;
	String displayName;
	
	ArrayList<String> trendList;
	NewsListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		
		newsList = (ListView) findViewById(android.R.id.list);
		TextView trendName = (TextView) findViewById(R.id.trendName);
		trendList = new ArrayList<String>();
		
		Intent getNewsListIntent = getIntent();
		Bundle bundle = new Bundle();
		bundle = getNewsListIntent.getExtras();
		newsTrendName = bundle.getString("newsTrendName");
		trendList = bundle.getStringArrayList("trendList");
		displayName = bundle.getString("displayName");
		
		trendName.setText(newsTrendName.toUpperCase());
		
		open = new AssetDatabaseOpenHelper(this);
		db = open.openDatabase();
		
		Cursor c = open.newsTableItems(newsTrendName, db);
		
		newsItems = new ArrayList<NewsItems>();
		//got the cursor. now extract values from cursor and store it in a NewsItems list as newsItem object for each row.
		newsItems = extractValues(c);
		
		adapter = new NewsListAdapter(NewsList.this, newsItems, newsTrendName);
		newsList.setAdapter(adapter);
		
		newsList.setOnItemClickListener(new OnItemClickListener() 
		{
			boolean newsListFlag = false;
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) 
			{
				Intent newsViewIntent = new Intent(NewsList.this, NewsViewActivity.class);
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("NewsItem", adapter.getItem(position));
				
				//new addition
				bundle.putString("newsTrendName", newsTrendName);
				bundle.putStringArrayList("trendList", trendList);
				bundle.putString("displayName", displayName);
				
				newsListFlag = true;
				
				newsViewIntent.putExtra("newsListFlag", newsListFlag);
				newsViewIntent.putExtras(bundle);
				startActivity(newsViewIntent);
				
			}
		});
		
		newsList.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			String linkUrl;
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v, final int position, long id) {
				newsItem = new NewsItems();
				
				CharSequence[] items = {"Save"};
				AlertDialog.Builder builder = new AlertDialog.Builder(NewsList.this);
				builder.setTitle("Add to Favourites");
				builder.setItems(items, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						
						newsItem = adapter.getItem(position);
						linkUrl = newsItem.getLink();
						int rowCheck = open.insertUserPrefData(db, linkUrl);
						if(rowCheck!=-1){
							new AlertDialog.Builder(NewsList.this)
							.setTitle("Success")
							.setMessage("News Added to your Favourites!")
							.setPositiveButton("Done", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
								}
							}).show();
							
						}
						
					}
				});
				
				AlertDialog alert = builder.create();
				alert.show();
				
				return false;
			}
		});
		
	}

	/*
	 * Extract values from the cursor and return List<newsItems> object
	 */
	private List<NewsItems> extractValues(Cursor c) 
	{
			while(c.moveToNext()){
			int columnTitle = c.getColumnIndex("title");
			int columnDescription = c.getColumnIndex("description");
			int columnLink = c.getColumnIndex("link");
			int columnGuid = c.getColumnIndex("guid");
			int columnPubdate = c.getColumnIndex("pubdate");
			int columnMedia = c.getColumnIndex("media");
			
			//adding newsItem object to the list of NewsItems
			newsItems.add(new NewsItems(c.getString(columnTitle).toString(), 
					c.getString(columnLink).toString(), 
					c.getString(columnDescription).toString(), 
					c.getString(columnPubdate).toString(), 
					c.getString(columnGuid).toString(), 
					c.getString(columnMedia).toString()));
		}
		return newsItems;
	}
	
	@Override
	public void onBackPressed()
	{
		Intent intent1 = new Intent(NewsList.this, TrendActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("Trend", trendList);
		intent1.putExtras(bundle);
		intent1.putExtra("userNameToDisplay", displayName);
		startActivity(intent1);
		NewsList.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_list, menu);
		return true;
	}

}
