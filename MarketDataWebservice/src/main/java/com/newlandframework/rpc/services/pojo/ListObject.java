package com.newlandframework.rpc.services.pojo;

import java.util.List;

public class ListObject extends  AbstractJsonObject{
	   // �б����  
    private List<?> items;  
  
    public List<?> getItems() {  
        return items;  
    }  
  
    public void setItems(List<?> items) {  
        this.items = items;  
    }  
}
