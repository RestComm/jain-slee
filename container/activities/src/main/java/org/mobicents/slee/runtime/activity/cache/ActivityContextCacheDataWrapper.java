package org.mobicents.slee.runtime.activity.cache;

import java.util.Map;
import java.util.Set;

import javax.slee.facilities.TimerID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cluster.MobicentsCluster;

/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

public class ActivityContextCacheDataWrapper 
{
	private static final String STRING_ID_NODE_NAME = "str-id";
	private static final String KEY_ACTIVITY_FLAGS_NODE_NAME = "flags";
	private static final String KEY_LAST_ACCESS_NODE_NAME = "time";
	
	private AttachedSbbEntitiesCacheData attachedSbbEntitiesCacheData;
	private AttachedTimersEntitiesCacheData attachedTimersEntitiesCacheData;
	private NameBoundsCacheData nameBoundsCacheData;
	private CmpAttributesCacheData cmpAttributesCacheData;
	private MetadataCacheData metadataCacheData;
	private IsEndingCacheData isEndingCacheData;
	
	public ActivityContextCacheDataWrapper(ActivityContextHandle activityContextHandle,
			MobicentsCluster cluster) {
		
		this.attachedSbbEntitiesCacheData=new AttachedSbbEntitiesCacheData(activityContextHandle, cluster.getMobicentsCache());
		this.attachedTimersEntitiesCacheData=new AttachedTimersEntitiesCacheData(activityContextHandle, cluster.getMobicentsCache());
		this.nameBoundsCacheData=new NameBoundsCacheData(activityContextHandle, cluster.getMobicentsCache());
		this.cmpAttributesCacheData=new CmpAttributesCacheData(activityContextHandle, cluster.getMobicentsCache());
		this.metadataCacheData=new MetadataCacheData(activityContextHandle, cluster.getMobicentsCache());
		this.isEndingCacheData=new IsEndingCacheData(activityContextHandle, cluster.getMobicentsCache());		
	}
	
	public void create(Integer activityFlags) {
		metadataCacheData.setObject(true, KEY_ACTIVITY_FLAGS_NODE_NAME, activityFlags);
	}
	
	public void update(Integer activityFlags) {
		create(activityFlags);
	}
	
	public Boolean exists() {
		return metadataCacheData.getObject(false, KEY_ACTIVITY_FLAGS_NODE_NAME)!=null;
	}
	
	public Integer getFlags() {	
		return (Integer)metadataCacheData.getObject(false, KEY_ACTIVITY_FLAGS_NODE_NAME);		
	}
	
	public void setLastAccess(Long timestamp) {
		metadataCacheData.setObject(true, KEY_LAST_ACCESS_NODE_NAME, timestamp);
	}
	
	public Long getLastAccess() {	
		return (Long)metadataCacheData.getObject(false, KEY_LAST_ACCESS_NODE_NAME);		
	}
	
	public boolean isEnding() {	
		return isEndingCacheData.getElement()!=null;		
	}
	
	
	public boolean setEnding(boolean value) {
		if (value) {
			if (!isEnding()) {
				isEndingCacheData.set(true);				
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (isEnding()) {
				isEndingCacheData.removeElement();				
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public void setStringID(String stringID) {
		metadataCacheData.setObject(true, STRING_ID_NODE_NAME, stringID);
	}
	
	public String getStringID() {
		return (String)metadataCacheData.getObject(false, STRING_ID_NODE_NAME);
	}
	
	/**
	 * Tries to attaches an sbb entity
	 * 
	 * @param sbbEntityId
	 * @return true if it was attached, false if already was attached
	 */
	public boolean attachSbbEntity(SbbEntityID sbbEntityId) {
		return attachedSbbEntitiesCacheData.attachSbb(true, sbbEntityId);		
	}

	/**
	 * Detaches an sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public boolean detachSbbEntity(SbbEntityID sbbEntityId) {
		return attachedSbbEntitiesCacheData.detachSbb(false, sbbEntityId);			
	}

	/**
	 * Verifies if there at least one sbb entity attached
	 * 
	 * @return false is there are no sbb entities attached, true otherwise
	 */
	public boolean noSbbEntitiesAttached() {
		return !attachedSbbEntitiesCacheData.hasSbbs(false);		
	}

	/**
	 * Return a set with all sbb entities attached.
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getSbbEntitiesAttached() {
		return attachedSbbEntitiesCacheData.getSbbs(false);
	}

	/**
	 * Attaches a timer
	 * 
	 * @param timerID
	 */
	public boolean attachTimer(TimerID timerID) {
		return attachedTimersEntitiesCacheData.attachTime(true, timerID);
	}

	/**
	 * Detaches a timer
	 * 
	 * @param timerID
	 */
	public boolean detachTimer(TimerID timerID) {
		return attachedTimersEntitiesCacheData.detachTimer(false, timerID);	
	}

	/**
	 * Verifies if there at least one timer attached
	 * 
	 * @return false is there are no timers attached, true otherwise
	 */
	public boolean noTimersAttached() {
		return !attachedTimersEntitiesCacheData.hasTimers(false);		
	}

	/**
	 * Returns the set of timers attached to the ac
	 * 
	 * @return
	 */
	public Set<TimerID> getAttachedTimers() {
		return attachedTimersEntitiesCacheData.getTimers(false);									
	}

	/**
	 * Adds the specified name to the set of names bound to the ac
	 * 
	 * @param name
	 */
	public void nameBound(String name) {
		nameBoundsCacheData.bind(true, name);		
	}

	/**
	 * Removes the specified name from the set of names bound to the ac
	 * 
	 * @param name
	 */
	public boolean nameUnbound(String name) {
		return nameBoundsCacheData.unbind(false, name);		
	}

	/**
	 * Verifies if there at least one name bound to the ac
	 * 
	 * @return false is there are no names bound, true otherwise
	 */
	public boolean noNamesBound() {
		return !nameBoundsCacheData.hasNameBounds(false);		
	}

	/**
	 * Returns the set of names bound to the ac
	 * 
	 * @return
	 */
	public Set<String> getNamesBoundCopy() {
		return nameBoundsCacheData.getNameBounds(false);		
	}

	/**
	 * Sets the aci cmp attribute
	 * 
	 * @param attrName
	 * @param attrValue
	 */
	public void setCmpAttribute(String attrName, Object attrValue) {
		if(attrValue!=null)
			cmpAttributesCacheData.setAttribute(true, attrName, attrValue);
		/*else
			cmpAttributesCacheData.removeAttribute(false,attrName);*/
	}

	/**
	 * Retrieves the aci cmp attribute
	 * 
	 * @param attrName
	 * @return
	 */
	public Object getCmpAttribute(String attrName) {
		return cmpAttributesCacheData.getAttribute(false, attrName);		
	}

	/**
	 * Retrieves a map copy of the aci attributes set
	 * 
	 * @return
	 */
	public Map<String,Object> getCmpAttributesCopy() {
		return cmpAttributesCacheData.getAttributes(false);		
	}
	
	public void remove() {
		attachedSbbEntitiesCacheData.removeNode();
		attachedTimersEntitiesCacheData.removeNode();
		nameBoundsCacheData.removeNode();
		cmpAttributesCacheData.removeNode();
		metadataCacheData.removeNode();
		isEndingCacheData.removeElement();
	}
}