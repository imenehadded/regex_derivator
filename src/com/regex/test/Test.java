package com.regex.test;

import com.regex.derivator.Derivative;
import com.regex.derivator.Expression;
import com.regex.derivator.String2Expression;

public class Test {

	public static void main(String[] args) {

		String expression = "(^r)*(r.(^a))";
		String action = "a";

		String2Expression str2Expression = new String2Expression(expression);
		Expression expressionObj  = str2Expression.getExpression();	
		
		Derivative derivative = new Derivative(expressionObj, action);	
 
		Expression expressionNew  = derivative.getExpression();
 		
		String result = expressionNew.toString();
		
		System.out.println(expression + " : " + result);

	}

}
