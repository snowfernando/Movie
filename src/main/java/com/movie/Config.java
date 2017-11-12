package com.movie;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private static Config instance = null;
	private Properties properties = null;
	
	private Config(){
		init();
	}
	
	public static Config getInstance(){
		if(instance == null) {
	         instance = new Config();
	      }
	      return instance;
	}

	public void init(){
		//System.out.println("Config initialized... ");
		try (InputStream in = new FileInputStream("movie.config")) {
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			System.out.println("Error initializing config... ");
			e.printStackTrace();
		}
	}
	
	public String getProperty(String propertyName){
		
		return properties.getProperty(propertyName);
		
	}
	
}
