/*******************************************************************************
 * Copyright (c) 2015 Dirk Fauth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dirk Fauth <dirk.fauth@googlemail.com> - initial API and implementation
 *******************************************************************************/
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
