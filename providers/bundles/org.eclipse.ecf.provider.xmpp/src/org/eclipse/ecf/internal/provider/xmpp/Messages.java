/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.provider.xmpp;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ecf.internal.provider.xmpp.messages"; //$NON-NLS-1$
	public static String XMPPChatRoomContainer_Exception_Connect_Wrong_Type;
	public static String XMPPIncomingFileTransfer_Progress_Data;
	public static String XMPPIncomingFileTransfer_Exception_User_Cancelled;
	public static String XMPPIncomingFileTransfer_Status_Transfer_Completed_OK;
	public static String XMPPIncomingFileTransfer_Status_Transfer_Exception;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
