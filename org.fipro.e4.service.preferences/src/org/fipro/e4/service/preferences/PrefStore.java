/*******************************************************************************
 * Copyright (c) 2015 Dirk Fauth.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Dirk Fauth <dirk.fauth@googlemail.com> - initial API and implementation
 *******************************************************************************/
package org.fipro.e4.service.preferences;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.jface.preference.IPreferenceStore;

import jakarta.inject.Qualifier;

/**
 * Annotation to get an {@link IPreferenceStore} injected via
 * {@link ExtendedObjectSupplier}. Intended to be used on migration paths, if
 * modifying the code for &#64;Preference annotation injection mechanism is not
 * simply possible.
 * 
 * <pre>
 * &#64;Inject
 * &#64;PrefStore(nodePath = "myNodePath")
 * IPreferenceStore store;
 * ...
 * store.addPropertyChangeListener(new IPropertyChangeListener() {
 * 			
 *     &#64;Override
 *     public void propertyChange(PropertyChangeEvent event) {
 *         // do something
 *     }
 * });
 * </pre>
 */
@Qualifier
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrefStore {

	String nodePath() default "";
	
}
