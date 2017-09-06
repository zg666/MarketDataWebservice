package com.newlandframework.rpc.services.impl;

/*
 * 行情数据接口
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.jws.WebService;

import com.newlandframework.rpc.boot.MarketDataQtz;
import com.newlandframework.rpc.services.MarketDataService;
@WebService(endpointInterface = "com.newlandframework.rpc.services.MarketDataService",serviceName="marketDataService") 
public class MarketDataServiceImpl implements MarketDataService{

	@Override
	public String getMarketData(String time) {
/*		String t[]=time.split(":");
		String s=t[2];  // 当前时间的秒
		
		Set set = MarketDataQtz.m.keySet();
		Iterator iter = set.iterator();
		String key="";
		while (iter.hasNext()) {
		 key = (String) iter.next();  // 最近的一次数据的key
		
		}
		int is=Integer.parseInt(s);
		int ikey=Integer.parseInt(key);
		// 如果当前时间与最近一次取数据的时间   57 58 59 00 01 02 03 04
		String marketData="";
		//  如果当前时间的秒与最近一次取数据的时间都以0开头:   00 01 02 03 04
		if(s.startsWith("0")&&key.startsWith("0")){
			// 如果秒相隔小于3秒,则根据最近一次取数据的key获取值,否则就根据当前时间取数据
			if(Integer.parseInt(s.substring(1))-Integer.parseInt(key.substring(1))<3){
				marketData=MarketDataQtz.m.get(key);
			}else{
				marketData=MarketDataQtz.m.get(time);
			}
			
			
		}
		// 如果当前秒不以0开头，且最近一次取数据的时间秒以0开头    s: 10   key:08
		if(!s.startsWith("0")&&key.startsWith("0")){
			if(is-Integer.parseInt(s.substring(1))<3){
				marketData=MarketDataQtz.m.get(key);
			}else{
				marketData=MarketDataQtz.m.get(time);
			}
		}
		// 如果当前秒以0开头，且最近一次取数据的时间秒不以0开头    s: 00 01 02 03 04  key:58
		if(s.startsWith("0")&&!key.startsWith("0")){
			// 如果s: 00 01 02
			if(Integer.parseInt(s.substring(1))<3){
				if(s.equals("00")){
					s="60";
				}
				if(s.equals("01")){
					s="61";
				}
				if(s.equals("02")){
					s="62";
				}
				// s-key=60-58<3
				if(Integer.parseInt(s)-Integer.parseInt(key)<3){
					marketData=MarketDataQtz.m.get(key);
				}else{
					marketData=MarketDataQtz.m.get(time);
				}
			}
			// 如果s: 03 04 05 那么key为00 01 02
			else{
				if(Integer.parseInt(s.substring(1))-Integer.parseInt(key.substring(1))<3){
					marketData=MarketDataQtz.m.get(key);
				}else{
					marketData=MarketDataQtz.m.get(time);
				}
			}
			
			
		}
		// 如果都不以0开头
		if(s.startsWith("0")&&!key.startsWith("0")){
			if(Integer.parseInt(s)-Integer.parseInt(key)<3){
				marketData=MarketDataQtz.m.get(key);
			}else{
				marketData=MarketDataQtz.m.get(time);
			}
		}*/
		
	  
	       String marketData=MarketDataQtz.marketData;
		return marketData;
	}

}
