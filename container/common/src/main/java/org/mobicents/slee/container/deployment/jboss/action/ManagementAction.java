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

package org.mobicents.slee.container.deployment.jboss.action;

/**
 * A management action to be executed by the internal deployer.
 * 
 * @author martins
 * 
 */
public interface ManagementAction {
	
	/**
	 * the type of action
	 * 
	 * @author martins
	 * 
	 */
	public enum Type {
		DEPLOY_MANAGEMENT, RESOURCE_MANAGEMENT, SERVICE_MANAGEMENT
	}

	/**
	 * Executes the action.
	 * 
	 * @throws Exception
	 */
	public void invoke() throws Exception;

	/**
	 * Retrieves the action type.
	 * 
	 * @return
	 */
	public Type getType();

}
