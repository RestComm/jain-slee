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

package org.mobicents.slee.container.activity;

import java.util.Map;
import java.util.Set;

import javax.slee.facilities.TimerID;

import org.mobicents.slee.container.sbbentity.SbbEntityID;


public interface ActivityContextCacheDataInterface {

	Object putObject(Object key, Object value);

	Object getObject(Object key);

	Object removeObject(Object key);

	boolean isEnding();

	boolean setEnding(boolean value);

	boolean attachSbbEntity(SbbEntityID sbbEntityId);

	boolean detachSbbEntity(SbbEntityID sbbEntityId);

	boolean noSbbEntitiesAttached();

	Set<SbbEntityID> getSbbEntitiesAttached();

	boolean attachTimer(TimerID timerID);

	boolean detachTimer(TimerID timerID);

	boolean noTimersAttached();

	Set getAttachedTimers();

	void nameBound(String name);

	boolean nameUnbound(String name);

	boolean noNamesBound();

	Set getNamesBoundCopy();

	void setCmpAttribute(String attrName, Object attrValue);

	Object getCmpAttribute(String attrName);

	Map getCmpAttributesCopy();

	String getStringID();

	void setStringID(String sid);

	boolean exists();

	boolean create();

	boolean isRemoved();

	boolean remove();
}
