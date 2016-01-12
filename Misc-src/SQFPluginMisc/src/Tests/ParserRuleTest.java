package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import OutputParser.Grammar;
import OutputParser.ParserRule;

public class ParserRuleTest {
	
	@Test
	public void needsLeftFactoring_ITest() {
		ParserRule rule = new ParserRule("rule");
		
		rule.setAsAtomicRule(true);
		
		rule.addSyntax("Start Following");
		rule.addSyntax("Test test");
		
		assertFalse(rule.needsLeftFactoring_I());
		
		rule.addSyntax("Start Next");
		
		assertTrue(rule.needsLeftFactoring_I());
	}
	
	@Test
	public void removeRuleCallTest() {
		ParserRule rule = new ParserRule("rule");
		
		rule.setAsAtomicRule(true);
		
		rule.addSyntax("This is a test");
		
		rule.removeRuleCall("is");
		
		assertTrue(rule.isEmpty());
		
		rule.addSyntax("This (is | are) test");
		
		rule.removeRuleCall("are");
		
		assertEquals("This is test", rule.getRuleContent());
		
		ParserRule cRule = new ParserRule("rule");
		cRule.setAsAtomicRule(true);
		
		cRule.addSyntax("This (is | are) tist");
		
		cRule.removeRuleCall("is");
		
		assertEquals("This are tist", cRule.getRuleContent());
		
		ParserRule cRule2 = new ParserRule("rule");
		cRule2.setAsAtomicRule(true);
		
		cRule2.addSyntax("Miau this is");
		
		cRule2.removeRuleCall("is");
		
		assertTrue(cRule2.isEmpty());
	}
	
	@Test
	public void simplifyTest() {
		ParserRule rule1 = createBasicParserRule("Tester", 3, false);
		ParserRule rule2 = createBasicParserRule("Tester", 3, false);
		
		rule1.simplify();
		
		// shouldn't have changed
		assertEquals(rule2.toString(), rule1.toString());
		
		// optional call
		rule1.addSyntax("(test)? this is");
		rule2.addSyntax("this is");
		rule2.addSyntax("test this is");
		
		rule1.simplify();
		
		assertEquals(rule2.toString(), rule1.toString());
		
		// multi-ruleCall
		rule1.addSyntax("(test)* this is");
		rule2.addSyntax("this is");
		rule2.addSyntax("(test)+ this is");
		
		rule1.simplify();
		
		assertEquals(rule2.toString(), rule1.toString());
		
		ParserRule rule3 = createBasicParserRule("Tester", 3, false);
		ParserRule rule4 = createBasicParserRule("Tester", 3, false);
		
		rule3.addSyntax("(test)? this is");
		rule3.addSyntax("(miau)* that can be");
		
		rule4.addSyntax("this is");
		rule4.addSyntax("test this is");
		rule4.addSyntax("that can be");
		rule4.addSyntax("(miau)+ that can be");
		
		rule3.simplify();
		
		assertEquals(rule4.toString(), rule3.toString());
		
		ParserRule rule5 = createBasicParserRule("Tester", 3, false);
		ParserRule rule6 = createBasicParserRule("Tester", 3, false);
		
		rule5.addSyntax("(test)?");
		rule5.addSyntax("(test2)*");
		rule6.addSyntax("(test)?");
		rule6.addSyntax("(test2)*");
		
		rule5.simplify();
		
		// shouldn't have changed anything because it can't be simplified
		
		assertEquals(rule6.toString(), rule5.toString());
		
		ParserRule rule7 = createBasicParserRule("Tester", 3, false);
		ParserRule rule8 = createBasicParserRule("Tester", 3, false);
		
		rule7.addSyntax("(test1 | test2) this is");
		rule8.addSyntax("test1 this is");
		rule8.addSyntax("test2 this is");
		
		rule7.simplify();
		
		assertEquals(rule8.toString(), rule7.toString());
		
		ParserRule rule9 = createBasicParserRule("Tester", 3, false);
		ParserRule rule10 = rule9.copy();
		
		rule9.addSyntax("(Drei | Vier)");
		rule10.addSyntax("Drei");
		rule10.addSyntax("Vier");
		
		rule9.simplify();
		
		assertEquals(rule10.toString(), rule9.toString());
		
		ParserRule rule11 = createBasicParserRule("Tester", 3, false);
		ParserRule rule12 = rule11.copy();
		
		rule11.addSyntax("(Drei | Vier)+");
		rule12.addSyntax("(Drei)+");
		rule12.addSyntax("(Vier)+");
		
		rule11.simplify();
		
		assertEquals(rule12.toString(), rule11.toString());
		
		ParserRule rule13 = createBasicParserRule("Tester", 3, false);
		ParserRule rule14 = rule13.copy();
		
		rule13.addSyntax("(Drei | Vier)+ Test");
		rule14.addSyntax("(Drei)+ Test");
		rule14.addSyntax("(Vier)+ Test");
		
		rule13.simplify();
		
		assertEquals(rule14.toString(), rule13.toString());
		
		ParserRule rule15 = createBasicParserRule("Tester", 3, false);
		ParserRule rule16 = rule15.copy();
		
		rule15.addSyntax("(Drei | Vier)* Test");
		rule16.addSyntax("Test");
		rule16.addSyntax("(Drei)+ Test");
		rule16.addSyntax("(Vier)+ Test");
		
		rule15.simplify();
		
		assertEquals(rule16.toString(), rule15.toString());
	}
	
