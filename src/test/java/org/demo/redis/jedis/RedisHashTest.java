package org.demo.redis.jedis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.JedisPool;

public class RedisHashTest {

	private RedisHash getRedisHash() {
		JedisPool jedisPool = new JedisPool("localhost", 6379);
		return new RedisHash(jedisPool, "cache:departements");
	}
	
	@Test
	public void test1() {
		RedisHash departements = getRedisHash();
		
		departements.deleteAll();
		assertEquals(0, departements.countAll());
		
		Map<String,String> map = new HashMap<>();
		map.put("35", "Ile et Vilaine");
		map.put("44", "Loire Atlantique");
		departements.set(map);
		assertEquals(2, departements.countAll());
		assertEquals("Loire Atlantique", departements.get("44"));
		assertNull(departements.get("75"));
		
		departements.set("49", "Maine et Loire");
		assertEquals(3, departements.countAll());
		
		assertTrue(departements.exists("35"));
		assertEquals(1, departements.delete("35"));
		assertEquals(0, departements.delete("35"));
		assertFalse(departements.exists("35"));
		
		Set<String> keys = departements.getAllKeys();
		assertTrue(keys.contains("44"));
		assertFalse(keys.contains("93"));

		List<String> values = departements.getAllValues();
		assertEquals(2, values.size());
		
		assertEquals(0, departements.setIfNotExist("44", "New"));
		assertEquals(1, departements.setIfNotExist("85", "Vendée"));

//		departements.deleteAll();
//		assertEquals(0, departements.countAll());
	}

}
