package com.newlandframework.rpc.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
// �ͻ���
public class TestTCPReceive {
	 public static void main(String[] args) { 
         Socket socket=null; 
         try { 
            //�Է���˷����������� 
            socket=new Socket("localhost", 8080); 
             
            //���ܷ������Ϣ����ӡ 
            InputStream is=socket.getInputStream(); 
            byte b[]=new byte[1024]; 
            is.read(b); 
            System.out.println(new String(b)); 
             
            //������˷�����Ӧ��Ϣ 
            OutputStream os=socket.getOutputStream(); 
            os.write("yes,I have received you message!".getBytes()); 
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
     }
}
