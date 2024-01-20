package com.roche.FizzBuzz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fizzBuzz")
public class FizzBuzz {

	// map to store statistics of requests
	Map<String, Integer> statisticsMap = new HashMap<>();

	@GetMapping("/generateFizzBuzz/{int1}/{int2}/{limit}/{str1}/{str2}")
	public List<String> generateFizzBuzz(@PathVariable int int1, @PathVariable int int2, @PathVariable int limit,
			@PathVariable String str1, @PathVariable String str2) {
		// Increment the request count for this combination
		String requestKey = int1 + "_" + int2 + "_" + limit + "_" + str1 + "_" + str2;
		statisticsMap.put(requestKey, statisticsMap.getOrDefault(requestKey, 0) + 1);

		// Generate FizzBuzz
		List<String> fizzBuzzList = new ArrayList<>();
		for (int i = 1; i <= limit; i++) {
			if (i % (int1 * int2) == 0) {
				fizzBuzzList.add(str1 + str2);
			} else if (i % int1 == 0) {
				fizzBuzzList.add(str1);
			} else if (i % int2 == 0) {
				fizzBuzzList.add(str2);
			} else {
				fizzBuzzList.add(String.valueOf(i));
			}
		}
		return fizzBuzzList;
	}

	@GetMapping("/statistics")
	public Map<String, Object> getStatistics() {
		Map.Entry<String, Integer> mostUsedRequest = statisticsMap.entrySet().stream().max(Map.Entry.comparingByValue())
				.orElse(null);

		Map<String, Object> statistics = new HashMap<>();
		if (mostUsedRequest != null) {
			String[] params = mostUsedRequest.getKey().split("_");
			int int1 = Integer.parseInt(params[0]);
			int int2 = Integer.parseInt(params[1]);
			int limit = Integer.parseInt(params[2]);
			String str1 = params[3];
			String str2 = params[4];

			statistics.put("mostUsedRequest",
					Map.of("int1", int1, "int2", int2, "limit", limit, "str1", str1, "str2", str2));
			statistics.put("numberOfHits", mostUsedRequest.getValue());
		} else {
			statistics.put("mostUsedRequest", null);
			statistics.put("numberOfHits", 0);
		}
		return statistics;
	}
}
