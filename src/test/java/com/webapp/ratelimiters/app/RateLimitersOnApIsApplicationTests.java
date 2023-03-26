package com.webapp.ratelimiters.app;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.webapp.ratelimiters.app.controller.ControllerClass;
import com.webapp.ratelimiters.app.properties.configureProperties;


@WebMvcTest(ControllerClass.class)

class RateLimitersOnApIsApplicationTests {
	configureProperties cpTest = new configureProperties();
	
	String secretCode=cpTest.getRatelimiterGetAllData("secret");
	
	@Autowired
	private MockMvc mockMvc;
	  
	@MockBean
	private ControllerClass controllerclass;
	 
	@Test
	  public void findAllApiTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
	    			.get("/api/findall").header("secretkey", secretCode)
	    			.accept(MediaType.APPLICATION_JSON))
	        .andDo(print())
	        .andExpect(status().is2xxSuccessful());
	  }
	
	@Test
	  public void findByIDTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
	    			.get("/api/findByID/1").header("secretkey", secretCode)
	    			.accept(MediaType.APPLICATION_JSON))
	        .andDo(print())
	        .andExpect(status().is2xxSuccessful());
	  }

}
