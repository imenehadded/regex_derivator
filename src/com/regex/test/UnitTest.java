package com.regex.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.regex.derivator.Derivative;
import com.regex.derivator.Expression;
import com.regex.derivator.String2Expression;

public class UnitTest {

	private String ACTION = "a";

	private String derivator(String expression) {

		Expression expressionObj = new String2Expression(expression).getExpression();

		Derivative derivative = new Derivative(expressionObj, ACTION);

		Expression expressionNew = derivative.getExpression();

		String result = expressionNew.toString();

		return result;
	}

	@Test
	public void derive_constant() {

		assertEquals("", this.derivator("0"), "0");
		assertEquals("", this.derivator("^0"), "1");
		assertEquals("", this.derivator("1"), "0");
		assertEquals("", this.derivator("^1"), "1");
		assertEquals("", this.derivator("a"), "1");
		assertEquals("", this.derivator("^a"), "0");
		assertEquals("", this.derivator("b"), "0");
		assertEquals("", this.derivator("^b"), "1");
	}

	@Test
	public void derive_concat() {

		assertEquals("", this.derivator("a.b"), "b");
		assertEquals("", this.derivator("b.a"), "0");
		assertEquals("", this.derivator("c.b"), "0");
		assertEquals("", this.derivator("^(a.b)"), "^b");
		assertEquals("", this.derivator("^(c.b)"), "1");

		/*
		 * assertEquals("", this.derivator("a.b.c"), "b"); assertEquals("",
		 * this.derivator("a.(b.c)"), "b"); assertEquals("",
		 * this.derivator("a.^(b.c)"), "b"); assertEquals("",
		 * this.derivator("(a.b).c"), "b");
		 */
	}

	@Test
	public void derive_union() {

		assertEquals("", this.derivator("a+b"), "1");
		assertEquals("", this.derivator("c+b"), "0");
		assertEquals("", this.derivator("^(a+b)"), "0");
		assertEquals("", this.derivator("^(c+b)"), "1");

	}

	@Test
	public void derive_star() {

		assertEquals("", this.derivator("a*b"), "a*b");
		assertEquals("", this.derivator("c*b"), "0");

	}

	@Test
	public void derive_complexed() {

		assertEquals("", this.derivator("(^r)*(r.(^a))"), "0");

	}

}
