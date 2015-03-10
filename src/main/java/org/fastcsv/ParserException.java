package org.fastcsv;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = -3934167406521662351L;

	public ParserException(Exception e){
		super(e);
	}
	
	public ParserException(String message, Exception e){
		super(message, e);
	}
	
}
