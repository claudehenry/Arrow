package com.ch.arrows;

import java.io.IOException;

import com.ch.arrows.program.Expression;
import com.ch.arrows.program.Line;
import com.ch.arrows.program.Program;
import com.ch.arrows.program.Token;
import com.ch.arrows.program.Token.TokenType;
import com.ch.arrows.program.Variable;

public class Main {

	public static void main(String... args) throws IOException {

		Program p = IO.loadFile("res/hello.arw");

		// Interpreter.run(p.getLine(2), p);
		
		Interpreter.compile(p);

		for (Line l : p.getLines())
			for (Expression s : l.getExpressions())
				System.out.println(s);
		
		Interpreter.run(p);
		
		for (Variable v : p.getVariables())
			System.out.println(v);
		
	}

}
