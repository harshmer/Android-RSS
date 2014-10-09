package com.example.flipnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity 
{
	SQLiteDatabase db;
	AssetDatabaseOpenHelper open;
	Trends trends;
	ProgressBar loading;
	
	ArrayList<String> trendList = null;
	EditText username, emailId;	
	Button register;
	int check = -1;
	String name = null;
	
	String nameCheck, emailCheck;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loading = (ProgressBar) findViewById(R.id.progressBar1);
		loading.setVisibility(View.INVISIBLE); //loading invisible at first
		
		username = (EditText) findViewById(R.id.name);
		nameCheck = username.getText().toString();
		
		emailId = (EditText) findViewById(R.id.email);
		emailCheck = emailId.getText().toString();
		
		register = (Button) findViewById(R.id.register);
		
		//database open
		open = new AssetDatabaseOpenHelper(this);
		db = open.openDatabase();//returns the open database
		
		trends = new Trends();
		trends = open.newsTrends(db);//returns news trends list from database
		
		trendList = new ArrayList<String>();
		trendList.add(trends.getBusiness());
		trendList.add(trends.getEntertainment());
		trendList.add(trends.getHealth());
		trendList.add(trends.getScience());
		trendList.add(trends.getSports());
		trendList.add(trends.getTechnology());
		trendList.add(trends.getWorld());
		
		//checks for user in db
		name = open.checkUser(db);
		
		if(name!=null){
			username.setVisibility(View.GONE);
			emailId.setVisibility(View.GONE);
			register.setVisibility(View.GONE);
			
			loading.setVisibility(View.VISIBLE);
			
			new AsyncLoadXMLFeed().execute();
			
		}
		
		//Inserting the user in db once and forever.
		register.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) 
			{
				if(!(username.getText().toString().isEmpty()) && !(emailId.getText().toString().isEmpty()))
				{
				check = open.storeUser(db, username.getText().toString(), emailId.getText().toString());
				if (check!=-1){
					username.setVisibility(View.GONE);
					emailId.setVisibility(View.GONE);
					register.setVisibility(View.GONE);
					
					loading.setVisibility(View.VISIBLE);
					name = username.getText().toString();
					new AsyncLoadXMLFeed().execute();
				}
				}
			}
		});
		
	}
	
	@SuppressWarnings("rawtypes")
	private class AsyncLoadXMLFeed extends AsyncTask {

		@Override
		protected void onPostExecute(Object result) 
		{
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			finish();
			Intent trendsActivity = new Intent(MainActivity.this, TrendActivity.class);
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("Trend", trendList);
			trendsActivity.putExtras(bundle);
			trendsActivity.putExtra("userNameToDisplay", name);//sending the username to display
			startActivity(trendsActivity);
			MainActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			
			db.close();
		}

		@Override
		protected Object doInBackground(Object... params) 
		{
			
			Map<String, String> newsTrendsLinks = new HashMap<String, String>();
			newsTrendsLinks.put("technology", "http://feeds.bbci.co.uk/news/technology/rss.xml");
			newsTrendsLinks.put("business", "http://feeds.bbci.co.uk/news/business/rss.xml");
			newsTrendsLinks.put("sports", "http://feeds.bbci.co.uk/sport/0/rss.xml");
			newsTrendsLinks.put("entertainment", "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml");
			newsTrendsLinks.put("Science", "http://feeds.bbci.co.uk/news/science_and_environment/rss.xml");
			newsTrendsLinks.put("world", "http://feeds.bbci.co.uk/news/rss.xml");
			newsTrendsLinks.put("health", "http://feeds.bbci.co.uk/news/health/rss.xml");
			
			
			NewsXmlParser parser = new NewsXmlParser();
			List<NewsItems> newsItems = null;
			
			for (java.util.Map.Entry<String, String> entry : newsTrendsLinks.entrySet())
			{
				String trend = entry.getKey();
				String trendLink = entry.getValue();
				newsItems = parser.parse(trendLink, trend, db);
				
			}
			 
			return null;
		}

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
