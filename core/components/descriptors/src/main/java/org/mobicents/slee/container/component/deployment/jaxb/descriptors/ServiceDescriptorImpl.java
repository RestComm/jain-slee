/**
 * Start time:17:15:13 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.text.ParseException;

import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.deployment.jaxb.slee.service.Service;
import org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml;

import org.w3c.dom.Document;

/**
 * Start time:17:15:13 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceDescriptorImpl extends JAXBBaseUtilityClass{

	private ServiceXml serviceXML = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml llServiceXML = null;

	private Service service = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.service.Service llService = null;
	private int index = -1;

	private String description = null;
	private ComponentKey rootSbb = null;
	private ComponentKey serviceKey=null;
	private byte defaultPriority = (byte) -10;
	private String addressProfileTable = null;
	// Depraceted in 1.1
	private String resourceInfoProfileTable = null;


	
	
	private ServiceDescriptorImpl(Document doc, ServiceXml serviceXML, int index) throws DeploymentException {
		super(doc);

		this.index = index;
		this.serviceXML = serviceXML;
		buildDescriptionMap();

	}

	private ServiceDescriptorImpl(
			Document doc,
			org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml serviceXML,
			int index) throws DeploymentException {
		super(doc);

		this.index = index;
		this.llServiceXML = serviceXML;
		buildDescriptionMap();

	}

	public static ServiceDescriptorImpl[] parseDocument(Document serviceJar,
			DeployableUnitID duID) throws DeploymentException {
		if (isDoctypeSlee11(serviceJar.getDoctype())) {
			try {
				org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml psj = (org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml) JAXBBaseUtilityClass
						.getUnmarshaller(false).unmarshal(serviceJar);
				if (psj.getService() == null || psj.getService().size() == 0) {
					// Akward
					throw new ParseException(
							"No elements to parse in sbb-jar descriptor", 0);
				}
				ServiceDescriptorImpl[] table = new ServiceDescriptorImpl[psj.getService()
						.size()];
				for (int i = 0; i < psj.getService().size(); i++) {
					table[i] = new ServiceDescriptorImpl(serviceJar, psj, i);
				}
				return table;
			} catch (Exception e) {

				throw new DeploymentException(
						"Failed to parse xml descriptor of a sbb jar due to: ",
						e);
			}

		} else {
			try {
				org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml psj = (org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml) JAXBBaseUtilityClass
						.getUnmarshaller(true).unmarshal(serviceJar);
				if (psj.getService() == null || psj.getService().size() == 0) {
					// Akward
					throw new ParseException(
							"No elements to parse in sbb-jar descriptor", 0);
				}
				ServiceDescriptorImpl[] table = new ServiceDescriptorImpl[psj.getService()
						.size()];
				for (int i = 0; i < psj.getService().size(); i++) {
					table[i] = new ServiceDescriptorImpl(serviceJar, psj, i);
				}
				return table;
			} catch (Exception e) {

				throw new DeploymentException(
						"Failed to parse xml descriptor of a sbb jar due to: ",
						e);
			}
		}
	}
	
	
	@Override
	public void buildDescriptionMap() {
		if(isSlee11())
		{
			this.llService=this.llServiceXML.getService().get(index);
			this.description = llService.getDescription() == null ? null
					: this.llService.getDescription().getvalue();
			this.rootSbb=new ComponentKey(this.llService.getRootSbb().getSbbName().getvalue(),this.llService.getRootSbb().getSbbVendor().getvalue(),this.llService.getRootSbb().getSbbVersion().getvalue());
			this.defaultPriority=Byte.parseByte(this.llService.getDefaultPriority().getvalue());
			this.serviceKey=new ComponentKey(this.llService.getServiceName().getvalue(),this.llService.getServiceVendor().getvalue(),this.llService.getServiceVersion().getvalue());
			//Optional
			if(this.llService.getAddressProfileTable()!=null)
			{
				this.addressProfileTable=this.llService.getAddressProfileTable().getvalue();
			}
			
		}else
		{
			this.service=this.serviceXML.getService().get(index);
			this.description = service.getDescription() == null ? null
					: this.service.getDescription().getvalue();
			this.rootSbb=new ComponentKey(this.service.getRootSbb().getSbbName().getvalue(),this.service.getRootSbb().getSbbVendor().getvalue(),this.service.getRootSbb().getSbbVersion().getvalue());
			this.defaultPriority=Byte.parseByte(this.service.getDefaultPriority().getvalue());
			this.serviceKey=new ComponentKey(this.service.getServiceName().getvalue(),this.service.getServiceVendor().getvalue(),this.service.getServiceVersion().getvalue());
			//Optional
			if(this.service.getAddressProfileTable()!=null)
			{
				this.addressProfileTable=this.service.getAddressProfileTable().getvalue();
			}
			if(this.service.getResourceInfoProfileTable()!=null)
			{
				this.resourceInfoProfileTable=this.service.getResourceInfoProfileTable().getvalue();
			}
		}

	}

	@Override
	public Object getJAXBDescriptor() {
		if(isSlee11())
		{
			return llService;
		}else
		{
			return service;
		}
	}


	public int getIndex() {
		return index;
	}

	public String getDescription() {
		return description;
	}

	public ComponentKey getRootSbb() {
		return rootSbb;
	}

	public ComponentKey getServiceKey() {
		return serviceKey;
	}

	public byte getDefaultPriority() {
		return defaultPriority;
	}

	public String getAddressProfileTable() {
		return addressProfileTable;
	}

	public String getResourceInfoProfileTable() {
		return resourceInfoProfileTable;
	}


	
}
