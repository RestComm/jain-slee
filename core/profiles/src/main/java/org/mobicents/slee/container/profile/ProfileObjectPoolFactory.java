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