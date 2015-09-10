package org.fipro.e4.service.preferences.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.fipro.e4.service.preferences.IPreferenceStoreFactoryService;

/**
 * Implementation of {@link IPreferenceStoreFactoryService} that creates and manages instances of 
 * {@link ScopedPreferenceStore} for the instance scope and specified qualifiers.
 */
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
