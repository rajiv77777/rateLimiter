# **Rate Limiter implementation on REST APIs using token-bucket algorithm**

## **Introduction**
### This project has 2 APIs implemented with RateLimiters and  user authorization based on a token code for accessing the API. One Api is used to query all the datas from the H2 Database and another API displays the record with respect to ID query parameter from the same database.
#

## **Dependencies**
|S.No|Dependency|Version|
----|-----|-------|
|1|spring-boot-starter-parent|3.0.5|
|2|com.h2database|-|
|3|bucket4j-core|4.10.0|
|4|mockito-junit-jupiter|-|
#

## **Implementation**
### The 2 APIs will be running on port 8080 with the following context roots - http://localhost:8080/api/findall and http://localhost:8080/api/findByID/1

`@GetMapping(value = "/api/findall")`

`@GetMapping(value = "/api/findByID/{id}")`

1. /api/findall gives all the data from the H2 Database
2. /api/findByID/1 gives the data for the particular ID. Lets saw the Database has IDs from 1 to 10 , **/api/findByID/1** ->1 can be replaced with any numbers for 1 to 10 to fetch the datas from the particular table.

### Each API has its own Ratelimiters configured in the application.properties file as shown in below screenshot.
### `ratelimiterGetDataByIDAPI` stores the allowed transaction in the allowed window time <br>
### `waitTimeGetDataByIDAPI` stores the allowed window time or wait time in **seconds**
<br>

![propertiesfile](https://github.com/rajiv77777/rateLimiter/blob/master/pics/ratelimiter-ApplicationProperties.png)

### As show above the GetAllDataApi which is `@GetMapping(value = "/api/findall")` has a preconfigured properties for ratelimiter and waitime per window.

### But the GetDataByID which is `@GetMapping(value = "/api/findByID/{id}")` has a default configuration mentioned in the backend so if the  `ratelimiterGetDataByIDAPI=3 and waitTimeGetDataByIDAPI=20` are removed from the application.properties the API is capable of setting its own default values for `ratelimiterGetDataByIDAPI and waitTimeGetDataByIDAPI` which are commented  for ratelimiter and waitime per window. The assigning of default values is as below.

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

### if the required parameters are missing when checked with `bundle.containsKey()` then the default values are assigned which are `ratelimiter = 5 and waitTime = 30seconds`
<br>

### There is an additional parameter which is checked to enable user based or token based API ratelimiting. If a user has the secret code when sending the `GET` request then only the required datas are revealed. The implementation and reference images are attached below.

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

### The request header checks the for the secretkey and it accepts the request only if it matches the secretkey. The secretkey configuration is mentioned in application.properties file.<br> `secret=Password@#12345`<br>The secretkey can be changed from the properties file and no manual intervention on code is required. A sample postman snapshot is as below.
![postman](https://github.com/rajiv77777/rateLimiter/blob/master/pics/secretKey-postman.png)