	@Test
	public void setLineTest() {
		ParserRule solution1 = new ParserRule("Test1");
		solution1.setAsAtomicRule(true);
		solution1.addSyntax("Mama");
		solution1.addSyntax("Oma");
		solution1.addSyntax("Kind");
		
		ParserRule test1 = new ParserRule("Test1");
		test1.setAsAtomicRule(true);
		test1.addSyntax("Mama");
		test1.addSyntax("Papa");
		test1.addSyntax("Kind");
		
		test1.setLine(1, "Oma");
		
		assertEquals(solution1.toString(), test1.toString());
	}
	
	@Test
	public void removeLineTest() {
		ParserRule test1 = new ParserRule("Rule1");
		test1.setAsAtomicRule(true);
		test1.addSyntax("Eins");
		test1.addSyntax("Zwei");
		test1.addSyntax("Drei");
		test1.addSyntax("Vier");
		
		ParserRule test2 = test1.copy();
		ParserRule test3 = test1.copy();
		
		ParserRule solution1 = new ParserRule("Rule1");
		solution1.setAsAtomicRule(true);
		solution1.addSyntax("Eins");
		solution1.addSyntax("Zwei");
		solution1.addSyntax("Vier");
		
		test1.removeLine(2);
		
		assertEquals(solution1.toString(), test1.toString());
		
		ParserRule solution2 = new ParserRule("Rule1");
		solution2.setAsAtomicRule(true);
		solution2.addSyntax("Eins");
		solution2.addSyntax("Zwei");
		solution2.addSyntax("Drei");
		
		test2.removeLine(3);
		
		assertEquals(solution2.toString(), test2.toString());
		
		ParserRule solution3 = new ParserRule("Rule1");
		solution3.setAsAtomicRule(true);
		solution3.addSyntax("Zwei");
		solution3.addSyntax("Drei");
		solution3.addSyntax("Vier");
		
		test3.removeLine(0);
		
		assertEquals(solution3.toString(), test3.toString());
	}
	
	@Test
	public void removeLinesTest() {
		ParserRule rule1 = new ParserRule("Rule");
		rule1.setAsAtomicRule(true);
		rule1.addSyntax("Eins");
		rule1.addSyntax("Zwei");
		rule1.addSyntax("Drei");
		rule1.addSyntax("Vier");
		rule1.addSyntax("F�nf");
		
		ParserRule rule2 = rule1.copy();
		
		ParserRule solution1 = new ParserRule("Rule");
		solution1.setAsAtomicRule(true);
		solution1.addSyntax("Eins");
		solution1.addSyntax("Drei");
		solution1.addSyntax("F�nf");
		
		ArrayList<Integer> indexes1 = new ArrayList<Integer>();
		indexes1.add(1);
		indexes1.add(3);
		
		rule1.removeLines(indexes1);
		
		assertEquals(solution1.toString(), rule1.toString());
		
		ParserRule solution2 = new ParserRule("Rule");
		solution2.setAsAtomicRule(true);
		solution2.addSyntax("Zwei");
		solution2.addSyntax("Vier");
		
		ArrayList<Integer> indexes2 = new ArrayList<Integer>();
		indexes2.add(0);
		indexes2.add(4);
		indexes2.add(2);
		
		rule2.removeLines(indexes2);
		
		assertEquals(solution2.toString(), rule2.toString());
	}
	
	@Test
	public void removeStartRuleCallTest() {
		ParserRule rule1 = new ParserRule("Rule");
		rule1.setAsAtomicRule(true);
		rule1.addSyntax("Eins");
		rule1.addSyntax("Zwei");
		rule1.addSyntax("Drei");
		rule1.addSyntax("Vier");
		rule1.addSyntax("Drei");
		
		ParserRule solution = new ParserRule("Rule");
		solution.setAsAtomicRule(true);
		solution.addSyntax("Eins");
		solution.addSyntax("Zwei");
		solution.addSyntax("Vier");
		
		rule1.removeStartRuleCall("Drei");
		
		assertEquals(solution.toString(), rule1.toString());
		
		ParserRule rule2 = new ParserRule("Rule");
		rule2.setAsAtomicRule(true);
		rule2.addSyntax("Eins");
		rule2.addSyntax("Zwei");
		rule2.addSyntax("Drei");
		rule2.addSyntax("(Drei | Vier)");
		rule2.addSyntax("Drei");
		
		rule2.simplify();
		rule2.removeStartRuleCall("Drei");
		
		assertEquals(solution.toString(), rule2.toString());
	}
	
