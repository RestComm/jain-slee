/*
 * Created on Aug 3, 2004
 *
 *The Open SLEE project
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component;

import java.io.Serializable;

import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.management.ServiceDescriptor;

import org.jboss.logging.Logger;

/** Implements the Deployment descriptor information parsed
 * from the service.xml file. Note that the set methods of this
 * class are to be ivoked by the XML parser.
 * 
 *@author M. Ranganatha
 */
public interface ServiceDescriptorImpl extends Serializable, ServiceDescriptor, 
									DeployedComponent {
	
	
	
	
	
	
	
	
	
	public void setDeployableUnit ( DeployableUnitIDImpl deployableUnitID) ;
	
	
	/** Method to be invoked by the XML parser to set the 
	 * deployment component Id. 
	 * 
	 * @param rootSbb
	 */
	
	public void setRootSbb(SbbID rootSbb);
	
	
	
	
	
	

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceDescriptor#getRootSbb()
	 */
	public SbbID getRootSbb() ;

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceDescriptor#getAddressProfileTable()
	 */
	public String getAddressProfileTable() ;

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceDescriptor#getResourceInfoProfileTable()
	 */
	public String getResourceInfoProfileTable();

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getDeployableUnit()
	 */
	public DeployableUnitID getDeployableUnit() ;
	
	public void setDeployableUnit( DeployableUnitID deployableUnitID) ;
	
	/** Set the source of the service.
	 * @param source -- source of the service
	 */
	public void setSource(String source) ;

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getSource()
	 */
	public String getSource() ;

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getID()
	 */
	public ComponentID getID() ;

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getName()
	 */
	public String getName() ;

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getVendor()
	 */
	public String getVendor() ;

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getVersion()
	 */
	public String getVersion() ;
	
	
	
	
	

    /**
     * @return Returns the defaultPriority.
     */
    public byte getDefaultPriority() ;
    
    
    /**
     * @param defaultPriority The defaultPriority to set.
     */
    public void setDefaultPriority(byte defaultPriority) ;
    
    public void setServiceID(ServiceIDImpl serviceID) ;

    

   
}
