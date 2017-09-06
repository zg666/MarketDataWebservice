package com.newlandframework.rpc.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
// 客户端
public class TestTCPReceive {
	 public static void main(String[] args) { 
         Socket socket=null; 
         try { 
            //对服务端发起连接请求 
            socket=new Socket("localhost", 8080); 
             
            //接受服务端消息并打印 
            InputStream is=socket.getInputStream(); 
            byte b[]=new byte[1024]; 
            is.read(b); 
            System.out.println(new String(b)); 
             
            //给服务端发送响应信息 
            OutputStream os=socket.getOutputStream(); 
            os.write("yes,I have received you message!".getBytes()); 
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
     }
}
