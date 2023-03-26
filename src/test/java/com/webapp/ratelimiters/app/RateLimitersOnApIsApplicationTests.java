package com.webapp.ratelimiters.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ResourceBundle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.webapp.ratelimiters.app.controller.ControllerClass;
@SpringBootTest
@WebMvcTest(ControllerClass.class)
class RateLimitersOnApIsApplicationTests {

	@Autowired
	private MockMvc mvc;
	
	/*Test to find whether the API accepts REST calls in JSON format*/
	@Test
	public void findAllTest() throws Exception {
		  mvc.perform(MockMvcRequestBuilders
		  			.get("/api/findall")
		  			.accept(MediaType.APPLICATION_JSON))
		      .andDo(print())
		      .andExpect(status().isOk());
	}
	
	/*Test to find whether the API accepts REST calls in JSON format*/
	@Test
	public void findByIDTest() throws Exception {
		  mvc.perform(MockMvcRequestBuilders
		  			.get("/api/findByID/1")
		  			.accept(MediaType.APPLICATION_JSON))
		      .andDo(print())
		      .andExpect(status().isOk());
	}

	/*Test to fetch properties file*/
	@Test
	public void configurePropertiesTest() throws Exception{
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		assertEquals(bundle, "application");;
	}
}