	@Test
	public void equalsTest() {
		ParserRule rule1 = createBasicParserRule();
		ParserRule rule2 = createBasicParserRule();
		
		assertTrue(rule1.equals(rule2));
		
		rule2.setName("Miauuuuuuu");
		
		assertFalse(rule1.equals(rule2));
		
		rule1.setName("Miauuuuuuu");
		
		assertTrue(rule1.equals(rule2));
	}
	
	@Test
	public void mergeWithTest() {
		ParserRule rule1 = createBasicParserRule();
		ParserRule rule2 = createBasicParserRule();
		ParserRule compare1 = rule1.copy();
		
		rule1.mergeWith(rule2);
		
		assertTrue(rule1.equals(compare1));
		
		rule2.addSyntax("DaTest");
		
		rule1.mergeWith(rule2);
		
		compare1.addLineToRuleContent("DaTest");
		
		assertTrue(rule1.equals(compare1));
	}
	
	@Test
	public void getCommonStartRulesTest() {
		Grammar g = new Grammar();
		
		ParserRule rule1 = new ParserRule("Dummy");
		rule1.setAsAtomicRule(true);
		
		ParserRule d1 = new ParserRule("Mama");
		ParserRule d2 = new ParserRule("Papa");
		ParserRule d3 = new ParserRule("Grandma");
		
		d3.setAsAtomicRule(true);
		d3.addSyntax("Dummy");
		
		g.addRule(d1);
		g.addRule(d2);
		g.addRule(d3);
		
		ParserRule rule2 = rule1.copy();
		
		rule1.addSyntax("Mama is cool");
		rule1.addSyntax("Papa is cool");
		rule1.addSyntax("Grandma is cool");
		
		g.addRule(rule1);
		g.addRule(rule2);
		
		g.createStartRuleForecasts();
		
		assertTrue(ParserRule.getCommonStartRules(rule1, rule2).isEmpty());
		
		rule2.addSyntax("Papa is wow");
		
		ArrayList<String> solution = new ArrayList<String>();
		solution.add("Papa");
		
		g.createStartRuleForecasts();
		
		assertEquals(solution, ParserRule.getCommonStartRules(rule1, rule2));
		
		rule2.addSyntax("Mama wow is");
		
		g.createStartRuleForecasts();
		
		solution.clear();
		solution.add("Mama");
		solution.add("Papa");
		
		assertEquals(solution, ParserRule.getCommonStartRules(rule1, rule2));
		
		rule2.addSyntax("Grandma bakes cakes");
		
		solution.clear();
		solution.add("Mama");
		solution.add("Papa");
		solution.add("Grandma");
		solution.add("Dummy");

		g.createStartRuleForecasts();
		
		assertEquals(solution, ParserRule.getCommonStartRules(rule1, rule2));
	}
	
	// //////////////////////////////////////////MISC///////////////////////////////////////////////////////
	
	/**
	 * Creates a basic ParserRule with some predefined content
	 * 
	 * @param name
	 *            The name of the rule
	 * @param alternatives
	 *            The amount of alternatives this ruleshould have
	 * @param choice
	 *            Whether or not the rule should contain ParserChoice within an alternative
	 * @return The generated ParserRule
	 */
	public static ParserRule createBasicParserRule(String name, int alternatives, boolean choice) {
		ParserRule newRule = new ParserRule(name);
		
		newRule.setAsAtomicRule(true);
		
		for (int i = 0; i < (alternatives - 1); i++) {
			newRule.addSyntax("Alternative" + i + " this is");
		}
		
		if (choice) {
			newRule.addSyntax("(Alternative" + (alternatives - 1) + " | Alternative" + alternatives
					+ ") this is");
		} else {
			newRule.addSyntax("Alternative" + (alternatives - 1) + " this is");
		}
		
		newRule.format();
		
		return newRule;
	}
	
	/**
	 * Creates a basic ParserRule with some predefined content and a ParserChoice
	 * 
	 * @param name
	 *            The name of the rule
	 * @param alternatives
	 *            The amount of alternatives this ruleshould have
	 * @return The generated ParserRule
	 */
	public static ParserRule createBasicParserRule(String name, int alternatives) {
		return createBasicParserRule(name, alternatives, true);
	}
	
	/**
	 * Creates a basic ParserRule with some predefined content of 3 lines and a ParserChoice
	 * 
	 * @param name
	 *            The name of the rule
	 * @return The generated ParserRule
	 */
	public static ParserRule createBasicParserRule(String name) {
		return createBasicParserRule(name, 3, true);
	}
	
	/**
	 * Creates a basic ParserRule (name = "Dummy") with some predefined content of 3 lines and a
	 * ParserChoice
	 * 
	 * @return The generated ParserRule
	 */
	public static ParserRule createBasicParserRule() {
		return createBasicParserRule("Dummy", 3, true);
	}
	
}