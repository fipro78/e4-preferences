package org.fipro.e4.service.preferences;

import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.fipro.e4.service.preferences.impl.PreferenceManagerSupplier;
import org.osgi.framework.FrameworkUtil;

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
	}

	@Override
	public void createPage() {
		try {
			setPage(this.pageClass.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		if (getLabelImage() != null) {
			getPage().setImageDescriptor(getImageDescriptor());
		}
		getPage().setTitle(getLabelText());

		((PreferencePage) getPage()).setPreferenceStore(this.store);
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
