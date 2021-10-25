package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

	@Autowired
	RedisRepository redisRepository;
	
	@Autowired
	UserDataCacheRepo userDataCacheRepo;
	
	@GetMapping("/cache")
	public String getCachedData() {
		return userDataCacheRepo.getData();
	}
	
	@GetMapping("/api")
	public String getData() {

		dataSetInHash();

		System.out.println(redisRepository.getRootKeys());
		System.out.println(redisRepository.getHashKeys("abc"));
		redisRepository.persistData("def", "data5", "value5");
		System.out.println(redisRepository.getHashKeys("def"));
		System.out.println(redisRepository.getResponse("def", "data5"));

		redisRepository.deleteKey("def", "data5");
		System.out.println(redisRepository.getHashKeys("def"));

		redisRepository.deleteRootKey("abc");
		System.out.println(redisRepository.getRootKeys());

		redisRepository.deleteRootKey("def");
		redisRepository.deleteRootKey("ghi");
		redisRepository.deleteRootKey("jkl");
		System.out.println(redisRepository.getRootKeys());

		return "Success";
	}

	private void dataSetInHash() {
		Map<String, String> map = new HashMap<>();
		map.put("data1", "value1");
		map.put("data2", "value2");
		map.put("data3", "value3");
		map.put("data4", "value4");
		redisRepository.putAll("abc", map);
		redisRepository.putAll("def", map);
		redisRepository.putAll("ghi", map);
		redisRepository.putAll("jkl", map);
	}
}
