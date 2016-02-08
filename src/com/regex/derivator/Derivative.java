package com.regex.derivator;

import com.regex.derivator.String2Expression.EXPRESSION_SYMBOL;

public class Derivative {
	Expression exp;
	String action;

	public Derivative(Expression exp, String action) {
		this.exp = exp;
		this.action = action;
	}

	public Expression getExpression() {
		
		if (exp.with_not) {
			return derive_not(exp, this.action);
		}

		if (exp.name_action.equals("")) {// expression

			// exp*exp
			if (exp.opperator == EXPRESSION_SYMBOL.binary_star) {
				return derive_star(exp, this.action);
			}

			// exp.exp
			if (exp.opperator == EXPRESSION_SYMBOL.concatenation) {
				return derive_mult(exp, this.action);
			}

			// exp+exp
			if (exp.opperator == EXPRESSION_SYMBOL.union) {
				return derive_add(exp, this.action);
			}

		} else {// action
			return derive_constant(exp, this.action);
		}
		
		System.err.println("Expression not derivated");
		return exp;
	}

	private Expression derive_star(Expression exp, String action) {
 
		// exp1*exp2=d(exp1).((exp1*exp2)+d(exp2))
		 
		//(exp1*exp2)
		Expression left_exp = new Expression();
		left_exp.left_exp =  exp.left_exp;
		left_exp.right_exp = exp.right_exp;
		left_exp.opperator = EXPRESSION_SYMBOL.binary_star;
		
		//((exp1*exp2)+d(exp2))
		Expression right_exp = new Expression();
		right_exp.left_exp = left_exp;
		right_exp.opperator = EXPRESSION_SYMBOL.union;
		right_exp.right_exp = new Derivative(exp.right_exp, action).getExpression();
		
		//d(exp1).((exp1*exp2)+d(exp2))
		Expression return_exp = new Expression();
		return_exp.left_exp = new Derivative(exp.left_exp , action).getExpression();
		return_exp.opperator = EXPRESSION_SYMBOL.concatenation ;
		return_exp.right_exp = right_exp;

		return return_exp;
	}

	private Expression derive_not(Expression expression, String action) {

		Expression clone_exp = new Expression();
		clone_exp.left_exp = expression.left_exp;
		clone_exp.opperator = expression.opperator;
		clone_exp.right_exp = expression.right_exp;
		clone_exp.name_action = expression.name_action;
		clone_exp.with_not = false;

		Derivative derv = new Derivative(clone_exp, action);

		Expression return_exp = derv.getExpression();
		return_exp.with_not = true;

		return return_exp;
	}

	private Expression derive_constant(Expression exp, String action) {

		Expression return_exp = new Expression();
		return_exp.with_not = exp.with_not;

		// a/a
		if (exp.name_action.equals(action)) {
			return_exp.name_action = "1";
			return return_exp;
		}
		// a/0
		if (exp.is_numeric_zero()) {

			return_exp.name_action = "0";
			return return_exp;
		}
		// a/1
		if (exp.is_numeric_one()) {
			return_exp.name_action = "0";
			return return_exp;
		}

		// a/b
		if (exp.name_action.equals(action) == false) {
			return_exp.name_action = "0";
			return return_exp;
		}
 
		return exp;

	}

	private Expression derive_mult(Expression exp, String action) {

		Expression return_exp = new Expression();
		Derivative derv = new Derivative(exp.left_exp, action);

		return_exp.left_exp = derv.getExpression();
		return_exp.right_exp = exp.right_exp;
		return_exp.opperator = EXPRESSION_SYMBOL.concatenation;

		return return_exp;
	}

	private Expression derive_add(Expression exp, String action) {

		Expression return_exp = new Expression();
		Derivative derv_gauche = new Derivative(exp.left_exp, action);
		Derivative derv_droite = new Derivative(exp.right_exp, action);

		return_exp.left_exp = derv_gauche.getExpression();
		return_exp.right_exp = derv_droite.getExpression();
		return_exp.opperator = EXPRESSION_SYMBOL.union;

		return return_exp;
	}

}
