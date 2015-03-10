package org.fastcsv.converters;

import org.fastcsv.Converter;

public class LongConverter implements Converter<Long>{

	@Override
	public Long convert(String data) {
		return Long.valueOf(data);
	}

}
