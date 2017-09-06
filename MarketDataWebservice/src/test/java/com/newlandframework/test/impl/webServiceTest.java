package com.newlandframework.test.impl;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
public class webServiceTest {


	public static void main(String[] args) {
		/*webServiceTest t = new webServiceTest();
		String result = t.invokeRemoteFuc();
		System.out.println(result);*/
		
		try {
			  String endpoint = "http://192.168.211.1:8080/MarketDataWebservice/cxf/getMarketData?wsdl";
			  //直接引用远程的wsdl文件
			  //以下都是套路 
			  Service service = new Service();
			  Call call = (Call) service.createCall();
			   QName qName = new QName("http://services.rpc.newlandframework.com/", "getMarketData"); 
			  call.setTargetEndpointAddress(endpoint);
			  call.setOperationName(qName);//WSDL里面描述的接口名称
			  call.addParameter("time", org.apache.axis.encoding.XMLType.XSD_DATE,
			  javax.xml.rpc.ParameterMode.IN);//接口的参数
			  call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//设置返回类型 
			  Date d=new Date();
		       SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			  String temp = sdf.format(d);
			  String result = (String)call.invoke(new Object[]{temp});
			  //给方法传递参数，并且调用方法
			  System.out.println("当前时间："+temp+",result is "+result);
			  }
			  catch (Exception e) {
			  System.err.println(e.toString());
			  }
	}
}

