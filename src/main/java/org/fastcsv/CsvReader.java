package org.fastcsv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface CsvReader<T> {

	Collection<T> parse(InputStream stream) throws IOException;
	
	Collection<T> parse(InputStream stream, String delimiter) throws IOException;
	
}
