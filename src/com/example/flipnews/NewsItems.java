package com.example.flipnews;

import java.io.Serializable;

public class NewsItems implements Serializable
{
	// All <item> node name
    String title;
    String link;
    String description;
    String pubdate;
    String guid;
    String mediaThumbnail;
    
    
      
    public NewsItems() {
		super();
	}

	public NewsItems(String title, String link, String description, String pubdate, String guid,String mediaThumbnail) 
    {
		super();
		this.title = title;
		this.link = link;
		this.description = description;
		this.pubdate = pubdate;
		this.guid = guid;
		this.mediaThumbnail = mediaThumbnail;
	}
    
	//All getters
	public String getTitle() {
		return title;
	}
	public String getLink() {
		return link;
	}
	public String getDescription() {
		return description;
	}
	public String getPubdate() {
		return pubdate;
	}
	public String getGuid() {
		return guid;
	}
	public String getMediaThumbnail() {
		return mediaThumbnail;
	}
	
	//all setters
	public void setTitle(String title) {
		this.title = title;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setMediaThumbnail(String mediaThumbnail) {
		this.mediaThumbnail = mediaThumbnail;
	}
    

}
