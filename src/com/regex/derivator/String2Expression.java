package com.regex.derivator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class String2Expression {

	public class EXPRESSION_SYMBOL {
		public static final int union = 1;// +
		public static final int concatenation = 2;// .
		public static final int alpha = 3;// Alphabetic
		public static final int binary_not = 4;// ^
		public static final int binary_star = 5;// *
		public static final int opening_parenthesis = 6;// (
		public static final int closing_parenthesis = 7;// )
		public static final int begin = 8;// start of expression
		public static final int end = 9;// end of expression
	}

	public class EXPRESSION_DEVIS {
		public String left_exp = "";
		public String right_exp = "";
		public int opperator = 0;
	}

	private String exp = "";

	public String2Expression(String exp) {
		this.exp = remove_parentese(exp);
	}

	private String remove_parentese(String exp_string) {

		List<Integer> exp_list = stringExp2List(exp_string);
		int nb_element = exp_list.size();

		Integer symbole = exp_list.get(1);

		if (symbole != EXPRESSION_SYMBOL.opening_parenthesis)
			return exp_string;

		int nb_parentese_droit = 0;

		for (int i = 2; i < nb_element; i++) {
			symbole = exp_list.get(i);

			if (symbole == EXPRESSION_SYMBOL.opening_parenthesis)
				nb_parentese_droit++;

			if (symbole == EXPRESSION_SYMBOL.closing_parenthesis) {
				nb_parentese_droit--;
				symbole = exp_list.get(i + 1);

				if (symbole != EXPRESSION_SYMBOL.end && nb_parentese_droit == -1)
					return exp_string;
				if (symbole == EXPRESSION_SYMBOL.end && nb_parentese_droit == -1) {
					exp_string = exp_string.substring(1, exp_string.length() - 1);

					return remove_parentese(exp_string);
				}

			}
		}

		return exp_string;
	}

	private String remove_not(String exp_string) {

		exp_string = remove_parentese(exp_string);

		if (!iswith_not(exp_string))
			return exp_string;

		List<Integer> exp_list = stringExp2List(exp_string);

		Integer symbole = exp_list.get(1);

		if (symbole != EXPRESSION_SYMBOL.binary_not)
			return exp_string;

		exp_string = exp_string.substring(1, exp_string.length());

		return remove_not(exp_string);
	}

	public EXPRESSION_DEVIS split_expression(String exp_string) {

		List<Integer> exp_list = this.stringExp2List(exp_string);

		Integer symbole;
		int index_opperator = 0;

		List<Integer> list_position_opperator = new ArrayList<Integer>();

		int nb_element = exp_list.size();
		for (int i = 0; i < nb_element; i++) {
			symbole = exp_list.get(i);
			if (symbole == EXPRESSION_SYMBOL.union || symbole == EXPRESSION_SYMBOL.concatenation
					|| symbole == EXPRESSION_SYMBOL.binary_star) {
				list_position_opperator.add(i);
			}
		}

		for (Integer position : list_position_opperator) {
			index_opperator = position;

			if (this.is_high_praiority_opperator(exp_list, position))
				break;

		}

		index_opperator--;

		EXPRESSION_DEVIS exp_div = new EXPRESSION_DEVIS();

		exp_div.left_exp = exp_string.substring(0, index_opperator);
		exp_div.right_exp = exp_string.substring(index_opperator + 1, exp_string.length());
		exp_div.opperator = opperatorToSymbol(exp_string.charAt(index_opperator));

		return exp_div;
	}

	private boolean is_high_praiority_opperator(List<Integer> exp_list, Integer position) {

		int nb_gauche = 0;
		int nb_element = exp_list.size();
		Integer symbole;

		// Calculate balance of parenthesis
		for (int i = position - 1; i >= 0; i--) {
			symbole = exp_list.get(i);

			if (symbole == EXPRESSION_SYMBOL.closing_parenthesis)
				nb_gauche--;
			if (symbole == EXPRESSION_SYMBOL.opening_parenthesis)
				nb_gauche++;
		}

		// if closing/opening parenthesis are equal
		if (nb_gauche == 0) {
			symbole = exp_list.get(position);
			if (symbole == EXPRESSION_SYMBOL.concatenation) {
				Integer symbole_1 = exp_list.get(position + 1);
				Integer symbole_2 = exp_list.get(position + 2);

				if (symbole_1 == EXPRESSION_SYMBOL.opening_parenthesis || (symbole_1 == EXPRESSION_SYMBOL.binary_not
						&& symbole_2 == EXPRESSION_SYMBOL.opening_parenthesis)) {

					int nb_parentese = 0;
					for (int i = position; i < nb_element; i++) {
						symbole = exp_list.get(i);
						if (symbole == EXPRESSION_SYMBOL.opening_parenthesis)
							nb_parentese++;
						if (symbole == EXPRESSION_SYMBOL.closing_parenthesis) {
							nb_parentese--;
							if (nb_parentese == 0) {
								symbole = exp_list.get(i + 1);
								if (symbole == EXPRESSION_SYMBOL.end)
									return true;

								if (symbole == EXPRESSION_SYMBOL.binary_star) {
									symbole = exp_list.get(i + 2);
									if (symbole == EXPRESSION_SYMBOL.end)
										return true;
								}
								return false;
							}
						}
					}

				}
			}
			return true;
		}

		boolean valide_exp = true;

		for (int i = nb_element - 1; i >= 0; i--) {
			symbole = exp_list.get(i);

			if (symbole == EXPRESSION_SYMBOL.closing_parenthesis || symbole == EXPRESSION_SYMBOL.binary_star)
				valide_exp = false;

			if (symbole == EXPRESSION_SYMBOL.closing_parenthesis)
				nb_gauche--;

		}

		if (valide_exp) {

			symbole = exp_list.get(position);
			if (symbole == EXPRESSION_SYMBOL.concatenation) {
				Integer symbole_1 = exp_list.get(position + 1);
				Integer symbole_2 = exp_list.get(position + 2);

				if (symbole_1 == EXPRESSION_SYMBOL.opening_parenthesis || (symbole_1 == EXPRESSION_SYMBOL.binary_not
						&& symbole_2 == EXPRESSION_SYMBOL.opening_parenthesis)) {

					int nb_parentese = 0;
					for (int i = position; i < nb_element; i++) {
						symbole = exp_list.get(i);
						if (symbole == EXPRESSION_SYMBOL.opening_parenthesis)
							nb_parentese++;
						if (symbole == EXPRESSION_SYMBOL.closing_parenthesis) {
							nb_parentese--;
							if (nb_parentese == 0) {
								symbole = exp_list.get(i + 1);
								if (symbole == EXPRESSION_SYMBOL.end)
									return true;

								if (symbole == EXPRESSION_SYMBOL.binary_star) {
									symbole = exp_list.get(i + 2);
									if (symbole == EXPRESSION_SYMBOL.end)
										return true;
								}
								return false;
							}
						}
					}

				}
			}
		}

		return false;
	}

	private boolean isAction(String exp_string) {

		String action_pattern = "[\\^|\\(]*(\\w+)[\\*|\\)]*";

		Pattern pattern = Pattern.compile(action_pattern);
		Matcher matcher = pattern.matcher(exp_string);
		boolean is_matched = matcher.matches();

		return is_matched;
	}

	private List<Integer> stringExp2List(String exp_string) {

		int taille = exp_string.length();

		List<Integer> liste_exp = new ArrayList<Integer>();

		liste_exp.add(EXPRESSION_SYMBOL.begin);

		for (int i = 0; i < taille; i++) {

			char char_exp = exp_string.charAt(i);

			if (Character.isLetterOrDigit(char_exp) || char_exp == '_')
				liste_exp.add(EXPRESSION_SYMBOL.alpha);

			if (char_exp == '^')
				liste_exp.add(EXPRESSION_SYMBOL.binary_not);

			if (char_exp == '+')
				liste_exp.add(EXPRESSION_SYMBOL.union);

			if (char_exp == '.')
				liste_exp.add(EXPRESSION_SYMBOL.concatenation);

			if (char_exp == '*')
				liste_exp.add(EXPRESSION_SYMBOL.binary_star);

			if (char_exp == '(')
				liste_exp.add(EXPRESSION_SYMBOL.opening_parenthesis);

			if (char_exp == ')')
				liste_exp.add(EXPRESSION_SYMBOL.closing_parenthesis);
		}

		liste_exp.add(EXPRESSION_SYMBOL.end);

		return liste_exp;

	}

	public Expression getExpression() {

		Expression new_exp = new Expression();

		if (this.isAction(exp) == true) {

			new_exp.with_not = this.iswith_not(exp);

			new_exp.name_action = this.getNameAction(exp);

		} else {

			new_exp.with_not = this.iswith_not(exp);

			String exp_string = this.remove_not(exp);

			exp_string = this.remove_not(exp_string);
			exp_string = this.remove_parentese(exp_string);

			EXPRESSION_DEVIS exp_dev = this.split_expression(exp_string);

			String left_exp = exp_dev.left_exp;
			String right_exp = exp_dev.right_exp;
			new_exp.opperator = exp_dev.opperator;

			String2Expression new_left_exp = new String2Expression(left_exp);
			String2Expression new_right_exp = new String2Expression(right_exp);

			new_exp.right_exp = new_right_exp.getExpression();
			new_exp.left_exp = new_left_exp.getExpression();

		}

		return new_exp;
	}

	private String getNameAction(String exp_string) {

		boolean start_alpha = false;

		String name_action = "";

		int taille = exp_string.length();

		Character[] others = { '+', '.', '(', ')', '*', '^' };

		List<Character> l_others = Arrays.asList(others);

		for (int i = 0; i < taille; i++) {

			char char_exp = exp_string.charAt(i);

			if (l_others.contains(char_exp) == false) {
				name_action += char_exp;
				start_alpha = true;
			} else if (start_alpha) {
				return name_action;
			}

		}

		return name_action;
	}

	private boolean iswith_not(String exp_string) {

		exp_string = this.remove_parentese(exp_string);

		List<Integer> exp_list = this.stringExp2List(exp_string);

		int nb_element = exp_list.size();

		Integer symbole = exp_list.get(1);

		if (symbole != EXPRESSION_SYMBOL.binary_not && symbole != EXPRESSION_SYMBOL.opening_parenthesis) {

			return false;
		}

		if (symbole == EXPRESSION_SYMBOL.opening_parenthesis) {
			symbole = exp_list.get(nb_element - 2);

			if (symbole != EXPRESSION_SYMBOL.binary_star)
				return false;

			symbole = exp_list.get(3);
		} else {
			symbole = exp_list.get(2);
		}

		switch (symbole) {
		case EXPRESSION_SYMBOL.alpha:
			for (int i = 3; i < nb_element; i++) {
				symbole = exp_list.get(i);
				if (symbole != EXPRESSION_SYMBOL.alpha && symbole != EXPRESSION_SYMBOL.binary_star
						&& symbole != EXPRESSION_SYMBOL.end)
					return false;
			}
			return true;
		case EXPRESSION_SYMBOL.opening_parenthesis:

			List<Integer> parentese_ouvert = new ArrayList<Integer>();
			parentese_ouvert.add(2);

			for (int i = 3; i < nb_element; i++) {
				symbole = exp_list.get(i);

				if (symbole == EXPRESSION_SYMBOL.opening_parenthesis)
					parentese_ouvert.add(i);

				if (symbole == EXPRESSION_SYMBOL.closing_parenthesis) {
					if (parentese_ouvert.size() == 1) {
						symbole = exp_list.get(i + 1);

						if (symbole == EXPRESSION_SYMBOL.binary_star) {
							symbole = exp_list.get(i + 2);

							if (symbole == EXPRESSION_SYMBOL.end) {
								return true;
							}

							return false;

						} else if (symbole == EXPRESSION_SYMBOL.end) {
							return true;
						}

						return false;
					}
					parentese_ouvert.remove(parentese_ouvert.size() - 1);
				}

			}
			return false;
		}

		return false;
	}

	private static int opperatorToSymbol(char opperator) {

		if (opperator == '.')
			return EXPRESSION_SYMBOL.concatenation;

		if (opperator == '+')
			return EXPRESSION_SYMBOL.union;

		if (opperator == '*')
			return EXPRESSION_SYMBOL.binary_star;

		return 0;
	}

	public static String symbolToOpperator(int symbole) {

		if (symbole == EXPRESSION_SYMBOL.concatenation)
			return ".";
		if (symbole == EXPRESSION_SYMBOL.union)
			return "+";
		if (symbole == EXPRESSION_SYMBOL.binary_star)
			return "*";
		return "";
	}

}
