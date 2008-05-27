package org.mobicents.slee.container.component;

import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;

import org.jboss.logging.Logger;

public class MobicentsServiceDescriptorInternalImpl implements MobicentsServiceDescriptorInternal, ServiceDescriptorImpl {

private SbbID rootSbb;
	
	private String addressProfileTable;
	
	private String resourceInfoProfileTable;

	
	private DeployableUnitIDImpl deployableUnitID;
	
	private String source;
	
	private ServiceIDImpl serviceID;
	
    private static final long serialVersionUID = 6463738200621505295L;
	
	private byte defaultPriority;
	
	private static Logger logger;
	
	static {
	    logger = Logger.getLogger(ServiceDescriptorImpl.class);
	}

	
	
	
	
	/** Constructor
	 * 
	 */
	public MobicentsServiceDescriptorInternalImpl() {
		
	}
	
	public void setDeployableUnit ( DeployableUnitIDImpl deployableUnitID) {
	    this.deployableUnitID = deployableUnitID;
	}
	
	
	/** Method to be invoked by the XML parser to set the 
	 * deployment component Id. 
	 * 
	 * @param rootSbb
	 */
	
	public void setRootSbb(SbbID rootSbb){
	    logger.debug("setRootSbb " + rootSbb);
		
		this.rootSbb = rootSbb;
	}
	
	
	/** Set the address profile table.
	 *@param addressProfileTable -- name of the address profile table entry 
	 *for the service.
	 */
	public void setAddressProfileTable(String addressProfileTable) {
		this.addressProfileTable = addressProfileTable;
	}
	
	/** Set the resource info profile table entry.
	 * @param resourceInfoProfileTable -- name of the resource info profile table entry
	 */
	public void setResourceInfoProfileTable(String resourceInfoProfileTable) {
		this.resourceInfoProfileTable = resourceInfoProfileTable;
	}
	
	
	

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceDescriptor#getRootSbb()
	 */
	public SbbID getRootSbb() {
		return this.rootSbb;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceDescriptor#getAddressProfileTable()
	 */
	public String getAddressProfileTable() {
		return this.addressProfileTable;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceDescriptor#getResourceInfoProfileTable()
	 */
	public String getResourceInfoProfileTable() {
		return this.resourceInfoProfileTable;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getDeployableUnit()
	 */
	public DeployableUnitID getDeployableUnit() {
	
	    return deployableUnitID;
	    
	   
	}
	
	public void setDeployableUnit( DeployableUnitID deployableUnitID) {
	    this.deployableUnitID = (DeployableUnitIDImpl) deployableUnitID;
	}
	
	/** Set the source of the service.
	 * @param source -- source of the service
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getSource()
	 */
	public String getSource() {
		return this.source;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getID()
	 */
	public ComponentID getID() {
		return this.serviceID;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getName()
	 */
	public String getName() {
		return this.serviceID.id.getName();
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getVendor()
	 */
	public String getVendor() {
		return this.serviceID.id.getVendor();
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ComponentDescriptor#getVersion()
	 */
	public String getVersion() {
		return this.serviceID.id.getVersion();
	}
	
	
	
	
	

    /**
     * @return Returns the defaultPriority.
     */
    public byte getDefaultPriority() {
        return defaultPriority;
    }
    
    
    /**
     * @param defaultPriority The defaultPriority to set.
     */
    public void setDefaultPriority(byte defaultPriority) {
        this.defaultPriority = defaultPriority;
    }
    
    public void setServiceID(ServiceIDImpl serviceID) {
        this.serviceID = serviceID;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.management.DeployedComponent#checkDeployment()
     */
    public void checkDeployment() throws DeploymentException {
        // TODO Auto-generated method stub
        
    }

	public LibraryID[] getLibraries() {
		// TODO Auto-generated method stub
		return null;
	}

}
