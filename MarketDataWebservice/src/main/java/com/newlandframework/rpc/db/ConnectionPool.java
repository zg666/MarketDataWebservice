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
	    /*线程安全数组*/
	    private volatile Vector<Connection> pool;  
	    private volatile Vector<Connection> recordPool;
	    private volatile Connection selectConnection;
	    /*公有属性*/  
	    private String url = rb.getString("url");  
	   
	    private String username = rb.getString("user");
	    private String password =rb.getString("password");
	    private String driverClassName =rb.getString("driver"); 
	    /*连接池中储存的最大数量*/
	    private final int poolSize = 10;  
	    /*当线程池耗尽时，最大新建数量*/
	    private final int newPoolSize = 80;
	    private static ConnectionPool instance = null;  
	    private Connection conn = null; 
	    private static ConnectionPool mConnectionPool = null;

	    /**
	     * 这里定义三组顶层父类，没一个父类对象控制一把锁，他们意义在与多个锁的互不排斥。
	     */
	    private Object obj3 = new Object();
	    private Object obj4 = new Object();
	    private Object obj5 = new Object();
	    /*不同于synchronized的另一种锁机制*/
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

	    /*构造方法，做一些初始化工作*/  
	    private ConnectionPool() throws SQLException, ClassNotFoundException { 
	        System.out.println("初始化开始");
	        pool = new Vector<Connection>();  
	        recordPool = new Vector<Connection>();
	        selectConnection = load();
	        for (int i = 0; i < poolSize; i++) {  
	                conn = load();  
	                pool.add(conn);  
	        }  
	        System.out.println("初始化结束");  
	    }  

	    /*得到select使用的连接，查询不同于其他三项，查询可以并发进行执行，
	     * 所以我们只需提供一个连接来供所有用户使用*/
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

	    /* 返回连接到连接池
	     * 在这里进行控制，如果连接池里的连接数大于我们规定的数量，则对此连接进行关闭
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
	        /*考虑到多线程访问，防止当多名用户同时拿连接或回收时产生误差，在这里进行双层判断*/
	        if(recordPool.size() < newPoolSize){

	                    if(pool.size() > 0){
	                        return pool.remove(0);
	                    }
	                    if(recordPool.size() < newPoolSize){
	                        Connection conn = load();
	                        recordPool.add(conn);
	                        System.out.println("得到新建的连接");
	                        return conn;    
	                    }
	        }
	        return null;
	    }

	    /* 返回连接池中的一个数据库连接
	     * 如果连接池中已经耗尽了Connextion
	     * 则创建新的使用 
	     */
	    public Connection getConnection() throws ClassNotFoundException, InterruptedException {  
	            if (pool.size() > 0) {  
	                synchronized (obj3) {
	                    if(pool.size() > 0){
	                        System.out.println("得到连接");
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
	            /*如果连接池耗尽，并且新建连接也到最大值，那么在这里排队等待*/        
	            try {
	                synchronized (obj5) {
	                    while(true){
	                        System.out.println("进入等待");
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

	    /*拿到新建连接*/
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
