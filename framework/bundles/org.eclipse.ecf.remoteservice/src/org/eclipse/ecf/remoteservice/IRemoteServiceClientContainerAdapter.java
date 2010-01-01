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

import java.util.Dictionary;
import org.eclipse.ecf.remoteservice.util.IRemoteCallParameterSerializer;

/**
 * @since 3.3
 */
public interface IRemoteServiceClientContainerAdapter extends IRemoteServiceContainerAdapter {

	/**
	 * <p>
	 * Register remoteCallables for given serviceInterfaceNames.  This method allows providers to register {@link IRemoteCallable}
	 * instances and associate an array of IRemoteCallables with each given serviceInterfaceName, so that subsequent lookup operations result in 
	 * appropriate remote service registrations.  The IRemoteCallable instances should correspond to <b>methods</b> within the
	 * particular service interface class.</p>
	 * <p></p>
	 * <p>Note that the number of serviceInterfaceNames (i.e. the length of the given String[]) <b>must</b> be equal to
	 * the number of rows of the remoteCallable two-dimensional array.  </p>
	 * <p></p>
	 * <p>For example, suppose we have a service interface "org.eclipse.ecf.IFoo":
	 * <pre>
	 * public interface IFoo {
	 *     public String getFoo();
	 * }
	 * </pre>
	 * We can define for this service inteface the following two dimensional array of callables:
	 * <pre>
	 * IRemoteCallable[][] callables = new IRemoteCallable[] { new RemoteCallable("foo","foo/bar/resourcePath",null,requestType) }};
	 * </pre>
	 * and then register with this method:
	 * <pre>
	 * IRemoteServiceRegistration reg = this.registerRemoteCallable(new String[] { "org.eclipse.ecf.IFoo" }, callables, null);
	 * </pre>
	 * 
	 * @param remoteCallables the IRemoteCallables to register.  Each IRemoteCallable represents a specific method->resourcePath mapping.
	 * Must not be <code>null</code>.
	 * @param properties any service properties to associate with the given registration.
	 * @return IRemoteServiceRegistration to use to unregister the remote service.  Will not be <code>null</code>.
	 */
	public IRemoteServiceRegistration registerRemoteCallables(String[] serviceInterfaceNames, IRemoteCallable[][] remoteCallable, Dictionary properties);

	/**
	 * Register remoteCallables for remote service client.  This method allows providers to register {@link IRemoteCallable}
	 * instances, so that subsequent lookup operations result in appropriate remote service registrations.
	 * 
	 * @param remoteCallables the IRemoteCallables to register.  Each IRemoteCallable represents a specific method->resourcePath mapping.
	 * Must not be <code>null</code>.
	 * @param properties any service properties to associate with the given registration.
	 * @return IRemoteServiceRegistration to use to unregister the remote service.  Will not be <code>null</code>.
	 */
	public IRemoteServiceRegistration registerRemoteCallables(IRemoteCallable[] remoteCallables, Dictionary properties);

	/**
	 * Get the remote call parameter serializer for thie client container.
	 * @return IRemoteCallParameterSerializer to use to serializing remote call parameters.  May be <code>null</code>.
	 */
	public IRemoteCallParameterSerializer getRemoteCallParameterSerializer();

	/**
	 * Set the remote call parameter serializer.
	 * @param serializer the remote call parameter serializer to set for this container.  May be <code>null</code>.
	 */
	public void setRemoteCallParameterSerializer(IRemoteCallParameterSerializer serializer);

}
