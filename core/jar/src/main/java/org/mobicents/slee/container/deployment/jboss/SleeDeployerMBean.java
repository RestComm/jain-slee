/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-8-1                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.deployment.jboss;

import java.util.Iterator;

import javax.management.ObjectName;

import org.jboss.deployment.SubDeployerMBean;

/**
 * @author Ivelin Ivanov
 * @version <tt>$Revision: 1.2 $</tt>
 *
 */
public interface SleeDeployerMBean extends SubDeployerMBean{

    public Iterator getDeployedApplications();

    public boolean getValidateDTDs();

    public void setValidateDTDs(boolean validate);

    public ObjectName getSleeManagementMBean();

    public void setSleeManagementMBean(ObjectName newSM);

}
