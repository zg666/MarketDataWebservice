package com.newlandframework.test.impl;

import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.newlandframework.rpc.services.MarketDataService;



public class Test {
    public static void main(String[] args) {  
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");  

    	MarketDataService manage = (MarketDataService) ctx.getBean("marketData");

        Date d=new Date();
	       SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	       String time=sdf.format(d);
        String marketData = manage.getMarketData(time);



        System.out.println("当前行情数据"+marketData);
        ctx.destroy();
    
    }  
}
