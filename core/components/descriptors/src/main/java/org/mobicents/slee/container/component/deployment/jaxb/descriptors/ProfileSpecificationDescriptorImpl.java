/**
 * Start time:13:41:11 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermision;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MEnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MIndexedAttribue;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpecCollator;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpecManagementAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpecProfileCMPInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpecProfileLocalInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpecProfileManagementInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileSpecProfileTableInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQuery;
import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileIndex;
import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpec;
import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpecJar;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Collator;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Query;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.SecurityPermissions;
import org.w3c.dom.Document;

/**
 * Start time:13:41:11 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecificationDescriptorImpl extends JAXBBaseUtilityClass{

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar llProfileSpecJar = null;
	private ProfileSpecJar profileSpecJar = null;
	private int index = -1;


	// 1.0 stuff + some 1.1
	private String description = null;
	// name/vendor/version
	private ProfileSpecificationID profileSpecificationID = null;

	private MProfileSpecProfileCMPInterface profileCMPInterface = null;
	// This could be string, but lets be consistent
	private MProfileSpecProfileManagementInterface profileManagementInterface = null;
	private MProfileSpecManagementAbstractClass profileAbstractClass = null;
	// This possibly should also be object?
	private Set<MIndexedAttribue> indexedAttributes = null;
	// FIXME: add hints here?

	// 1.1 Stuff
	private Set<ProfileSpecificationID> profileSpecRefs = null;
	private Set<LibraryID> libraryRefs = null;
	private List<MProfileSpecCollator> collators = null;

	private MProfileSpecProfileTableInterface profileTableInterface = null;
	private MUsageParametersInterface profileUsageParameterInterface = null;
	private List<MEnvEntry> envEntries = null;
	private List<MQuery> queryElements = null;
	private MProfileSpecProfileLocalInterface profileLocalInterface = null;
	private boolean profileHints = false;
	private String readOnly = null;
	private String eventsEnabled = null;
	// those are profile-spec jar wide, so we include in each descriptor :)

	// private SecurityPermision securityPermisions=null;
	private MSecurityPermision securityPremissions = null;


	/**
	 * @param doc
	 * @throws DeploymentException
	 */
	private ProfileSpecificationDescriptorImpl(Document doc){
		super(doc);

	}

	private ProfileSpecificationDescriptorImpl(Document doc,
			ProfileSpecJar profileSpecJar, int index){
		super(doc);

		this.index = index;
		this.profileSpecJar = profileSpecJar;
		buildDescriptionMap();

	}

	private ProfileSpecificationDescriptorImpl(
			Document doc,
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar llProfileSpecJar,
			int index) throws DeploymentException {
		super(doc);
		this.index = index;
		this.llProfileSpecJar = llProfileSpecJar;
		buildDescriptionMap();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.component.deployment.jaxb.descriptors.
	 * JAXBBaseUtilityClass#buildDescriptionMap()
	 */
	@Override
	public void buildDescriptionMap()  {
		if (isSlee11()) {
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpec specs = this.llProfileSpecJar
					.getProfileSpec().get(index);
			this.description = specs.getDescription() != null ? specs
					.getDescription().getvalue() : null;
			this.profileSpecificationID = new ProfileSpecificationID(specs.getProfileSpecName()
					.getvalue(), specs.getProfileSpecVendor().getvalue(), specs
					.getProfileSpecVersion().getvalue());

			this.readOnly = specs.getProfileReadOnly();
			this.eventsEnabled = specs.getProfileEventsEnabled();
			// Here we ignore description elements for now :)
			this.libraryRefs = new HashSet<LibraryID>();
			if (specs.getLibraryRef() != null
					&& specs.getLibraryRef().size() > 0) {

				for (LibraryRef ref : specs.getLibraryRef()) {
					libraryRefs.add(new LibraryID(ref.getLibraryName().getvalue(),ref.getLibraryVendor().getvalue(),ref.getLibraryVersion().getvalue()));
				}
			}

			this.profileSpecRefs = new HashSet<ProfileSpecificationID>();
			if (specs.getProfileSpecRef() != null
					&& specs.getProfileSpecRef().size() > 0) {
				for (ProfileSpecRef ref : specs.getProfileSpecRef()) {					
					profileSpecRefs.add(new ProfileSpecificationID(ref.getProfileSpecName().getvalue(),ref.getProfileSpecVendor().getvalue(),ref.getProfileSpecVersion().getvalue()));
				}
			}

			// Collator, what ever that is
			this.collators = new ArrayList<MProfileSpecCollator>();
			if (specs.getCollator() != null && specs.getCollator().size() > 0) {
				for (Collator collator : specs.getCollator()) {
					this.collators.add(new MProfileSpecCollator(collator));
				}
			}

			// Obligatory
			this.profileCMPInterface = new MProfileSpecProfileCMPInterface(specs
					.getProfileClasses().getProfileCmpInterface());

			// Optional
			if (specs.getProfileClasses().getProfileLocalInterface() != null) {
				this.profileLocalInterface = new MProfileSpecProfileLocalInterface(
						specs.getProfileClasses().getProfileLocalInterface());
			}
			// Optional
			if (specs.getProfileClasses().getProfileManagementInterface() != null) {
				this.profileManagementInterface = new MProfileSpecProfileManagementInterface(
						specs.getProfileClasses()
								.getProfileManagementInterface());
			}

			// Optional
			if (specs.getProfileClasses().getProfileAbstractClass() != null) {
				this.profileAbstractClass = new MProfileSpecManagementAbstractClass(
						specs.getProfileClasses().getProfileAbstractClass());
			}

			// Optional
			if (specs.getProfileClasses().getProfileTableInterface() != null) {
				this.profileTableInterface = new MProfileSpecProfileTableInterface(
						specs.getProfileClasses().getProfileTableInterface());
			}

			// Optional
			this.envEntries = new ArrayList<MEnvEntry>();
			if (specs.getEnvEntry() != null && specs.getEnvEntry().size() > 0) {
				for (org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry entry : specs
						.getEnvEntry()) {
					this.envEntries.add(new MEnvEntry(entry));
				}
			}

			// Optional
			this.queryElements = new ArrayList<MQuery>();
			if (specs.getQuery() != null && specs.getQuery().size() > 0) {
				for (Query q : specs.getQuery()) {
					this.queryElements.add(new MQuery(q));
				}
			}

			if (specs.getProfileHints() != null) {
				this.profileHints = Boolean.parseBoolean(specs.getProfileHints().getSingleProfile());
			}

			if (this.llProfileSpecJar.getSecurityPermissions() != null) {
				SecurityPermissions secPerm = this.llProfileSpecJar
						.getSecurityPermissions();
				this.securityPremissions = new MSecurityPermision(secPerm
						.getDescription() == null ? null : secPerm
						.getDescription().getvalue(), secPerm
						.getSecurityPermissionSpec().getvalue());
			}
			
			//Optional
			if(specs.getProfileClasses().getProfileUsageParametersInterface()!=null)
			{
				this.profileUsageParameterInterface=new MUsageParametersInterface(specs.getProfileClasses().getProfileUsageParametersInterface());
			}

		} else {
			ProfileSpec specs = this.profileSpecJar.getProfileSpec().get(index);
			// FIXME: should catch Runtime and throw Deployment
			this.description = specs.getDescription() != null ? specs
					.getDescription().getvalue() : null;
			this.profileSpecificationID = new ProfileSpecificationID(specs.getProfileSpecName()
					.getvalue(), specs.getProfileSpecVendor().getvalue(), specs
					.getProfileSpecVersion().getvalue());

			// Obligatory
			this.profileCMPInterface = new MProfileSpecProfileCMPInterface(specs
					.getProfileClasses().getProfileCmpInterfaceName());

			// Optional
			if (specs.getProfileClasses().getProfileManagementInterfaceName() != null)
				this.profileManagementInterface = new MProfileSpecProfileManagementInterface(
						specs.getProfileClasses()
								.getProfileManagementInterfaceName());

			// Optional
			if (specs.getProfileClasses()
					.getProfileManagementAbstractClassName() != null)
				this.profileAbstractClass = new MProfileSpecManagementAbstractClass(
						specs.getProfileClasses()
								.getProfileManagementAbstractClassName());

			this.indexedAttributes = new HashSet<MIndexedAttribue>();
			if (specs.getProfileIndex() != null
					&& specs.getProfileIndex().size() > 0) {
				for (ProfileIndex index : specs.getProfileIndex()) {
					this.indexedAttributes.add(new MIndexedAttribue(index));
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.component.deployment.jaxb.descriptors.
	 * JAXBBaseUtilityClass#getJAXBDescriptor()
	 */
	@Override
	public Object getJAXBDescriptor() {
		return this.isSlee11() ? this.llProfileSpecJar : this.profileSpecJar;
	}

	

	

	/**
	 * Profile specs document contains multiple profile-specs. This method
	 * converts them into multiple instances of descriptors - its language, no
	 * constructor can return mulitple values
	 * 
	 * @param profileSpecs
	 * @return
	 */
	public static ProfileSpecificationDescriptorImpl[] parseDocument(
			Document profileSpecs, DeployableUnitID duID)
			throws DeploymentException {
		if (isDoctypeSlee11(profileSpecs.getDoctype())) {
			try {
				org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar psj = (org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar) JAXBBaseUtilityClass
						.getUnmarshaller(false).unmarshal(profileSpecs);
				if (psj.getProfileSpec() == null
						|| psj.getProfileSpec().size() == 0) {
					// Akward
					throw new ParseException(
							"No elements to parse in profile-jar descriptor", 0);
				}
				ProfileSpecificationDescriptorImpl[] table = new ProfileSpecificationDescriptorImpl[psj
						.getProfileSpec().size()];
				for (int i = 0; i < psj.getProfileSpec().size(); i++) {
					table[i] = new ProfileSpecificationDescriptorImpl(
							profileSpecs, psj, i);
				}
				return table;
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new DeploymentException(
						"Failed to parse xml descriptor of a profile jar due to: ",
						e);
			}

		} else {
			try {
				
				ProfileSpecJar psj = (ProfileSpecJar) JAXBBaseUtilityClass
						.getUnmarshaller(true).unmarshal(profileSpecs);
				if (psj.getProfileSpec() == null
						|| psj.getProfileSpec().size() == 0) {
					// Akward
					throw new ParseException(
							"No elements to parse in profile-jar descriptor", 0);
				}
				ProfileSpecificationDescriptorImpl[] table = new ProfileSpecificationDescriptorImpl[psj
						.getProfileSpec().size()];
				for (int i = 0; i < psj.getProfileSpec().size(); i++) {
					table[i] = new ProfileSpecificationDescriptorImpl(
							profileSpecs, psj, i);
				}
				return table;
			} catch (Exception e) {
				e.printStackTrace();
				throw new DeploymentException(
						"Failed to parse xml descriptor of a profile jar due to: ",
						e);
			}
		}
	}

	public Set<LibraryID> getLibraryRefs() {
		return libraryRefs;
	}

	public int getIndex() {
		return index;
	}

	public String getDescription() {
		return description;
	}

	public ProfileSpecificationID getProfileSpecificationID() {
		return profileSpecificationID;
	}

	public MProfileSpecProfileCMPInterface getProfileCMPInterface() {
		return profileCMPInterface;
	}

	public MProfileSpecProfileManagementInterface getProfileManagementInterface() {
		return profileManagementInterface;
	}

	public MProfileSpecManagementAbstractClass getProfileAbstractClass() {
		return profileAbstractClass;
	}

	public Set<MIndexedAttribue> getIndexedAttributes() {
		return indexedAttributes;
	}

	public Set<ProfileSpecificationID> getProfileSpecRefs() {
		return profileSpecRefs;
	}

	public List<MProfileSpecCollator> getCollators() {
		return collators;
	}

	public MProfileSpecProfileTableInterface getProfileTableInterface() {
		return profileTableInterface;
	}

	public MUsageParametersInterface getProfileUsageParameterInterface() {
		return profileUsageParameterInterface;
	}

	public List<MEnvEntry> getEnvEntries() {
		return envEntries;
	}

	public List<MQuery> getQueryElements() {
		return queryElements;
	}

	public MProfileSpecProfileLocalInterface getProfileLocalInterface() {
		return profileLocalInterface;
	}

	public boolean getProfileHints() {
		return profileHints;
	}

	public String getReadOnly() {
		return readOnly;
	}

	public String getEventsEnabled() {
		return eventsEnabled;
	}

	public MSecurityPermision getSecurityPremissions() {
		return securityPremissions;
	}


	
	
	
}
