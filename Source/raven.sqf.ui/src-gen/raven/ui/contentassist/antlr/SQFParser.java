/*
 * generated by Xtext
 */
package raven.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import com.google.inject.Inject;

import raven.services.SQFGrammarAccess;

public class SQFParser extends AbstractContentAssistParser {
	
	@Inject
	private SQFGrammarAccess grammarAccess;
	
	private Map<AbstractElement, String> nameMappings;
	
	@Override
	protected raven.ui.contentassist.antlr.internal.InternalSQFParser createParser() {
		raven.ui.contentassist.antlr.internal.InternalSQFParser result = new raven.ui.contentassist.antlr.internal.InternalSQFParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}
	
	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getCodeAccess().getAlternatives(), "rule__Code__Alternatives");
					put(grammarAccess.getDecContentAccess().getNegAlternatives_0_0(), "rule__DecContent__NegAlternatives_0_0");
					put(grammarAccess.getVarContentAccess().getAlternatives(), "rule__VarContent__Alternatives");
					put(grammarAccess.getVarContentAccess().getUnOPAlternatives_0_0_0(), "rule__VarContent__UnOPAlternatives_0_0_0");
					put(grammarAccess.getVarContentAccess().getUnOPAlternatives_2_0_0(), "rule__VarContent__UnOPAlternatives_2_0_0");
					put(grammarAccess.getVarContentAccess().getAlternatives_2_1(), "rule__VarContent__Alternatives_2_1");
					put(grammarAccess.getVarContentAccess().getUnOPAlternatives_4_0_0(), "rule__VarContent__UnOPAlternatives_4_0_0");
					put(grammarAccess.getControlStructureAccess().getAlternatives(), "rule__ControlStructure__Alternatives");
					put(grammarAccess.getIfTypeAccess().getAlternatives_4(), "rule__IfType__Alternatives_4");
					put(grammarAccess.getIfTypeAccess().getAlternatives_4_0_1(), "rule__IfType__Alternatives_4_0_1");
					put(grammarAccess.getForTypeAccess().getAlternatives_1(), "rule__ForType__Alternatives_1");
					put(grammarAccess.getForeachTypeAccess().getAlternatives_4(), "rule__ForeachType__Alternatives_4");
					put(grammarAccess.getAbstractDeclarationReferenceAccess().getAlternatives(), "rule__AbstractDeclarationReference__Alternatives");
					put(grammarAccess.getOPERATORAccess().getAlternatives(), "rule__OPERATOR__Alternatives");
					put(grammarAccess.getANYTHINGAccess().getAlternatives(), "rule__ANYTHING__Alternatives");
					put(grammarAccess.getBooleanAccess().getAlternatives(), "rule__Boolean__Alternatives");
					put(grammarAccess.getBooleanAccess().getBoolAlternatives_0_0(), "rule__Boolean__BoolAlternatives_0_0");
					put(grammarAccess.getBoolCommandAccess().getAlternatives(), "rule__BoolCommand__Alternatives");
					put(grammarAccess.getCodeAccess().getGroup_0(), "rule__Code__Group_0__0");
					put(grammarAccess.getCodeAccess().getGroup_1(), "rule__Code__Group_1__0");
					put(grammarAccess.getCodeAccess().getGroup_2(), "rule__Code__Group_2__0");
					put(grammarAccess.getDeclarationAccess().getGroup(), "rule__Declaration__Group__0");
					put(grammarAccess.getBracketContentAccess().getGroup(), "rule__BracketContent__Group__0");
					put(grammarAccess.getBracketContentAccess().getGroup_1(), "rule__BracketContent__Group_1__0");
					put(grammarAccess.getDecContentAccess().getGroup(), "rule__DecContent__Group__0");
					put(grammarAccess.getDecContentAccess().getGroup_2(), "rule__DecContent__Group_2__0");
					put(grammarAccess.getVarContentAccess().getGroup_0(), "rule__VarContent__Group_0__0");
					put(grammarAccess.getVarContentAccess().getGroup_2(), "rule__VarContent__Group_2__0");
					put(grammarAccess.getVarContentAccess().getGroup_2_2(), "rule__VarContent__Group_2_2__0");
					put(grammarAccess.getVarContentAccess().getGroup_3(), "rule__VarContent__Group_3__0");
					put(grammarAccess.getVarContentAccess().getGroup_3_2(), "rule__VarContent__Group_3_2__0");
					put(grammarAccess.getVarContentAccess().getGroup_4(), "rule__VarContent__Group_4__0");
					put(grammarAccess.getVarContentAccess().getGroup_6(), "rule__VarContent__Group_6__0");
					put(grammarAccess.getVarContentAccess().getGroup_6_1(), "rule__VarContent__Group_6_1__0");
					put(grammarAccess.getArrayLiteralAccess().getGroup(), "rule__ArrayLiteral__Group__0");
					put(grammarAccess.getArrayLiteralAccess().getGroup_1(), "rule__ArrayLiteral__Group_1__0");
					put(grammarAccess.getArrayLiteralAccess().getGroup_1_1(), "rule__ArrayLiteral__Group_1_1__0");
					put(grammarAccess.getIfTypeAccess().getGroup(), "rule__IfType__Group__0");
					put(grammarAccess.getIfTypeAccess().getGroup_4_0(), "rule__IfType__Group_4_0__0");
					put(grammarAccess.getIfTypeAccess().getGroup_4_0_1_0(), "rule__IfType__Group_4_0_1_0__0");
					put(grammarAccess.getIfTypeAccess().getGroup_4_0_1_0_3(), "rule__IfType__Group_4_0_1_0_3__0");
					put(grammarAccess.getIfTypeAccess().getGroup_4_0_1_1(), "rule__IfType__Group_4_0_1_1__0");
					put(grammarAccess.getIfTypeAccess().getGroup_4_1(), "rule__IfType__Group_4_1__0");
					put(grammarAccess.getWhileTypeAccess().getGroup(), "rule__WhileType__Group__0");
					put(grammarAccess.getForTypeAccess().getGroup(), "rule__ForType__Group__0");
					put(grammarAccess.getForTypeAccess().getGroup_1_0(), "rule__ForType__Group_1_0__0");
					put(grammarAccess.getForTypeAccess().getGroup_1_1(), "rule__ForType__Group_1_1__0");
					put(grammarAccess.getForTypeAccess().getGroup_1_1_5(), "rule__ForType__Group_1_1_5__0");
					put(grammarAccess.getForeachTypeAccess().getGroup(), "rule__ForeachType__Group__0");
					put(grammarAccess.getSwitchTypeAccess().getGroup(), "rule__SwitchType__Group__0");
					put(grammarAccess.getSwitchTypeAccess().getGroup_6(), "rule__SwitchType__Group_6__0");
					put(grammarAccess.getSwitchTypeAccess().getGroup_7(), "rule__SwitchType__Group_7__0");
					put(grammarAccess.getMethodAccess().getGroup(), "rule__Method__Group__0");
					put(grammarAccess.getModelAccess().getElementsAssignment(), "rule__Model__ElementsAssignment");
					put(grammarAccess.getCodeAccess().getDecAssignment_0_0(), "rule__Code__DecAssignment_0_0");
					put(grammarAccess.getCodeAccess().getControlAssignment_1_0(), "rule__Code__ControlAssignment_1_0");
					put(grammarAccess.getCodeAccess().getMethodAssignment_2_0(), "rule__Code__MethodAssignment_2_0");
					put(grammarAccess.getDeclarationAccess().getNameAssignment_0(), "rule__Declaration__NameAssignment_0");
					put(grammarAccess.getDeclarationAccess().getBrConAssignment_2(), "rule__Declaration__BrConAssignment_2");
					put(grammarAccess.getBracketContentAccess().getDecConAssignment_0(), "rule__BracketContent__DecConAssignment_0");
					put(grammarAccess.getBracketContentAccess().getCompAssignment_1_0(), "rule__BracketContent__CompAssignment_1_0");
					put(grammarAccess.getBracketContentAccess().getContentAssignment_1_1(), "rule__BracketContent__ContentAssignment_1_1");
					put(grammarAccess.getDecContentAccess().getNegAssignment_0(), "rule__DecContent__NegAssignment_0");
					put(grammarAccess.getDecContentAccess().getSingleContentAssignment_1(), "rule__DecContent__SingleContentAssignment_1");
					put(grammarAccess.getDecContentAccess().getOpAssignment_2_0(), "rule__DecContent__OpAssignment_2_0");
					put(grammarAccess.getDecContentAccess().getNextConAssignment_2_1(), "rule__DecContent__NextConAssignment_2_1");
					put(grammarAccess.getVarContentAccess().getUnOPAssignment_0_0(), "rule__VarContent__UnOPAssignment_0_0");
					put(grammarAccess.getVarContentAccess().getNumAssignment_0_1(), "rule__VarContent__NumAssignment_0_1");
					put(grammarAccess.getVarContentAccess().getStringAssignment_1(), "rule__VarContent__StringAssignment_1");
					put(grammarAccess.getVarContentAccess().getUnOPAssignment_2_0(), "rule__VarContent__UnOPAssignment_2_0");
					put(grammarAccess.getVarContentAccess().getReferenceAssignment_2_1_0(), "rule__VarContent__ReferenceAssignment_2_1_0");
					put(grammarAccess.getVarContentAccess().getForEachVarAssignment_2_1_1(), "rule__VarContent__ForEachVarAssignment_2_1_1");
					put(grammarAccess.getVarContentAccess().getSelAssignment_2_2_0(), "rule__VarContent__SelAssignment_2_2_0");
					put(grammarAccess.getVarContentAccess().getIndexAssignment_2_2_1(), "rule__VarContent__IndexAssignment_2_2_1");
					put(grammarAccess.getVarContentAccess().getArrayContentAssignment_3_1(), "rule__VarContent__ArrayContentAssignment_3_1");
					put(grammarAccess.getVarContentAccess().getExecuteAssignment_3_2_0(), "rule__VarContent__ExecuteAssignment_3_2_0");
					put(grammarAccess.getVarContentAccess().getMethodNameAssignment_3_2_1(), "rule__VarContent__MethodNameAssignment_3_2_1");
					put(grammarAccess.getVarContentAccess().getUnOPAssignment_4_0(), "rule__VarContent__UnOPAssignment_4_0");
					put(grammarAccess.getVarContentAccess().getEmbracedAssignment_4_1(), "rule__VarContent__EmbracedAssignment_4_1");
					put(grammarAccess.getVarContentAccess().getEmbrConAssignment_4_2(), "rule__VarContent__EmbrConAssignment_4_2");
					put(grammarAccess.getVarContentAccess().getBoolAssignment_5(), "rule__VarContent__BoolAssignment_5");
					put(grammarAccess.getVarContentAccess().getParamAssignment_6_0(), "rule__VarContent__ParamAssignment_6_0");
					put(grammarAccess.getVarContentAccess().getIndexAssignment_6_1_1(), "rule__VarContent__IndexAssignment_6_1_1");
					put(grammarAccess.getArrayLiteralAccess().getConAssignment_0(), "rule__ArrayLiteral__ConAssignment_0");
					put(grammarAccess.getArrayLiteralAccess().getContentAssignment_1_0(), "rule__ArrayLiteral__ContentAssignment_1_0");
					put(grammarAccess.getArrayLiteralAccess().getNextContentAssignment_1_1_1(), "rule__ArrayLiteral__NextContentAssignment_1_1_1");
					put(grammarAccess.getControlStructureAccess().getIfStatAssignment_0(), "rule__ControlStructure__IfStatAssignment_0");
					put(grammarAccess.getControlStructureAccess().getWhileStatAssignment_1(), "rule__ControlStructure__WhileStatAssignment_1");
					put(grammarAccess.getControlStructureAccess().getForStatAssignment_2(), "rule__ControlStructure__ForStatAssignment_2");
					put(grammarAccess.getControlStructureAccess().getForEachStatAssignment_3(), "rule__ControlStructure__ForEachStatAssignment_3");
					put(grammarAccess.getControlStructureAccess().getSwitchStatAssignment_4(), "rule__ControlStructure__SwitchStatAssignment_4");
					put(grammarAccess.getIfTypeAccess().getConditionAssignment_2(), "rule__IfType__ConditionAssignment_2");
					put(grammarAccess.getIfTypeAccess().getThenCodeAssignment_4_0_1_0_1(), "rule__IfType__ThenCodeAssignment_4_0_1_0_1");
					put(grammarAccess.getIfTypeAccess().getElseCodeAssignment_4_0_1_0_3_2(), "rule__IfType__ElseCodeAssignment_4_0_1_0_3_2");
					put(grammarAccess.getIfTypeAccess().getThenCodeAssignment_4_0_1_1_2(), "rule__IfType__ThenCodeAssignment_4_0_1_1_2");
					put(grammarAccess.getIfTypeAccess().getElseCodeAssignment_4_0_1_1_6(), "rule__IfType__ElseCodeAssignment_4_0_1_1_6");
					put(grammarAccess.getIfTypeAccess().getExitCodeAssignment_4_1_2(), "rule__IfType__ExitCodeAssignment_4_1_2");
					put(grammarAccess.getWhileTypeAccess().getConditionAssignment_2(), "rule__WhileType__ConditionAssignment_2");
					put(grammarAccess.getWhileTypeAccess().getLoopCodeAssignment_6(), "rule__WhileType__LoopCodeAssignment_6");
					put(grammarAccess.getForTypeAccess().getBeginAssignment_1_0_2(), "rule__ForType__BeginAssignment_1_0_2");
					put(grammarAccess.getForTypeAccess().getConditionAssignment_1_0_6(), "rule__ForType__ConditionAssignment_1_0_6");
					put(grammarAccess.getForTypeAccess().getEndAssignment_1_0_10(), "rule__ForType__EndAssignment_1_0_10");
					put(grammarAccess.getForTypeAccess().getFromAssignment_1_1_2(), "rule__ForType__FromAssignment_1_1_2");
					put(grammarAccess.getForTypeAccess().getToAssignment_1_1_4(), "rule__ForType__ToAssignment_1_1_4");
					put(grammarAccess.getForTypeAccess().getStepAssignment_1_1_5_1(), "rule__ForType__StepAssignment_1_1_5_1");
					put(grammarAccess.getForTypeAccess().getLoopCodeAssignment_4(), "rule__ForType__LoopCodeAssignment_4");
					put(grammarAccess.getForVarDeclarationAccess().getNameAssignment(), "rule__ForVarDeclaration__NameAssignment");
					put(grammarAccess.getForeachTypeAccess().getCodeAssignment_1(), "rule__ForeachType__CodeAssignment_1");
					put(grammarAccess.getForeachTypeAccess().getArrayAssignment_4_0(), "rule__ForeachType__ArrayAssignment_4_0");
					put(grammarAccess.getForeachTypeAccess().getArrayLiteralAssignment_4_1(), "rule__ForeachType__ArrayLiteralAssignment_4_1");
					put(grammarAccess.getSwitchTypeAccess().getVarAssignment_2(), "rule__SwitchType__VarAssignment_2");
					put(grammarAccess.getSwitchTypeAccess().getValueAssignment_6_1(), "rule__SwitchType__ValueAssignment_6_1");
					put(grammarAccess.getSwitchTypeAccess().getCaseCodeAssignment_6_4(), "rule__SwitchType__CaseCodeAssignment_6_4");
					put(grammarAccess.getSwitchTypeAccess().getDefaultCodeAssignment_7_2(), "rule__SwitchType__DefaultCodeAssignment_7_2");
					put(grammarAccess.getMethodAccess().getParamsAssignment_0(), "rule__Method__ParamsAssignment_0");
					put(grammarAccess.getMethodAccess().getExecuteAssignment_1(), "rule__Method__ExecuteAssignment_1");
					put(grammarAccess.getMethodAccess().getMethodNameAssignment_2(), "rule__Method__MethodNameAssignment_2");
					put(grammarAccess.getAbstractDeclarationReferenceAccess().getDeclarationAssignment_0(), "rule__AbstractDeclarationReference__DeclarationAssignment_0");
					put(grammarAccess.getAbstractDeclarationReferenceAccess().getLoopDeclarationAssignment_1(), "rule__AbstractDeclarationReference__LoopDeclarationAssignment_1");
					put(grammarAccess.getObjectAccess().getNameAssignment(), "rule__Object__NameAssignment");
					put(grammarAccess.getANYTHINGAccess().getBoolAssignment_0(), "rule__ANYTHING__BoolAssignment_0");
					put(grammarAccess.getANYTHINGAccess().getNumAssignment_1(), "rule__ANYTHING__NumAssignment_1");
					put(grammarAccess.getANYTHINGAccess().getStringAssignment_2(), "rule__ANYTHING__StringAssignment_2");
					put(grammarAccess.getANYTHINGAccess().getReferenceAssignment_3(), "rule__ANYTHING__ReferenceAssignment_3");
					put(grammarAccess.getBooleanAccess().getBoolAssignment_0(), "rule__Boolean__BoolAssignment_0");
					put(grammarAccess.getBooleanAccess().getCommandAssignment_1(), "rule__Boolean__CommandAssignment_1");
					put(grammarAccess.getBooleanContentAccess().getBoolConAssignment(), "rule__BooleanContent__BoolConAssignment");
					put(grammarAccess.getMethodNameAccess().getRefAssignment(), "rule__MethodName__RefAssignment");
				}
			};
		}
		return nameMappings.get(element);
	}
	
	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			raven.ui.contentassist.antlr.internal.InternalSQFParser typedParser = (raven.ui.contentassist.antlr.internal.InternalSQFParser) parser;
			typedParser.entryRuleModel();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}
	
	public SQFGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(SQFGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
