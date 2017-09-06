package com.newlandframework.rpc.db;

import java.util.ResourceBundle;



public class SingletonDB {  //閸楁洑绶�


	private String driver = null;
	private String url = null;
	private String user = null;
	private String password = null;
	private int initConnNum = 0;  //閸掓繂顫愭潻鐐村复閺侊拷
	private int maxConnNum = 0;//閺堬拷銇囨潻鐐村复閺侊拷
	private int incrementConnNum = 0;//婢х偤鏆辨潻鐐村复閺侊拷

	private SingletonDB() {
		ResourceBundle	rb=ResourceBundle.getBundle("db");
		driver=rb.getString("driver");
		url=rb.getString("url");
		user=rb.getString("user");
		password=rb.getString("password");
		initConnNum=Integer.parseInt(rb.getString("initConnNum"));
		maxConnNum=Integer.parseInt(rb.getString("maxConnNum"));
		incrementConnNum=Integer.parseInt(rb.getString("incrementConnNum"));
		
		

	};

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getInitConnNum() {
		return initConnNum;
	}

	public void setInitConnNum(int initConnNum) {
		this.initConnNum = initConnNum;
	}

	public int getMaxConnNum() {
		return maxConnNum;
	}

	public void setMaxConnNum(int maxConnNum) {
		this.maxConnNum = maxConnNum;
	}

	public int getIncrementConnNum() {
		return incrementConnNum;
	}

	public void setIncrementConnNum(int incrementConnNum) {
		this.incrementConnNum = incrementConnNum;
	}

	private static SingletonDB singleton;

	public static SingletonDB getSingleton() {
		if (singleton == null) {
			singleton = new SingletonDB();
		}

		return singleton;
	}

}
