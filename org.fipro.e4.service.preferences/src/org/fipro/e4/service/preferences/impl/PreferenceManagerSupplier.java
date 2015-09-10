package org.fipro.e4.service.preferences.impl;

import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.e4.core.di.suppliers.IObjectDescriptor;
import org.eclipse.e4.core.di.suppliers.IRequestor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.fipro.e4.service.preferences.ContributedPreferenceNode;
import org.fipro.e4.service.preferences.IPreferenceStoreFactoryService;
import org.fipro.e4.service.preferences.PreferenceNodeContribution;

/**
 * {@link ExtendedObjectSupplier} for creating and managing a
 * {@link PreferenceManager}. This implementation will only create one instance
 * and return the same instance everytime.
 */
public class PreferenceManagerSupplier extends ExtendedObjectSupplier {

	PreferenceManager mgr;

	private IPreferenceStoreFactoryService service;

	@Override
	public Object get(IObjectDescriptor descriptor, IRequestor requestor, boolean track, boolean group) {
		return getManager();
	}

	/**
	 * 
	 * @return The {@link PreferenceManager} managed by this
	 *         {@link PreferenceManagerSupplier}
	 */
	protected PreferenceManager getManager() {
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
	public synchronized void addPreferenceStoreFactory(IPreferenceStoreFactoryService service) {
		this.service = service;
	}

	/**
	 * Unbind the {@link IPreferenceStoreFactoryService}
	 * 
	 * @param service
	 *            The {@link IPreferenceStoreFactoryService} to unbind
	 */
	public synchronized void removePreferenceStoreFactory(IPreferenceStoreFactoryService service) {
		this.service = null;
	}

	/**
	 * Bind {@link PreferenceNodeContribution} that will add
	 * {@link PreferenceNode}s to the {@link PreferenceManager} managed by this
	 * {@link PreferenceManagerSupplier}.
	 * 
	 * @param node
	 *            The {@link PreferenceNodeContribution} to bind.
	 */
	public synchronized void addPreferenceNode(PreferenceNodeContribution node) {
		for (ContributedPreferenceNode contribNode : node.getPreferenceNodes()) {

			// set the IPreferenceStore to the nodes
			contribNode.setStore(service.getPreferenceStoreInstance(contribNode.getNodeQualifier()));

			if (contribNode.getPath() == null) {
				getManager().addToRoot(contribNode);
			} else {
				getManager().addTo(contribNode.getPath(), contribNode);
			}
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
		for (ContributedPreferenceNode contribNode : node.getPreferenceNodes()) {
			getManager().remove(contribNode);
		}
	}
}
