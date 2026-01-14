package com.semi.framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RequestMapping {
	
	///Field
	private static RequestMapping requestMapping;
	private Map<String, Action> map;
	private Properties properties;
	
	///Constructor
	private RequestMapping(String resources) {
		
		map = new HashMap<String, Action>();
		
		InputStream in = null;
		try{
			in = getClass().getClassLoader().getResourceAsStream(resources);
			properties = new Properties();
			properties.load(in);
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties 파일 로딩 실패 :"  + ex);
		}finally{
			if(in != null){
				try{ 
					in.close(); 
				} catch(Exception ex){ }
			}
		}
	} 
	
	///Method
	public synchronized static RequestMapping getInstance(String resources){
		if(requestMapping == null){
			requestMapping = new RequestMapping(resources);
		}
		return requestMapping;
	}
	
	public Action getAction(String path){
	    try {
	        // 컨텍스트 경로가 포함되어 있으면 제거 (예: /semiProject/listNotice.do → /listNotice.do)
	        String cleanPath = path;
	        if (path != null && path.contains(".do")) {
	            int doIndex = path.indexOf(".do");
	            int slashIndex = path.lastIndexOf("/", doIndex);
	            if (slashIndex >= 0) {
	                cleanPath = path.substring(slashIndex);
	            }
	        }

	        System.out.println("[RequestMapping] original path: " + path + " → cleanPath: " + cleanPath);

	        String className = properties.getProperty(cleanPath);
	        if (className == null || className.trim().isEmpty()) {
	            throw new IllegalArgumentException("No action mapping for path: " + cleanPath + " (original: " + path + ")");
	        }
	        className = className.trim();

	        Action action = map.get(cleanPath);
	        if (action == null) {
	            action = (Action) Class.forName(className).newInstance();
	            map.put(cleanPath, action);
	        }
	        return action;
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to get action for path: " + path + " - " + e, e);
	    }
	}

}