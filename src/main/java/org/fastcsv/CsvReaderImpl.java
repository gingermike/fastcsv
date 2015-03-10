package org.fastcsv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CsvReaderImpl<T> implements CsvReader<T>{

	private Parser<T> _parser;
	
	private CsvReaderImpl(Class<T> clazz){
		_parser = ParserClassLoader.getInstance().getParser(clazz);
	}
	
	public static <T> CsvReader<T> getCsvReader(Class<T> model){
		return new CsvReaderImpl<T>(model);
	}
	
	@Override
	public Collection<T> parse(InputStream stream, String delimiter) throws IOException {
		List<T> models = new LinkedList<T>();
		InputStreamReader ir = null;
		BufferedReader br = null;
		try{
			ir = new InputStreamReader(stream);
			br = new BufferedReader(ir);
			String line = br.readLine();
			Map<String, Integer> fields = getFields(line, delimiter);
			line = br.readLine();
			while(line != null){
				String[] row = line.split(delimiter);
				T model = _parser.parse(fields, row);
				models.add(model);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new ParserException("Cannot find file", e);
		} finally{
			if(br != null)
				br.close();
			
			if(ir != null)
				ir.close();
		}
		return models;
	}
	
	@Override
	public Collection<T> parse(InputStream stream) throws IOException{
		return parse(stream, ",");
	}
	
	private Map<String, Integer> getFields(String line, String delimiter) {
		Map<String, Integer> fieldToIndex = new HashMap<String, Integer>();
		String[] fields = line.split(delimiter);
		for(int i = 0; i < fields.length; i++){
			fieldToIndex.put(fields[i], i);
		}
		return fieldToIndex;
	}

	
	
	
}
