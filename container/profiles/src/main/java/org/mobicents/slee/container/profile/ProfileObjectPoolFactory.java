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

package org.mobicents.slee.container.profile;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.log4j.Logger;

/**
 * Implements the methods invoked by the object pool to create new PRofile Objects
 * Object
 * 
 * @author Eduardo Martins
 */
public class ProfileObjectPoolFactory implements PoolableObjectFactory {

	private static Logger logger = Logger.getLogger(ProfileObjectPoolFactory.class);
	   
	private final ProfileTableImpl profileTable; 
	
    public ProfileObjectPoolFactory(ProfileTableImpl profileTable) {
    	this.profileTable = profileTable;
    }

    public void activateObject(Object obj) throws java.lang.Exception {
           	
    }

    public void destroyObject(Object obj) throws java.lang.Exception {
    	
    	if (logger.isTraceEnabled()) {
			logger.trace("destroyObject() for "+obj);
        }
        
    	ProfileObjectImpl profileObject = (ProfileObjectImpl) obj;
       profileObject.unsetProfileContext();
    }

    /**
     * Create a new instance of this object and set the SbbContext This places
     * it into the object pool.
     */
    public Object makeObject() {
        
    	if (logger.isTraceEnabled()) {
			logger.trace("makeObject()");
        }
    	
    	ProfileObjectImpl profileObject = new ProfileObjectImpl(profileTable);
		profileObject.setProfileContext(new ProfileContextImpl(profileTable));
        return profileObject;
    }

    public void passivateObject(Object obj) throws java.lang.Exception {
    	            
    }

    public boolean validateObject(Object obj) {
    	return ((ProfileObjectImpl) obj).getState() == ProfileObjectState.POOLED;        
    }

}