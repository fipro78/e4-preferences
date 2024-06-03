/*******************************************************************************
 * Copyright (c) 2015, 2024 Dirk Fauth.
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

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.fipro.e4.service.preferences.impl.PreferenceManagerContextFunction;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.Logger;
import org.osgi.service.log.LoggerFactory;

/**
 * Specialization of {@link PreferenceNode} that overrides
 * {@link PreferenceNode#createPage()} to support {@link IPreferencePage}
 * creation for contributed pages from other bundles. Otherwise the page
 * instance creation will fail because of ClassNotFoundExceptions.
 */
public class ContributedPreferenceNode extends PreferenceNode {

	private final String path;
	private final String nodeQualifier;
	private final Class<? extends IPreferencePage> pageClass;
	
	private IPreferenceStore store;
	
	private IEclipseContext context;
	private Logger logger;

	/**
	 * Create a new ContributedPreferenceNode.
	 * 
	 * @param id
	 *            the node id
	 * @param label
	 *            the label used to display the node in the preference dialog's
	 *            tree
	 * @param imageDescriptor
	 *            the image displayed left of the label in the preference
	 *            dialog's tree, or null if none
	 * @param pageClass
	 *            the preference page implementation, this class must implement
	 *            {@link IPreferencePage}
	 * @param path
	 *            the path of the node to which the contributed node should be
	 *            added to
	 * @param nodeQualifier
	 *            the qualifier used to look up the preference node
	 */
	public ContributedPreferenceNode(
			String id, 
			String label, 
			ImageDescriptor imageDescriptor,
			Class<? extends IPreferencePage> pageClass, 
			String path, 
			String nodeQualifier) {

		super(id, label, imageDescriptor, pageClass.getName());
		this.path = path;
		this.pageClass = pageClass;

		this.nodeQualifier = (nodeQualifier != null) 
				? nodeQualifier 
				: FrameworkUtil.getBundle(pageClass).getSymbolicName();
	}

	@Override
	public void createPage() {
		// create the page via DI using the OSGi service context
		try {
			IPreferencePage page = ContextInjectionFactory.make(this.pageClass, getContext());
			setPage(page);
			
			if (getLabelImage() != null) {
				getPage().setImageDescriptor(getImageDescriptor());
			}
			getPage().setTitle(getLabelText());
			
			((PreferencePage) getPage()).setPreferenceStore(this.store);
		}
		catch (Exception e) {
			if (getLogger() != null) {
				getLogger().error("Error on creating instance of {}", this.pageClass.getName(), e);
			}
		}
	}

	/**
	 * 
	 * @return The {@link IEclipseContext} that can be used to lookup OSGi services.
	 */
	private synchronized IEclipseContext getContext() {
		if (this.context == null) {
			this.context = EclipseContextFactory.getServiceContext(
					FrameworkUtil.getBundle(this.pageClass).getBundleContext());
		}
		return this.context;
	}
	
	/**
	 * 
	 * @return The {@link Logger} named for this class or <code>null</code> if no
	 *         {@link LoggerFactory} is available.
	 */
	private Logger getLogger() {
		if (this.logger == null) {
			LoggerFactory factory = getContext().get(LoggerFactory.class);
			this.logger = factory.getLogger(getClass());
		}
		return this.logger;
	}
	
	/**
	 * @return the path of the node to which the contributed node should be
	 *         added to
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @return the qualifier used to look up the preference node
	 */
	public String getNodeQualifier() {
		return nodeQualifier;
	}

	/**
	 * Set the {@link IPreferenceStore} that should be used by the contributed
	 * {@link IPreferencePage}. This method will be called by the
	 * {@link PreferenceManagerContextFunction} when the
	 * {@link PreferenceNodeContribution} is added to it.
	 * 
	 * @param store
	 *            The {@link IPreferenceStore} that should be used with the
	 *            created {@link IPreferencePage}
	 */
	public void setStore(IPreferenceStore store) {
		this.store = store;
	}

}
