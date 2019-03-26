package com.likeyichu.lenskit.demo;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;





@WebService
public class HelloService {

	public String say(String question){  
		System.out.println(question);
	
	        
	        
        return question;  
    }  
      
    public static void main(String[] args) {  
                /* 
         * 参数1：服务地址 
         * 参数2：服务类 
         */  
        Endpoint.publish("http://127.0.0.1:9001/wsservice/hello", new HelloService()); 
        System.out.println("success");
    }  
}
