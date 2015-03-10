package org.fastcsv.converters;

import org.fastcsv.Converter;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public Integer convert(String data) {
		return Integer.valueOf(data);
	}

	
	
}
