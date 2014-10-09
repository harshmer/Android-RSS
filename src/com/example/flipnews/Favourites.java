package com.example.flipnews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Favourites extends Activity 
{
	SQLiteDatabase db;
	AssetDatabaseOpenHelper open;
	
	ArrayList<String> newsItemLink, trendList;
	FavouriteAdapter adapter;
	
	ListView favouriteListView;
	TextView userName;
	String displayName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
		
		//wiring the views
		favouriteListView = (ListView) findViewById(android.R.id.list);
		userName = (TextView) findViewById(R.id.userName);
		
		//get Intent
		Intent getFavouriteIntent = getIntent();
		Bundle bundle = getFavouriteIntent.getExtras();
		trendList = bundle.getStringArrayList("TrendList");
		
		//get the display name
		displayName = getFavouriteIntent.getStringExtra("displayName");
		
		//set the display name to the username to be displayed on the screen
		userName.setText(displayName);
		
		//open database
		open = new AssetDatabaseOpenHelper(Favourites.this);
		
		db = open.openDatabase();
		
		Cursor c = open.getUserFavourites(db);
		
		
		//gets the news links arraylist
		newsItemLink = getLinks(c);
		
		//set the adapter
		adapter = new FavouriteAdapter(this, newsItemLink);
		favouriteListView.setAdapter(adapter);
		
		//on item click listener
		favouriteListView.setOnItemClickListener(new OnItemClickListener() {
			boolean favouriteFalg = false;
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) 
			{
				favouriteFalg = true;
				Intent newsWebView_Favourite = new Intent(Favourites.this, NewsViewActivity.class);
				
				newsWebView_Favourite.putExtra("favFlag", favouriteFalg);
				newsWebView_Favourite.putExtra("newsFavouriteLink", adapter.getItem(pos));
				
				Bundle bundle = new Bundle();
				bundle.putString("displayName", displayName);
				bundle.putStringArrayList("trendList", trendList);
				
				newsWebView_Favourite.putExtras(bundle);
				
				//call the same webview with flag set in intent to identify the caller
				startActivity(newsWebView_Favourite);
				
			}
		});
	}
		
	
	/*
	 * returns the news links stored in the cursor retrieved from database
	 */
	private ArrayList<String> getLinks(Cursor c) 
	{
		newsItemLink = new ArrayList<String>();
		NewsItems newsItem = new NewsItems();
		
		while(c.moveToNext()){
			int columnLink = c.getColumnIndex("newslink");
			newsItem.setLink(c.getString(columnLink));
			newsItemLink.add(newsItem.getLink().toString());
		
		}
		return newsItemLink;
	}

	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(Favourites.this, TrendActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("Trend", trendList);
		intent.putExtras(bundle);
		intent.putExtra("userNameToDisplay", displayName);
		startActivity(intent);
		Favourites.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

}
