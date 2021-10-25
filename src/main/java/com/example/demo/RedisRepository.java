package com.example.demo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
public class RedisRepository {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private final HashOperations<String, String, String> hashOperations;

	public RedisRepository(final RedisTemplate<String, Object> redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();
	}

	public Set<String> getRootKeys() {
		return redisTemplate.keys("*");

	}

	public Set<String> getHashKeys(String rootKey) {
		Set<String> hashKeys = new HashSet<>();
		if (!ObjectUtils.isEmpty(rootKey)) {
			hashKeys = hashOperations.keys(rootKey);
		}

		return hashKeys;
	}

	public void putAll(String rootKey, Map<String, String> map) {
		hashOperations.putAll(rootKey, map);
		redisTemplate.getConnectionFactory().getConnection().save();
	}

	public void persistData(String rootKey, String dataKey, String value) {
		hashOperations.put(rootKey, dataKey, value);
		save();
	}

	public String saveData(String rootKey, String dataKey, String value) {
		hashOperations.put(rootKey, dataKey, value);
		return hashOperations.get(rootKey, dataKey);
	}

	public String getResponse(String rootKey, String dataKey) {

		return hashOperations.get(rootKey, dataKey);
	}

	public void deleteRootKey(String rootKey) {
		redisTemplate.delete(rootKey);
		save();
	}

	public void deleteKey(String rootKey,String dataKey) {
		hashOperations.delete(rootKey,dataKey);
		save();
	}
	public void save() {
		redisTemplate.getConnectionFactory().getConnection().save();
	}

}
