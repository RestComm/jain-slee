/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

/* Created on Aug 12, 2004 */


package org.mobicents.slee.container.component;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.management.DeployableUnitID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.deployment.DeployableUnitDeployer;

/** An Identifier for deployable units in the Container.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 *
 */
public class DeployableUnitIDImpl implements DeployableUnitID, Serializable  {
    private DeployableUnitDescriptorImpl deployableUnitDescriptor;
    private HashSet<String> jarFileEntries;
    private URL sourceURL;
    private static Logger logger;
    private String description;
   
    static {
        logger = Logger.getLogger( DeployableUnitIDImpl.class);
    }
    
    private static final long serialVersionUID = 2677944696275808203L;
    
    private int id;
    private URI sourceURI;
    private transient DeployableUnitDeployer duDeployer;
    public int getID() {
		return id;
	}
    
    
    public DeployableUnitIDImpl (int id) {
        this.id = id;
        this.jarFileEntries = new HashSet<String>();
    }
    
    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		return ((DeployableUnitIDImpl)obj).id == this.id;
    	}
    	else {
    		return false;
    	}
    }

    public String toString() {
        return "DeployableUnitID[" + id + "]";
    }
    
    public void setDescriptor ( DeployableUnitDescriptorImpl descriptor) {
        this.deployableUnitDescriptor = descriptor;
    }
    
    public DeployableUnitDescriptorImpl getDescriptor( ) {
        return this.deployableUnitDescriptor;
    }
    
    public Set<String> getDeployedFiles() {
        return this.jarFileEntries;     
    }

    /**
     *@param sourceDirLocation -- dir from where the source was read.
     */
    public void setSourceURL(URL srcUrl) {
        
        this.sourceURL = srcUrl;
    }
   
    /**
     * get the source dir
     *
     */
    public URL getSourceURL ( ) {
        return this.sourceURL;
    }


    /**
     * @param sourceUri
     */
    public void setSourceURI(URI sourceUri) {
        this.sourceURI = sourceUri;    
    }
    
    public URI getSourceURI() {
        return this.sourceURI;
    }
    
    public void setDescription ( String description ) {
        this.description = description;
    }
    
    public String getDescription () {
        return this.description;
    }


    /**
     * 
     * @return the DeployableUnitDeployer in charge of the DU deployment
     * 
     */
    public void setDUDeployer(DeployableUnitDeployer deployer) {
        duDeployer = deployer;
    }

    public DeployableUnitDeployer getDUDeployer() {
        return duDeployer;
    }

    
}

