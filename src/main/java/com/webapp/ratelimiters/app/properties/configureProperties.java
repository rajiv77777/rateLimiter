package com.webapp.ratelimiters.app.properties;

import java.util.ResourceBundle;

public class configureProperties {
ResourceBundle bundle = ResourceBundle.getBundle("application");  
	

	public String getRatelimiterGetAllData(String ratelimiterGetAllDataAPI) {
			return bundle.getString(ratelimiterGetAllDataAPI);
	}

	public String getWaitTimeGetAllData(String waitTimeGetAllDataAPI) {
			return bundle.getString(waitTimeGetAllDataAPI);

	}
	public String getRatelimiterGetDataByID(String ratelimiterGetDataByIDAPI) {
		if(bundle.containsKey(ratelimiterGetDataByIDAPI)) {
			return bundle.getString(ratelimiterGetDataByIDAPI);
		}
		return "5";
	}

	public String getWaitTimeGetDataByID(String waitTimeGetDataByIDAPI) {
		if(bundle.containsKey(waitTimeGetDataByIDAPI)) {
			return bundle.getString(waitTimeGetDataByIDAPI);
		}
		return "30";
	}
	
	public String getSecret(String secret) {
		return bundle.getString(secret);
	}
}
