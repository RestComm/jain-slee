package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeploymentException;
import javax.xml.bind.JAXBException;

import org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.Jar;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.ServiceXml;
import org.w3c.dom.Document;

public class DeployableUnitDescriptorImpl extends JAXBBaseUtilityClass
		implements DeployableUnitDescriptor {

	private DeployableUnit duDescriptor10 = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit duDescriptor11 = null;

	
	private List<String> jarEntries=new ArrayList<String>();
	private List<String> serviceEndtries=new ArrayList<String>();
	public DeployableUnitDescriptorImpl(Document document)
			throws DeploymentException {

		super(document);
		// Here we have to parse
//		try {
//			if (isSlee11()) {
//				duDescriptor11 = (org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit) getUnmarshaller()
//						.unmarshal(super.desccriptorDocument);
//			} else {
//				duDescriptor10 = (DeployableUnit) getUnmarshaller().unmarshal(
//						super.desccriptorDocument);
//			}
//			buildDescriptionMap();
//		} catch (JAXBException jaxbe) {
//
//			throw new DeploymentException(
//					"Failed to parse descriptor due to: ", jaxbe);
//		} catch(RuntimeException re)
//		{
//			throw new DeploymentException(
//					"Failed to parse descriptor due to: ", re);
//		}
	}

	public ComponentID[] getComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDeploymentDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildDescriptionMap() throws DeploymentException{
		
		//This is akward, since we have two classes with the same name in different package
		//We could use reflections but it would a killer in case of event definitions and such ;[
		try {
			for(Object o: (this.isSlee11()?this.duDescriptor11.getJarOrServiceXml():this.duDescriptor10.getJarOrServiceXml()))
			{
				if(o.getClass().getCanonicalName().contains("Jar"))
				{
					String v=null;
					if(this.isSlee11())
					{
						Jar j=(Jar) o;
						v=j.getvalue();
					}else
					{
						org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar j=(org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar) o;
						v=j.getvalue();
					}	
					
					this.jarEntries.add(v);
			
				}else if(o.getClass().getCanonicalName().contains("Service"))
				{
					String v=null;
					if(this.isSlee11())
					{
						ServiceXml j=(ServiceXml) o;
						v=j.getvalue();
					}else
					{
						org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml j=(org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml) o;
						v=j.getvalue();
					}	
					this.serviceEndtries.add(v);
				}else
				{
					logger.severe("Unknown jaxb element: "+o.getClass());
				}
			}
		} catch (Exception e) {
			throw new DeploymentException(
					"Failed to build descriptors map: ", e);
		} 
	}

}
