/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.provider.ISharedObjectInstantiator;

/**
 * Description of a remote ISharedObject instance.
 * 
 */
public class ReplicaSharedObjectDescription extends SharedObjectDescription
		implements Serializable {
	private static final long serialVersionUID = 8168017915032875581L;
	protected static long staticID = 0;
	public static long getNextUniqueIdentifier() {
		return staticID++;
	}
	protected ID homeID;
	protected String className;
	protected long identifier;
	
	public ReplicaSharedObjectDescription(String name,
			ISharedObjectInstantiator inst, String desc, Map props) {
		super(name, inst, desc, props);
	}
	public ReplicaSharedObjectDescription(String name, ID objectID, ID homeID, Map dict, long ident) {
		super(name,null,null,null);
		this.id = objectID;
		this.homeID = homeID;
		if (dict != null) {
			this.properties = dict;
		} else {
			this.properties = new HashMap();
		}
		this.identifier = ident;
	}
	public ReplicaSharedObjectDescription(ID objectID, ID homeID,
			String className, Map dict, long ident) {
		super(null, null, null, null);
		this.id = objectID;
		this.homeID = homeID;
		this.className = className;
		this.identifier = ident;
		if (dict == null) {
			this.properties = new HashMap();
		} else {
			this.properties = dict;
		}
	}
	public ReplicaSharedObjectDescription(ID id, String className) {
		this(id, className, getNextUniqueIdentifier());
	}
	public ReplicaSharedObjectDescription(ID id, String className, long ident) {
		this(id, null, className, null, ident);
	}
	public ReplicaSharedObjectDescription(ID id, String className, Map dict) {
		this(id, null, className, dict, getNextUniqueIdentifier());
	}
	public ReplicaSharedObjectDescription(ID id, String className, Map dict,
			long ident) {
		this(id, null, className, dict, ident);
	}
	public String getClassname() {
		return className;
	}
	public String getDescription() {
		return description;
	}
	public ID getHomeID() {
		return homeID;
	}
	public long getIdentifier() {
		return identifier;
	}
	public ISharedObjectInstantiator getInstantiator() {
		return instantiator;
	}
	public String getName() {
		return name;
	}
	public Map getProperties() {
		return properties;
	}
	public void setClassname(String theName) {
		this.className = theName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setHomeID(ID theID) {
		this.homeID = theID;
	}
	public void setIdentifier(long identifier) {
		this.identifier = identifier;
	}
	public void setProperties(Map props) {
		this.properties = props;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer("SharedObjectDescription[");
		sb.append("id:").append(id).append(";");
		sb.append("homeID:").append(homeID).append(";");
		sb.append("class:").append(className).append(";");
		sb.append("props:").append(properties).append(";");
		sb.append("ident:").append(identifier).append("]");
		return sb.toString();
	}
}