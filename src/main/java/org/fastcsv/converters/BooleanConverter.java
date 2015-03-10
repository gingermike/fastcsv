package org.fastcsv.converters;

import org.fastcsv.Converter;

public class BooleanConverter implements Converter<Boolean>{

	@Override
	public Boolean convert(String data) {
		return Boolean.valueOf(data);
	}

}
