package com.webapp.ratelimiters.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class rateLimiterModel {
	
	@Id
	private int id;
	private String name;
	private String sports;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSports() {
		return sports;
	}
	public void setSports(String sports) {
		this.sports = sports;
	}
	
}
