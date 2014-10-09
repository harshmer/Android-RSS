package com.example.flipnews;

import java.io.Serializable;

public class Trends implements Serializable {
	
	private String business;
	private String entertainment; 
	private String health;
	private String science;
	private String sports;
	private String technology;
	private String world;
	
	/*
	 * all getters
	 */
	public String getBusiness() {
		return business;
	}
	public String getEntertainment() {
		return entertainment;
	}
	public String getHealth() {
		return health;
	}
	public String getScience() {
		return science;
	}
	public String getSports() {
		return sports;
	}
	public String getTechnology() {
		return technology;
	}
	public String getWorld() {
		return world;
	}
	
	/*
	 * all setters
	 */
	public void setBusiness(String business) {
		this.business = business;
	}
	public void setEntertainment(String entertainment) {
		this.entertainment = entertainment;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public void setScience(String science) {
		this.science = science;
	}
	public void setSports(String sports) {
		this.sports = sports;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public void setWorld(String world) {
		this.world = world;
	}

}
