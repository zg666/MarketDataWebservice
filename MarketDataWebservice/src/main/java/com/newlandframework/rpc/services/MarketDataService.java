package com.newlandframework.rpc.services;

import java.io.IOException;
import java.io.OutputStream;

import javax.jws.WebParam;
import javax.jws.WebService;


/*
 * 行情数据接口
 */
@WebService  
public interface MarketDataService {
	
	String getMarketData(@WebParam(name="time") String time);

	  
}
 