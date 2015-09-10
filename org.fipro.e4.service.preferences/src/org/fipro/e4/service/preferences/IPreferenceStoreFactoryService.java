package org.fipro.e4.service.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Service that is responsible for creating and managing JFace
 * {@link IPreferenceStore} instances.
 */
public interface IPreferenceStoreFactoryService {

	/**
	 * Return the {@link IPreferenceStore} for the given qualifier.
	 * 
	 * @param qualifier
	 *            the qualifier to look up the preference node
	 * @return the {@link IPreferenceStore} for the given qualifier
	 */
	public IPreferenceStore getPreferenceStoreInstance(final String qualifier);
}
