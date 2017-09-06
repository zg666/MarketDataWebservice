package com.newlandframework.rpc.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnPool {//杩炴帴姹犵被
	static SingletonDB sing = SingletonDB.getSingleton();
	static int initConnNum = sing.getInitConnNum();
	static int maxConnNum = sing.getMaxConnNum();
	static int incrementConnNum = sing.getIncrementConnNum();
	private static ArrayList<PooledConn>  pools = null; // 杩炴帴姹�

	public  static  void createConnPool() { // 鍒涘缓杩炴帴姹犲苟涓斿垵濮嬪寲杩炴帴
		if (pools != null) {
			return;
		} else {
			pools = new ArrayList<PooledConn>();
			for (int i = 0; i < initConnNum; i++) {
				pools.add(new PooledConn());
			}
		}
	}

	public static  Connection getFreeConn() {
		Connection conn = null;

		for (PooledConn pooledConn : pools) {// 寰幆浠庤繛鎺ユ睜涓壘鍒扮┖闂�
			if (!pooledConn.getBusy()&&isEnble(pooledConn.getConn())) {
				conn = pooledConn.getConn();
				pooledConn.setBusy(true);
				break;
			}
		}
		if (conn == null) {
			int num = incrementConnNum;
			if (pools.size() < maxConnNum) {// 濡傛灉灏忎簬鏈�ぇ瑙勫畾鏁板氨鍙互澧炲姞
				if ((pools.size() + num) > maxConnNum) {
					num =maxConnNum - pools.size();
				} 

				for (int i = 0; i < num - 1; i++) { // 澧炲姞num-1涓繛鎺�
					pools.add(new PooledConn());
				}
				PooledConn pc = new PooledConn();// 灏嗘渶鍚庝竴涓繛鎺ョ敤鎺�
				conn = pc.getConn();// 浼犻�鍑哄幓鐨勮繛鎺�
				pc.setBusy(true);// 鏀瑰彉鐘舵�
				pools.add(pc); // 鏈�悗涓�釜杩炴帴涔熻鍔犲叆鍒版睜涓�
			}
		}
		return conn;
	}

	public static Connection getConn() {
		Connection conn = null;
		createConnPool();
		conn = getFreeConn();
		while (conn == null) {
			// 绛夊緟2绉�
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			conn = getFreeConn();
		}
		return conn;
	}

	public    static  void closeConn(Connection conn) {// 閲婃斁杩炴帴
		for (PooledConn pc : pools) {
			if (conn!=null&&pc.getConn() == conn) {
				pc.setBusy(false);
			}
		}

	}

	public  static void colseConnPools() {// 鍏抽棴杩炴帴姹�
		try {
			for (PooledConn pc : pools) {
				if (pc.getBusy()) {
					pc.getConn().close();
				} else {
					Thread.sleep(2000);
					pc.getConn().close();
				}
			}
		} catch (Exception e) {
		}
	}

	public  static boolean isEnble(Connection conn) {// 杩炴帴鏄惁鍙敤
		boolean flag = true;
		if (conn == null) {
			flag = false;
		} else {
			try {
				conn.createStatement().execute("select getDate()");
			} catch (SQLException e) {
				flag = false;
				e.printStackTrace();
			}
		}
		return flag;
	}

}
