/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.remote;

import org.apache.log4j.Logger;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionService;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionServiceImpl;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.rmi.RmiServerInterface;
import org.mobicents.slee.container.util.JndiRegistry;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RmiServerInterfaceImpl extends AbstractSleeContainerModule implements RmiServerInterface {

	private final static Logger logger = Logger.getLogger(RmiServerInterfaceImpl.class);

    private String address;
	private int port;
    private Registry registry;
	private RemoteSleeConnectionServiceImpl remoteService;

	public RmiServerInterfaceImpl() {
		super();
	}

	@Override
	public void sleeInitialization() {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Starting Slee Service RMI Server on port: " + this.port);
			}
			remoteService = new RemoteSleeConnectionServiceImpl(
					super.sleeContainer.getSleeConnectionService(),
					super.sleeContainer.getComponentRepository());

            register(InetAddress.getByName(this.address), this.port);
		} catch (Exception e) {
			logger.error("Failed to start RMI server for Remote slee service", e);
		}
	}

	@Override
	public void sleeShutdown() {
		deregister();
	}

	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private void register(InetAddress serverAddress, int rmiRegistryPort) {
		try {
			System.setProperty("java.rmi.server.hostname", serverAddress.getHostName()) ;

			registry = LocateRegistry.createRegistry(
					rmiRegistryPort, null,
					new BindingAddressCorrectnessSocketFactory(serverAddress));

			registry.bind("RemoteSleeConnectionService", remoteService);
            if (logger.isDebugEnabled()) {
                logger.debug("RMI listener bound, port " + rmiRegistryPort);
            }
		} catch (Exception e) {
			throw new RuntimeException("Failed to bind due to:", e);
		}
	}

	private boolean deregister() {
		try {
			registry.unbind("RemoteSleeConnectionService");
			return UnicastRemoteObject.unexportObject(registry, false);
		} catch (RemoteException e) {
			throw new RuntimeException("Failed to unbind due to", e);
		} catch (NotBoundException e) {
			throw new RuntimeException("Failed to unbind due to", e);
		}
	}

	private static class BindingAddressCorrectnessSocketFactory
            extends RMISocketFactory
            implements Serializable {

		private InetAddress bindingAddress = null;

		public BindingAddressCorrectnessSocketFactory(InetAddress ipInterface) {
			this.bindingAddress = ipInterface;
		}

		public ServerSocket createServerSocket(int port) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(port, 50, bindingAddress);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return (serverSocket);
		}

		public Socket createSocket(String dummy, int port) throws IOException {
			return (new Socket(bindingAddress, port));
		}

		public boolean equals(Object other) {
			return (other != null &&
					other.getClass() == this.getClass());
		}
	}
}
