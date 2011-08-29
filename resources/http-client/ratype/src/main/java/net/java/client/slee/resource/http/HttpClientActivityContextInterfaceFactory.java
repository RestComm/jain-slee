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

package net.java.client.slee.resource.http;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface HttpClientActivityContextInterfaceFactory {

	public static final ResourceAdaptorTypeID RESOURCE_ADAPTOR_TYPE_ID = new ResourceAdaptorTypeID(
			"HttpClientResourceAdaptorType", "org.mobicents", "4.0");

	public ActivityContextInterface getActivityContextInterface(
			HttpClientActivity acivity) throws NullPointerException,
			UnrecognizedActivityException, FactoryException;

}
