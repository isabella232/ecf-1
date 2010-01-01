/*******************************************************************************
* Copyright (c) 2009 Composent, Inc. and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Composent, Inc. - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.remoteservice;

import java.util.*;
import org.eclipse.core.runtime.Assert;

/**
 * Factory for creating {@link IRestParameter} instances.
 * @since 3.3
 */
public class RemoteCallParameterFactory {

	public static IRemoteCallParameter[] createParameters(String[] names, Object[] values) {
		Assert.isNotNull(names);
		Assert.isNotNull(values);
		Assert.isTrue(names.length == values.length);
		List result = new ArrayList();
		for (int i = 0; i < names.length; i++) {
			result.add(new RemoteCallParameter(names[i], values[i]));
		}
		return (IRemoteCallParameter[]) result.toArray(new IRemoteCallParameter[] {});
	}

	public static IRemoteCallParameter[] createParameters(String name, Object value) {
		return createParameters(new String[] {name}, new Object[] {value});
	}

	public static IRemoteCallParameter[] createParameters(String name1, Object value1, String name2, Object value2) {
		return createParameters(new String[] {name1, name2}, new Object[] {value1, value2});
	}

	public static IRemoteCallParameter[] createParameters(String name1, Object value1, String name2, Object value2, String name3, Object value3) {
		return createParameters(new String[] {name1, name2, name3}, new Object[] {value1, value2, value3});
	}

	public static IRemoteCallParameter[] createParameters(Map nameValues) {
		List names = new ArrayList();
		List values = new ArrayList();
		for (Iterator i = nameValues.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			names.add(key);
			values.add(nameValues.get(key));
		}
		return createParameters((String[]) names.toArray(new String[] {}), values.toArray(new Object[] {}));
	}

}
