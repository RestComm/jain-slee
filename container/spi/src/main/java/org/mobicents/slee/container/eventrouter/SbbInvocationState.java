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

package org.mobicents.slee.container.eventrouter;

/**
 * 
 * This class represents the Invocation State of an sbb object
 * It is used in rollback handling to determine what to do since
 * this depends on whether an sbb object method invocation was happening
 * at the time the exception was thrown
 * 
 * @author Tim
 * @author martins
 *
 * 
 */
public enum SbbInvocationState {
	
	NOT_INVOKING, INVOKING_SBB_CREATE, INVOKING_SBB_POSTCREATE, INVOKING_SBB_LOAD, INVOKING_SBB_STORE, INVOKING_EVENT_HANDLER
}
