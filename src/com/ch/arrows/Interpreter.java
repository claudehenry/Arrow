package com.ch.arrows;

import java.util.ArrayList;

import com.ch.arrows.program.Expression;
import com.ch.arrows.program.Line;
import com.ch.arrows.program.Program;
import com.ch.arrows.program.Token;
import com.ch.arrows.program.Token.TokenType;
import com.ch.arrows.program.Type;
import com.ch.arrows.program.Value;
import com.ch.arrows.program.Variable;

public class Interpreter {

	public static final String commentDeclarator = "++>";
	public static final String functionDeclarator = "func";
	public static final char charsetDelimiterChar = '\"';
	public static final char charsetEscapeChar = '\\';
	public static final char whitespaceChar = ' ';

	public static final String memberAccessor = "->";
	public static final String assignmentOperator = "<-";
	public static final String functionDeclOperator = "=>";
	public static final String functionPassOperator = "<=";
	public static final String typeDeclOperator = "==>";
	public static final String optionalTypeDeclarator = "<:";
	public static final String guardDelimier = "|";

	public static final String add = "+";
	public static final String sub = "-";
	public static final String mul = "*";
	public static final String div = "/";
	public static final String pow = "**";
	// public static final String add = "+";
	public static final String[] operators = { add, sub, mul, div, pow, memberAccessor, assignmentOperator,
			functionDeclOperator, functionPassOperator, optionalTypeDeclarator };

	public static final String[][] operatorsInReversePrecedanceOrder = {
			new String[] { assignmentOperator, functionPassOperator, guardDelimier, }, // level 1
			new String[] { optionalTypeDeclarator, },
			new String[] { add, sub, }, //
			new String[] { mul, div, }, //
			new String[] { memberAccessor, },
	};

	public static final String constMod = "let";
	public static final String[] modifiers = { constMod };

	public static void run(Line l, Program p) {
		ArrayList<Expression> exp = l.getExpressions();

		if (exp.size() == 0)
			return;
		if (l.isComment())
			return;

		for (Expression e : exp) {
			try {
				evaluate(e, null, p);
			} catch (SyntaxError e1) {
				e1.printStackTrace();
			}
		}

		// if (tokens.size() > 2) {
		// if (tokens.get(1).getData().equals(assignmentOperator)) {
		// String varname = tokens.get(0).getData();
		// String rawval = tokens.get(2).getData();
		// String type = (tokens.get(2).getType() == Type.STRING) ? _string :
		// "";
		// if (tokens.size() == 5) {
		// if (tokens.get(3).equals(optionalTypeDeclarator)) {
		// type = tokens.get(4).getData();
		// }
		// } else if (type.equals("")) {
		// if (isIntegral(rawval))
		// type = _int;
		// else if (isFloating(rawval))
		// type = _float;
		// }
		// if (type.equals(_int))
		// p.addVariable(new Variable<Integer>(varname,
		// Integer.parseInt(rawval)));
		// else if (type.equals(_float))
		// p.addVariable(new Variable<Float>(varname,
		// Float.parseFloat(rawval)));
		// else if (type.equals(_string))
		// p.addVariable(new Variable<String>(varname, rawval));
		// else {
		// Variable<?> v;
		// if ((v = p.getVar(rawval)) != null) {
		// p.addVariable(v.copy(varname));
		// }
		//
		// }
		// }
		// }

	}

	public static Value evaluate(Expression e, Expression parrent, Program p) throws SyntaxError {
		ArrayList<Token> tks = e.getExpressionTokens();
		int oppos = e.getOperatorPos();
		if (oppos > -1) {
			String op = e.getOperator();
			if (op.equals(assignmentOperator)) {
				// left hand
				if (tks.get(oppos - 1) instanceof Expression)
					throw new SyntaxError("Cannot have Expression on the left hand side of assignement operator");
				String varname = tks.get(oppos - 1).getData();
				ArrayList<String> mods = new ArrayList<>();
				for (int i = 0; i < oppos - 1; i++) {
					Token t = tks.get(i);
					if (t instanceof Expression)
						throw new SyntaxError("Cannot have Expression on the left hand side of assignement operator");
					boolean valid_mod = false;
					for (String s : modifiers) {
						if (t.getData().equals(s)) {
							valid_mod = true;
							break;
						}
					}
					if (!valid_mod)
						throw new SyntaxError("Invalid modifier: compiler could not understand \'" + t.getData()
								+ "\'.");
					mods.add(tks.get(i).getData());
				}

				// right hand
				Value v;
				if (tks.get(oppos + 1) instanceof Expression) {
					v = evaluate((Expression) tks.get(oppos + 1), e, p);

				} else {
					v = getInferredVal(tks.get(oppos + 1));
				}

				p.addVariable(new Variable(varname, v));
				return v;

			}

			if (op.equals(add)) {

				Token left_t = e.getToken(oppos - 1), right_t = e.getToken(oppos + 1);
				Value left, right;
				if (right_t == null) {
					throw new SyntaxError("Error: cannot have no right operand for operator '+'");
				}

				if (right_t instanceof Expression) {
					right = evaluate((Expression) right_t, e, p);
				} else {
					right = getInferredVal(right_t);
				}

				if (left_t == null) {
					return right;
				}
				
				if (left_t instanceof Expression) {
					left = evaluate((Expression) left_t, e, p);
				} else {
					left = getInferredVal(left_t);
				}
				
				return left.add(right);
			}
			
			if (op.equals(mul)) {

				Token left_t = e.getToken(oppos - 1), right_t = e.getToken(oppos + 1);
				Value left, right;
				if (right_t == null) {
					throw new SyntaxError("Error: cannot have no right operand for operator '+'");
				}

				if (right_t instanceof Expression) {
					right = evaluate((Expression) right_t, e, p);
				} else {
					right = getInferredVal(right_t);
				}

				if (left_t == null) {
					return right;
				}
				
				left = getInferredVal(left_t);
				
				return left.mul(right);
			}

		}
		return new Value(Type.VOID, "NULL");
	}

	private static Value getInferredVal(Token raw) {

		Type type = Type.VOID;
		String val = raw.getData();

		if (raw.getType() == TokenType.STRING) {
			type = Type.STRING;
		} else {
			if (isIntegral(raw.getData())) {
				type = Type.INT;
			} else if (isFloating(raw.getData())) {
				type = Type.FLOAT;
			}
		}

		return new Value(type, val);

	}

	private static boolean isIntegral(String s) {
		return containsOnly(s, new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' });
	}

	private static boolean containsOnly(String s, char[] chars) {
		String str = s;
		for (char c : chars) {
			str = str.replace("" + c, "");
		}
		return str.length() == 0;
	}

	private static boolean isFloating(String s) {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static void run(Program p) {
		for (Line l : p.getLines())
			run(l, p);
	}

	// public static boolean isWholeLineComment(Line l, Program p) {
	// return p.isInMultyLineComment() || l.getLine().replace(" ",
	// "").startsWith(commentDeclarator);
	// }

	public static boolean isLineEmpty(Line l) {
		return l.getLine().replace(" ", "").equals("");
	}

	public static boolean validateLine(String line_text) {
		return true;
	}

	public static void throwSyntaxError(Line l) {
		System.err.println("Syntax Error: at line " + l.getLineNumber() + ", \"" + l.getLine() + "\".");
	}

	public static void compile(Program p) {
		for (Line l : p.getLines())
			l.compile();
	}

}
