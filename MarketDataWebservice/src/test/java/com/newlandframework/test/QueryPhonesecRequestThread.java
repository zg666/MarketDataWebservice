package com.newlandframework.test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.newlandframework.rpc.services.AddCalculate;
import com.newlandframework.rpc.services.MarketDataService;

import com.newlandframework.rpc.services.pojo.T_SYS_PHONESEC;

public class QueryPhonesecRequestThread  implements Runnable {
	  private CountDownLatch signal;
	    private CountDownLatch finish;
	    private String time = "";
	    private MarketDataService md;
	  

	    public QueryPhonesecRequestThread(MarketDataService md, CountDownLatch signal, CountDownLatch finish, String time) {
	        this.signal = signal;
	        this.finish = finish;
	        this.time = time;
	        this.md = md;
	      
	    }

	    public void run() {
	        try {
	        	//ConcurrentHashMap<> hm=m;
	            signal.await();
                //System.out.println("fstart="+fstart);
	        
	          String phonesec = md.getMarketData(time); 
	          
	          System.out.println("时间："+time+"，对应数据为："+phonesec);
	         //   System.out.println("calc add result:[" + add + "]");

	            finish.countDown();
	        } catch (InterruptedException ex) {
	            Logger.getLogger(QueryPhonesecRequestThread.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
}
