package com.omrobbie.cataloguemovie.mvp.model.detail;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguagesItem{

	@SerializedName("name")
	private String name;

	@SerializedName("iso_639_1")
	private String iso6391;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIso6391(String iso6391){
		this.iso6391 = iso6391;
	}

	public String getIso6391(){
		return iso6391;
	}

	@Override
 	public String toString(){
		return 
			"SpokenLanguagesItem{" + 
			"name = '" + name + '\'' + 
			",iso_639_1 = '" + iso6391 + '\'' + 
			"}";
		}
}