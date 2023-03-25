package com.webapp.ratelimiters.app.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.webapp.ratelimiters.app.DAO.rateLimiterRepo;
import com.webapp.ratelimiters.app.bucket4jConfig.bucket4jConfiguration;
import com.webapp.ratelimiters.app.model.rateLimiterModel;
import com.webapp.ratelimiters.app.properties.configureProperties;

@RestController
public class ControllerClass {
	public static int rateLimiterPerTimeWindow_GetAllDataApi;
	public static int rateLimiterWaitTime_GetAllDataApi;
	public static int rateLimiterPerTimeWindow_GetDataByIDApi;
	public static int rateLimiterWaitTime_GetDataByIDApi;
	public static String secretCode;
	
	@Autowired
	rateLimiterRepo repo;
	
	static {
		 configureProperties cp = new configureProperties();

		 secretCode=cp.getRatelimiterGetAllData("secret");
		 rateLimiterPerTimeWindow_GetAllDataApi=Integer.parseInt(cp.getRatelimiterGetAllData("ratelimiterGetAllDataAPI"));
		 rateLimiterWaitTime_GetAllDataApi = Integer.parseInt(cp.getRatelimiterGetAllData("waitTimeGetAllDataAPI"));
		 rateLimiterPerTimeWindow_GetDataByIDApi=Integer.parseInt(cp.getRatelimiterGetDataByID("ratelimiterGetDataByIDAPI"));
		 rateLimiterWaitTime_GetDataByIDApi = Integer.parseInt(cp.getWaitTimeGetDataByID("waitTimeGetDataByIDAPI"));
		 System.out.println("Ratelimits and Wait time configurations - "+rateLimiterPerTimeWindow_GetAllDataApi+" "+rateLimiterWaitTime_GetAllDataApi+" "+rateLimiterPerTimeWindow_GetDataByIDApi+" "+rateLimiterWaitTime_GetDataByIDApi+" "+", Secret Code for Auth "+secretCode);
	}	 	
	 bucket4jConfiguration bc = new bucket4jConfiguration(rateLimiterPerTimeWindow_GetAllDataApi,rateLimiterWaitTime_GetAllDataApi);

	 @GetMapping(value = "/api/findall")
	 public ResponseEntity<List<rateLimiterModel>> findAll(@RequestHeader("secretkey") String secret) { 
		 if(secret.equals(secretCode)) {
			 if (bc.bucket.tryConsume(1)) {
			        return ResponseEntity.ok(repo.findAll());
			    }
			 
			 System.out.println("Threashold reached - findAll API");
			 return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		 }
			 return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}

	 bucket4jConfiguration bc1 = new bucket4jConfiguration(rateLimiterPerTimeWindow_GetDataByIDApi,rateLimiterWaitTime_GetDataByIDApi);
	 @GetMapping(value = "/api/findByID/{id}")
	 public ResponseEntity<Optional<rateLimiterModel>> findByID(@RequestHeader("secretkey") String secret,@PathVariable ("id") int id ) { 
		 if(secret.equals(secretCode)) {
			 if (bc1.bucket.tryConsume(1)) {
			        return ResponseEntity.ok(repo.findById(id));
			    }
			 
			 System.out.println("Threashold reached - findByID API");
			 return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		 }
		 return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

	 }
}
