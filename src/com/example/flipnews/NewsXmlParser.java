package com.example.flipnews;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class NewsXmlParser
{
	
	// RSS XML document CHANNEL tag
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static String TAG_MEDIA = "media:thumbnail";
    
    private String thumbnail_1 = null;
	private String thumbnail_2 = null;
	private boolean flag = false;
	private String mediaUrl = "url";
	private int rowCheck = 0;
	    
	public List<NewsItems> parse(String rssUrl, String newsTrend, SQLiteDatabase db)
	{
		
	List<NewsItems> newsItem = null;
	
	
	NewsItems currNewsitem = null;
	String curText = "";
	
	try {
		
		URL url = new URL(rssUrl);
		
		// Get our factory and PullParser
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = factory.newPullParser();
		
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		InputStream in = new BufferedInputStream(urlConnection.getInputStream());

		//InputStream fis = ctx.getAssets().open("F1Racers.xml");
		//BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		
		xpp.setInput(in, null);
		
		//create hashmap for inserting the values to the database
		ContentValues tableValues = new ContentValues();
		
		boolean done = false;
		// get initial eventType
		int eventType = xpp.getEventType();
		
		// Loop through pull events until we reach END_DOCUMENT
		while (eventType != XmlPullParser.END_DOCUMENT && !done) 
		{
			String tagname = null;
					// React to different event types appropriately
						switch (eventType) 
						{
						case XmlPullParser.START_DOCUMENT:
							newsItem = new ArrayList<NewsItems>();
							break;
							
						case XmlPullParser.START_TAG:
							// Get the current tag
							tagname = xpp.getName();
							if (tagname.equalsIgnoreCase(TAG_ITEM)) {
								// If we are starting a new <item> block we need a new newsItem object to represent it
								currNewsitem = new NewsItems();
							}
							else if (currNewsitem!=null){
								if(tagname.equalsIgnoreCase(TAG_TITLE))
									currNewsitem.setTitle(xpp.nextText());
								
								else if(tagname.equalsIgnoreCase(TAG_LINK))
									currNewsitem.setLink(xpp.nextText());
								
								else if(tagname.equalsIgnoreCase(TAG_DESRIPTION))
									currNewsitem.setDescription(xpp.nextText());
								
								else if(tagname.equalsIgnoreCase(TAG_GUID))
									currNewsitem.setGuid(xpp.nextText());
								
								else if(tagname.equalsIgnoreCase(TAG_PUB_DATE))
									currNewsitem.setPubdate(xpp.nextText());
								
								else if(tagname.equalsIgnoreCase(TAG_MEDIA)){
									
									//as there r two thumbnail sizes & hence take one and compare with other big size
									thumbnail_1 = xpp.getAttributeValue(null, "width").toString();
									
									if(!flag) //check flag for copying the first low value
										thumbnail_2 = thumbnail_1;
									
									flag = true; // set flag for low value being copied
									
									if(Integer.parseInt(thumbnail_2) < Integer.parseInt(thumbnail_1)){
										thumbnail_1 = xpp.getAttributeValue(null, "width").toString();
										mediaUrl = xpp.getAttributeValue(null, "url").toString();
										currNewsitem.setMediaThumbnail(mediaUrl);
									}
									
								}
							}
							break;

						case XmlPullParser.END_TAG:
							tagname = xpp.getName();
							if(tagname.equalsIgnoreCase(TAG_ITEM) && currNewsitem!=null)
							{
								if(currNewsitem.getTitle()!=null && 
										currNewsitem.getDescription()!=null && 
										currNewsitem.getLink()!=null && 
										currNewsitem.getGuid()!=null && 
										currNewsitem.getPubdate()!=null && 
										currNewsitem.getMediaThumbnail()!=null)
								{
									newsItem.add(currNewsitem);
									tableValues.put("title", currNewsitem.getTitle());
									tableValues.put("description", currNewsitem.getDescription());
									tableValues.put("link", currNewsitem.getLink());
									tableValues.put("guid", currNewsitem.getGuid());
									tableValues.put("pubdate", currNewsitem.getPubdate());
									tableValues.put("media", currNewsitem.getMediaThumbnail());

									rowCheck  = (int) db.insert(newsTrend, null, tableValues);
								}
							}
							else if(tagname.equalsIgnoreCase(TAG_CHANNEL))
								done = true;
							break;

						default:
							break;
						}
						//move on to next iteration
						eventType = xpp.next();
					}
					
	} 
	
	catch (Exception e) {
		e.getMessage();
		e.getLocalizedMessage();
	}
	
	//person list returned back to the main activity to the adapter
	return newsItem;
	
	}

}
