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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.fipro.e4.service.preferences.IPreferenceStoreFactoryService;
import org.osgi.service.component.annotations.Component;

/**
 * Implementation of {@link IPreferenceStoreFactoryService} that creates and manages instances of 
 * {@link ScopedPreferenceStore} for the instance scope and specified qualifiers.
 */
@Component
public class PreferenceStoreFactoryServiceImpl implements IPreferenceStoreFactoryService {

	protected Map<String, IPreferenceStore> storeReferences = new HashMap<>();
	
	@Override
	public IPreferenceStore getPreferenceStoreInstance(String qualifier) {
		if (!this.storeReferences.containsKey(qualifier)) {
			this.storeReferences.put(qualifier, new ScopedPreferenceStore(InstanceScope.INSTANCE, qualifier));
		}
		return this.storeReferences.get(qualifier);
	}

}
