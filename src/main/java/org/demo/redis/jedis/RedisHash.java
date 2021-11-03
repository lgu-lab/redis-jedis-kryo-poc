package org.demo.redis.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisHash {

	private final JedisPool jedisPool;
	private final String redisKey;

	public RedisHash(JedisPool jedisPool, String key) {
		super();
		this.jedisPool = jedisPool;
		this.redisKey = key;
	}

	// JEDIS POOL  : see https://www.alibabacloud.com/help/doc-detail/98726.htm 
	// Jedis manages the resource pool by using Apache Commons-pool2. 
	// When you define JedisPool, we recommend that you pay attention to the GenericObjectPoolConfig parameter of the resource pool. 
	// The following sample code shows how to use this parameter.
	/*
	 GenericObjectPoolConfig jedisPoolConfig = new GenericObjectPoolConfig();
	 jedisPoolConfig.setMaxTotal(...);
	 jedisPoolConfig.setMaxIdle(...);
	 jedisPoolConfig.setMinIdle(...);
	 jedisPoolConfig.setMaxWaitMillis(...);
	 
	 JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisHost, redisPort, timeout, redisPassword);
	 */
	// jedis.close() : In JedisPool mode, the Jedis resource is returned to the resource pool.
	// managed with "try-with-resources"
	
	private Jedis getJedis() {
		try {
			return jedisPool.getResource();
		} catch (Exception e) {
			throw new IllegalStateException("Cannot get Jedis from JedisPool", e);
		}
	}
	// ---------------------------------------------------------------
	// methods for "SINGLE" element
	// ---------------------------------------------------------------
	public String get(String hashKey) {
		try (Jedis jedis = getJedis()) {
			return jedis.hget(redisKey, hashKey);
		}
	}

	public long set(String hashKey, String hashValue) {
		try (Jedis jedis = getJedis()) {
			return jedis.hset(redisKey, hashKey, hashValue);
		}
	}

	public long setIfNotExist(String hashKey, String hashValue) {
		try (Jedis jedis = getJedis()) {
			return jedis.hsetnx(redisKey, hashKey, hashValue);
		}
	}

	public long delete(String hashKey) {
		try (Jedis jedis = getJedis()) {
			return jedis.hdel(redisKey, hashKey);
		}
	}

	public boolean exists(String hashKey) {
		try (Jedis jedis = getJedis()) {
			return jedis.hexists(redisKey, hashKey);
		}
	}

	// ---------------------------------------------------------------
	// methods for "MULTIPLE" elements
	// ---------------------------------------------------------------
	public List<String> get(String... hashKeys) {
		try (Jedis jedis = getJedis()) {
			return jedis.hmget(redisKey, hashKeys);
		}
	}

	public long set(Map<String, String> map) {
		// return jedis.hmset(redisKey, map);
		// As per Redis 4.0.0, HMSET is considered deprecated. Please prefer HSET in new
		// code.
		try (Jedis jedis = getJedis()) {
			return jedis.hset(redisKey, map);
		}
	}

	// ---------------------------------------------------------------
	// methods for "ALL" elements
	// ---------------------------------------------------------------
	public long countAll() {
		try (Jedis jedis = getJedis()) {
			return jedis.hlen(redisKey);
		}
	}

	public Map<String, String> getAll() {
		try (Jedis jedis = getJedis()) {
			return jedis.hgetAll(redisKey);
		}
	}

	public Set<String> getAllKeys() {
		try (Jedis jedis = getJedis()) {
			return jedis.hkeys(redisKey);
		}
	}

	public List<String> getAllValues() {
		try (Jedis jedis = getJedis()) {
			return jedis.hvals(redisKey);
		}
	}

	public long deleteAll() {
		try (Jedis jedis = getJedis()) {
			return jedis.del(redisKey);
		}
	}

}
