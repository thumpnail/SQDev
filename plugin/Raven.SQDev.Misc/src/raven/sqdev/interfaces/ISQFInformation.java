package raven.sqdev.interfaces;

import java.util.Collection;
import java.util.Map;

import raven.sqdev.infoCollection.base.SQFCommand;
import raven.sqdev.infoCollection.base.Variable;
import raven.sqdev.misc.Macro;

/**
 * An interface describing an object that can provide the needed information for
 * the parsing of a SQF file
 * 
 * @author Raven
 *
 */
public interface ISQFInformation {

	/**
	 * Gets the binary SQF operators
	 */
	public Map<String, SQFCommand> getBinaryOperators();

	/**
	 * Gets the binary SQF operators keywords as string
	 */
	public Collection<String> getBinaryKeywords();

	/**
	 * Gets the unary SQF operators
	 */
	public Map<String, SQFCommand> getUnaryOperators();

	/**
	 * Gets the unary SQF operators keywords as string
	 */
	public Collection<String> getUnaryKeywords();

	/**
	 * Gets the nular SQF operators
	 */
	public Map<String, SQFCommand> getNularOperators();

	/**
	 * Gets the nular SQF operators keywords as string
	 */
	public Collection<String> getNularKeywords();

	/**
	 * Gets the configured magic variables
	 */
	public Map<String, Variable> getMagicVariables();

	/**
	 * Gets the configured magic variable-keywords as string
	 */
	public Collection<String> getMagicVariableNames();

	/**
	 * Gets the configured macros
	 */
	public Map<String, Macro> getMacros();

	/**
	 * Gets the configured macro-keywords as string
	 */
	public Collection<String> getMacroNames();
}
