package raven.sqdev.sqf.processing.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import dataStructures.AbstractSQFTokenFactory;
import raven.sqdev.constants.ProblemMessages;
import raven.sqdev.exceptions.SQDevException;
import raven.sqdev.interfaces.ISQFParseSupplier;
import raven.sqdev.interfaces.ITreeProcessingResult;
import raven.sqdev.misc.DataTypeList;
import raven.sqdev.misc.EDataType;
import raven.sqdev.misc.FileUtil;
import raven.sqdev.misc.Macro;
import raven.sqdev.parser.misc.ParseUtil;
import raven.sqdev.parser.misc.SQFTokenFactory;
import raven.sqdev.parser.sqf.SQFInformation;

public class SQFProcessingTest {

	public static final File KEYWORD_FILE = new File(
			makeOSCompatible(System.getProperty("user.dir") + "/resources/sqf/SQFKeywords.txt"));

	protected static SQFInformation info;
	protected static ISQFParseSupplier supplier;
	protected static Map<String, Macro> macros;

	@Before
	public void setUp() throws Exception {
		macros = new HashMap<String, Macro>();

		info = getSQFInformation(macros);
		SQFTokenFactory factory = new SQFTokenFactory(info.getBinaryKeywords(), info.getUnaryKeywords());
		factory.initialize();

		supplier = new ISQFParseSupplier() {

			@Override
			public AbstractSQFTokenFactory getTokenFactory() {
				return factory;
			}

			@Override
			public Map<String, Macro> getMacros() {
				return macros;
			}
		};
	}

	@Test
	public void assignments() throws IOException {
		ITreeProcessingResult result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("myVar = 3".getBytes()),
				supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 1);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredGlobalVariables().keySet().contains("myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("myVar = 3;".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 1);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredGlobalVariables().keySet().contains("myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("_myVar = 3".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("_myVar = 3;".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private _myVar = 3".getBytes()), supplier,
				info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private _myVar = 3;".getBytes()), supplier,
				info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("_myVar = []".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("_myVar = [];".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("_myVar = {}".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("_myVar = {};".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		macros.put("GVAR", new Macro("GVAR"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("GVAR(myVar) = {}".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 1);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredGlobalVariables().keySet().contains("GVAR(myVar)"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("GVAR(myVar) = {}".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 1);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredGlobalVariables().keySet().contains("GVAR(myVar)"));

		macros.clear();
	}

	@Test
	public void assignmentErros() throws IOException {
		ITreeProcessingResult result = ParseUtil
				.parseAndProcessSQF(new ByteArrayInputStream("someOperator myVar = 3".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 1);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals("Missing ';'", result.getMarkers().iterator().next().getMessage());
		assertTrue(result.getDeclaredGlobalVariables().keySet().contains("myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("player = 3".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.reservedKeyword("player"), result.getMarkers().iterator().next().getMessage());

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("= 3".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.operatorIsNotUnary("="), result.getMarkers().iterator().next().getMessage());

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("=".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.operatorIsNotNular("="), result.getMarkers().iterator().next().getMessage());
	}

	@Test
	public void privateTest() throws IOException {
		ITreeProcessingResult result = ParseUtil
				.parseAndProcessSQF(new ByteArrayInputStream("private _myVar = 3".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().iterator().next().equals("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private \"_myVar\"".getBytes()), supplier,
				info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private ['_myVar', \"_yourVar\"]".getBytes()),
				supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 2);
		assertTrue(result.getMarkers().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_yourvar"));
	}

	@Test
	public void privateErrorTest() throws IOException {
		ITreeProcessingResult result = ParseUtil
				.parseAndProcessSQF(new ByteArrayInputStream("private myVar = 3".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.privateVariablesMustBeLocal(), result.getMarkers().iterator().next().getMessage());

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private \"myVar\"".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.canOnlyDeclareLocalVariable(), result.getMarkers().iterator().next().getMessage());

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private ['myVar', \"_yourVar\"]".getBytes()),
				supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.canOnlyDeclareLocalVariable(), result.getMarkers().iterator().next().getMessage());
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_yourvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private ['_myVar', \"yourVar\"]".getBytes()),
				supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 1);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.canOnlyDeclareLocalVariable(), result.getMarkers().iterator().next().getMessage());
		assertTrue(result.getDeclaredLocalVariables().keySet().contains("_myvar"));

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("private ['myVar', \"yourVar\"]".getBytes()),
				supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 2);
		assertEquals(ProblemMessages.canOnlyDeclareLocalVariable(), result.getMarkers().iterator().next().getMessage());
		assertEquals(ProblemMessages.canOnlyDeclareLocalVariable(), result.getMarkers().iterator().next().getMessage());
	}

	@Test
	public void unaryExpressions() throws IOException {
		ITreeProcessingResult result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("hint 'Test'".getBytes()),
				supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 0);

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("hint \"Test\"".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 0);

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("hint format []".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 0);
	}

	@Test
	public void unaryExpressionErrors() throws IOException {
		ITreeProcessingResult result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("hint 3".getBytes()),
				supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.expectedTypeButGot(
				new DataTypeList(new EDataType[] { EDataType.STRING, EDataType.STRUCTURED_TEXT }),
				new DataTypeList(EDataType.NUMBER)), result.getMarkers().iterator().next().getMessage());

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("hint []".getBytes()), supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.expectedTypeButGot(
				new DataTypeList(new EDataType[] { EDataType.STRING, EDataType.STRUCTURED_TEXT }),
				new DataTypeList(EDataType.ARRAY)), result.getMarkers().iterator().next().getMessage());

		result = ParseUtil.parseAndProcessSQF(new ByteArrayInputStream("hint {}".getBytes()) {
		}, supplier, info);
		assertTrue(result.getDeclaredGlobalVariables().size() == 0);
		assertTrue(result.getDeclaredLocalVariables().size() == 0);
		assertTrue(result.getMarkers().size() == 1);
		assertEquals(ProblemMessages.expectedTypeButGot(
				new DataTypeList(new EDataType[] { EDataType.STRING, EDataType.STRUCTURED_TEXT }),
				new DataTypeList(EDataType.CODE)), result.getMarkers().iterator().next().getMessage());
	}


	/**
	 * Makes the given path (that uses "/" as a FileSeparator) compatible with the
	 * current OS by using the actual OS-FileSeparator
	 * 
	 * @param path
	 *            The path to process
	 * @return The processed path
	 */
	protected static String makeOSCompatible(String path) {
		return path.replace("/", File.separator);
	}

	/**
	 * Gets the needed SQFInformation
	 * 
	 * @param macros
	 *            The macro-list that should be used
	 */
	protected static SQFInformation getSQFInformation(Map<String, Macro> macros) {
		return new SQFInformation(macros) {
			@Override
			protected String getKeywordContent() {
				try {
					return FileUtil.getContent(KEYWORD_FILE);
				} catch (SQDevException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		};
	}

}
