package com.ch.arrows.program;

import java.util.ArrayList;

import com.ch.arrows.Interpreter;

public class Expression extends Token {

	private ArrayList<Token> expressionTokens;
	private int operatorPos;

	public Expression() {
		super("", TokenType.EXPRESSION);
		this.expressionTokens = new ArrayList<>();
		this.operatorPos = genOperatorPos();
	}

	@Override
	public String toString() {
		String ts = "Expression: {\n";
		for (Token t : expressionTokens)
			for (String s : t.toString().split("\n"))
				ts += "\t" + s + "\n";
		ts += "}";
		return ts;
	}

	public ArrayList<Token> getExpressionTokens() {
		return expressionTokens;
	}

	public Token getToken(int i) {
		if (i < 0 || i >= expressionTokens.size())
			return null;
		return expressionTokens.get(i);
	}

	public void add(Token t) {
		expressionTokens.add(t);
		this.operatorPos = genOperatorPos();
	}

	private int genOperatorPos() {
		if (expressionTokens.size() == 0) return -1;
		for (String[] operators : Interpreter.operatorsInReversePrecedanceOrder) {
			int i = 0;
			for (Token t : expressionTokens) {
				if (!(t instanceof Expression)) {
					for (String op : operators)
						if (t.getData().equals(op))
							return i;
					i++;
				}
			}
		}
		return -1;
	}

	public int getOperatorPos() {
		return operatorPos;
	}

	public String getOperator() {
		return (operatorPos != -1) ? expressionTokens.get(operatorPos).getData() : "";
	}

	/**
	 * Dangerous function. is only used to subdivide expressions in lines.
	 * cannot be accessed outside of the internal package.
	 * 
	 * @param newTokens
	 */
	void setTokens(ArrayList<Token> newTokens) {
		this.expressionTokens = newTokens;
	}

	/**
	 * Dangerous function. is only used to subdivide expressions in lines.
	 * cannot be accessed outside of the internal package.
	 * 
	 * @param newOppos
	 */
	public void setOppos(int newOppos) {
		this.operatorPos = newOppos;
		
	}

}
