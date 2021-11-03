package org.demo.redis.jedis.app;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class Main1 {

	private static final String DEPARTEMENTS = "departements";

	public static void main(String[] args) {
		// Jedis jedis = new Jedis();
		// Jedis jedis = new Jedis("localhost", 6379);

		// Connection pooling
		// see https://www.baeldung.com/jedis-java-redis-client-library#Connection
		try (JedisPool jedisPool = new JedisPool("localhost", 6379)) {

			Jedis jedis = jedisPool.getResource();

			jedis.set("key1", "val1");
			String v = jedis.get("key1");
			System.out.println("key1 --> " + v);

			// Transactions guarantee atomicity and thread safety operations,
			// which means that requests from other clients will never be handled
			// concurrently during Redis transactions:
			Transaction t = jedis.multi();
			t.set("key1:aa:bb", "aa-bb");
			t.set("key1:aa:cc", "aa-ccs");
			t.exec();

			initDep(jedis);

			System.out.println("departement '44' : " + getDep(jedis, "44"));
			System.out.println("departement '35' : " + getDep(jedis, "35"));

			setDep(jedis, "49", "Maine et Loire");
			System.out.println("departement '49' : " + getDep(jedis, "49"));

			clearDep(jedis);

			System.out.println("departement '44' : " + getDep(jedis, "44"));
		}

		// TODO : Pub/Sub example & Pipeline example

	}

	private static void initDep(Jedis jedis) {
		System.out.println("initDep");
		Map<String, String> map = new HashMap<>();
		map.put("35", "Ile et Vilaine");
		map.put("44", "Loire Atlantique");
		long r = jedis.hset(DEPARTEMENTS, map);
		System.out.println(" r = " + r);
	}

	private static String getDep(Jedis jedis, String code) {
		System.out.println("getDep(" + code + ")");
		return jedis.hget(DEPARTEMENTS, code);
		// jedis.hdel(code, null)
	}

	private static void setDep(Jedis jedis, String code, String text) {
		System.out.println("setDep(" + code + "," + text + ")");
		long r = jedis.hset(DEPARTEMENTS, code, text);
		System.out.println(" r = " + r);
	}

	private static void clearDep(Jedis jedis) {
		System.out.println("clearDep");
		long r = jedis.del(DEPARTEMENTS);
		System.out.println(" r = " + r);
	}
}
