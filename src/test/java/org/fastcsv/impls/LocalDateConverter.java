package org.fastcsv.impls;

import org.fastcsv.Converter;
import org.joda.time.LocalDate;

public class LocalDateConverter implements Converter<LocalDate>{

	@Override
	public LocalDate convert(String data) {
		return LocalDate.parse(data);
	}
	
}
