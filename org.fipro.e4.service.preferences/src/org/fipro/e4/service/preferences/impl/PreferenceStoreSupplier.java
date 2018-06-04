/*******************************************************************************
 * Copyright (c) 2015, 2018 Dirk Fauth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dirk Fauth <dirk.fauth@googlemail.com> - initial API and implementation
 *******************************************************************************/
package org.fipro.e4.service.preferences.impl;

import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.e4.core.di.suppliers.IObjectDescriptor;
import org.eclipse.e4.core.di.suppliers.IRequestor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.fipro.e4.service.preferences.ContributedPreferenceNode;
import org.fipro.e4.service.preferences.IPreferenceStoreFactoryService;
import org.fipro.e4.service.preferences.PrefStore;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * {@link ExtendedObjectSupplier} implementation for the {@link PrefStore}
 * annotation. Uses a {@link IPreferenceStoreFactoryService} implementation for
 * providing a {@link IPreferenceStore}.
 */
@Component(
		service=ExtendedObjectSupplier.class,
		property="dependency.injection.annotation=org.fipro.e4.service.preferences.PrefStore")
public class PreferenceStoreSupplier extends ExtendedObjectSupplier {

	private IPreferenceStoreFactoryService service;

	@Override
	public Object get(IObjectDescriptor descriptor, IRequestor requestor, boolean track, boolean group) {
		String qualifier = getKey(descriptor);
		if (qualifier != null) {
			return service.getPreferenceStoreInstance(qualifier);
		}
		return null;
	}

	private String getKey(IObjectDescriptor descriptor) {
		if (descriptor != null) {
			PrefStore qualifier = descriptor.getQualifier(PrefStore.class);
			return qualifier.nodePath();

		}
		return null;
	}

	/**
	 * Bind the {@link IPreferenceStoreFactoryService}
	 * 
	 * @param service
	 *            the {@link IPreferenceStoreFactoryService} that is used to
	 *            provide an {@link IPreferenceStore} to the
	 *            {@link ContributedPreferenceNode}s
	 */
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	public synchronized void addPreferenceStoreFactory(IPreferenceStoreFactoryService service) {
		this.service = service;
	}
}
