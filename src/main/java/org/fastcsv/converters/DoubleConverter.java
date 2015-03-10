package org.fastcsv.converters;

import org.fastcsv.Converter;

public class DoubleConverter implements Converter<Double> {

	@Override
	public Double convert(String data) {
		return Double.valueOf(data);
	}

}
