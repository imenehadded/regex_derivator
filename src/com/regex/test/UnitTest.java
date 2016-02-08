package com.regex.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.regex.derivator.Derivative;
import com.regex.derivator.Expression;
import com.regex.derivator.String2Expression;

public class UnitTest {
	
 
	private Sample[] EXPRESSIONS_SAMPLES = {
			new Sample("0","0"),
			new Sample("^0","1"),
			new Sample("1","0"),
			new Sample("^1","1"),
			new Sample("a","1"),
			new Sample("^a","0"),
			new Sample("b","0"), 
			new Sample("^b","1"),
			new Sample("a.b","b"),
			new Sample("c.b","0"), 
			new Sample("a+b","1"),
			new Sample("c+b","0"), 
			new Sample("^(a.b)","^b"),
			new Sample("^(c.b)","1"), 
			new Sample("^(a+b)","0"),
			new Sample("^(c+b)","1"), 
			new Sample("a*b","a*b"),
			new Sample("c*b","0"), 
			new Sample("(^r)*(r.(^a))","0"),
			} ;
	
	private String ACTION = "a";
	
	private String derivator(String expression){
		
		String2Expression str2Expression = new String2Expression(expression);
		Expression expressionObj  = str2Expression.getExpression();	
 
		Derivative derivative = new Derivative(expressionObj, ACTION);	
 
		Expression expressionNew  = derivative.getExpression();
 		
		String result = expressionNew.toString();
	 
		return result;
	}
	
	@Test
	public void main_test(){
		
		for(Sample sample : EXPRESSIONS_SAMPLES){
			
	 
			String result = this.derivator(sample.expression);
			 
			String err_msg =  sample.expression+" must be "+sample.result+" got "+result+" instead";
			System.out.println(err_msg);
			
			assertEquals(err_msg, sample.result, result);
		}
		
	}

	class Sample{
		public String expression;
		public String result;
		public Sample(String expression,String result){
			this.expression = expression;
			this.result = result;	
		}
	}
}
