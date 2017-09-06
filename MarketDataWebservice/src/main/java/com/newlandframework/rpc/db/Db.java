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
    // �������ݿ������ַ  
    private String HOST;  
    // �������ݿ��û���  
    private String USERNAME;  
    // �������ݿ�����  
    private String PASSWORD;  
    // �������ݿ�������Ϣ  
    private String DRIVER ;  
    //�������ӳ�  
    private static Set<Connection> conn_pools;  
    //���ӳ��б��������������  
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
     * �������ݿ����ӳ� 
     * @param i ���ӳ����������� 
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
      * ��ȡһ������ 
      * @return 
      * @throws SQLException  
      */  
     public  Connection getPoolConn() throws SQLException{  
         synchronized(this){  
             Iterator<Connection> iter=conn_pools.iterator();  
             while (iter.hasNext()) {  
                 Connection conn=iter.next();  
                 conn_pools.remove(conn);  
                 System.out.println(Thread.currentThread().getName()+"��ȡ��һ�����ӣ���ǰ��ʣ"+conn_pools.size()+"������");  
                 if(isValid(conn)){  
                     return conn;  
                 }else{  
                     System.out.println(Thread.currentThread().getName()+"�������Ѿ����ڣ��½����ӣ�");  
                     return DriverManager.getConnection(HOST, USERNAME, PASSWORD);     
                 }        
             }        
             try {  
                System.out.println(Thread.currentThread().getName()+"�����ӳ���û�������ˣ��ȴ������У�");  
                wait();  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
            System.out.println(Thread.currentThread().getName()+"�����̱߳����ѣ�");  
            return this.getPoolConn();  
         }         
     }  
    /** 
     * �ж������Ƿ���� 
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
     * ִ��SQL���,executeUpdate��������ʵ���޸ģ����룬ɾ�����,����ǲ����򷵻ز���������ID�����򷵻�Ӱ������� 
     *  
     * @throws SQLException 
     */  
    public HashMap<String,Integer> query(String sql, Object obj) throws SQLException {  
        HashMap<String,Integer> map=new HashMap<>();  
        Connection conn=this.getPoolConn();  
        int index = -1;// ִ��SQLӰ�������  
        // �������ݿ�SQL����ִ�ж���  
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
        if(index>0 && sql.substring(0,6).equals("insert")) {//��¼����ɹ�    
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
     * ִ���з��ؼ���SQL���,executeQuery��������ʵ�ֲ�ѯ���,��¼������Ϊint 
     *  
     * @throws SQLException 
     */  
    public List<Map<String, Object>> sql(String sql, Object obj)throws SQLException {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        Connection conn=this.getPoolConn();  
        // �������ݿ�SQL����ִ�ж���  
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
        int column_long = result.getMetaData().getColumnCount();// ��ȡһ�е��ֶ���Ϣ  
            while (result.next()) {  
                Map<String, Object> map = new HashMap<String, Object>();  
                for (int i = 0; i < column_long; i++) {  
                    String key = result.getMetaData().getColumnLabel(i + 1);                                      
                    Object value = result.getObject(key);                 
                    if(value instanceof Long){  
                        value=Integer.valueOf(String.valueOf(value));//�������¼������ʱ����longת��Ϊint�������  
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
     * �ر����ݿ����� 
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
