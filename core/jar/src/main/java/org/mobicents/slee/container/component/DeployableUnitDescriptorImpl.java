/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitDescriptor;


/**
 * 
 * The DeployableUnitDescriptor interface defines the interface 
 * of a Java object that describes an installed deployable unit.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 */

public class DeployableUnitDescriptorImpl implements DeployableUnitDescriptor, Serializable {
    private String url;
    private LinkedList components;
    private LinkedList installedComponents;
    private Date deploymentDate;
    private DeployableUnitIDImpl deployableUnitID;
    // TODO -- replace this with a url that points at some place in the jboss cache
    // where these things will reside.
    private transient File tmpDeploymentDirectory;
    private transient File tmpDUJarsDirectory;
    
    
    private String description;
    private HashSet jars;
    private URI sourceUri;
	private transient Collection jarNodes;
	private transient Collection serviceNodes;
	
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("url = " + url).append("\nDate = " + deploymentDate).
        append("\ndeployableUnitID = " + deployableUnitID ).
        append("\ndescription = " + description).
        append("\ncomponents = ");
        for ( Iterator it = components.iterator(); it.hasNext(); ) {
            sbuf.append("\n"+it.next() );
        }
        sbuf.append("\njars = ");
        for (Iterator it = jars.iterator(); it.hasNext(); ) {
            sbuf.append("\n" +it.next());
        }
        return sbuf.toString();
        
    }
   
    // These are the locations where the DU Is unpacked/unjarred and
    // these directories are needed when you delte the file.
    public void setTmpDeploymentDirectory ( File deploymentDirectory ) {
        this.tmpDeploymentDirectory = deploymentDirectory;
    }
    
    public File getTmpDeploymentDirectory( ) {
        return this.tmpDeploymentDirectory;
    }
    
    public void setTmpDUJarsDirectory ( File tmpDUJarsDirectory ) {
        this.tmpDUJarsDirectory = tmpDUJarsDirectory;
    }
    
    public File getTmpDUJarsDirectory () {
        return this.tmpDUJarsDirectory;
    }
    
    public DeployableUnitDescriptorImpl(String url, Date date){
        this.url = url;
        this.deploymentDate = date;
        
        this.components = new LinkedList();
        this.installedComponents=new LinkedList();
        this.jars = new HashSet();
    }
    
   
        
    


    public String getURL() {
        return url;
    }
    
    public URI getSourceURI() {
        return this.sourceUri;
    }
    
    public Date getDeploymentDate() {
        return deploymentDate;
    }

    public ComponentID[] getComponents() {
        ComponentID[] retval = new ComponentID[ components.size()];
        components.toArray(retval);
        return retval;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the jars.
     */
    public String[] getJars() {
        if ( jars == null) return null;
        String[] jarArray = new String[ jars.size()];
        jars.toArray(jarArray);
        return jarArray;
    }
    
    /**
     * Add a jar to the descriptor
     * @param jars
     */
    public void addJar( String jar) {
       this.jars.add(jar);
        
    }
    
    public void setDeployableUnit ( DeployableUnitIDImpl deployableUnit) {
        this.deployableUnitID = deployableUnit;
    }
    
    public DeployableUnitIDImpl getDeployableUnit() {
        return this.deployableUnitID;
    }



    /** Add a component to this descriptor.
     * 
     * @param componentID
     */
    public void addComponent(ComponentID componentID) {
        this.components.add(componentID);  
    }

    /**
     * Set the list of JAR Nodes as they appear in the XML Descriptor. Used during parsing.
     * @param jnodes
     */
	public void setJarNodes(Collection jnodes) {
		this.jarNodes = jnodes;
	}

    /**
     * @return the list of JAR Nodes as they appear in the XML Descriptor. 
     */
	public Collection getJarNodes() {
		return this.jarNodes;
	}

    /**
     * Set the list of Service Nodes as they appear in the XML Descriptor. Used during parsing.
     * @param snodes
     */
	public void setServiceNodes(Collection snodes) {
		this.serviceNodes = snodes;
	}

    /**
     * @return the list of Service Nodes as they appear in the XML Descriptor. 
     */
	public Collection getServiceNodes() {
		return this.serviceNodes;
	}

	public void installComponent(ComponentID cid)
	{
		if(!hasInstalledComponent(cid))
			installedComponents.add(cid);
			
	}
  
   public void uninstallComponent(ComponentID cid)
   {
	   installedComponents.remove(cid);
   }
   
   public boolean hasInstalledComponent(ComponentID cid)
   {
	   return installedComponents.contains(cid);
   }
   
   public ComponentID[] getInstalledComponents()
   {
	   ComponentID[] retval = new ComponentID[ installedComponents.size()];
	   installedComponents.toArray(retval);
       return retval;
   }
   
}
