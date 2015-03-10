package org.fastcsv;

import java.util.Map;

public interface Parser<T> {
	
	T parse(Map<String, Integer> fieldNameToColumn, String[] row);
	
}
