package com.newlandframework.rpc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnDB {
	public static Connection getConn() {
		return ConnPool.getConn();
	}
 
	public static void closeConn(Connection conn) {
		ConnPool.closeConn(conn);
	}
	
	public static void main(String[] args) {
	Connection conn=	getConn();
	closeConn(conn);
	}

}
