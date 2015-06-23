package raven.sqf.tests;

import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import raven.SQFInjectorProvider;
import raven.sQF.BooleanContent;
import raven.sQF.BracketContent;
import raven.sQF.Code;
import raven.sQF.ControlStructure;
import raven.sQF.DecContent;
import raven.sQF.Declaration;
import raven.sQF.Model;
import raven.sQF.VarContent;
import raven.sQF.ifType;
import raven.validation.SQFValidator;

@RunWith(XtextRunner.class)
@InjectWith(SQFInjectorProvider.class)
@SuppressWarnings("all")
public class SQF_ValidatorFunctionsTest {
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
  @Test
  public void getTypeTest() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("test1 = 3;");
      _builder.newLine();
      _builder.append("test2 = \"Mama\";");
      _builder.newLine();
      _builder.append("test3 = [3];");
      _builder.newLine();
      _builder.append("test4 = (\"test\" + \"test\");");
      _builder.newLine();
      _builder.append("test5 = -(3 + 4 - test1)");
      _builder.newLine();
      _builder.append("test6 = ([3] + [2,5]);");
      _builder.newLine();
      _builder.newLine();
      _builder.append("refTest1 = test1;");
      _builder.newLine();
      _builder.append("refTest2 = test2;");
      _builder.newLine();
      _builder.append("refTest3 = test3;");
      _builder.newLine();
      _builder.append("refTest4 = test4;");
      _builder.newLine();
      _builder.append("refTest5 = test5;");
      _builder.newLine();
      _builder.append("refTest6 = test6;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("test7 = true;");
      _builder.newLine();
      _builder.append("test8 = false;");
      _builder.newLine();
      _builder.append("test9 = (3 < 4);");
      _builder.newLine();
      _builder.append("test10 = \"Mama\" == \"Papa\";");
      _builder.newLine();
      _builder.newLine();
      _builder.append("refTest7 = test7;");
      _builder.newLine();
      _builder.append("refTest8 = test8;");
      _builder.newLine();
      _builder.append("reftest9 = test9;");
      _builder.newLine();
      _builder.append("refTest10 = test10;");
      _builder.newLine();
      _builder.append("reftest11 = test1 isEqualTo test2;");
      _builder.newLine();
      final Model model = this._parseHelper.parse(_builder);
      EList<Code> _elements = model.getElements();
      Code _get = _elements.get(0);
      final Declaration vCon1 = _get.getDec();
      String _type = SQFValidator.getType(vCon1);
      Assert.assertEquals("number", _type);
      EList<Code> _elements_1 = model.getElements();
      Code _get_1 = _elements_1.get(1);
      final Declaration vCon2 = _get_1.getDec();
      String _type_1 = SQFValidator.getType(vCon2);
      Assert.assertEquals("string", _type_1);
      EList<Code> _elements_2 = model.getElements();
      Code _get_2 = _elements_2.get(2);
      final Declaration vCon3 = _get_2.getDec();
      String _type_2 = SQFValidator.getType(vCon3);
      Assert.assertEquals("array", _type_2);
      EList<Code> _elements_3 = model.getElements();
      Code _get_3 = _elements_3.get(3);
      final Declaration vCon4 = _get_3.getDec();
      String _type_3 = SQFValidator.getType(vCon4);
      Assert.assertEquals("string", _type_3);
      EList<Code> _elements_4 = model.getElements();
      Code _get_4 = _elements_4.get(4);
      final Declaration vCon5 = _get_4.getDec();
      String _type_4 = SQFValidator.getType(vCon5);
      Assert.assertEquals("number", _type_4);
      EList<Code> _elements_5 = model.getElements();
      Code _get_5 = _elements_5.get(5);
      final Declaration vCon6 = _get_5.getDec();
      String _type_5 = SQFValidator.getType(vCon6);
      Assert.assertEquals("array", _type_5);
      EList<Code> _elements_6 = model.getElements();
      Code _get_6 = _elements_6.get(6);
      final Declaration vCon7 = _get_6.getDec();
      String _type_6 = SQFValidator.getType(vCon7);
      Assert.assertEquals("number", _type_6);
      EList<Code> _elements_7 = model.getElements();
      Code _get_7 = _elements_7.get(7);
      final Declaration vCon8 = _get_7.getDec();
      String _type_7 = SQFValidator.getType(vCon8);
      Assert.assertEquals("string", _type_7);
      EList<Code> _elements_8 = model.getElements();
      Code _get_8 = _elements_8.get(8);
      final Declaration vCon9 = _get_8.getDec();
      String _type_8 = SQFValidator.getType(vCon9);
      Assert.assertEquals("array", _type_8);
      EList<Code> _elements_9 = model.getElements();
      Code _get_9 = _elements_9.get(9);
      final Declaration vCon10 = _get_9.getDec();
      String _type_9 = SQFValidator.getType(vCon10);
      Assert.assertEquals("string", _type_9);
      EList<Code> _elements_10 = model.getElements();
      Code _get_10 = _elements_10.get(10);
      final Declaration vCon11 = _get_10.getDec();
      String _type_10 = SQFValidator.getType(vCon11);
      Assert.assertEquals("number", _type_10);
      EList<Code> _elements_11 = model.getElements();
      Code _get_11 = _elements_11.get(11);
      final Declaration vCon12 = _get_11.getDec();
      String _type_11 = SQFValidator.getType(vCon12);
      Assert.assertEquals("array", _type_11);
      EList<Code> _elements_12 = model.getElements();
      Code _get_12 = _elements_12.get(12);
      final Declaration vCon13 = _get_12.getDec();
      String _type_12 = SQFValidator.getType(vCon13);
      Assert.assertEquals("boolean", _type_12);
      EList<Code> _elements_13 = model.getElements();
      Code _get_13 = _elements_13.get(13);
      final Declaration vCon14 = _get_13.getDec();
      String _type_13 = SQFValidator.getType(vCon14);
      Assert.assertEquals("boolean", _type_13);
      EList<Code> _elements_14 = model.getElements();
      Code _get_14 = _elements_14.get(14);
      final Declaration vCon15 = _get_14.getDec();
      String _type_14 = SQFValidator.getType(vCon15);
      Assert.assertEquals("boolean", _type_14);
      EList<Code> _elements_15 = model.getElements();
      Code _get_15 = _elements_15.get(15);
      final Declaration vCon16 = _get_15.getDec();
      String _type_15 = SQFValidator.getType(vCon16);
      Assert.assertEquals("boolean", _type_15);
      EList<Code> _elements_16 = model.getElements();
      Code _get_16 = _elements_16.get(16);
      final Declaration vCon17 = _get_16.getDec();
      String _type_16 = SQFValidator.getType(vCon17);
      Assert.assertEquals("boolean", _type_16);
      EList<Code> _elements_17 = model.getElements();
      Code _get_17 = _elements_17.get(17);
      final Declaration vCon18 = _get_17.getDec();
      String _type_17 = SQFValidator.getType(vCon18);
      Assert.assertEquals("boolean", _type_17);
      EList<Code> _elements_18 = model.getElements();
      Code _get_18 = _elements_18.get(18);
      final Declaration vCon19 = _get_18.getDec();
      String _type_18 = SQFValidator.getType(vCon19);
      Assert.assertEquals("boolean", _type_18);
      EList<Code> _elements_19 = model.getElements();
      Code _get_19 = _elements_19.get(19);
      final Declaration vCon20 = _get_19.getDec();
      String _type_19 = SQFValidator.getType(vCon20);
      Assert.assertEquals("boolean", _type_19);
      EList<Code> _elements_20 = model.getElements();
      Code _get_20 = _elements_20.get(20);
      final Declaration vCon21 = _get_20.getDec();
      String _type_20 = SQFValidator.getType(vCon21);
      Assert.assertEquals("boolean", _type_20);
      BracketContent _brCon = vCon15.getBrCon();
      DecContent _decCon = _brCon.getDecCon();
      VarContent _singleContent = _decCon.getSingleContent();
      String _type_21 = SQFValidator.getType(_singleContent);
      Assert.assertEquals("number", _type_21);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("test1 = \"Mama\";");
      _builder_1.newLine();
      _builder_1.append("test2 = \"test\" + test1;");
      _builder_1.newLine();
      _builder_1.append("test3 = 3;");
      _builder_1.newLine();
      _builder_1.append("test4 = test3 * 4 + test3;");
      _builder_1.newLine();
      _builder_1.append("test5 = test1 isEqualTo test3;");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("if(test1 == test2) then {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.newLine();
      _builder_1.append("};");
      _builder_1.newLine();
      final Model model2 = this._parseHelper.parse(_builder_1);
      EList<Code> _elements_21 = model2.getElements();
      Code _get_21 = _elements_21.get(0);
      Declaration _dec = _get_21.getDec();
      BracketContent _brCon_1 = _dec.getBrCon();
      DecContent _decCon_1 = _brCon_1.getDecCon();
      final VarContent input1 = _decCon_1.getSingleContent();
      String _type_22 = SQFValidator.getType(input1);
      Assert.assertEquals("string", _type_22);
      EList<Code> _elements_22 = model2.getElements();
      Code _get_22 = _elements_22.get(1);
      Declaration _dec_1 = _get_22.getDec();
      BracketContent _brCon_2 = _dec_1.getBrCon();
      DecContent _decCon_2 = _brCon_2.getDecCon();
      EList<VarContent> _nextCon = _decCon_2.getNextCon();
      final VarContent input2 = _nextCon.get(0);
      String _type_23 = SQFValidator.getType(input2);
      Assert.assertEquals("string", _type_23);
      EList<Code> _elements_23 = model2.getElements();
      Code _get_23 = _elements_23.get(2);
      Declaration _dec_2 = _get_23.getDec();
      BracketContent _brCon_3 = _dec_2.getBrCon();
      DecContent _decCon_3 = _brCon_3.getDecCon();
      final VarContent input3 = _decCon_3.getSingleContent();
      String _type_24 = SQFValidator.getType(input3);
      Assert.assertEquals("number", _type_24);
      EList<Code> _elements_24 = model2.getElements();
      Code _get_24 = _elements_24.get(3);
      Declaration _dec_3 = _get_24.getDec();
      BracketContent _brCon_4 = _dec_3.getBrCon();
      DecContent _decCon_4 = _brCon_4.getDecCon();
      EList<VarContent> _nextCon_1 = _decCon_4.getNextCon();
      final VarContent input4 = _nextCon_1.get(1);
      String _type_25 = SQFValidator.getType(input4);
      Assert.assertEquals("number", _type_25);
      EList<Code> _elements_25 = model2.getElements();
      Code _get_25 = _elements_25.get(4);
      Declaration _dec_4 = _get_25.getDec();
      BracketContent _brCon_5 = _dec_4.getBrCon();
      DecContent _decCon_5 = _brCon_5.getDecCon();
      final VarContent input5 = _decCon_5.getSingleContent();
      String _type_26 = SQFValidator.getType(input5);
      Assert.assertEquals("string", _type_26);
      EList<Code> _elements_26 = model2.getElements();
      Code _get_26 = _elements_26.get(4);
      Declaration _dec_5 = _get_26.getDec();
      BracketContent _brCon_6 = _dec_5.getBrCon();
      EList<DecContent> _content = _brCon_6.getContent();
      DecContent _get_27 = _content.get(0);
      final VarContent input6 = _get_27.getSingleContent();
      String _type_27 = SQFValidator.getType(input6);
      Assert.assertEquals("number", _type_27);
      EList<Code> _elements_27 = model2.getElements();
      Code _get_28 = _elements_27.get(5);
      ControlStructure _control = _get_28.getControl();
      ifType _ifStat = _control.getIfStat();
      BooleanContent _condition = _ifStat.getCondition();
      BracketContent _boolCon = _condition.getBoolCon();
      DecContent _decCon_6 = _boolCon.getDecCon();
      final VarContent input7 = _decCon_6.getSingleContent();
      String _type_28 = SQFValidator.getType(input7);
      Assert.assertEquals("string", _type_28);
      EList<Code> _elements_28 = model2.getElements();
      Code _get_29 = _elements_28.get(5);
      ControlStructure _control_1 = _get_29.getControl();
      ifType _ifStat_1 = _control_1.getIfStat();
      BooleanContent _condition_1 = _ifStat_1.getCondition();
      BracketContent _boolCon_1 = _condition_1.getBoolCon();
      EList<DecContent> _content_1 = _boolCon_1.getContent();
      DecContent _get_30 = _content_1.get(0);
      final VarContent input8 = _get_30.getSingleContent();
      String _type_29 = SQFValidator.getType(input8);
      Assert.assertEquals("string", _type_29);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void getThatReferenceTest() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("test1 = 3;");
      _builder.newLine();
      _builder.append("test2 = test1;");
      _builder.newLine();
      _builder.append("test3 = test2;");
      _builder.newLine();
      final Model model = this._parseHelper.parse(_builder);
      EList<Code> _elements = model.getElements();
      Code _get = _elements.get(0);
      Declaration _dec = _get.getDec();
      BracketContent _brCon = _dec.getBrCon();
      final DecContent result = _brCon.getDecCon();
      EList<Code> _elements_1 = model.getElements();
      Code _get_1 = _elements_1.get(1);
      Declaration _dec_1 = _get_1.getDec();
      BracketContent _brCon_1 = _dec_1.getBrCon();
      DecContent _decCon = _brCon_1.getDecCon();
      final VarContent input1 = _decCon.getSingleContent();
      DecContent _thatReference = SQFValidator.getThatReference(input1);
      Assert.assertEquals(result, _thatReference);
      EList<Code> _elements_2 = model.getElements();
      Code _get_2 = _elements_2.get(2);
      Declaration _dec_2 = _get_2.getDec();
      BracketContent _brCon_2 = _dec_2.getBrCon();
      DecContent _decCon_1 = _brCon_2.getDecCon();
      final VarContent input2 = _decCon_1.getSingleContent();
      DecContent _thatReference_1 = SQFValidator.getThatReference(input2);
      Assert.assertEquals(result, _thatReference_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void getBracketContentTest() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("test1 = (3+4);");
      _builder.newLine();
      _builder.append("test2 = (\"mama\"+\"papa\");");
      _builder.newLine();
      _builder.append("test3 = (test1)");
      _builder.newLine();
      _builder.append("test4 = 4;");
      _builder.newLine();
      final Model model = this._parseHelper.parse(_builder);
      EList<Code> _elements = model.getElements();
      Code _get = _elements.get(0);
      Declaration _dec = _get.getDec();
      BracketContent _brCon = _dec.getBrCon();
      DecContent _decCon = _brCon.getDecCon();
      VarContent _singleContent = _decCon.getSingleContent();
      final BracketContent result1 = _singleContent.getEmbrCon();
      EList<Code> _elements_1 = model.getElements();
      Code _get_1 = _elements_1.get(0);
      Declaration _dec_1 = _get_1.getDec();
      BracketContent _brCon_1 = _dec_1.getBrCon();
      DecContent _decCon_1 = _brCon_1.getDecCon();
      final VarContent input1 = _decCon_1.getSingleContent();
      BracketContent _bracketContent = SQFValidator.getBracketContent(input1);
      Assert.assertEquals(result1, _bracketContent);
      EList<Code> _elements_2 = model.getElements();
      Code _get_2 = _elements_2.get(1);
      Declaration _dec_2 = _get_2.getDec();
      BracketContent _brCon_2 = _dec_2.getBrCon();
      DecContent _decCon_2 = _brCon_2.getDecCon();
      VarContent _singleContent_1 = _decCon_2.getSingleContent();
      final BracketContent result2 = _singleContent_1.getEmbrCon();
      EList<Code> _elements_3 = model.getElements();
      Code _get_3 = _elements_3.get(1);
      Declaration _dec_3 = _get_3.getDec();
      BracketContent _brCon_3 = _dec_3.getBrCon();
      DecContent _decCon_3 = _brCon_3.getDecCon();
      final VarContent input2 = _decCon_3.getSingleContent();
      BracketContent _bracketContent_1 = SQFValidator.getBracketContent(input2);
      Assert.assertEquals(result2, _bracketContent_1);
      EList<Code> _elements_4 = model.getElements();
      Code _get_4 = _elements_4.get(2);
      Declaration _dec_4 = _get_4.getDec();
      BracketContent _brCon_4 = _dec_4.getBrCon();
      DecContent _decCon_4 = _brCon_4.getDecCon();
      VarContent _singleContent_2 = _decCon_4.getSingleContent();
      final BracketContent result3 = _singleContent_2.getEmbrCon();
      EList<Code> _elements_5 = model.getElements();
      Code _get_5 = _elements_5.get(2);
      Declaration _dec_5 = _get_5.getDec();
      BracketContent _brCon_5 = _dec_5.getBrCon();
      DecContent _decCon_5 = _brCon_5.getDecCon();
      final VarContent input3 = _decCon_5.getSingleContent();
      BracketContent _bracketContent_2 = SQFValidator.getBracketContent(input3);
      Assert.assertEquals(result3, _bracketContent_2);
      EList<Code> _elements_6 = model.getElements();
      Code _get_6 = _elements_6.get(3);
      Declaration _dec_6 = _get_6.getDec();
      final BracketContent result4 = _dec_6.getBrCon();
      EList<Code> _elements_7 = model.getElements();
      Code _get_7 = _elements_7.get(3);
      Declaration _dec_7 = _get_7.getDec();
      BracketContent _brCon_6 = _dec_7.getBrCon();
      DecContent _decCon_6 = _brCon_6.getDecCon();
      final VarContent input4 = _decCon_6.getSingleContent();
      BracketContent _bracketContent_3 = SQFValidator.getBracketContent(input4);
      Assert.assertEquals(result4, _bracketContent_3);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void getParentNameTest() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("test1 = \"Miau\"");
      _builder.newLine();
      _builder.append("test2 = test1;");
      _builder.newLine();
      _builder.append("test3 = 3;");
      _builder.newLine();
      _builder.append("test4 = (test1 + \"Test\");");
      _builder.newLine();
      final Model model = this._parseHelper.parse(_builder);
      EList<Code> _elements = model.getElements();
      Code _get = _elements.get(0);
      Declaration _dec = _get.getDec();
      BracketContent _brCon = _dec.getBrCon();
      DecContent _decCon = _brCon.getDecCon();
      final VarContent input1 = _decCon.getSingleContent();
      String _parentName = SQFValidator.getParentName(input1);
      Assert.assertEquals("test1", _parentName);
      EList<Code> _elements_1 = model.getElements();
      Code _get_1 = _elements_1.get(1);
      Declaration _dec_1 = _get_1.getDec();
      BracketContent _brCon_1 = _dec_1.getBrCon();
      DecContent _decCon_1 = _brCon_1.getDecCon();
      final VarContent input2 = _decCon_1.getSingleContent();
      String _parentName_1 = SQFValidator.getParentName(input2);
      Assert.assertEquals("test2", _parentName_1);
      EList<Code> _elements_2 = model.getElements();
      Code _get_2 = _elements_2.get(2);
      Declaration _dec_2 = _get_2.getDec();
      BracketContent _brCon_2 = _dec_2.getBrCon();
      DecContent _decCon_2 = _brCon_2.getDecCon();
      final VarContent input3 = _decCon_2.getSingleContent();
      String _parentName_2 = SQFValidator.getParentName(input3);
      Assert.assertEquals("test3", _parentName_2);
      EList<Code> _elements_3 = model.getElements();
      Code _get_3 = _elements_3.get(3);
      Declaration _dec_3 = _get_3.getDec();
      BracketContent _brCon_3 = _dec_3.getBrCon();
      DecContent _decCon_3 = _brCon_3.getDecCon();
      final VarContent input4 = _decCon_3.getSingleContent();
      String _parentName_3 = SQFValidator.getParentName(input4);
      Assert.assertEquals("test4", _parentName_3);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void getParentTest() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("test1 = 4;");
      _builder.newLine();
      _builder.append("test2 = \"Test\";");
      _builder.newLine();
      _builder.append("test3 = test1;");
      _builder.newLine();
      _builder.append("test4 = ((3+4)*(-18));");
      _builder.newLine();
      final Model model = this._parseHelper.parse(_builder);
      EList<Code> _elements = model.getElements();
      Code _get = _elements.get(0);
      final Declaration result1 = _get.getDec();
      BracketContent _brCon = result1.getBrCon();
      DecContent _decCon = _brCon.getDecCon();
      final VarContent input1 = _decCon.getSingleContent();
      Declaration _parent = SQFValidator.getParent(input1);
      Assert.assertEquals(result1, _parent);
      EList<Code> _elements_1 = model.getElements();
      Code _get_1 = _elements_1.get(1);
      final Declaration result2 = _get_1.getDec();
      BracketContent _brCon_1 = result2.getBrCon();
      DecContent _decCon_1 = _brCon_1.getDecCon();
      final VarContent input2 = _decCon_1.getSingleContent();
      Declaration _parent_1 = SQFValidator.getParent(input2);
      Assert.assertEquals(result2, _parent_1);
      EList<Code> _elements_2 = model.getElements();
      Code _get_2 = _elements_2.get(2);
      final Declaration result3 = _get_2.getDec();
      BracketContent _brCon_2 = result3.getBrCon();
      DecContent _decCon_2 = _brCon_2.getDecCon();
      final VarContent input3 = _decCon_2.getSingleContent();
      Declaration _parent_2 = SQFValidator.getParent(input3);
      Assert.assertEquals(result3, _parent_2);
      EList<Code> _elements_3 = model.getElements();
      Code _get_3 = _elements_3.get(3);
      final Declaration result4 = _get_3.getDec();
      BracketContent _brCon_3 = result4.getBrCon();
      DecContent _decCon_3 = _brCon_3.getDecCon();
      final VarContent input4 = _decCon_3.getSingleContent();
      Declaration _parent_3 = SQFValidator.getParent(input4);
      Assert.assertEquals(result4, _parent_3);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void getReferenceNameTest() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("test1 = \"Mama\";");
      _builder.newLine();
      _builder.append("test2 = test1;");
      _builder.newLine();
      _builder.append("test3 = test2 + test1;");
      _builder.newLine();
      final Model model = this._parseHelper.parse(_builder);
      EList<Code> _elements = model.getElements();
      Code _get = _elements.get(1);
      Declaration _dec = _get.getDec();
      BracketContent _brCon = _dec.getBrCon();
      DecContent _decCon = _brCon.getDecCon();
      final VarContent input1 = _decCon.getSingleContent();
      String _referenceName = SQFValidator.getReferenceName(input1);
      Assert.assertEquals("test1", _referenceName);
      EList<Code> _elements_1 = model.getElements();
      Code _get_1 = _elements_1.get(2);
      Declaration _dec_1 = _get_1.getDec();
      BracketContent _brCon_1 = _dec_1.getBrCon();
      DecContent _decCon_1 = _brCon_1.getDecCon();
      final VarContent input2 = _decCon_1.getSingleContent();
      String _referenceName_1 = SQFValidator.getReferenceName(input2);
      Assert.assertEquals("test2", _referenceName_1);
      EList<Code> _elements_2 = model.getElements();
      Code _get_2 = _elements_2.get(2);
      Declaration _dec_2 = _get_2.getDec();
      BracketContent _brCon_2 = _dec_2.getBrCon();
      DecContent _decCon_2 = _brCon_2.getDecCon();
      EList<VarContent> _nextCon = _decCon_2.getNextCon();
      final VarContent input3 = _nextCon.get(0);
      String _referenceName_2 = SQFValidator.getReferenceName(input3);
      Assert.assertEquals("test1", _referenceName_2);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}