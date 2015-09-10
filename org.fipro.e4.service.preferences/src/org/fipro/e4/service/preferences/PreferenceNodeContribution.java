package org.fipro.e4.service.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Base class for contributing {@link IPreferencePage}s to a
 * {@link PreferenceDialog} via OSGi declarative service. It supports
 * contributing a single page (via simple constructor usage) or multiple pages
 * by adding additional nodes.
 * <p>
 * To contribute a single page:
 * 
 * <pre>
 * public class MyPreferenceContribution extends PreferenceNodeContribution {
 * 
 * 	public MyPreferenceContribution() {
 * 		super("myId", "myLabel", null, MyPreferencePage.class, null, null);
 * 	}
 * }
 * </pre>
 * 
 * To contribute a multiple pages add additional nodes:
 * 
 * <pre>
 * public class MyPreferenceContribution extends PreferenceNodeContribution {
 * 
 * 	public MyPreferenceContribution() {
 * 		super("myId", "myLabel", null, MyPreferencePage.class, null, null);
 * 
 * 		addPreferenceNode("myOtherId", "myOtherLabel", null, MyOtherPreferencePage.class, null, null);
 * 	}
 * }
 * </pre>
 * </p>
 * <p>
 * <b>Note:</b> There are also several convenience constructors and methods for
 * avoiding <code>null</code> parameters.
 * </p>
 */
public class PreferenceNodeContribution {

	private final List<ContributedPreferenceNode> nodes = new ArrayList<>();

	/**
	 * Creates a {@link PreferenceNodeContribution} with the given settings.
	 * Will set the imageDescriptor, the path and the nodeQualifier setting 
	 * to <code>null</code>.
	 * 
	 * @param id
	 *            the node id
	 * @param label
	 *            the label used to display the node in the preference dialog's
	 *            tree
	 * @param pageClass
	 *            the preference page implementation, this class must implement
	 *            {@link IPreferencePage}
	 */
	public PreferenceNodeContribution(
			String id, 
			String label, 
			Class<? extends IPreferencePage> pageClass) {
		this(id, label, null, pageClass, null, null);
	}

	/**
	 * Creates a {@link PreferenceNodeContribution} with the given settings.
	 * Will set the imageDescriptor and the nodeQualifier setting to
	 * <code>null</code>.
	 * 
	 * @param id
	 *            the node id
	 * @param label
	 *            the label used to display the node in the preference dialog's
	 *            tree
	 * @param pageClass
	 *            the preference page implementation, this class must implement
	 *            {@link IPreferencePage}
	 * @param path
	 *            the path of the node to which the contributed node should be
	 *            added to
	 */
	public PreferenceNodeContribution(
			String id, 
			String label, 
			Class<? extends IPreferencePage> pageClass, 
			String path) {
		this(id, label, null, pageClass, path, null);
	}

	/**
	 * Creates a {@link PreferenceNodeContribution} with the given settings.
	 * Will set the imageDescriptor setting to <code>null</code>.
	 * 
	 * @param id
	 *            the node id
	 * @param label
	 *            the label used to display the node in the preference dialog's
	 *            tree
	 * @param pageClass
	 *            the preference page implementation, this class must implement
	 *            {@link IPreferencePage}
	 * @param path
	 *            the path of the node to which the contributed node should be
	 *            added to
	 * @param nodeQualifier
	 *            the qualifier used to look up the preference node
	 */
	public PreferenceNodeContribution(
			String id, 
			String label, 
			Class<? extends IPreferencePage> pageClass, 
			String path, 
			String nodeQualifier) {
		this(id, label, null, pageClass, path, nodeQualifier);
	}

	/**
	 * Creates a {@link PreferenceNodeContribution} with the given settings.
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
	public PreferenceNodeContribution(
			String id, 
			String label, 
			ImageDescriptor imageDescriptor,
			Class<? extends IPreferencePage> pageClass, 
			String path,
			String nodeQualifier) {
		
		this.nodes.add(new ContributedPreferenceNode(
				id, 
				label, 
				imageDescriptor, 
				pageClass, 
				path, 
				nodeQualifier));
	}

	/**
	 * 
	 * @return The list of preference nodes that are contributed by this
	 *         {@link PreferenceNodeContribution}.
	 */
	public List<ContributedPreferenceNode> getPreferenceNodes() {
		return this.nodes;
	}

	/**
	 * Adds a {@link PreferenceNodeContribution} with the given settings.
	 * Will set the imageDescriptor, the path and the nodeQualifier setting 
	 * to <code>null</code>.
	 * 
	 * @param id
	 *            the node id
	 * @param label
	 *            the label used to display the node in the preference dialog's
	 *            tree
	 * @param pageClass
	 *            the preference page implementation, this class must implement
	 *            {@link IPreferencePage}
	 */
	public void addPreferenceNode(
			String id, 
			String label, 
			Class<? extends IPreferencePage> pageClass) {
		addPreferenceNode(id, label, null, pageClass, null, null);
	}

	/**
	 * Adds a {@link PreferenceNodeContribution} with the given settings. Will
	 * set the imageDescriptor and the nodeQualifier setting to
	 * <code>null</code>.
	 * 
	 * @param id
	 *            the node id
	 * @param label
	 *            the label used to display the node in the preference dialog's
	 *            tree
	 * @param pageClass
	 *            the preference page implementation, this class must implement
	 *            {@link IPreferencePage}
	 * @param path
	 *            the path of the node to which the contributed node should be
	 *            added to
	 */
	public void addPreferenceNode(
			String id, 
			String label, 
			Class<? extends IPreferencePage> pageClass, 
			String path) {
		addPreferenceNode(id, label, null, pageClass, path, null);
	}

	/**
	 * Adds a {@link PreferenceNodeContribution} with the given settings.
	 * Will set the imageDescriptor setting to <code>null</code>.
	 * 
	 * @param id
	 *            the node id
	 * @param label
	 *            the label used to display the node in the preference dialog's
	 *            tree
	 * @param pageClass
	 *            the preference page implementation, this class must implement
	 *            {@link IPreferencePage}
	 * @param path
	 *            the path of the node to which the contributed node should be
	 *            added to
	 * @param nodeQualifier
	 *            the qualifier used to look up the preference node
	 */
	public void addPreferenceNode(
			String id, 
			String label, 
			Class<? extends IPreferencePage> pageClass, 
			String path, 
			String nodeQualifier) {
		addPreferenceNode(id, label, null, pageClass, path, nodeQualifier);
	}
	
	/**
	 * Adds a {@link PreferenceNodeContribution} with the given settings.
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
	public void addPreferenceNode(
			String id, 
			String label, 
			ImageDescriptor imageDescriptor,
			Class<? extends IPreferencePage> pageClass, 
			String path,
			String nodeQualifier) {
		
		this.nodes.add(new ContributedPreferenceNode(
				id, 
				label, 
				imageDescriptor, 
				pageClass, 
				path, 
				nodeQualifier));
	}
}
