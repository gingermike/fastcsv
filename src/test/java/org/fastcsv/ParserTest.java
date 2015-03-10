package org.fastcsv;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.fastcsv.impls.Model;
import org.junit.Test;


public class ParserTest {

	@Test
	public void shouldParse() throws IOException{
		
		//Assemble
		CsvReader<Model> reader = CsvReaderImpl.getCsvReader(Model.class);
		InputStream stream = ParserTest.class.getResourceAsStream("test.csv");
		
		//Act
		Collection<Model> models = reader.parse(stream, "\t");
		
		//Assert
		assertEquals(6, models.size());
		
	}
	
	
	
}
