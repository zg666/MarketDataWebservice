package com.newlandframework.rpc.boot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/*
 * 
 * 行情数据是交易时间从我们的行情计算后台以TCP/IP数据流的方式实时接收的，
 * 每3秒刷新一次，接收后保存在发布服务进程内的内存中，供接口查询
 */


public class MarketDataQtz {
	  public static HashMap<String,String> m=new HashMap<String,String>();
	  public static String marketData="";
	    public String execute()  {  
	    	
	    	
	       Date d=new Date();
	       SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	       String time=sdf.format(d);
	       marketData=time+"当前行情数据";
	       
	       // 以数据流方式从后台获取信息
	 /*  	Socket socket=null; 
        try { 
           //对服务端发起连接请求 
           socket=new Socket("localhost", 8081); 
            
           //接受服务端消息并打印 
           InputStream is=socket.getInputStream(); 
           byte b[]=new byte[1024]; 
           is.read(b); 
           marketData=new String(b);
           System.out.println(marketData); 
            
           //给服务端发送响应信息 
           OutputStream os=socket.getOutputStream(); 
           os.write("yes,I have received you message!".getBytes()); 
       } catch (IOException e) { 
           // TODO Auto-generated catch block 
           e.printStackTrace(); 
       } */
	       
	       
	    /*   System.out.println(time+",,,,,"+time+"qqq");
	       MarketDataQtz.m.clear();
	       MarketDataQtz.m.put(time, time+"qqq");*/
	       
	       System.out.println("当前行情数据："+marketData);
	       return marketData;
	    }  
}
