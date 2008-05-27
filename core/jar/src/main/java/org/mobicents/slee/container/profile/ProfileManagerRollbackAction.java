/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-5-22                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.profile;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.logging.Logger;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * Used to schedule jmx cleanup on profile table creation rollback
 * 
 * @author mranga
 * @author Ivelin Ivanov
 *
 */
public class ProfileManagerRollbackAction implements
    TransactionalAction {
    
    	private static final Logger logger = Logger.getLogger(ProfileManagerRollbackAction.class);
    
        private ObjectName profileTableObjectName;
        private MBeanServer mbeanServer;

        ProfileManagerRollbackAction(ObjectName profileTableObjectName, MBeanServer msvr) {
            this.profileTableObjectName = profileTableObjectName;
            mbeanServer = msvr;
        }

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mobicents.slee.runtime.transaction.TransactionalAction#execute()
		 */
		public void execute() {
		    try {
		        mbeanServer.unregisterMBean(profileTableObjectName);
		    } catch (Exception e) {
		        logger.error("Error unregistering mbean "
		                + profileTableObjectName, e);
		    }
		
		}
}

