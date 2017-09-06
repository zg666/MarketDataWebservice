package com.newlandframework.rpc.db;

import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.HashSet;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.ResourceBundle;
import java.util.Set;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;

  
public class Db {  
    // 定义数据库物理地址  
    private String HOST;  
    // 定义数据库用户名  
    private String USERNAME;  
    // 定义数据库密码  
    private String PASSWORD;  
    // 定义数据库驱动信息  
    private String DRIVER ;  
    //交换连接池  
    private static Set<Connection> conn_pools;  
    //连接池中被激活的连接数量  
    private int connNum;  
    ResourceBundle	rb=ResourceBundle.getBundle("db");
    public Db() throws Exception{             
         
    	
        this.DRIVER=rb.getString("driver");   
      
        this.HOST=rb.getString("host");  
        this.USERNAME= rb.getString("user");
        this.PASSWORD= rb.getString("password");
        this.connNum=Integer.parseInt(rb.getString("initConnNum")); 
        try {  
            Class.forName(DRIVER);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * 创建数据库连接池 
     * @param i 连接池中连接数量 
     */  
     public void createPools() throws SQLException{  
        if(conn_pools==null){  
            conn_pools = new HashSet<Connection>(connNum);    
            for (int i = 0; i < connNum; i++) {  
                conn_pools.add(DriverManager.getConnection(HOST, USERNAME, PASSWORD));                 
            }       
        }                            
     }  
     /** 
      * 获取一个连接 
      * @return 
      * @throws SQLException  
      */  
     public  Connection getPoolConn() throws SQLException{  
         synchronized(this){  
             Iterator<Connection> iter=conn_pools.iterator();  
             while (iter.hasNext()) {  
                 Connection conn=iter.next();  
                 conn_pools.remove(conn);  
                 System.out.println(Thread.currentThread().getName()+"：取出一个连接，当前还剩"+conn_pools.size()+"个连接");  
                 if(isValid(conn)){  
                     return conn;  
                 }else{  
                     System.out.println(Thread.currentThread().getName()+"：连接已经过期，新建连接！");  
                     return DriverManager.getConnection(HOST, USERNAME, PASSWORD);     
                 }        
             }        
             try {  
                System.out.println(Thread.currentThread().getName()+"：连接池中没有连接了，等待连接中！");  
                wait();  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
            System.out.println(Thread.currentThread().getName()+"：该线程被唤醒！");  
            return this.getPoolConn();  
         }         
     }  
    /** 
     * 判断连接是否可用 
     * @param conn 
     * @return 
     */  
    private boolean isValid(Connection conn) {  
        try {  
            if (conn == null || conn.isClosed()) {  
                return false;  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return true;  
    }  
  
    /** 
     * 执行SQL语句,executeUpdate方法可以实现修改，插入，删除语句,如果是插入则返回插入自增的ID，否则返回影响的行数 
     *  
     * @throws SQLException 
     */  
    public HashMap<String,Integer> query(String sql, Object obj) throws SQLException {  
        HashMap<String,Integer> map=new HashMap<>();  
        Connection conn=this.getPoolConn();  
        int index = -1;// 执行SQL影响的行数  
        // 定义数据库SQL语句的执行对象  
        PreparedStatement pstate;  
        if(sql.substring(0,6).equals("insert")){              
            pstate = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  
        }else{  
            pstate = conn.prepareStatement(sql);  
        }     
        if(obj instanceof Object[]){  
            Object[] object=(Object[])obj;  
            if (object.length >= 1) {  
                for (int i = 0; i < object.length; i++) {  
                    pstate.setObject(i + 1, object[i]);  
                }  
            }  
        }else if(obj instanceof List){  
            @SuppressWarnings("unchecked")  
            List<Object> object=(List<Object>)obj;  
            if (object.size() >= 1) {  
                for (int i = 0; i < object.size(); i++) {  
                    pstate.setObject(i + 1,object.get(i));  
                }  
            }  
        }else if(obj!=null){  
            pstate.setObject(1, obj);  
        }  
        int lastinsertid=-1;  
        index=pstate.executeUpdate();    
        if(index>0 && sql.substring(0,6).equals("insert")) {//记录保存成功    
            ResultSet result=pstate.getGeneratedKeys();   
            if(result.next()) {  
                try {  
                    lastinsertid=result.getInt(1);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }       
            }                     
        }    
        map.put("affectLine", index);         
        map.put("lastId", lastinsertid);  
        conn_pools.add(conn);  
        synchronized(this){  
            notifyAll();  
        }  
        return map;  
    }  
  
    /** 
     * 执行有返回集的SQL语句,executeQuery方法可以实现查询语句,记录数需求为int 
     *  
     * @throws SQLException 
     */  
    public List<Map<String, Object>> sql(String sql, Object obj)throws SQLException {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        Connection conn=this.getPoolConn();  
        // 定义数据库SQL语句的执行对象  
        PreparedStatement pstate = conn.prepareStatement(sql);  
        if(obj instanceof Object[]){  
            Object[] object=(Object[])obj;  
            if (object.length >= 1) {  
                for (int i = 0; i < object.length; i++) {  
                    pstate.setObject(i + 1, object[i]);  
                }  
            }  
        }else if(obj instanceof List){  
            @SuppressWarnings("unchecked")  
            List<Object> object=(List<Object>)obj;  
            if (object.size() >= 1) {  
                for (int i = 0; i < object.size(); i++) {  
                    pstate.setObject(i + 1,object.get(i));  
                }  
            }  
        }else if(obj!=null){  
            pstate.setObject(1, obj);  
        }  
        ResultSet result = pstate.executeQuery();  
        int column_long = result.getMetaData().getColumnCount();// 获取一行的字段信息  
            while (result.next()) {  
                Map<String, Object> map = new HashMap<String, Object>();  
                for (int i = 0; i < column_long; i++) {  
                    String key = result.getMetaData().getColumnLabel(i + 1);                                      
                    Object value = result.getObject(key);                 
                    if(value instanceof Long){  
                        value=Integer.valueOf(String.valueOf(value));//当需求记录数不大时，将long转换为int方便计算  
                    }  
                    if (value == null) {  
                        value = "";  
                    }  
                    map.put(key, value);  
                }  
                list.add(map);  
            }  
        result.close();  
        conn_pools.add(conn);  
        synchronized(this){  
            notifyAll();  
        }  
        return list;  
    }  
    /** 
     * 关闭数据库链接 
     */  
    public void closejdbc(Connection conn) {  
        try {  
            if (conn.isClosed() != true)  
                conn.close();  
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
    
   
}  
