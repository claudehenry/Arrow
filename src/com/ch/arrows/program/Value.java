package com.ch.arrows.program;

public class Value {
	
	private Type type;
	private String val;
	
	public Value(Type type, String val) {
		super();
		this.type = type;
		this.val = val;
	}
	
	public Type getType() {
		return type;
	}
	public String getVal() {
		return val;
	}

	@Override
	public String toString() {
		return val + " <: " + type;
	}

	public Value add(Value other) {
		return new Value(Type.INT, "" + (Integer.parseInt(val) + Integer.parseInt(other.val)));
	}
	
	public Value mul(Value other) {
		return new Value(Type.INT, "" + (Integer.parseInt(val) * Integer.parseInt(other.val)));
	}

}
