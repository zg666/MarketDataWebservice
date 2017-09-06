package com.newlandframework.rpc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {

	ResourceBundle	rb=ResourceBundle.getBundle("db");
	    /*�̰߳�ȫ����*/
	    private volatile Vector<Connection> pool;  
	    private volatile Vector<Connection> recordPool;
	    private volatile Connection selectConnection;
	    /*��������*/  
	    private String url = rb.getString("url");  
	   
	    private String username = rb.getString("user");
	    private String password =rb.getString("password");
	    private String driverClassName =rb.getString("driver"); 
	    /*���ӳ��д�����������*/
	    private final int poolSize = 10;  
	    /*���̳߳غľ�ʱ������½�����*/
	    private final int newPoolSize = 80;
	    private static ConnectionPool instance = null;  
	    private Connection conn = null; 
	    private static ConnectionPool mConnectionPool = null;

	    /**
	     * ���ﶨ�����鶥�㸸�࣬ûһ������������һ�����������������������Ļ����ų⡣
	     */
	    private Object obj3 = new Object();
	    private Object obj4 = new Object();
	    private Object obj5 = new Object();
	    /*��ͬ��synchronized����һ��������*/
	    private Lock lock = new ReentrantLock();
	    private Lock lock2 = new ReentrantLock();
	    private Lock lock3 = new ReentrantLock();
	    private Lock lock4 = new ReentrantLock();

	    public static ConnectionPool getInstance() throws SQLException, ClassNotFoundException{
	        if(mConnectionPool != null){
	            return mConnectionPool;
	        }
	        synchronized (ConnectionPool.class) {
	            if (mConnectionPool == null){
	                mConnectionPool = new ConnectionPool();
	            }
	            return mConnectionPool;
	        }
	    }

	    /*���췽������һЩ��ʼ������*/  
	    private ConnectionPool() throws SQLException, ClassNotFoundException { 
	        System.out.println("��ʼ����ʼ");
	        pool = new Vector<Connection>();  
	        recordPool = new Vector<Connection>();
	        selectConnection = load();
	        for (int i = 0; i < poolSize; i++) {  
	                conn = load();  
	                pool.add(conn);  
	        }  
	        System.out.println("��ʼ������");  
	    }  

	    /*�õ�selectʹ�õ����ӣ���ѯ��ͬ�����������ѯ���Բ�������ִ�У�
	     * ��������ֻ���ṩһ���������������û�ʹ��*/
	    public Connection getSelectConnection() throws SQLException, InterruptedException{
	        if(selectConnection != null && !selectConnection.isClosed()){
	            return selectConnection;
	        }
	        if(lock2.tryLock(8L,TimeUnit.SECONDS)){
	            if(selectConnection == null || selectConnection.isClosed()){
	                selectConnection = load();
	            }
	            lock2.unlock();
	        }
	        return selectConnection;
	    }

	    /* �������ӵ����ӳ�
	     * ��������п��ƣ�������ӳ�����������������ǹ涨����������Դ����ӽ��йر�
	     */
	    public void release(Connection con) throws SQLException, InterruptedException {
	        if(con == null || con.isClosed()){
	            recordPool.remove(con);
	            return;
	        }
	        if(lock3.tryLock(8L,TimeUnit.SECONDS)){
	            if (pool.size() >= poolSize){
	                con.close();
	                lock3.unlock();
	                return;
	            }  
	            if(pool.size() < poolSize){
	                pool.add(con); 
	            }
	            lock3.unlock();
	        }
	    }  

	    private Connection getNewConnection(){
	        /*���ǵ����̷߳��ʣ���ֹ�������û�ͬʱ�����ӻ����ʱ���������������˫���ж�*/
	        if(recordPool.size() < newPoolSize){

	                    if(pool.size() > 0){
	                        return pool.remove(0);
	                    }
	                    if(recordPool.size() < newPoolSize){
	                        Connection conn = load();
	                        recordPool.add(conn);
	                        System.out.println("�õ��½�������");
	                        return conn;    
	                    }
	        }
	        return null;
	    }

	    /* �������ӳ��е�һ�����ݿ�����
	     * ������ӳ����Ѿ��ľ���Connextion
	     * �򴴽��µ�ʹ�� 
	     */
	    public Connection getConnection() throws ClassNotFoundException, InterruptedException {  
	            if (pool.size() > 0) {  
	                synchronized (obj3) {
	                    if(pool.size() > 0){
	                        System.out.println("�õ�����");
	                        return pool.remove(0);
	                    }
	                }
	            }
	            if(lock4.tryLock(8L,TimeUnit.SECONDS)){
	                Connection con = getNewConnection();
	                lock4.unlock();
	                if(con != null){
	                    return con;
	                }
	            }
	            /*������ӳغľ��������½�����Ҳ�����ֵ����ô�������Ŷӵȴ�*/        
	            try {
	                synchronized (obj5) {
	                    while(true){
	                        System.out.println("����ȴ�");
	                        Thread.sleep(1*50);
	                        for (int i = 0; i < recordPool.size(); ) {
	                            if (recordPool.get(i).isClosed() || recordPool.get(i)==null) {
	                                recordPool.remove(i);
	                            } else{
	                                i++;
	                            }
	                        }
	                        if(pool.size() != 0){
	                            recordPool.add(pool.get(0));
	                            return pool.remove(0);
	                        }
	                        if(recordPool.size() < newPoolSize){
	                            Connection conn = load();
	                            recordPool.add(conn);
	                            return conn;
	                        }
	                    }
	                }
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            return null;
	    }  

	    /*�õ��½�����*/
	    private Connection load(){
	        try {
	            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	            return DriverManager.getConnection(url, username, password);
	        } catch (ClassNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }        
	        return null;
	    }
}
