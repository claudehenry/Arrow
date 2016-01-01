package com.ch.arrows.program;

public class Token {
	
	public static enum TokenType {
		STRING, OPERATOR, COMMENT, EXPRESSION, OTHER
	}
	
	protected String data;
	protected TokenType type;
	
	public Token(String data, TokenType type) {
		this.data = data;
		this.type = type;
	}
	
	public String getData() {
		return data;
	}

	public TokenType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "Token: {" + data + "} : " + type;
	}

}
