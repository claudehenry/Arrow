package com.ch.arrows.program;

public class Variable {
	
	private Value value;
	private String name;
	
	public Variable(String name, Value value) {
		this.name = name;
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Value value) {
		// add type difference conflict if i feel like not being a cunt.
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public Variable copy(String varname) {
		return new Variable(varname, value);
	}

	@Override
	public String toString() {
		return name + " = " + value;
	}
	
}
