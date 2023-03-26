# **Rate Limiter implementation on REST APIs using token-bucket algorithm**

## **Introduction**
#
### This project has 2 APIs implemented with RateLimiters and  user authorization based on a token code for accessing the API. One Api is used to query all the datas from the H2 Database and another API displays the record with respect to ID query parameter from the same database.
#

## **Dependencies**
#
|S.No|Dependency|Version|
----|-----|-------|
|1|spring-boot-starter-parent|3.0.5|
|2|com.h2database|-|
|3|bucket4j-core|4.10.0|
|4|mockito-junit-jupiter|-|
#

## **Implementation**
#
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
#
## **Exception Handling**
#
1. ### When a request crosses its transaction limit in the assigned window time, the API throws an exception <br> `ERROR CODE - 429 Too Many Requests` as a HTTP status code.
2. ### When a request has a invalid secret key, the API throws <br> `ERROR CODE - 403 Forbidden` as an unauthorized user .
#
## **Bucket4j dependency Working and integration**
#
    public class bucket4jConfiguration {

	public final Bucket bucket;

	public bucket4jConfiguration(int transactionsAllowed,int waitTime) {
		 Bandwidth limit = Bandwidth.classic(transactionsAllowed, Refill.intervally(transactionsAllowed, Duration.ofSeconds(waitTime)));
	     this.bucket = Bucket4j.builder()
	             .addLimit(limit)
	             .build();
	    }
    }

### Bucket4j uses Token Bucket Alogorithm. Let's consider an API that has a rate limit of 100 requests per minute. We can create a bucket with a capacity of 100, and a refill rate of 100 tokens per minute. If we receive 70 requests, which is fewer than the available tokens in a given minute, we would add only 30 more tokens at the start of the next minute to bring the bucket up to capacity. On the other hand, if we exhaust all the tokens in 40 seconds, we would wait for 20 seconds to refill the bucket.
<br>

### As per the constructor initialisation we are creating an object with the transactionsAllowed and waitTime from the application.properties file.
`public bucket4jConfiguration(int transactionsAllowed,int waitTime)`
<br>

### And also to get no confilict there are individual objects created for individual APIs
### `@GetMapping(value = "/api/findall")` has its own bucket object which consumes its tokens and refills its token by a individual object.

    bucket4jConfiguration bc = new bucket4jConfiguration(rateLimiterPerTimeWindow_GetAllDataApi,rateLimiterWaitTime_GetAllDataApi);

	 @GetMapping(value = "/api/findall")

### `@GetMapping(value = "/api/findByID/{id}")` has its own bucket object which consumes its tokens and refills its token by a individual object.
     bucket4jConfiguration bc1 = new bucket4jConfiguration(rateLimiterPerTimeWindow_GetDataByIDApi,rateLimiterWaitTime_GetDataByIDApi);
	 @GetMapping(value = "/api/findByID/{id}"
#
## **Tesing the SpringBoot App**
#
### 1.Create a folder under C:\Users\<user>\RateLimiter in local machine
### 2.Open GIT bash and run `git clone -b master https://github.com/rajiv77777/rateLimiter.git`
### 3.Open Spring Suite application (STS) and import the project as `existing Maven project` from C:\Users\<user>\RateLimiter. After importing do maven clean install so that the dependencies are installed.
### 4.When Maven build is successfull. Run the application as Spring boot application
### 5.Application will be running from port 8080
### 6.Open http://localhost:8080/h2-console/ and use the below mentioned params and connect to the Database
### ![h2console](https://github.com/rajiv77777/rateLimiter/blob/master/pics/h2console-login.png)
### 7.In the SQL window run the below mentioned queries. Refernce screenshot as below.
    insert into RATE_LIMITER_MODEL values (1,'Ram','Football');
    insert into RATE_LIMITER_MODEL values (2,'peter','Football');
    insert into RATE_LIMITER_MODEL values (3,'Rajiv','Cricket');
    insert into RATE_LIMITER_MODEL values (4,'john','Baseball');
    insert into RATE_LIMITER_MODEL values (5,'Harry','Cricket');
    
### ![insertDb](https://github.com/rajiv77777/rateLimiter/blob/master/pics/insertDB.png)
### 8. DB records should be updated as below.
### ![insertDbsuccess](https://github.com/rajiv77777/rateLimiter/blob/master/pics/insertDB-success.png)
### 9.Run the below mentioned command to fetch all DB datas
    select * from RATE_LIMITER_MODEL;
### ![selectAllDb](https://github.com/rajiv77777/rateLimiter/blob/master/pics/select-all-FromDB.png)
### 10. Open Postman and create 2 GET requests as `http://localhost:8080/api/findall` and `http://localhost:8080/api/findByID/1`
### 11. Add header to both of the GET requests as below and test the ratelimiters based on API and User authorization. Reference image is as below.
### ![selectAllDb](https://github.com/rajiv77777/rateLimiter/blob/master/pics/postman-headers.png)
### Note:If external params are failing to fetch from application.properties, please maven build it again to load the `Resourcebundle` and run as spring boot application.