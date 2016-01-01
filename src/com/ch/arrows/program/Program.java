package com.ch.arrows.program;

import java.util.ArrayList;

public class Program {

	private ArrayList<Line> lines;

	private ArrayList<Variable> vars;

	private boolean multy_line_comment_flag;

	public Program(String name) {
		lines = new ArrayList<>();
		vars = new ArrayList<>();
	}

	public boolean isInMultyLineComment() {
		return multy_line_comment_flag;
	}

	public void addLine(Line line) {
		lines.add(line);
	}

	public Line getLine(int i) {
		if (i > lines.size())
			throw new RuntimeException("parameter excedes number of lines in the program! given: " + i + ", size: "
					+ lines.size());
		Line l = lines.get(i - 1);
		if (l.getLineNumber() == i)
			return lines.get(i - 1);
		throw new RuntimeException("Line numbers are not in sync with file system");
	}

	public int getNumberOfLines() {
		return lines.size();
	}

	public ArrayList<Line> getLines() {
		return lines;
	}

	public ArrayList<Variable> getVariables() {
		return vars;
	}
	
	public void addVariable(Variable var) {
		this.vars.add(var);
	}

	public Variable getVar(String name) {
		for (Variable v : vars)
			if (v.getName().equals(name))
				return v;
		return null;
	}
}
