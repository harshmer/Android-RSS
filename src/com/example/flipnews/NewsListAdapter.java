/**
 * 
 */
package com.example.flipnews;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * @author Harsh Mer
 *
 */
public class NewsListAdapter extends ArrayAdapter<NewsItems>
{
	List<NewsItems> newsItems;
	Activity c;
	String trendName;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	public NewsListAdapter(Activity context, List<NewsItems> newsItems, String trendName) {
		super(context, R.layout.newslist, newsItems);
		this.c = context;
		this.newsItems = newsItems;
		this.trendName = trendName;
		
		//Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        
        //Setup options for ImageLoader so it will handle caching for us.
        options = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflator = c.getLayoutInflater();
		View row = inflator.inflate(R.layout.newslist, null);
		
		NewsItems newsItem = newsItems.get(position);
		
		TextView title = (TextView) row.findViewById(R.id.title);
		//TextView trendName = (TextView) row.findViewById(R.id.trendName);
		TextView description = (TextView) row.findViewById(R.id.description);
		TextView pubdate = (TextView) row.findViewById(R.id.pubdate);
		ImageView media = (ImageView) row.findViewById(R.id.media);
		
		title.setText(newsItem.getTitle());
		//trendName.setText(this.trendName);
		description.setText(newsItem.getDescription());
		pubdate.setText(newsItem.getPubdate());
		
		ImageLoadingListener listener = new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		
		//set image using that library file
		//Load the image and use our options so caching is handled.
		imageLoader.displayImage(getItem(position).mediaThumbnail, media, options, listener);
		
		return row;
	}

}
