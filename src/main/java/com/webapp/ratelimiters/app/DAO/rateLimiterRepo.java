package com.webapp.ratelimiters.app.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.webapp.ratelimiters.app.model.rateLimiterModel;


public interface rateLimiterRepo extends JpaRepository<rateLimiterModel, Integer> {
	List<rateLimiterModel> findByName(String name);
}
