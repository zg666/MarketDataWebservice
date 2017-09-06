package com.newlandframework.rpc.services.pojo;

import java.io.Serializable;



// ºÅ¶Î±ípojo
public class T_SYS_PHONESEC implements Serializable {


  private String fid;
  private String fstart;
  
  private String fprovince_id;
  

  private String fcity_id;
  

  private String fcreate_time;
  

  private String freserve;
 
  @Override
  public String toString() {
	     return"T_SYS_PHONESEC [fid=" + fid + ", fstart=" + fstart + ", fprovince_id=" + fprovince_id + ",fcity_id="+fcity_id+",fcreate_time="+fcreate_time+",freverve="+freserve+"]";
	  }

	public String getFid() {
		return fid;
	}


	public void setFid(String fid) {
		this.fid = fid;
	}


	public String getFstart() {
		return fstart;
	}


	public void setFstart(String fstart) {
		this.fstart = fstart;
	}


	public String getFprovince_id() {
		return fprovince_id;
	}


	public void setFprovince_id(String fprovince_id) {
		this.fprovince_id = fprovince_id;
	}


	public String getFcity_id() {
		return fcity_id;
	}


	public void setFcity_id(String fcity_id) {
		this.fcity_id = fcity_id;
	}


	public String getFcreate_time() {
		return fcreate_time;
	}


	public void setFcreate_time(String fcreate_time) {
		this.fcreate_time = fcreate_time;
	}


	public String getFreserve() {
		return freserve;
	}


	public void setFreserve(String freserve) {
		this.freserve = freserve;
	}

   

}
