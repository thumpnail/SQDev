/*
 * generated by Xtext
 */
package raven;

import org.eclipse.xtext.junit4.IInjectorProvider;

import com.google.inject.Injector;

public class SQFUiInjectorProvider implements IInjectorProvider {
	
	@Override
	public Injector getInjector() {
		return raven.ui.internal.SQFActivator.getInstance().getInjector("raven.SQF");
	}
	
}
