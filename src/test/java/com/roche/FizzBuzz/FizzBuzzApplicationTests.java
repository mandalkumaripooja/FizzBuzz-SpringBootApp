package com.roche.FizzBuzz;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.roche.FizzBuzz.controller.FizzBuzz;

@WebMvcTest(FizzBuzz.class)
class FizzBuzzApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FizzBuzz fizzBuzzController;

	@Test
	public void testGenerateFizzBuzzWithMoreThanOneLimit() throws Exception {
		List<String> list = List.of("1", "2", "Fizz", "4", "Buzz");

		Mockito.when(fizzBuzzController.generateFizzBuzz(3, 5, 5, "Fizz", "Buzz")).thenReturn(list);
		mockMvc.perform(MockMvcRequestBuilders.get("/fizzBuzz/generateFizzBuzz/3/5/5/Fizz/Buzz"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1]").value("2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2]").value("Fizz"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[3]").value("4"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[4]").value("Buzz"));
	}

	@Test
	void testGenerateFizzBuzzWithLimitOne() throws Exception {
		List<String> list = List.of("1");
		Mockito.when(fizzBuzzController.generateFizzBuzz(3, 5, 1, "Fizz", "Buzz")).thenReturn(list);

		mockMvc.perform(MockMvcRequestBuilders.get("/fizzBuzz/generateFizzBuzz/3/5/1/Fizz/Buzz"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("1"));
	}

	@Test
	void testGetStatisticsWithRequest() throws Exception {

		Map<String, Object> statistics = new HashMap<>();
		statistics.put("mostUsedRequest", Map.of("int1", 3, "int2", 5, "limit", 5, "str1", "Fizz", "str2", "Buzz"));
		statistics.put("numberOfHits", 10);

		Mockito.when(fizzBuzzController.getStatistics()).thenReturn(statistics);
		mockMvc.perform(get("/fizzBuzz/statistics")).andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.mostUsedRequest.int1").value(3))
				.andExpect(jsonPath("$.mostUsedRequest.int2").value(5))
				.andExpect(jsonPath("$.mostUsedRequest.limit").value(5))
				.andExpect(jsonPath("$.mostUsedRequest.str1").value("Fizz"))
				.andExpect(jsonPath("$.mostUsedRequest.str2").value("Buzz"))
				.andExpect(jsonPath("$.numberOfHits").value(10));
	}

	@Test
	void testGetStatisticsWithNoRequest() throws Exception {

		Map<String, Object> statistics = new HashMap<>();
		statistics.put("mostUsedRequest", 0);
		statistics.put("numberOfHits", 0);

		Mockito.when(fizzBuzzController.getStatistics()).thenReturn(statistics);
		mockMvc.perform(MockMvcRequestBuilders.get("/fizzBuzz/statistics"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.mostUsedRequest").value(0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.numberOfHits").value(0));
	}
}
