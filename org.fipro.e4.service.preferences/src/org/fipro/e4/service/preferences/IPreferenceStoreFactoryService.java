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
