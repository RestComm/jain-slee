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

package org.mobicents.slee.services.sip.location.nonha;

import java.rmi.server.UID;
import java.util.concurrent.ConcurrentHashMap;

import org.mobicents.slee.services.sip.location.RegistrationBinding;

public class RegistrationBindings {

	private ConcurrentHashMap<String,RegistrationBinding> bindings = new ConcurrentHashMap<String,RegistrationBinding>();
	private String uid = new UID().toString();
	
	public ConcurrentHashMap<String, RegistrationBinding> getBindings() {
		return bindings;
	}
	
	@Override
	public int hashCode() {
		return uid.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((RegistrationBindings)obj).uid.equals(this.uid);
		}
		else {
			return false;
		}
	}
}
