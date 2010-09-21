package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

import java.util.ArrayList;
import java.util.List;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.library.EventTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.library.LibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.library.ProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.library.ResourceAdaptorTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.library.SbbRef;
import org.mobicents.slee.container.component.library.JarDescriptor;

/**
 * 
 * MLibrary.java
 * 
 * <br>
 * Project: mobicents <br>
 * 3:19:02 AM Jan 30, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MLibrary {

	private String description;

	private List<LibraryID> libraryRefs = new ArrayList<LibraryID>();

	private List<EventTypeID> eventTypeRefs = new ArrayList<EventTypeID>();
	private List<ProfileSpecificationID> profileSpecRefs = new ArrayList<ProfileSpecificationID>();
	private List<ResourceAdaptorTypeID> raTypeRefs = new ArrayList<ResourceAdaptorTypeID>();
	private List<SbbID> sbbRefs = new ArrayList<SbbID>();

	private String libraryName;
	private String libraryVendor;
	private String libraryVersion;

	private List<JarDescriptor> jar = new ArrayList<JarDescriptor>();

	public MLibrary(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Library library11) {

		this.description = library11.getDescription() == null ? null
				: library11.getDescription().getvalue();

		for (LibraryRef libraryRef : library11.getLibraryRef()) {
			this.libraryRefs.add(new LibraryID(libraryRef.getLibraryName()
					.getvalue(), libraryRef.getLibraryVendor().getvalue(),
					libraryRef.getLibraryVersion().getvalue()));
		}

		for (EventTypeRef eventTypeRef : library11.getEventTypeRef()) {
			eventTypeRefs.add(new EventTypeID(eventTypeRef.getEventTypeName()
					.getvalue(), eventTypeRef.getEventTypeVendor().getvalue(),
					eventTypeRef.getEventTypeVersion().getvalue()));
		}

		for (ProfileSpecRef profileSpecRef : library11.getProfileSpecRef()) {
			profileSpecRefs.add(new ProfileSpecificationID(profileSpecRef
					.getProfileSpecName().getvalue(), profileSpecRef
					.getProfileSpecVendor().getvalue(), profileSpecRef
					.getProfileSpecVersion().getvalue()));
		}

		for (ResourceAdaptorTypeRef raTypeRef : library11
				.getResourceAdaptorTypeRef()) {
			raTypeRefs.add(new ResourceAdaptorTypeID(raTypeRef
					.getResourceAdaptorTypeName().getvalue(), raTypeRef
					.getResourceAdaptorTypeVendor().getvalue(), raTypeRef
					.getResourceAdaptorTypeVersion().getvalue()));
		}

		for (SbbRef sbbRef : library11.getSbbRef()) {
			sbbRefs.add(new SbbID(sbbRef.getSbbName().getvalue(), sbbRef
					.getSbbVendor().getvalue(), sbbRef.getSbbVersion()
					.getvalue()));
		}

		this.libraryName = library11.getLibraryName().getvalue();
		this.libraryVendor = library11.getLibraryVendor().getvalue();
		this.libraryVersion = library11.getLibraryVersion().getvalue();

		for (org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Jar jar11 : library11
				.getJar()) {
			this.jar.add(new MJar(jar11));
		}
	}

	public String getDescription() {
		return description;
	}

	public List<LibraryID> getLibraryRefs() {
		return libraryRefs;
	}

	public List<EventTypeID> getEventTypeRefs() {
		return eventTypeRefs;
	}

	public List<ProfileSpecificationID> getProfileSpecRefs() {
		return profileSpecRefs;
	}
	
	public List<ResourceAdaptorTypeID> getRaTypeRefs() {
		return raTypeRefs;
	}
	
	public List<SbbID> getSbbRefs() {
		return sbbRefs;
	}
	
	public String getLibraryName() {
		return libraryName;
	}

	public String getLibraryVendor() {
		return libraryVendor;
	}

	public String getLibraryVersion() {
		return libraryVersion;
	}

	public List<JarDescriptor> getJar() {
		return jar;
	}

}
