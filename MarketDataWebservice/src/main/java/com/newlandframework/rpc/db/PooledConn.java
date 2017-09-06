package com.newlandframework.rpc.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class PooledConn {

	private Connection conn = null; // 杩炴�?
	private boolean busy = false; // 鐘舵�?false绌洪�?true 琚娇鐢�?
	public PooledConn() {
		SingletonDB sing = SingletonDB.getSingleton();
		try {
			Class.forName(sing.getDriver());
			conn = DriverManager.getConnection(sing.getUrl(), sing.getUser(),
					sing.getPassword());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public boolean getBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

}
