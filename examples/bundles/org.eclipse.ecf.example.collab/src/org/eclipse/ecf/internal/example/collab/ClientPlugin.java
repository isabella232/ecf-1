/****************************************************************************
 * Copyright (c) 2004, 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.internal.example.collab;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.discovery.IDiscoveryContainerAdapter;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.ui.views.IDiscoveryController;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plug-in class to be used in the desktop.
 */
public class ClientPlugin extends AbstractUIPlugin implements
		ClientPluginConstants {
	public static final String PLUGIN_ID = "org.eclipse.ecf.example.collab"; //$NON-NLS-1$
	// The shared instance.
	private static ClientPlugin plugin;

	private FontRegistry fontRegistry = null;
	private ServerStartup serverStartup = null;
	private DiscoveryStartup discoveryStartup = null;
	public static final String TCPSERVER_DISCOVERY_TYPE = "_ecftcp._tcp.local."; //$NON-NLS-1$
	protected static String serviceTypes[] = new String[] { TCPSERVER_DISCOVERY_TYPE };

	public static void log(String message) {
		getDefault().getLog().log(
				new Status(IStatus.OK, ClientPlugin.getDefault().getBundle()
						.getSymbolicName(), IStatus.OK, message, null));
	}

	public static void log(String message, Throwable e) {
		getDefault().getLog().log(
				new Status(IStatus.ERROR, ClientPlugin.getDefault().getBundle()
						.getSymbolicName(), IStatus.OK, message, e));
	}

	/**
	 * The constructor.
	 */
	public ClientPlugin() {
		super();
		plugin = this;
		this.fontRegistry = new FontRegistry();
	}

	public IDiscoveryController getDiscoveryController() {
		return new IDiscoveryController() {
			public void connectToService(IServiceInfo service) {
				synchronized (ClientPlugin.this) {
					if (discoveryStartup == null)
						return;
					discoveryStartup.connectToServiceFromInfo(service);
				}
			}

			public void startDiscovery() {
				try {
					getDefault().initDiscovery();
				} catch (Exception e) {
					ClientPlugin.log("Exception initializing discovery", e);
				}
			}

			public void stopDiscovery() {
				getDefault().disposeDiscovery();
			}

			public IDiscoveryContainerAdapter getDiscoveryContainer() {
				synchronized (ClientPlugin.this) {
					if (discoveryStartup == null)
						return null;
					return discoveryStartup.getDiscoveryContainer();
				}
			}

			public IContainer getContainer() {
				synchronized (ClientPlugin.this) {
					if (discoveryStartup == null)
						return null;
					return discoveryStartup.getContainer();
				}
			}

			public String[] getServiceTypes() {
				return serviceTypes;
			}

			public boolean isDiscoveryStarted() {
				return getDefault().isDiscoveryActive();
			}
		};
	}

	protected void setPreferenceDefaults() {
		this.getPreferenceStore().setDefault(ClientPlugin.PREF_USE_CHAT_WINDOW,
				false);
		this.getPreferenceStore().setDefault(
				ClientPlugin.PREF_DISPLAY_TIMESTAMP, true);
		// this.getPreferenceStore().setDefault(ClientPlugin.PREF_CHAT_FONT,
		// "");
		this.getPreferenceStore().setDefault(
				ClientPlugin.PREF_CONFIRM_FILE_SEND, true);
		// this.getPreferenceStore().setDefault(ClientPlugin.PREF_CONFIRM_FILE_RECEIVE,
		// true);
		this.getPreferenceStore().setDefault(
				ClientPlugin.PREF_CONFIRM_REMOTE_VIEW, true);
		this.getPreferenceStore().setDefault(ClientPlugin.PREF_START_SERVER,
				false);
		this.getPreferenceStore().setDefault(ClientPlugin.PREF_REGISTER_SERVER,
				false);

		this.getPreferenceStore().setDefault(
				ClientPlugin.PREF_SHAREDEDITOR_PLAY_EVENTS_IMMEDIATELY, true);
		this.getPreferenceStore().setDefault(
				ClientPlugin.PREF_SHAREDEDITOR_ASK_RECEIVER, true);
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setPreferenceDefaults();
	}

	public synchronized void initDiscovery() throws Exception {
		if (discoveryStartup == null) {
			discoveryStartup = new DiscoveryStartup();
		}
	}

	public synchronized void initServer() throws Exception {
		if (serverStartup == null) {
			serverStartup = new ServerStartup();
		}
	}

	public synchronized void registerServers() {
		if (discoveryStartup != null && serverStartup != null) {
			serverStartup.registerServers();
		}
	}

	public synchronized boolean isDiscoveryActive() {
		if (discoveryStartup == null)
			return false;
		else
			return discoveryStartup.isActive();
	}

	public synchronized boolean isServerActive() {
		if (serverStartup == null)
			return false;
		else
			return serverStartup.isActive();
	}

	public synchronized void disposeDiscovery() {
		if (discoveryStartup != null) {
			discoveryStartup.dispose();
			discoveryStartup = null;
		}
	}

	public synchronized void disposeServer() {
		if (serverStartup != null) {
			serverStartup.dispose();
			serverStartup = null;
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		disposeServer();
		disposeDiscovery();
	}

	public FontRegistry getFontRegistry() {
		return this.fontRegistry;
	}

	public Shell getActiveShell() {
		return this.getWorkbench().getDisplay().getActiveShell();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#createImageRegistry()
	 */
	protected ImageRegistry createImageRegistry() {
		ImageRegistry registry = super.createImageRegistry();
		registry.put(ClientPluginConstants.DECORATION_DEFAULT_PROVIDER,
				AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID,
						"icons/default_provider_image.gif").createImage());
		return registry;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ClientPlugin getDefault() {
		return plugin;
	}
}
