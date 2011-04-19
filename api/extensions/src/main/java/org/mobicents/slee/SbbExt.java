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

package org.mobicents.slee;

import javax.slee.Sbb;
import javax.slee.resource.InvalidConfigurationException;

/**
 * Extension interface for a JAIN SLEE 1.1 SBB, provides lifecycles methods to
 * deal with config properties, in a similar way as Resource Adaptors work.
 * 
 * A new state is introduced, Unconfigured, which stands between Does Not Exists
 * and Pooled states. Unconfigured is reached from Does Not Exists after
 * invocation of {@link Sbb#setSbbContext(javax.slee.SbbContext)}, and after
 * invocation of {@link SbbExt#sbbConfigure(ConfigProperties)} the Pooled state
 * is reached. In the reverse direction of the state machine, instead of moving
 * from Pooled to Does Not Exists state, now it goes from Pooled to
 * Unconfigured, upon invocation of {@link SbbExt#sbbUnconfigure()}, and upon
 * invocation of {@link Sbb#unsetSbbContext()} it goes to Does Not Exists state
 * again.
 * 
 * @author Eduardo Martins
 * 
 */
public interface SbbExt extends Sbb {

	/**
	 * The SLEE invokes this method on a SBB object in the Unconfigured state to
	 * provide it with configuration properties common for any SBB entity that
	 * uses the object.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            the configuration properties specified for the SBB object.
	 */
	public void sbbConfigure(ConfigProperties properties);

	/**
	 * The SLEE invokes this method on a SBB object in the Pooled state,
	 * indicating the transition to Unconfigured state. The object should
	 * release any resources acquired when invocation of
	 * {@link SbbExt#sbbConfigure(ConfigProperties)} or
	 * {@link SbbExt#sbbConfigurationUpdate(ConfigProperties)}.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 */
	public void sbbUnconfigure();

	/**
	 * This method is invoked by the SLEE whenever a new service, which refers
	 * the SBB, is activated by the Administrator, or when the Administrator
	 * attempts to update the configuration properties of an existing service
	 * which refers the SBB. The implementation of this method should examine
	 * the configuration properties supplied and verify that the configuration
	 * properties are valid for the SBB.
	 * <p>
	 * This method may be invoked on a SBB object in any valid state, therefore
	 * the implementation of this method should assume nothing about the
	 * internal state of the SBB object.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            contains the proposed new values for all configuration
	 *            properties specified for the SBB object.
	 * @throws InvalidConfigurationException
	 *             if the configuration properties are not valid for some
	 *             reason.
	 */
	public void sbbVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException;

	/**
	 * This method is invoked by the SLEE whenever the Administrator
	 * successfully updates a Service and SBB with new configuration properties.
	 * The implementation of this method should apply the new configuration
	 * properties to its internal state.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            contains the new values for all configuration properties
	 *            specified for the SBB object.
	 * @param properties
	 */
	public void sbbConfigurationUpdate(ConfigProperties properties);

}
