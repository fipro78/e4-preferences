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

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.fipro.e4.service.preferences.impl.PreferenceManagerSupplier;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.LogService;

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
	private LogService logger;

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
				? nodeQualifier : FrameworkUtil.getBundle(pageClass).getSymbolicName();

		this.context = EclipseContextFactory.getServiceContext(
				FrameworkUtil.getBundle(pageClass).getBundleContext());
		this.logger = context.get(LogService.class);
	}

	@Override
	public void createPage() {
		// create the page via DI using the OSGi service context
		try {
			IPreferencePage page = ContextInjectionFactory.make(this.pageClass, this.context);
			setPage(page);
			
			if (getLabelImage() != null) {
				getPage().setImageDescriptor(getImageDescriptor());
			}
			getPage().setTitle(getLabelText());
			
			((PreferencePage) getPage()).setPreferenceStore(this.store);
		}
		catch (Exception e) {
			if (this.logger != null) {
				this.logger.log(
						LogService.LOG_ERROR, 
						"Error on creating instance of " + this.pageClass.getName(), 
						e);
			}
		}
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
	 * {@link PreferenceManagerSupplier} when the
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
