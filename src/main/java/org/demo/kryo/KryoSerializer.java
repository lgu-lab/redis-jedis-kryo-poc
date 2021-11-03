package org.demo.kryo;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializer<T> {

	// https://github.com/EsotericSoftware/kryo#quickstart
	// https://www.baeldung.com/kryo
	// https://dzone.com/articles/3-ways-to-use-redis-hash-in-java
	// https://docs.mulesoft.com/mule-runtime/3.9/improving-performance-with-the-kryo-serializer
	// https://gist.github.com/Narcissu5/a012daa3713a865b1f55a2b03e196971
	
    private Kryo kryo;

    public KryoSerializer(List<Class<?>> classes) {
        kryo = new Kryo();
        for (Class<?> clazz : classes) {
            kryo.register(clazz);
        }
    }

    public byte[] serialize(T t) {
        ByteArrayOutputStream objStream = new ByteArrayOutputStream();
        Output objOutput = new Output(objStream);
        kryo.writeClassAndObject(objOutput, t);
        objOutput.close();
        return objStream.toByteArray();
    }

    public T deserialize(byte[] bytes) {
        @SuppressWarnings("unchecked")
		T readClassAndObject = (T) kryo.readClassAndObject(new Input(bytes));
		return readClassAndObject;
    }	
}
