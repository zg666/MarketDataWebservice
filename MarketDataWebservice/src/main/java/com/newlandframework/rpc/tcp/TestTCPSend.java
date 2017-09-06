package com.newlandframework.rpc.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
// 服务端
public class TestTCPSend {
	 public static void main(String[] args) { 
	      ServerSocket serverSocket=null; 
	      Socket socket=null; 
	      String msg="hello client,I am server.."; 
	      try { 
	          
	         //构造ServerSocket实例，指定端口监听客户端的连接请求 
	         serverSocket=new ServerSocket(8080); 
	         //建立跟客户端的连接 
	         socket=serverSocket.accept(); 
	          
	         //向客户端发送消息 
	         OutputStream os=socket.getOutputStream(); 
	         os.write(msg.getBytes()); 
	         InputStream is=socket.getInputStream(); 
	          
	         //接受客户端的响应 
	         byte[] b=new byte[1024]; 
	         is.read(b); 
	         System.out.println(new String(b)); 
	          
	      } catch (IOException e) { 
	         e.printStackTrace(); 
	      } finally { 
	         //操作结束，关闭socket  
	          try { 
	            serverSocket.close(); 
	            socket.close(); 
	          } catch (IOException e) { 
	            e.printStackTrace(); 
	          } 
	      }  
	  } 
}
