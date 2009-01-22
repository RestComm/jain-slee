package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.xml.bind.JAXBException;

import org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.Jar;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.ServiceXml;
import org.w3c.dom.Document;

public class DeployableUnitDescriptorImpl extends JAXBBaseUtilityClass
		 {

	private DeployableUnit duDescriptor = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit duDescriptorll = null;

	private List<String> jarEntries = new ArrayList<String>();
	private List<String> serviceEndtries = new ArrayList<String>();

	private DeployableUnitID duID = null;
	//FIXME: shouldn it be: java.net.URL ??
	private String url;
	private LinkedList components;
	private LinkedList installedComponents;
	private Date deploymentDate;
	private transient File tmpDeploymentDirectory;
	private transient File tmpDUJarsDirectory;

	// tmp, list of jars that already been deployed?
	private HashSet jars;


	public DeployableUnitDescriptorImpl(Document document)
			throws DeploymentException {

		super(document);
		// Here we have to parse
		try {
			if (isSlee11()) {
				duDescriptorll = (org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit) getUnmarshaller()
						.unmarshal(super.descriptorDocument);
			} else {
				duDescriptor = (DeployableUnit) getUnmarshaller().unmarshal(
						super.descriptorDocument);
			}
			buildDescriptionMap();
		} catch (JAXBException jaxbe) {

			throw new DeploymentException(
					"Failed to parse descriptor due to: ", jaxbe);
		} catch (RuntimeException re) {
			throw new DeploymentException(
					"Failed to parse descriptor due to: ", re);
		}
	}

	public ComponentID[] getComponents() {
		ComponentID[] retval = new ComponentID[components.size()];
		components.toArray(retval);
		return retval;
	}

	public String getURL() {
		return url;
	}

	public Date getDeploymentDate() {
		return deploymentDate;
	}

	public DeployableUnitID getDeployableUnitID() {
		return duID;
	}

	public void setDeployableUnitID(DeployableUnitID duID) {
		this.duID = duID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	/** Add a component to this descriptor.
	 * 
	 * @param componentID
	 */
	public void addComponent(ComponentID componentID) {
		this.components.add(componentID);
	}
	
	
	//TMP
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

	/**
	 * Add a jar to the descriptor
	 * @param jars
	 */
	public void addJar(String jar) {
		this.jars.add(jar);

	}
	
	
	@Override
	public void buildDescriptionMap() {

		// This is akward, since we have two classes with the same name in
		// different package
		// We could use reflections but it would a killer in case of event
		// definitions and such ;[

			for (Object o : (this.isSlee11() ? this.duDescriptorll
					.getJarOrServiceXml() : this.duDescriptor
					.getJarOrServiceXml())) {
				if (o.getClass().getCanonicalName().contains("Jar")) {
					String v = null;
					if (this.isSlee11()) {
						Jar j = (Jar) o;
						v = j.getvalue();
					} else {
						org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar j = (org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar) o;
						v = j.getvalue();
					}

					this.jarEntries.add(v);

				} else if (o.getClass().getCanonicalName().contains("Service")) {
					String v = null;
					if (this.isSlee11()) {
						ServiceXml j = (ServiceXml) o;
						v = j.getvalue();
					} else {
						org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml j = (org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml) o;
						v = j.getvalue();
					}
					this.serviceEndtries.add(v);
				} else {
					logger.severe("Unknown jaxb element: " + o.getClass());
				}
			}
	
	}

	@Override
	public Object getJAXBDescriptor() {
		return this.isSlee11() ? this.duDescriptorll : this.duDescriptor;
	}

}
