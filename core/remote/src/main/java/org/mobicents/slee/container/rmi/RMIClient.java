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

package org.mobicents.slee.container.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jboss.invocation.MarshalledInvocation;
import org.jboss.logging.Logger;

/**
 * 
 * @author amit.bhayani
 *
 */
public class RMIClient implements RMIProxy, InvocationHandler, Serializable {

	private static final long serialVersionUID = -1227816478668532463L;

	private static final Logger log = Logger.getLogger(RMIClient.class);

	protected transient Object local = null;

	private transient RMIServer rmiServer;

	protected String key = null;

	public RMIClient() {

	}

	public RMIClient(Object local, RMIServer server, String key) {
		this.local = local;
		this.rmiServer = server;
		this.key = key;
	}

	public boolean isLocal() {
		return local != null;
	}

	public Object invokeRemote(Object proxy, Method method, Object[] args)
			throws Throwable {

		MarshalledInvocation mi = new MarshalledInvocation(null, method, args,
				null, null, null);

		mi.setObjectName("");

		RMIResponse rsp = this.rmiServer.invoke(mi);

		return rsp.response;
	}

	public Method findLocalMethod(Method method, Object[] args)
			throws Exception {
		return method;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (local != null) {
			try {
				Method localMethod = findLocalMethod(method, args);
				return localMethod.invoke(local, args);
			} catch (java.lang.reflect.InvocationTargetException ite) {
				throw ite.getTargetException();
			}
		} else {
			return invokeRemote(null, method, args);
		}
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {

		this.key = stream.readUTF();
		RMIServer server = (RMIServer) RMIServer.rmiServers.get(key);
		if (server != null) {
			try {
				local = server.getLocal();
			} catch (Exception ignored) {
			}
		} else {
			this.rmiServer = (RMIServer) stream.readObject();
		}

	}

	private void writeObject(ObjectOutputStream stream) throws IOException {

		stream.writeUTF(this.key);
		stream.writeObject(this.rmiServer);
	}

}
