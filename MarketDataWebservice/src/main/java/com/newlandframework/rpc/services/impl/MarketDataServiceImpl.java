package com.newlandframework.rpc.services.impl;

/*
 * �������ݽӿ�
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
		String s=t[2];  // ��ǰʱ�����
		
		Set set = MarketDataQtz.m.keySet();
		Iterator iter = set.iterator();
		String key="";
		while (iter.hasNext()) {
		 key = (String) iter.next();  // �����һ�����ݵ�key
		
		}
		int is=Integer.parseInt(s);
		int ikey=Integer.parseInt(key);
		// �����ǰʱ�������һ��ȡ���ݵ�ʱ��   57 58 59 00 01 02 03 04
		String marketData="";
		//  �����ǰʱ����������һ��ȡ���ݵ�ʱ�䶼��0��ͷ:   00 01 02 03 04
		if(s.startsWith("0")&&key.startsWith("0")){
			// ��������С��3��,��������һ��ȡ���ݵ�key��ȡֵ,����͸��ݵ�ǰʱ��ȡ����
			if(Integer.parseInt(s.substring(1))-Integer.parseInt(key.substring(1))<3){
				marketData=MarketDataQtz.m.get(key);
			}else{
				marketData=MarketDataQtz.m.get(time);
			}
			
			
		}
		// �����ǰ�벻��0��ͷ�������һ��ȡ���ݵ�ʱ������0��ͷ    s: 10   key:08
		if(!s.startsWith("0")&&key.startsWith("0")){
			if(is-Integer.parseInt(s.substring(1))<3){
				marketData=MarketDataQtz.m.get(key);
			}else{
				marketData=MarketDataQtz.m.get(time);
			}
		}
		// �����ǰ����0��ͷ�������һ��ȡ���ݵ�ʱ���벻��0��ͷ    s: 00 01 02 03 04  key:58
		if(s.startsWith("0")&&!key.startsWith("0")){
			// ���s: 00 01 02
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
			// ���s: 03 04 05 ��ôkeyΪ00 01 02
			else{
				if(Integer.parseInt(s.substring(1))-Integer.parseInt(key.substring(1))<3){
					marketData=MarketDataQtz.m.get(key);
				}else{
					marketData=MarketDataQtz.m.get(time);
				}
			}
			
			
		}
		// ���������0��ͷ
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
