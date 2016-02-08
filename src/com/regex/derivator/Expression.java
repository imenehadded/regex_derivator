package com.regex.derivator;

import com.regex.derivator.String2Expression.EXPRESSION_SYMBOL;

public class Expression {

	public Expression left_exp = null;
	public Expression right_exp = null;
	public int opperator = 0;
	public boolean with_not = false;
	public String name_action = "";

	public boolean is_numeric_one() {

		return name_action.equals("1");
	}

	public boolean is_numeric_zero() {

		return name_action.equals("0");
	}

	private String toString(boolean is_main) {
		String result = "";

		if (this.with_not)
			result += "^";

		if (name_action.equals("") == true) {

			/*
			 * System.out.println("------------------");
			 * System.out.println(left_exp); System.out.println(right_exp);
			 * System.out.println(this.opperator);
			 * System.out.println(this.name_action);
			 * System.out.println("------------------");
			 */
			String opperator_str = String2Expression.symbolToOpperator(this.opperator);
			String left_exp = this.left_exp.toString(false);
			String right_exp = this.right_exp.toString(false);
			
			result =   left_exp+ opperator_str + right_exp;
			if(this.with_not==true  ){
				result =  "^("+ result+ ")";
			}else if (is_main == false  )
				result = "(" + result + ")";

		} else {
			result = name_action;
			if (this.with_not)
				result = "^("+result+")";
		}

		return result;
	}

	public String toString() {

		// System.out.println("without : " + this.toString(false));
		this.optimize_expression();
		String str_exp = this.toString(true);
		// System.out.println("optimize : " + str_exp);

		return str_exp;
	}

	private void optimize_expression() {

		//System.out.println("optimize_expression : "+this.toString(false));

		if (this.right_exp != null && this.left_exp != null) {
			this.left_exp.optimize_expression();
			this.right_exp.optimize_expression();
		}

		// ^1=0
		if (this.with_not && this.is_numeric_one()) {
			this.with_not = false;
			this.name_action = "0";
			return;
		}

		// ^0=1
		if (this.with_not && this.is_numeric_zero()) {
			this.with_not = false;
			this.name_action = "1";
			return;
		}

		// Expression as action, Nothing to optimize
		if (this.name_action.equals("") == false)
			return;

		// 1 oper exp | exp oper 1
		if ((this.right_exp.is_numeric_one() && !this.left_exp.is_numeric_zero())
				|| (this.left_exp.is_numeric_one() && !this.right_exp.is_numeric_zero())) {

			// 1*a=a 
			if (this.opperator == EXPRESSION_SYMBOL.binary_star) {
				/*
				 * if (this.right_exp.is_numeric_one()) this.put(this.left_exp);
				 * 
				 * if (this.left_exp.is_numeric_one()) this.put(this.right_exp);
				 */
				return;
			}
			// 1+a=1
			else if (this.opperator == EXPRESSION_SYMBOL.union) {
				this.left_exp = null;
				this.right_exp = null;
				this.name_action = "1";
			}
			// 1.a=a | a.1=a
			else if (this.opperator == EXPRESSION_SYMBOL.concatenation) {

				if (this.right_exp.is_numeric_one())
					this.put(this.left_exp);

				else if (this.left_exp.is_numeric_one())
					this.put(this.right_exp);

			}

			this.optimize_expression();
			return;
		} // end (1 opper exp)

		// 0 oper exp | exp oper 0
		if ((this.right_exp.is_numeric_zero() && !this.left_exp.is_numeric_one())
				|| (this.left_exp.is_numeric_zero() && !this.right_exp.is_numeric_one())) {

			// 0*a=a
			if (this.opperator == EXPRESSION_SYMBOL.binary_star) {
				/*
				 * // 0*a=a if (this.right_exp.is_numeric_zero())
				 * this.put(this.left_exp);
				 * 
				 * // a*0=a* if (this.left_exp.is_numeric_zero())
				 * this.put(this.right_exp);
				 */
				return;
			}

			else if (this.opperator == EXPRESSION_SYMBOL.union) {
				// a+0=a
				if (this.right_exp.is_numeric_zero())
					this.put(this.left_exp);

				// 0+a=a
				else if (this.left_exp.is_numeric_zero())
					this.put(this.right_exp);

			}
			// 0.a=0
			else if (this.opperator == EXPRESSION_SYMBOL.concatenation) {
				this.left_exp = null;
				this.right_exp = null;
				this.name_action = "0";
			}

			this.optimize_expression();
			return;
		}

		// 1 opper 0 | 0 opper 1
		if ((this.left_exp.is_numeric_one() && this.right_exp.is_numeric_zero())
				|| (this.left_exp.is_numeric_zero() && this.right_exp.is_numeric_one())) {

			if (this.opperator == EXPRESSION_SYMBOL.binary_star) {
				this.left_exp = null;
				this.right_exp = null;
				this.name_action = "1";
			}
			// 1+0=1
			if (this.opperator == EXPRESSION_SYMBOL.union) {
				this.left_exp = null;
				this.right_exp = null;
				this.name_action = "1";
			}
			// 0.1=0
			if (this.opperator == EXPRESSION_SYMBOL.concatenation) {
				this.left_exp = null;
				this.right_exp = null;
				this.name_action = "0";
			}

			this.optimize_expression();
			return;
		}

	}

	private void put(Expression new_expression) {
		 
		this.left_exp = new_expression.left_exp;
		this.right_exp = new_expression.right_exp;
		this.name_action = new_expression.name_action;
		this.opperator = new_expression.opperator;
		
		if (this.with_not && new_expression.with_not)
			this.with_not = false;

		if (this.with_not && !new_expression.with_not)
			this.with_not = true;

		if (!this.with_not && new_expression.with_not)
			this.with_not = true;

		if (!this.with_not && !new_expression.with_not)
			this.with_not = false;

	}

}
