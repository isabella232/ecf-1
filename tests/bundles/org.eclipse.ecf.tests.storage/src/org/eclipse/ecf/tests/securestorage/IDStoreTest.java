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

package org.eclipse.ecf.tests.securestorage;

import junit.framework.TestCase;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.internal.tests.securestorage.Activator;
import org.eclipse.ecf.storage.IIDEntry;
import org.eclipse.ecf.storage.IIDStore;
import org.eclipse.ecf.storage.INamespaceEntry;
import org.eclipse.equinox.security.storage.ISecurePreferences;

/**
 *
 */
public class IDStoreTest extends TestCase {

	IIDStore idStore;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		idStore = Activator.getDefault().getIDStore();
	}

	protected void clearStore() {
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		for (int i = 0; i < namespaces.length; i++) {
			namespaces[i].delete();
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		clearStore();
		idStore = null;
	}

	public void testIDStore() {
		assertNotNull(idStore);
	}

	protected IIDEntry addGUID() throws IDCreateException {
		final ID newGUID = IDFactory.getDefault().createGUID();
		return idStore.getEntry(newGUID);
	}

	protected IIDEntry addStringID(String value) throws IDCreateException {
		final ID newID = IDFactory.getDefault().createStringID(value);
		return idStore.getEntry(newID);
	}

	public void testStoreGUID() throws Exception {
		final ISecurePreferences prefs = addGUID().getPreferences();
		assertNotNull(prefs);
	}

	public void testStoreGUIDs() throws Exception {
		testStoreGUID();
		testStoreGUID();
		testStoreGUID();
	}

	public void testListEmptyNamespaces() throws Exception {
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		assertNotNull(namespaces);
	}

	public void testOneNamespace() throws Exception {
		testStoreGUID();
		testStoreGUID();
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		assertTrue(namespaces.length == 1);
	}

	public void testTwoNamespace() throws Exception {
		testStoreGUID();
		addStringID("1");
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		assertTrue(namespaces.length == 2);
	}

	public void testGetNamespaceNode() throws Exception {
		final ID newGUID = IDFactory.getDefault().createGUID();
		idStore.getEntry(newGUID);
		final ISecurePreferences namespacePrefs = idStore.getNamespaceEntry(newGUID.getNamespace()).getPreferences();
		assertNotNull(namespacePrefs);
		assertTrue(namespacePrefs.name().equals(newGUID.getNamespace().getName()));
	}

	public void testGetIDEntries() throws Exception {
		final ID newGUID = IDFactory.getDefault().createGUID();
		idStore.getEntry(newGUID);
		// Get namespace entry
		final INamespaceEntry namespaceEntry = idStore.getNamespaceEntry(newGUID.getNamespace());
		assertNotNull(namespaceEntry);

		final IIDEntry[] idEntries = namespaceEntry.getIDEntries();
		assertTrue(idEntries.length == 1);
		// Create GUID from idEntry
		final ID persistedGUID = idEntries[0].createID();
		assertNotNull(persistedGUID);
		assertTrue(persistedGUID.equals(newGUID));
	}

	public void testCreateAssociation() throws Exception {
		// Create two GUIDs and store them in idStore
		final ID guid1 = IDFactory.getDefault().createGUID();
		final IIDEntry entry1 = idStore.getEntry(guid1);
		final ID guid2 = IDFactory.getDefault().createGUID();
		final IIDEntry entry2 = idStore.getEntry(guid2);

		final String key = "foo";
		// Create association
		entry1.putAssociate(key, entry2, true);

		// Get entry1a
		final IIDEntry entry1a = idStore.getEntry(guid1);
		assertNotNull(entry1a);
		// Get associates (should include entry2)
		final IIDEntry[] entries = entry1a.getAssociates(key);
		assertNotNull(entries);
		assertTrue(entries.length == 1);
		// entry2a should be same as entry2
		final IIDEntry entry2a = entries[0];
		assertNotNull(entry2a);
		final ID guid2a = entry2a.createID();
		// and guid2a should equal guid2
		assertTrue(guid2.equals(guid2a));

	}

	public void testCreateAssociations() throws Exception {
		// Create two GUIDs and store them in idStore
		final ID guid1 = IDFactory.getDefault().createGUID();
		final IIDEntry entry1 = idStore.getEntry(guid1);
		final ID guid2 = IDFactory.getDefault().createGUID();
		final IIDEntry entry2 = idStore.getEntry(guid2);
		final ID guid3 = IDFactory.getDefault().createGUID();
		final IIDEntry entry3 = idStore.getEntry(guid3);
		final ID guid4 = IDFactory.getDefault().createGUID();
		final IIDEntry entry4 = idStore.getEntry(guid4);

		final String key1 = "foo";
		final String key2 = "foo2";

		// Create association
		entry1.putAssociate(key1, entry2, true);
		// Create another association with same key (key1)
		entry1.putAssociate(key1, entry3, false);
		// Create another association with different key (key2)
		entry1.putAssociate(key2, entry4, true);

		// Get entry1a
		final IIDEntry entry1a = idStore.getEntry(guid1);
		assertNotNull(entry1a);

		// Get associates (should include entry2)
		final IIDEntry[] entries1 = entry1a.getAssociates(key1);
		assertNotNull(entries1);
		assertTrue(entries1.length == 2);
		// entry2a should be same as entry2
		final IIDEntry entry2a = entries1[0];
		assertNotNull(entry2a);
		final ID guid2a = entry2a.createID();
		// and guid2a should equal guid2
		assertTrue(guid2.equals(guid2a));

		// entry3a should be same as entry2
		final IIDEntry entry3a = entries1[1];
		assertNotNull(entry3a);
		final ID guid3a = entry3a.createID();
		// and guid3a should equal guid3
		assertTrue(guid3.equals(guid3a));

		final IIDEntry[] entries2 = entry1a.getAssociates(key2);
		assertNotNull(entries2);
		assertTrue(entries2.length == 1);
		// entry4a should be same as entry4
		final IIDEntry entry4a = entries2[0];
		assertNotNull(entry4a);
		final ID guid4a = entry4a.createID();
		// and guid4a should equal guid4
		assertTrue(guid4.equals(guid4a));

	}

}
