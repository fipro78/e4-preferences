/*******************************************************************************
 * Copyright (c) 2024 Dirk Fauth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dirk Fauth <dirk.fauth@googlemail.com> - initial API and implementation
 *******************************************************************************/
package org.fipro.e4.service.preferences.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.fipro.e4.service.preferences.ContributedPreferenceNode;
import org.fipro.e4.service.preferences.IPreferenceStoreFactoryService;
import org.fipro.e4.service.preferences.PreferenceNodeContribution;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(
	service = IContextFunction.class, 
	property = "service.context.key=org.eclipse.jface.preference.PreferenceManager")
public class PreferenceManagerContextFunction extends ContextFunction {

	PreferenceManager mgr;

	private IPreferenceStoreFactoryService service;
	
	private List<ContributedPreferenceNode> nodes = new ArrayList<>();

	@Override
    public Object compute(IEclipseContext context, String contextKey) {
        return getManager();
    }

	/**
	 * 
	 * @return The {@link PreferenceManager} managed by this
	 *         {@link PreferenceManagerSupplier}
	 */
	protected synchronized PreferenceManager getManager() {
		if (this.mgr == null) {
			this.mgr = new PreferenceManager();
		}
		return mgr;
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
		for (Iterator<ContributedPreferenceNode> it = this.nodes.iterator(); it.hasNext();) {
			if (addContributedPreferenceNode(it.next())) {
				it.remove();
			}
		}
	}

	/**
	 * Bind {@link PreferenceNodeContribution} that will add
	 * {@link PreferenceNode}s to the {@link PreferenceManager} managed by this
	 * {@link PreferenceManagerSupplier}.
	 * 
	 * @param node
	 *            The {@link PreferenceNodeContribution} to bind.
	 */
	@Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC)
	public synchronized void addPreferenceNode(PreferenceNodeContribution node) {
		for (ContributedPreferenceNode contribNode : node.getPreferenceNodes()) {
			if (this.service != null) {
				if (addContributedPreferenceNode(contribNode)) {
					for (Iterator<ContributedPreferenceNode> it = this.nodes.iterator(); it.hasNext();) {
						if (addContributedPreferenceNode(it.next())) {
							it.remove();
						}
					}
				} else {
					// if we could not add a node to a path, we remember the node in case the
					// configured parent node comes later
					rememberContributedPreferenceNode(contribNode);
				}
			} else {
				rememberContributedPreferenceNode(contribNode);
			}
		}
	}

	private void rememberContributedPreferenceNode(ContributedPreferenceNode node) {
		this.nodes.add(node);
		this.nodes.sort((ContributedPreferenceNode o1, ContributedPreferenceNode o2) -> {
			int depth1 = o1.getPath() == null ? 0 : o1.getPath().split("\\.").length;
			int depth2 = o2.getPath() == null ? 0 : o2.getPath().split("\\.").length;
			return depth1 - depth2;
		});
	}
	
	private boolean addContributedPreferenceNode(ContributedPreferenceNode contribNode) {
		// set the IPreferenceStore to the nodes
		contribNode.setStore(service.getPreferenceStoreInstance(contribNode.getNodeQualifier()));

		if (contribNode.getPath() == null) {
			getManager().addToRoot(contribNode);
			return true;
		} else {
			return getManager().addTo(contribNode.getPath(), contribNode);
		}
	}
	
	/**
	 * Unbind the given {@link PreferenceNodeContribution} from the
	 * {@link PreferenceManager} managed by this
	 * {@link PreferenceManagerSupplier}.
	 * 
	 * @param node
	 *            The {@link PreferenceNodeContribution} to unbind.
	 */
	public synchronized void removePreferenceNode(PreferenceNodeContribution node) {
		node.getPreferenceNodes().forEach(getManager()::remove);
	}
}
