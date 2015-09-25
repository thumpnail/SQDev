package OutputParser;

import java.util.ArrayList;

import SQF.Functions;

public class Grammar {
	private String header;
	private ArrayList<ParserRule> ruleList;

	public Grammar() {
		setRules(new ArrayList<ParserRule>());
		setHeader("");
	}

	public Grammar(String header) {
		setRules(new ArrayList<ParserRule>());
		setHeader(header);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public ParserRule getRule(String name) {
		for (ParserRule currentRule : ruleList) {
			if (currentRule.getName().toLowerCase().equals(name.toLowerCase())) {
				return currentRule;
			}
		}

		System.err.println("Couldn't find rule with name '" + name + "'");
		return null;
	}

	public ParserRule getRule(int index) {
		return this.ruleList.get(index);
	}

	/**
	 * Finds out the index of the rule with the given name within this grammar. <br>
	 * Returns -1 if rule hasn't been found.
	 * 
	 * @param ruleName
	 *            The name of the wanted rule
	 * @return
	 */
	public int getRuleIndex(String ruleName) {
		for (int i = 0; i < this.getRuleCount(); i++) {
			ParserRule currentRule = this.getRule(i);

			if (currentRule.getName().toLowerCase()
					.equals(ruleName.toLowerCase())) {
				return i;
			}
		}

		System.err.println("Rule '" + ruleName + "' not found!");
		return -1;
	}

	public ArrayList<ParserRule> getRules() {
		ArrayList<ParserRule> copy = new ArrayList<ParserRule>(ruleList);
		return copy;
	}

	/**
	 * Sets the rule at the given position
	 * 
	 * @param rule
	 * @param index
	 */
	public void setRule(ParserRule rule, int index) {
		this.ruleList.set(index, rule);
	}

	/**
	 * @return The names of all rules the grammar contains
	 */
	public String[] getRuleNames() {
		String[] names = new String[ruleList.size()];

		for (int i = 0; i < ruleList.size(); i++) {
			ParserRule currentRule = ruleList.get(i);
			names[i] = currentRule.getName();
		}

		return names;
	}

	public int getRuleCount() {
		return this.ruleList.size();
	}

	public void setRules(ArrayList<ParserRule> rules) {
		this.ruleList = rules;
	}

	public void addRule(ParserRule rule) {
		ruleList.add(rule);
	}

	public String toString() {
		String grammarContent = this.getHeader();
		if (!grammarContent.endsWith("\n")) {
			// make sure there is a newline after the header
			grammarContent += "\n";
		}

		for (ParserRule currentRule : this.ruleList) {
			grammarContent += currentRule.toString();
		}

		return grammarContent;
	}

	/**
	 * Finds out if this grammar contains a rule with the given name
	 * 
	 * @param name
	 * @return
	 */
	public boolean containsRule(String name) {
		String[] names = this.getRuleNames();

		return Functions.isIn(names, name, true);
	}

	public void createAssignments() {
		for (ParserRule currentRule : ruleList) {
			currentRule.createAssignments();
		}
	}

	/**
	 * @return True if the grammar contains left-recursive ruleCalls
	 */
	public boolean isLeftRecursive() {
		boolean isLeftRecursive = false;

		for (ParserRule currentRule : this.getRules()) {
			if (currentRule.getReachableRules().contains(currentRule.getName())) {
				isLeftRecursive = true;
				break;
			}
		}

		return isLeftRecursive;
	}

	/**
	 * Checks if the given ruleContent contains a left-recursive ruleCall
	 * according to the given ruleNames
	 * 
	 * @param ruleContent
	 *            The content to be checked
	 * @param ruleNames
	 *            The names of all rules the method should check for
	 * @return
	 */
	public boolean containsLeftRecursion(String ruleContent,
			ArrayList<String> ruleNames, String currentRuleName) {

		String firstElement = getFirstElement(ruleContent);
		if (firstElement.endsWith("|")) {
			firstElement = firstElement.substring(0, firstElement.length() - 1);
		}
		firstElement = ParserRule.cleanString(firstElement);

		if (ruleNames.contains(firstElement)) {
			// check if the called rule itself can start with a rule call to
			// this rule again
			ParserRule toCheck = this.getRule(firstElement);
			if (toCheck.containsStartRuleCallOf(currentRuleName)) {
				return true;
			}
		} else {
			// if first Element is surrounded by brackets then check the bracket
			// content
			if (firstElement.contains("(") || firstElement.contains(")")) {
				// remove brackets and special characters
				firstElement = firstElement.replace("(", " ");
				firstElement = firstElement.replace(")", " ");
				firstElement = firstElement.replace("*", " ");
				firstElement = firstElement.replace("?", " ");
				firstElement = firstElement.replace("|", " ");
				firstElement = ParserRule.cleanString(firstElement);
				firstElement = Functions.reduceSpaceBetween(firstElement);

				String[] elements = Functions.getElements(firstElement);

				for (String currentFirstElement : elements) {
					if (ruleNames.contains(currentFirstElement)) {
						// check if the called rule itself can start with a rule
						// call to this rule again
						ParserRule toCheck = this.getRule(firstElement);
						if (toCheck.containsStartRuleCallOf(currentRuleName)) {
							return true;
						}
					}
				}
			}
		}

		while (ruleContent.contains("\t|")) {
			ruleContent = ruleContent.substring(ruleContent.indexOf("\t|") + 2);
			ruleContent = ParserRule.cleanString(ruleContent);
			firstElement = getFirstElement(ruleContent);
			if (firstElement.endsWith("|")) {
				firstElement = firstElement.substring(0,
						firstElement.length() - 1);
			}
			firstElement = ParserRule.cleanString(firstElement);

			if (ruleNames.contains(firstElement)) {
				// check if the called rule itself can start with a rule call to
				// this rule again
				ParserRule toCheck = this.getRule(firstElement);
				if (toCheck.containsStartRuleCallOf(currentRuleName)) {
					return true;
				} else {
					ParserRule currentRule = this.getRule(currentRuleName);
					if (!currentRule.getBaseRuleContent().equalsIgnoreCase(
							ruleContent)) {
						// we are in the atmomic rule
						String atomicName = currentRuleName + "Atomic";
						if (toCheck.containsStartRuleCallOf(atomicName)) {
							return true;
						}
					}
				}
			} else {
				// if first Element is surrounded by brackets thencheck the
				// bracket content
				if (firstElement.contains("(") || firstElement.contains(")")) {
					// remove brackets and special characters
					firstElement = firstElement.replace("(", " ");
					firstElement = firstElement.replace(")", " ");
					firstElement = firstElement.replace("*", " ");
					firstElement = firstElement.replace("?", " ");
					firstElement = firstElement.replace("|", " ");
					firstElement = ParserRule.cleanString(firstElement);
					firstElement = Functions.reduceSpaceBetween(firstElement);

					String[] elements = Functions.getElements(firstElement);

					for (String currentFirstElement : elements) {
						if (ruleNames.contains(currentFirstElement)) {
							// check if the called rule itself can start with a
							// rule call to this rule again
							ParserRule toCheck = this
									.getRule(currentFirstElement);
							if (toCheck
									.containsStartRuleCallOf(currentRuleName)) {
								return true;
							}
						}
					}
				}
			}
		}

		// if previous didn't match then there is no left-recursion
		return false;
	}

	public void removeLeftRecursion() {
		for (ParserRule currentRule : this.getRules()) {
			if (currentRule.isLeftRecursive()) {
				// process every left-recursive rule
				for (String currentReachableRuleName : currentRule
						.getReachableRules()) {
					// process every reachable rule
					boolean onlyAtomic = false;

					if (currentReachableRuleName.contains("Atomic")) {
						// get name of the mainRule
						currentReachableRuleName = currentReachableRuleName
								.substring(0, currentReachableRuleName
										.indexOf("Atomic"));
						// indicate that only the atomic content is relevant
						onlyAtomic = true;
					}

					// get target rule
					ParserRule targetRule = this
							.getRule(currentReachableRuleName);

					// find out whether or not this rule is guilty about the
					// left-recursion of the currentlyprocessed rule
					boolean isGuilty = false;

					if (onlyAtomic) {
						isGuilty = targetRule.containsStartRuleCallOf(
								currentRule.getName(), false);
					} else {
						isGuilty = targetRule
								.containsStartRuleCallOf(currentRule.getName());
					}

					if (isGuilty) {
						// TODO: implement left-factoring
						ParserRule ruleA;
						ParserRule ruleB;

						if (currentRule
								.howManyStartRuleCallsOf(currentReachableRuleName) >= targetRule
								.howManyStartRuleCallsOf(currentRule.getName())) {
							// if there are more ruleCalls in currentRule
							ruleA = currentRule;
							ruleB = targetRule;
						} else {
							//if there are more ruleCalls in targetRule
							ruleA = targetRule;
							ruleB = currentRule;
						}
						
						//remove parseAlternatives to the BaseRule in ruleA
						//change ruleCall to atomic in ruleB
						
						//process ruleA
						String ruleAAtomicContent = ruleA.getAtomicRuleContent();
						String newAtomicRuleContent = "";
						
						while(!ruleAAtomicContent.isEmpty()) {
							int index = ruleAAtomicContent.indexOf("\n");
							
							if(index < 0) {
								//if there is only one more line left without newLine
								index = ruleAAtomicContent.length();
							}
							
							String currentLine = ruleAAtomicContent.substring(0, index);
							currentLine = ParserRule.cleanString(currentLine);
							
							//remove processed line
							ruleAAtomicContent = ruleAAtomicContent.substring(index);
							ruleAAtomicContent = ParserRule.cleanString(ruleAAtomicContent);
							if(ruleAAtomicContent.startsWith("|")) {
								ruleAAtomicContent = ruleAAtomicContent.substring(1);
								ruleAAtomicContent = ParserRule.cleanString(ruleAAtomicContent);
							}
							
							String firstElement = getFirstElement(currentLine);
							
							if(Functions.startsWithLetter(firstElement)) {
								//single ruleCall
								if(firstElement.equals(ruleB.getName())) {
									//left-factor -> move line to BaseRule
									String newBaseRuleContent = ruleA.getBaseRuleContent();
									newBaseRuleContent += "\n| " + currentLine;
									
									ruleA.setBaseRuleContent(newBaseRuleContent);
								}else {
									//this line stays in the AtomicContent
									newAtomicRuleContent += "\n| " + currentLine;
								}
							}else {
								//possible multiRuleCall or optional parameter
								
								if(!firstElement.contains(ruleB.getName())) {
									//move on if the searched ruleCall is not in there
									newAtomicRuleContent += "\n| " + currentLine;
									continue;
								}
								
								if(firstElement.charAt(0) != '(') {
									//it's not a multiRuleCall
									if(currentLine.contains("<Injected>")) {
										//it is a user-injected line
										System.out.println("Couldn't process user-injected line '" 
												+ currentLine + "' in Grammar.removeLeftRecursion in ruleA");
									}else {
										System.err.println("Couldn't process line '"
												+ currentLine + "' in Grammar.removeLeftRecursion in ruleA");
									}
									//don't process line but keep it where it was
									newAtomicRuleContent += "\n| " + currentLine;
								}else {
									//differ multiRuleCall - optional parameter
									
									if(currentLine.startsWith(firstElement + "?") && !firstElement.contains("|")) {
										//process optional parameter
										int startIndex = firstElement.length() + 1; 
										currentLine = currentLine.substring(startIndex); //remove  optional parameter
										
										firstElement = firstElement.substring(1, startIndex - 2); //remove brackets
										
										//move optional alternative to the BaseContent
										String newBaseRuleContent = ruleA.getBaseRuleContent();
										newBaseRuleContent += "\n| " + firstElement + currentLine;
										
										ruleA.setBaseRuleContent(newBaseRuleContent);
										
										currentLine = ParserRule.cleanString(currentLine);
										
										//let fix part (without optional rule call) in the atomicContent
										newAtomicRuleContent += "\n| " + currentLine;
									} else {
										//process multi-ruleCalls
										
										//remove firstElement from currentLine
										currentLine = currentLine.substring(currentLine.indexOf(")") + 1);
										
										//remove brackets and clean up
										firstElement = firstElement.substring(1, firstElement.length() - 1);
										firstElement = firstElement.replace("|", " ");
										firstElement = Functions.reduceSpaceBetween(firstElement);
										
										String[] aElements = Functions.getElements(firstElement);
										
										//reset first element
										firstElement = "";
										
										String newAtomicStartRuleCall = "";
										
										//sort startRuleCall possibilities in atomic and base
										for(String currentElement : aElements) {
											if(currentElement.equals(ruleB.getName())) {
												if(firstElement.isEmpty()) {
													firstElement = currentElement;
												}else {
													//shouldn't be more than one element
													System.err.println("Multiple alternatives in ruleA in Grammar.removeLeftRecursion");
												}
											}else {
												newAtomicStartRuleCall += " | " + currentElement;
											}
										}
										
										if(newAtomicStartRuleCall.startsWith(" | ")) {
											//get rid of "|" and startSpaces
											newAtomicStartRuleCall = newAtomicStartRuleCall.substring(3);
										}
										
										if(newAtomicStartRuleCall.contains("|")) {
											//add brackets again because there are still multiple alternatives
											newAtomicStartRuleCall = "(" + newAtomicStartRuleCall+ ")";
										}
										
										//assemble and add line to the new atomicContent
										newAtomicRuleContent += "\n| " + newAtomicStartRuleCall + currentLine;
										
										if(currentLine.startsWith("?")) {
											//if it's an optional parameter then add brackets as well
											firstElement = "(" + firstElement + ")";
										}
										
										String newBaseRuleContent = ruleA.getBaseRuleContent();
										newBaseRuleContent += "\n| " + firstElement + currentLine;
										ruleA.setBaseRuleContent(newBaseRuleContent);
									}
								}
							}
						}
						
						newAtomicRuleContent = ParserRule.cleanString(newAtomicRuleContent);
						if(newAtomicRuleContent.startsWith("|")) {
							newAtomicRuleContent = newAtomicRuleContent.substring(1);
							newAtomicRuleContent = ParserRule.cleanString(newAtomicRuleContent);
						}
						ruleA.setAtomicRuleContent(newAtomicRuleContent);
						
						
						//process ruleB
						String ruleBAtomicContent = ruleB.getAtomicRuleContent();
						newAtomicRuleContent = ""; //reset
						
						while(!ruleBAtomicContent.isEmpty()) {
							int index = ruleBAtomicContent.indexOf("\n");
							
							if(index < 0) {
								index =  ruleBAtomicContent.length();
							}
							
							//get current line
							String currentLine = ruleBAtomicContent.substring(0, index);
							currentLine = ParserRule.cleanString(currentLine);
							
							//remove line from ruleBAtomicContent
							ruleBAtomicContent = ruleBAtomicContent.substring(index);
							ruleBAtomicContent = ParserRule.cleanString(ruleBAtomicContent);
							if(ruleBAtomicContent.startsWith("|")) {
								ruleBAtomicContent = ruleBAtomicContent.substring(1);
								ruleBAtomicContent = ParserRule.cleanString(ruleBAtomicContent);
							}
							
							//get first element
							String firstElement = getFirstElement(currentLine);
							
							if(Functions.startsWithLetter(firstElement)) {
								//single rule call
								if(firstElement.equals(ruleA.getName())) {
									//change firstElement to an atomicRuleCall and replace old ruleCall
									firstElement += "Atomic";
									currentLine = firstElement + currentLine.substring(currentLine.indexOf(" "));
									newAtomicRuleContent += "\n| " + currentLine;
								}else {
									//just keep line where it was
									newAtomicRuleContent += "\n| " + currentLine;
								}
							}else {
								//multi rule call or optional parameter
								
								if(!firstElement.contains(ruleA.getName())) {
									//move on if the searched ruleCall is not in there
									newAtomicRuleContent += "\n| " + currentLine;
									continue;
								}
								
								if(firstElement.charAt(0) != '(') {
									//it's not a multiRuleCall
									if(currentLine.contains("<Injected>")) {
										//it is a user-injected line
										System.out.println("Couldn't process user-injected line '" 
												+ currentLine + "' in Grammar.removeLeftRecursion in ruleB");
									}else {
										System.err.println("Couldn't process line '"
												+ currentLine + "' in Grammar.removeLeftRecursion in ruleB");
									}
									//don't process line but keep it where it was
									newAtomicRuleContent += "\n| " + currentLine;
								}else {
									if(currentLine.startsWith(firstElement + "?") && !firstElement.contains("|")) {
										//remove brackets
										firstElement = firstElement.substring(1, firstElement.length() - 1);
										
										firstElement = ParserRule.cleanString(firstElement);
										
										//change to atomicRuleCall and replace
										firstElement += "Atomic";
										firstElement = "(" + firstElement + ")"; //add brackets again
										
										currentLine = firstElement + currentLine.substring(currentLine.indexOf("?"));
										
										newAtomicRuleContent += "\n| " + currentLine;
									}else {
										//remove brackets
										firstElement = firstElement.substring(1, firstElement.length() - 1);
										
										firstElement = ParserRule.cleanString(firstElement);
										firstElement = firstElement.replace("|", " ");
										firstElement = Functions.reduceSpaceBetween(firstElement);
										
										String[] aElements = Functions.getElements(firstElement);
										firstElement = ""; //reset
										
										for(String currentElement : aElements) {
											if(currentElement.equals(ruleA.getName())) {
												currentElement += "Atomic";
											}
											
											firstElement += " | " + currentElement;
										}
										
										if(firstElement.startsWith(" | ")) {
											firstElement = firstElement.substring(3);
										}
										//format and add brackets again
										firstElement = "(" + ParserRule.cleanString(firstElement) + ")";
										
										//replace in currentLine
										currentLine = firstElement + currentLine.substring(currentLine.indexOf(")") + 1);
										
										newAtomicRuleContent += "\n| " + currentLine;
									}
								}
							}
						}
						
						newAtomicRuleContent = ParserRule.cleanString(newAtomicRuleContent);
						if(newAtomicRuleContent.startsWith("|")) {
							newAtomicRuleContent = newAtomicRuleContent.substring(1);
							newAtomicRuleContent = ParserRule.cleanString(newAtomicRuleContent);
						}
						ruleB.setAtomicRuleContent(newAtomicRuleContent);
					}
				}
				//update reachableRules
				this.createStartRuleForecasts();
			}
		}
	}

	public void removeLeftRecursion_FIRSTATTEMPT() {
		String[] ruleNames = this.getRuleNames();

		for (int i = 0; i < this.getRuleCount(); i++) {
			ParserRule currentRule = this.getRule(i);

			currentRule.setBaseRuleContent(removeLeftRecursion_FIRSTATTEMPT(
					currentRule.getBaseRuleContent(), ruleNames, this,
					currentRule));
			currentRule.setAtomicRuleContent(removeLeftRecursion_FIRSTATTEMPT(
					currentRule.getAtomicRuleContent(), ruleNames, this,
					currentRule));

			this.setRule(currentRule, i);
		}
	}

	public static String removeLeftRecursion_FIRSTATTEMPT(String ruleContent,
			String[] ruleNames, Grammar grammar, ParserRule currentRule) {
		/*
		 * TODO: process each line of the string and check whether or not it's
		 * left recursive and fix that -> consider left-recursion due to
		 * multiple rule calls
		 */
		boolean currentIsAtomic = !currentRule.getBaseRuleContent()
				.equalsIgnoreCase(ruleContent);
		int lineNumber = 0;
		String contentCopy = ruleContent;
		String currentRuleName = currentRule.getName();

		if (!contentCopy.contains("\n")) {
			// make sure string is processed at least once
			contentCopy += "\n";
		}

		while (contentCopy.contains("\n")) {
			// process each line
			lineNumber++; // remember which line is currently processed

			String firstElement = getFirstElement(contentCopy);
			if (firstElement.endsWith("|")) {
				firstElement = firstElement.substring(0,
						firstElement.length() - 1);
			}

			firstElement = ParserRule.cleanString(firstElement);

			boolean canBeLeftRecursive = Functions.isIn(ruleNames,
					firstElement, true);
			boolean isLeftRecursive = false;
			boolean isSurroundedByBrackets = false;

			if (!canBeLeftRecursive && firstElement.startsWith("(")) {
				isSurroundedByBrackets = true;

				// remove brackets and special characters
				firstElement = firstElement.replace("(", " ");
				firstElement = firstElement.replace(")", " ");
				firstElement = firstElement.replace("*", " ");
				firstElement = firstElement.replace("?", " ");
				firstElement = firstElement.replace("|", " ");
				firstElement = ParserRule.cleanString(firstElement);
				firstElement = Functions.reduceSpaceBetween(firstElement);

				String[] aElements = Functions.getElements(firstElement);

				for (int i = 0; i < aElements.length; i++) {
					firstElement = aElements[i];

					canBeLeftRecursive = Functions.isIn(ruleNames,
							firstElement, true);

					if (canBeLeftRecursive) {
						if (grammar.getRule(firstElement)
								.containsStartRuleCallOf(currentRuleName)) {
							break;
						}
					}
				}
			}

			if (canBeLeftRecursive) {
				isLeftRecursive = grammar.getRule(firstElement)
						.containsStartRuleCallOf(currentRuleName);

				if (!isLeftRecursive && currentIsAtomic) {
					isLeftRecursive = grammar
							.getRule(firstElement)
							.containsStartRuleCallOf(currentRuleName + "Atomic");
				}
			}

			if (isLeftRecursive) {
				ParserRule targetRule = grammar.getRule(firstElement);

				if (isSurroundedByBrackets) {
					// TODO: implement
				} else { // TODO: get rid of else?
					if (targetRule.containsStartRuleCallOf(currentRuleName
							+ "Atomic", true, false)) {
						// if target rule has already been left-factored

						firstElement += "Atomic";

						contentCopy = firstElement
								+ contentCopy.substring(contentCopy
										.indexOf(" "));
					} else {
						firstElement += "Atomic";
						// get line that has to be moved to the baseRule
						String currentLine = contentCopy.substring(0,
								contentCopy.indexOf("\n"));
						if (currentLine.contains(" ")) {
							currentLine = firstElement
									+ currentLine.substring(currentLine
											.indexOf(" "));
						} else {
							currentLine = firstElement;
						}
						// remove the line from current ruleContent
						contentCopy = contentCopy.substring(contentCopy
								.indexOf("\n") + 1);
						contentCopy = ParserRule.cleanString(contentCopy);
						if (contentCopy.startsWith("|")) {
							contentCopy = contentCopy.substring(1);
							contentCopy = ParserRule.cleanString(contentCopy);
						}

						// add the respective line to the baseRuleContent
						String baseRuleContent = currentRule
								.getBaseRuleContent();
						baseRuleContent += "\n| " + currentLine;
						currentRule.setBaseRuleContent(baseRuleContent);

						// grammar.setRule(currentRule,
						// grammar.getRuleIndex(currentRuleName));
					}
				}

				String newRuleContent = "";

				for (int k = 1; k < lineNumber; k++) {
					newRuleContent += ruleContent.substring(0,
							ruleContent.indexOf("\n") + 1);
					ruleContent = ruleContent.substring(ruleContent
							.indexOf("\n") + 1);
					ruleContent = ParserRule.cleanString(ruleContent);
				}

				newRuleContent += "| " + contentCopy;

				ruleContent = newRuleContent;
			}

			contentCopy = contentCopy.substring(contentCopy.indexOf("\n") + 1);

			contentCopy = ParserRule.cleanString(contentCopy);

			if (contentCopy.startsWith("|")) {
				contentCopy = contentCopy.substring(1);
				contentCopy = ParserRule.cleanString(contentCopy);
			}
		}

		return ruleContent;
	}

	public void createStartRuleForecasts() {
		for (ParserRule currentRule : this.getRules()) {
			// create first layer
			currentRule.setReachableRules(currentRule.getStartRuleCalls());
		}

		boolean proceed = true;

		while (proceed) {
			ArrayList<ArrayList<String>> previousReachableRules = new ArrayList<ArrayList<String>>();
			for (ParserRule currentRule : this.getRules()) {
				ArrayList<String> reachableRules = currentRule
						.getReachableRules();
				previousReachableRules.add(reachableRules); // store previous
															// list for
															// comparison

				for (String currentReachableRuleName : reachableRules) {

					ParserRule currentReachableRule;
					boolean onlyAtomic = false;

					// find referenced rule in this grammar
					if (this.containsRule(currentReachableRuleName)) {
						currentReachableRule = this
								.getRule(currentReachableRuleName);
					} else {
						if (currentReachableRuleName.contains("Atomic")) {
							currentReachableRuleName = currentReachableRuleName
									.substring(0, currentReachableRuleName
											.indexOf("Atomic"));

							if (this.containsRule(currentReachableRuleName)) {
								currentReachableRule = this
										.getRule(currentReachableRuleName);
								onlyAtomic = true; // indicate that only the
													// atomicRule is referenced
							} else {
								System.err
										.println("Couldn't find reference to rule '"
												+ currentReachableRuleName
												+ "'");
								return;
							}
						} else {
							System.err
									.println("Couldn't find reference to rule '"
											+ currentReachableRuleName + "'");
							return;
						}
					}

					// add reachable rules to the origin rule
					if (onlyAtomic) {
						// if only atomicRule was referenced then only add
						// startingRuleCalls from the atomicRule
						currentRule.addReachableRules(currentReachableRule
								.getStartRuleCalls(false));
					} else {
						currentRule.addReachableRules(currentReachableRule
								.getReachableRules());
					}
				}
			}

			proceed = false; // reset proceed

			for (int k = 0; k < this.getRuleCount(); k++) {
				ParserRule currentRule = this.getRule(k);

				if (!previousReachableRules.get(k).equals(
						currentRule.getReachableRules())) {
					// if last loop iteration didn change something
					proceed = true;
					break; // skip next comparisons
				}
			}
		}

		// format reachableRules
		for (ParserRule currentRule : this.getRules()) {
			currentRule.formatReachableRules();
		}
	}

	/**
	 * Returns the first element in a string (seperated by newline or surrounded
	 * by brackets)
	 * 
	 * @param string
	 *            The string containing the wished first element
	 * @return
	 */
	public static String getFirstElement(String string) {
		boolean converted = false;
		if (!string.contains("[") && !string.contains("]")) {
			string = string.replace("(", "[");
			string = string.replace(")", "]");
			converted = true;
		}
		String[] elements = Functions.getElements(string);

		String element = elements[0];

		if (converted) {
			element = element.replace("[", "(");
			element = element.replace("]", ")");
		}

		return element;
	}
	
	public void appendAnythingRule() {
		ParserRule anythingRule = new ParserRule("AbsolutelyAnything");
		
		anythingRule.create();
		
		for(ParserRule currentRule : this.getRules()) {
			anythingRule.addSyntax(currentRule.getName());
		}
		
		this.addRule(anythingRule);
	}
}