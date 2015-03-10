package org.fastcsv.impls;

import org.fastcsv.annotation.FieldConverter;
import org.fastcsv.annotation.Field;
import org.fastcsv.converters.LongConverter;
import org.joda.time.LocalDate;

public class Model {
	@Field("Field")
	private String field;
	
	@Field("Date")
	@FieldConverter(LocalDateConverter.class)
	private LocalDate date;
	
	@Field("Test")
	@FieldConverter(LongConverter.class)
	private Long id;

	public void setField(String field) {
		this.field = field;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
