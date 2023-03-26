package com.webapp.ratelimiters.app.bucket4jConfig;

import java.time.Duration;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;


public class bucket4jConfiguration {

	public final Bucket bucket;

	public bucket4jConfiguration(int transactionsAllowed,int waitTime) {
		 Bandwidth limit = Bandwidth.classic(transactionsAllowed, Refill.intervally(transactionsAllowed, Duration.ofSeconds(waitTime)));
	     this.bucket = Bucket4j.builder()
	             .addLimit(limit)
	             .build();
	}
	
}
