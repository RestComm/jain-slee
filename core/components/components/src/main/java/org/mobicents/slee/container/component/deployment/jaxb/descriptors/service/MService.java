package org.mobicents.slee.container.component.deployment.jaxb.descriptors.service;

/**
 * 
 * MService.java
 * 
 * <br>
 * Project: mobicents <br>
 * 3:45:00 AM Feb 12, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MService {

	private boolean isSlee11;

	private String description;

	private String serviceName;
	private String serviceVendor;
	private String serviceVersion;

	private MRootSbb rootSbb;

	private byte defaultPriority = 0;

	private String addressProfileTable;
	private String resourceInfoProfileTable;

	public MService(org.mobicents.slee.container.component.deployment.jaxb.slee.service.Service service10) {
		this.isSlee11 = false;
		this.description = service10.getDescription() == null ? null : service10.getDescription().getvalue();

		this.serviceName = service10.getServiceName().getvalue();
		this.serviceVendor = service10.getServiceVendor().getvalue();
		this.serviceVersion = service10.getServiceVersion().getvalue();

		this.rootSbb = new MRootSbb(service10.getRootSbb());

		this.defaultPriority = Byte.parseByte(service10.getDefaultPriority().getvalue());

		this.addressProfileTable = service10.getAddressProfileTable() == null ? null : service10.getAddressProfileTable().getvalue();
		this.resourceInfoProfileTable = service10.getResourceInfoProfileTable() == null ? null : service10.getResourceInfoProfileTable().getvalue();
	}

	public MService(org.mobicents.slee.container.component.deployment.jaxb.slee11.service.Service service11) {
		this.isSlee11 = true;
		this.description = service11.getDescription() == null ? null : service11.getDescription().getvalue();

		this.serviceName = service11.getServiceName().getvalue();
		this.serviceVendor = service11.getServiceVendor().getvalue();
		this.serviceVersion = service11.getServiceVersion().getvalue();

		this.rootSbb = new MRootSbb(service11.getRootSbb());

		this.defaultPriority = Byte.parseByte(service11.getDefaultPriority().getvalue());

		this.addressProfileTable = service11.getAddressProfileTable() == null ? null : service11.getAddressProfileTable().getvalue();

		// JAIN SLEE Specification (3.4.1, p45):
		// A resource-info-profile-table element.
		// Deprecated in 1.1: The Resource Info Profile Table has been
		// deprecated The Resource Adaptor
		// architecture defined herein describes how resource adaptors may
		// interact with Profile Tables and
		// Profiles. SBB and/or Service developers should avoid the use of the
		// Resource Info Profile Table as
		// it may be removed in a future version of this specification.
		// This element is optional. This table contains provisioned data that
		// should be passed to resource
		// adaptor entities when the Service is in running. This element
		// specifies the Resource Info Profile
		// Table of the Service. The Profile Specification of this Profile Table
		// must be the SLEE specifica-
		// tion defined Resource Info Profile Specification.
		//
		// ... but DTD doesn't allow it. FIXME: Support it in case DTD changes.
		// this.resourceInfoProfileTable =
		// service11.getResourceInfoProfileTable() == null ? null :
		// service11.getResourceInfoProfileTable().getvalue();
	}

	public String getDescription() {
		return description;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceVendor() {
		return serviceVendor;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public MRootSbb getRootSbb() {
		return rootSbb;
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

	public boolean isSlee11() {
		return isSlee11;
	}
}
