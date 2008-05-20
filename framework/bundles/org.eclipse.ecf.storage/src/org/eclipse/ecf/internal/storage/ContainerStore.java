/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.storage;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.storage.*;
import org.eclipse.equinox.security.storage.ISecurePreferences;

/**
 *
 */
public class ContainerStore implements IContainerStore {

	private static final String CONTAINER_NODE_NAME = "container"; //$NON-NLS-1$

	final IDStore idStore;

	public ContainerStore(IDStore idStore) {
		this.idStore = idStore;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IContainerStore#getContainerEntries()
	 */
	public IContainerEntry[] getContainerEntries() {
		INamespaceEntry[] namespaceEntries = idStore.getNamespaceEntries();
		List results = new ArrayList();
		for (int i = 0; i < namespaceEntries.length; i++) {
			IIDEntry[] idEntries = namespaceEntries[i].getIDEntries();
			for (int j = 0; j < idEntries.length; j++) {
				ISecurePreferences pref = idEntries[j].getPreferences();
				String[] names = pref.childrenNames();
				for (int k = 0; k < names.length; k++) {
					if (names[k].equals(CONTAINER_NODE_NAME))
						results.add(new ContainerEntry(pref.node(CONTAINER_NODE_NAME), idEntries[j]));
				}
			}
		}
		return (IContainerEntry[]) results.toArray(new IContainerEntry[] {});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IContainerStore#store(org.eclipse.ecf.storage.IStorableContainerAdapter)
	 */
	public IContainerEntry store(IStorableContainerAdapter containerAdapter) {
		String factoryName = containerAdapter.getFactoryName();
		Assert.isNotNull(factoryName);
		ID containerID = containerAdapter.getID();
		Assert.isNotNull(containerID);
		IIDEntry idEntry = idStore.store(containerID);
		ISecurePreferences prefs = idEntry.getPreferences();
		ISecurePreferences containerPrefs = prefs.node(CONTAINER_NODE_NAME);
		return new ContainerEntry(containerPrefs, idEntry);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == null)
			return null;
		if (adapter.isInstance(this)) {
			return this;
		}
		IAdapterManager adapterManager = Activator.getDefault().getAdapterManager();
		return (adapterManager == null) ? null : adapterManager.loadAdapter(this, adapter.getName());
	}

}
