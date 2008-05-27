/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileSpecificationDescriptorImpl.java
 * 
 * Created on 30 sept. 2004
 *
 */
package org.mobicents.slee.container.component;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationDescriptor;
import javax.slee.profile.ProfileSpecificationID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainerUtils;

/**
 * 
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 */
public class ProfileSpecificationDescriptorImpl implements
        ProfileSpecificationDescriptor, DeployedComponent,Serializable {

	private static final long serialVersionUID = 378729649581109594L;
	private String cmpInterfaceName=null;
    private String managementInterfaceName=null;
    private String managementAbstractClassName=null;
    //private ProfileSpecificationID profileSpecificationID=null;
    private DeployableUnitIDImpl deployableUnitID;
    //private String profileSpecificationIDKey;
    
    private ComponentKey profileKey;
    private String description;
    private String profileClassesDescription;
    private boolean isSingleProfile;
    private Map profileIndexes;
    private String source;
    
    private static Logger logger = Logger.getLogger(ProfileSpecificationDescriptorImpl.class);
    

    /**
     * 
     */
    public ProfileSpecificationDescriptorImpl() {
        
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileSpecificationDescriptor#getCMPInterfaceName()
     */
    public String getCMPInterfaceName() {        
        return cmpInterfaceName;
    }
    
    public File getDuPath() {
        return this.deployableUnitID.getDUDeployer().getTempClassDeploymentDir();
    }

    public ClassLoader getClassLoader() {
        // This could be a standard profile -- in which case the classloader to use
        // is the current thread classloader.
        if ( this.deployableUnitID == null ) {
            logger.debug("standard profile -- returning current classloader");
            return (ClassLoader) SleeContainerUtils.getCurrentThreadClassLoader();
        } else {
            return this.deployableUnitID.getDUDeployer().getClassLoader();
        }
    }
    
    public void setCMPInterfaceName(String cmpInterfaceName) {
        this.cmpInterfaceName=cmpInterfaceName;
    }

    public String getManagementInterfaceName() {        
        return managementInterfaceName;
    }
    
    public void setManagementInterfaceName(String managementInterfaceName) {
        this.managementInterfaceName=managementInterfaceName;
    }
    
    public String getManagementAbstractClassName() {
        return managementAbstractClassName;
    }
    
    public void setManagementAbstractClassName(String managementAbstractClassName) {
        this.managementAbstractClassName=managementAbstractClassName;
    }
    
    public ProfileSpecificationID getProfileSpecificationID() {
        return new ProfileSpecificationIDImpl(profileKey);
    }
    
    public void setProfileSpecificationID(ProfileSpecificationID profileSpecificationID) {
        this.profileKey=
            ((ProfileSpecificationIDImpl)profileSpecificationID).getComponentKey();
    }
    
    /*public String getProfileSpecificationIDKey() {
        return profileSpecificationIDKey;
    }
    
    public void setProfileSpecificationIDKey(String profileSpecificationIDKey) {
        this.profileSpecificationIDKey=profileSpecificationIDKey;
    }*/
    
    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getDeployableUnit()
     */
    public DeployableUnitID getDeployableUnit() {
       
        return this.deployableUnitID;
    }
    
    public void setDeployableUnit( DeployableUnitID deployableUnitID) {
        this.deployableUnitID = (DeployableUnitIDImpl) deployableUnitID;
    }

    protected void setSource(String source) {
        this.source = source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ComponentDescriptor#getSource()
     */
    public String getSource() {
        return this.source;
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getID()
     */
    public ComponentID getID() {
        return new ProfileSpecificationIDImpl(profileKey);
    }
    
    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getName()
     */
    public String getName() {        
        return profileKey.getName();
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getVendor()
     */
    public String getVendor() {
        return profileKey.getVendor();
    }

    /* (non-Javadoc)
     * @see javax.slee.management.ComponentDescriptor#getVersion()
     */
    public String getVersion() {
        return profileKey.getVersion();
    }

    /**
     * @param profileKey
     */
    public void setComponentKey(ComponentKey profileKey) {
        this.profileKey=profileKey;
    }

    /**
     * 
     * @return 
     */
    public ComponentKey getComponentKey() { 
        return profileKey;
    }

    /**
     * @param description
     */
    public void setDescription(String description) { 
        this.description=description;
    }

    /**
     * @return 
     */
    public String getDescription() { 
        return description;
    }

    /**
     * @param profileDescriptionStr
     */
    public void setClassesDescription(String profileDescriptionStr) {
        this.profileClassesDescription=profileDescriptionStr;
    }

    /**
     * @return 
     */
    public String getClassesDescription() {
        return profileClassesDescription;
    }

    /**
     * @param profileHintsSingleProfile
     */
    public void setSingleProfile(boolean profileHintsSingleProfile) {
       this.isSingleProfile=profileHintsSingleProfile;
        
    }
    /**
     * @return
     */
    public boolean getSingleProfile() {
        return isSingleProfile;
    }

    /**
     * @param profileIndexes
     */
    public void setProfileIndexes(Map profileIndexes) {
        this.profileIndexes=profileIndexes;        
    }
    
    /**
     * @return
     */
    public Map getProfileIndexes() {
        return profileIndexes;
    }
   
	/* (non-Javadoc)
     * @see org.mobicents.slee.container.management.DeployedComponent#checkDeployment()
     */
    public void checkDeployment() throws DeploymentException {
		// TODO
    }

	public LibraryID[] getLibraries() {
		throw new UnsupportedOperationException();
	}
}
