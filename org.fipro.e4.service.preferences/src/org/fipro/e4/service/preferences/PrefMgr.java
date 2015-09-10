package org.fipro.e4.service.preferences;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;

/**
 * Annotation to get a {@link PreferenceManager} injected via
 * {@link ExtendedObjectSupplier}. Can be used for creating a
 * {@link PreferenceDialog} that contains {@link PreferenceNode}s that are
 * contributed via {@link PreferenceNodeContribution} OSGi declarative services.
 * 
 * <pre>
 * public class PreferencesHandler {
 * 
 * 	&#64;Execute
 * 	public void execute(Shell shell, &#64;PrefMgr PreferenceManager manager) {
 * 		PreferenceDialog dialog = new PreferenceDialog(shell, manager);
 * 		dialog.open();
 * 	}
 * }
 * </pre>
 */
@Qualifier
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrefMgr {

}