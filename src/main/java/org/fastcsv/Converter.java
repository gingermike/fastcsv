package org.fastcsv;

public interface Converter<V> {

	V convert(String data);
	
}
