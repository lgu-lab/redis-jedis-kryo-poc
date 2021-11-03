package org.demo.kryo.app;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoMain1 {

	
	public static void main(String[] args) {
		
		// https://www.baeldung.com/kryo
		
		Kryo kryo = new Kryo();
		kryo.register(Date.class);
	    
	    byte[] ser1 = serialize(kryo, "abcdef"); 
		System.out.println("ser1 : " + ser1);
	    byte[] ser2 = serialize(kryo, new Date(915170400000L) ); 
		System.out.println("ser2 : " + ser2);
	    
		Object obj1 = deserialize(kryo, ser1);
		System.out.println("obj1 : " + obj1);
		Object obj2 = deserialize(kryo, ser2);
		System.out.println("obj2 : " + obj2);
	}
	
    public static byte[] serialize(Kryo kryo, Object o) {
        ByteArrayOutputStream objStream = new ByteArrayOutputStream();
        Output objOutput = new Output(objStream);
        kryo.writeClassAndObject(objOutput, o);
        objOutput.close();
        return objStream.toByteArray();
    }
    
    public static Object deserialize(Kryo kryo, byte[] bytes) {
        return kryo.readClassAndObject(new Input(bytes));
    }
}
