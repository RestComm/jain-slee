/**
 * Start time:17:44:15 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitID;

import org.apache.log4j.Logger;
//import org.mobicents.slee.container.component.deployment.DeployableUnitDeployer;

/**
 * Start time:17:44:15 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DUComponent {

	
	protected org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorImpl duDescriptor=null;
	protected static final transient Logger logger=Logger.getLogger(DUComponent.class.getName());
	

	
	
	private transient File tmpDeploymentDirectory;
	private transient File tmpDUJarsDirectory;
	
	private LinkedList<ComponentID> components;
	private LinkedList<ComponentID> installedComponents;
	private Date deploymentDate;
	
	
	/**
	 * src url from which DU has been installed
	 */
	private URL sourceURL=null;
	private DeployableUnitID deployableUnitID=null;
	
	
	private List<String> jars;
	
	
	
	/**
	 * Add a jar to the descriptor
	 * @param jars
	 */
	public void addJar(String jar) {
		this.jars.add(jar);

	}
	
	
	/**
	 * @return Returns the jars.
	 */
	public String[] getJars() {
		if (jars == null)
			return null;
		String[] jarArray = new String[jars.size()];
		jars.toArray(jarArray);
		return jarArray;
	}


	public File getTmpDeploymentDirectory() {
		return tmpDeploymentDirectory;
	}


	public void setTmpDeploymentDirectory(File tmpDeploymentDirectory) {
		this.tmpDeploymentDirectory = tmpDeploymentDirectory;
	}


	public File getTmpDUJarsDirectory() {
		return tmpDUJarsDirectory;
	}


	public void setTmpDUJarsDirectory(File tmpDUJarsDirectory) {
		this.tmpDUJarsDirectory = tmpDUJarsDirectory;
	}


	public URL getSourceURL() {
		return sourceURL;
	}


	public void setSourceURL(URL sourceURL) {
		this.sourceURL = sourceURL;
		this.deployableUnitID=new DeployableUnitID(sourceURL.getFile());
	}


	public org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorImpl getDuDescriptor() {
		return duDescriptor;
	}


	public Date getDeploymentDate() {
		return deploymentDate;
	}


	public DeployableUnitID getDeployableUnitID() {
		return deployableUnitID;
	}


	public ComponentID[] getComponents() {
		ComponentID[] retval = new ComponentID[components.size()];
		components.toArray(retval);
		return retval;
	}


	public ComponentID[] getInstalledComponents() {
		
		ComponentID[] retval = new ComponentID[installedComponents.size()];
		installedComponents.toArray(retval);
		return retval;
		
	}


	
}
