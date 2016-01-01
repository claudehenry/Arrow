package com.ch.arrows.program;

import java.util.ArrayList;

import com.ch.arrows.Interpreter;
import com.ch.arrows.program.Token.TokenType;

public class Line {

	private String line;
	private int lineNumber;
	private boolean isComment;

	private ArrayList<Expression> expressions;

	public Line(String line, int lineNumber) {
		this.line = line;
		this.lineNumber = lineNumber;
	}
	
	public void compile() {
		seperateIntoTokens();

		if (expressions.size() == 1 && expressions.get(0).getExpressionTokens().size() == 1
				&& expressions.get(0).getExpressionTokens().get(0).getType() == TokenType.COMMENT)
			isComment = true;
	}

	protected void seperateIntoTokens() {
		expressions = new ArrayList<>();

		char[] line_chars = line.toCharArray();

		boolean inital_white_space_over = false;

		Expression e = new Expression();

		chars: for (int i = 0; i < line_chars.length; i++) {
			/* checks for initial whitespace */
			if (!inital_white_space_over && line_chars[i] == Interpreter.whitespaceChar)
				continue;
			/* determines when the initial whitespace is over */
			else if (!inital_white_space_over)
				inital_white_space_over = true;

			/* if the token is a comment */
			if (fetchFrom(line_chars, i, 3).equals(Interpreter.commentDeclarator)) {
				String comment_token = "";
				while (i < line_chars.length) {
					comment_token += line_chars[i];
					i++;
				}
				e.add(new Token(comment_token, TokenType.COMMENT));
				continue;
			}

			/* creates a token from an escaped UTF-8 charset declaration */
			if (line_chars[i] == Interpreter.charsetDelimiterChar) {
				String charset_token = "";
				i++;
				while (line_chars[i] != Interpreter.charsetDelimiterChar
						|| line_chars[i - 1] == Interpreter.charsetEscapeChar) {
					if (line_chars[i] == Interpreter.charsetEscapeChar)
						i++;
					charset_token += line_chars[i];
					i++;
					if (i >= line_chars.length)
						System.err.println("Could not find matching \" for charset expression in line " + lineNumber
								+ ", \'" + line + "\'.");
				}
				e.add(new Token(charset_token, TokenType.STRING));
				continue;
			}

			/* if the token is any non-special character */
			if (line_chars[i] != Interpreter.whitespaceChar) {
				String initializer_token = "" + line_chars[i++];
				for (; i < line_chars.length
						&& ((i + 1 >= line_chars.length) ? true : !fetchFrom(line_chars, i, 2).equals(
								Interpreter.memberAccessor)) && line_chars[i] != (Interpreter.whitespaceChar); i++) {
					initializer_token += line_chars[i];

				}
				// i--;
				for (String s : Interpreter.operators) {
					if (initializer_token.equals(s)) {
						e.add(new Token(initializer_token, TokenType.OTHER));
						continue chars;
					}
				}
				e.add(new Token(initializer_token, TokenType.OTHER));
				continue;
			}
		}
		
		if (!inital_white_space_over) return;

		subdivideExpression(e);

		expressions.add(e);
	}

	private void subdivideExpression(Expression e) {
		
		if (e.getOperatorPos() == -1) {
			return;
		}

		int newOppos;
		ArrayList<Token> newTokens = new ArrayList<>();
		ArrayList<Token> tks = e.getExpressionTokens();


		// left hand expressions
		if (e.getOperatorPos() > 1) {
			
			Expression exp = new Expression();
			
			for (int i = 0; i < e.getOperatorPos(); i++) {
				exp.add(tks.get(i));
			}
			
			subdivideExpression(exp);

			newTokens.add(exp);
			
		} else {
			for (int i = 0; i < e.getOperatorPos(); i++) {
				newTokens.add(tks.get(i));
			}
		}

		newOppos = newTokens.size();
		newTokens.add(e.getToken(e.getOperatorPos()));

		// right hand expressions
		if (e.getExpressionTokens().size() - (e.getOperatorPos() + 1) > 1) {

			Expression exp = new Expression();

			for (int i = e.getOperatorPos() + 1; i < tks.size(); i++) {
				exp.add(tks.get(i));
			}

			subdivideExpression(exp);

			newTokens.add(exp);

		} else {
			for (int i = e.getOperatorPos() + 1; i < tks.size(); i++) {
				newTokens.add(tks.get(i));
			}
		}

		e.setOppos(newOppos);
		e.setTokens(newTokens);
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public ArrayList<Expression> getExpressions() {
		return expressions;
	}

	private static final String fetchFrom(char[] charset, int start, int length) {
		String ret = "";
		if (start < 0 || length < 0 || start + length > charset.length) {
			return ret;
		}
		for (int i = start; i < start + length; i++) {
			ret += charset[i];
		}
		return ret;
	}

	public boolean isComment() {
		return isComment;
	}

}
