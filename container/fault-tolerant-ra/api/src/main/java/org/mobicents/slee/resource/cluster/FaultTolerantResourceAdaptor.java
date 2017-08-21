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

package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import javax.slee.resource.ResourceAdaptor;

/**
 * 
 * Abstract class for a fault tolerant JAIN SLEE 1.1 RA
 * 
 * @author martins
 * 
 */
public interface FaultTolerantResourceAdaptor<K extends Serializable, V extends Serializable>
		extends ResourceAdaptor {

	/**
	 * Callback from SLEE when the local RA was selected to recover the state
	 * for a replicated data key, which was owned by a cluster member that
	 * failed
	 * 
	 * @param key
	 */
	public void failOver(K key);

	/**
	 * Optional callback from SLEE when the replicated data key was removed from
	 * the cluster, this may be helpful when the local RA maintains local state.
	 * 
	 * @param key
	 */
	public void dataRemoved(K key);

	/**
	 * Invoked by SLEE to provide the fault tolerant context.
	 * 
	 * @param context
	 */
	public void setFaultTolerantResourceAdaptorContext(
			FaultTolerantResourceAdaptorContext<K, V> context);

	/**
	 * Invoked by SLEE to indicate that any references to the fault tolerant
	 * context should be removed.
	 */
	public void unsetFaultTolerantResourceAdaptorContext();
}
