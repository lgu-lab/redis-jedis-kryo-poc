package org.demo.redis.jedis.app;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class MainJedisCluster {

	public static void main(String[] args) {
		
		// https://www.baeldung.com/jedis-java-redis-client-library#Cluster
			
		// redis.clients.jedis.exceptions.JedisDataException : ERR This instance has cluster support disabled
		try (JedisCluster jedisCluster = new JedisCluster(new HostAndPort("localhost", 6379))) {
		    // use the jedisCluster resource as if it was a normal Jedis resource
			jedisCluster.set("cluster:aa:bb", "aa-bb");
			jedisCluster.set("cluster:aa:cc", "aa-cc");
		} 
		
		/*
		We only need to provide the host and port details from one of our master instances, 
		it will auto-discover the rest of the instances in the cluster.
		
		This is certainly a very powerful feature but it is not a silver bullet. 
		When using Redis Cluster you cannot perform transactions nor use pipelines, 
		two important features on which many applications rely for ensuring data integrity.
		
		Transactions are disabled because, in a clustered environment, 
		keys will be persisted across multiple instances. 
		Operation atomicity and thread safety cannot be guaranteed for operations 
		that involve command execution in different instances.
		
		Some advanced key creation strategies will ensure that data that is 
		interesting for you to be persisted in the same instance will get persisted that way. 
		In theory, that should enable you to perform transactions successfully using 
		one of the underlying Jedis instances of the Redis Cluster.
		
		Unfortunately, currently you cannot find out in which Redis instance 
		a particular key is saved using Jedis (which is actually supported natively by Redis), 
		so you do not know which of the instances you must perform the transaction operation. 
		If you are interested about this, you can find more information here.
		 */
	}

}
