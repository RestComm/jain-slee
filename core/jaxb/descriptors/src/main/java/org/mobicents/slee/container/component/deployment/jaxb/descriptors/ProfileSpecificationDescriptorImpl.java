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
import java.util.Map;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationDescriptor;
import javax.slee.profile.ProfileSpecificationID;
import javax.xml.bind.JAXBException;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.deployment.*;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.SecurityPermision;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.EnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.IndexedAttribue;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.ProfileSpecCollator;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.ProfileSpecManagementAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.ProfileSpecProfileCMPInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.ProfileSpecProfileLocalInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.ProfileSpecProfileManagementInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.ProfileSpecProfileTableInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.ProfileSpecProfileUsageParameterInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.QueryElement;
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
public class ProfileSpecificationDescriptorImpl extends JAXBBaseUtilityClass
		implements DeployedComponent {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar llProfileSpecJar = null;
	private ProfileSpecJar profileSpecJar = null;
	private int index = -1;
	private DeployableUnitID deployableUnitID;

	// 1.0 stuff + some 1.1
	private String description = null;
	// name/vendor/version
	private ComponentKey profileSpecKey = null;

	private ProfileSpecProfileCMPInterface profileCMPInterface = null;
	// This could be string, but lets be consistent
	private ProfileSpecProfileManagementInterface managementInterface = null;
	private ProfileSpecManagementAbstractClass managementAbstractClass = null;
	// This possibly should also be object?
	private Set<IndexedAttribue> indexedAttributes = null;
	// FIXME: add hints here?

	// 1.1 Stuff
	private ArrayList<ProfileSpecificationID> profileSpecReferences = null;
	private ArrayList<LibraryID> libraryRefs = null;
	private ArrayList<ProfileSpecCollator> collators = null;

	private ProfileSpecProfileTableInterface profileTableInterface = null;
	private ProfileSpecProfileUsageParameterInterface profileUsageParameterInterface = null;
	private ArrayList<EnvEntry> envEntries = null;
	private ArrayList<QueryElement> queryElements = null;
	private ProfileSpecProfileLocalInterface profileLocalInterface = null;
	private String profileHints = null;
	private String readOnly = null;
	private String eventsEnabled = null;
	// those are profile-spec jar wide, so we include in each descriptor :)

	// private SecurityPermision securityPermisions=null;
	private SecurityPermision securityPremissions = null;

	// Other:
	private String source = null;
	private ComponentID componentID = null;

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
			this.profileSpecKey = new ComponentKey(specs.getProfileSpecName()
					.getvalue(), specs.getProfileSpecVendor().getvalue(), specs
					.getProfileSpecVersion().getvalue());

			this.readOnly = specs.getProfileReadOnly();
			this.eventsEnabled = specs.getProfileEventsEnabled();
			// Here we ignore description elements for now :)
			this.libraryRefs = new ArrayList<LibraryID>();
			if (specs.getLibraryRef() != null
					&& specs.getLibraryRef().size() > 0) {

				for (LibraryRef ref : specs.getLibraryRef()) {
					// FIXME: add library ID
					// libraryRefs
				}
			}

			this.profileSpecReferences = new ArrayList<ProfileSpecificationID>();
			if (specs.getProfileSpecRef() != null
					&& specs.getProfileSpecRef().size() > 0) {
				for (ProfileSpecRef ref : specs.getProfileSpecRef()) {
					// FIXME: add profile ID
					// profileSpecReferences
				}
			}

			// Collator, what ever that is
			this.collators = new ArrayList<ProfileSpecCollator>();
			if (specs.getCollator() != null && specs.getCollator().size() > 0) {
				for (Collator collator : specs.getCollator()) {
					this.collators.add(new ProfileSpecCollator(collator));
				}
			}

			// Obligatory
			this.profileCMPInterface = new ProfileSpecProfileCMPInterface(specs
					.getProfileClasses().getProfileCmpInterface());

			// Optional
			if (specs.getProfileClasses().getProfileLocalInterface() != null) {
				this.profileLocalInterface = new ProfileSpecProfileLocalInterface(
						specs.getProfileClasses().getProfileLocalInterface());
			}
			// Optional
			if (specs.getProfileClasses().getProfileManagementInterface() != null) {
				this.managementInterface = new ProfileSpecProfileManagementInterface(
						specs.getProfileClasses()
								.getProfileManagementInterface());
			}

			// Optional
			if (specs.getProfileClasses().getProfileAbstractClass() != null) {
				this.managementAbstractClass = new ProfileSpecManagementAbstractClass(
						specs.getProfileClasses().getProfileAbstractClass());
			}

			// Optional
			if (specs.getProfileClasses().getProfileTableInterface() != null) {
				this.profileTableInterface = new ProfileSpecProfileTableInterface(
						specs.getProfileClasses().getProfileTableInterface());
			}

			// Optional
			this.envEntries = new ArrayList<EnvEntry>();
			if (specs.getEnvEntry() != null && specs.getEnvEntry().size() > 0) {
				for (org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry entry : specs
						.getEnvEntry()) {
					this.envEntries.add(new EnvEntry(entry));
				}
			}

			// Optional
			this.queryElements = new ArrayList<QueryElement>();
			if (specs.getQuery() != null && specs.getQuery().size() > 0) {
				for (Query q : specs.getQuery()) {
					this.queryElements.add(new QueryElement(q));
				}
			}

			if (specs.getProfileHints() != null) {
				this.profileHints = specs.getProfileHints().getSingleProfile();
			}

			if (this.llProfileSpecJar.getSecurityPermissions() != null) {
				SecurityPermissions secPerm = this.llProfileSpecJar
						.getSecurityPermissions();
				this.securityPremissions = new SecurityPermision(secPerm
						.getDescription() == null ? null : secPerm
						.getDescription().getvalue(), secPerm
						.getSecurityPermissionSpec().getvalue());
			}
			
			//Optional
			if(specs.getProfileClasses().getProfileUsageParametersInterface()!=null)
			{
				this.profileUsageParameterInterface=new ProfileSpecProfileUsageParameterInterface(specs.getProfileClasses().getProfileUsageParametersInterface());
			}

		} else {
			ProfileSpec specs = this.profileSpecJar.getProfileSpec().get(index);
			// FIXME: should catch Runtime and throw Deployment
			this.description = specs.getDescription() != null ? specs
					.getDescription().getvalue() : null;
			this.profileSpecKey = new ComponentKey(specs.getProfileSpecName()
					.getvalue(), specs.getProfileSpecVendor().getvalue(), specs
					.getProfileSpecVersion().getvalue());

			// Obligatory
			this.profileCMPInterface = new ProfileSpecProfileCMPInterface(specs
					.getProfileClasses().getProfileCmpInterfaceName());

			// Optional
			if (specs.getProfileClasses().getProfileManagementInterfaceName() != null)
				this.managementInterface = new ProfileSpecProfileManagementInterface(
						specs.getProfileClasses()
								.getProfileManagementInterfaceName());

			// Optional
			if (specs.getProfileClasses()
					.getProfileManagementAbstractClassName() != null)
				this.managementAbstractClass = new ProfileSpecManagementAbstractClass(
						specs.getProfileClasses()
								.getProfileManagementAbstractClassName());

			this.indexedAttributes = new HashSet<IndexedAttribue>();
			if (specs.getProfileIndex() != null
					&& specs.getProfileIndex().size() > 0) {
				for (ProfileIndex index : specs.getProfileIndex()) {
					this.indexedAttributes.add(new IndexedAttribue(index));
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

	public String getCMPInterfaceName() {
		return profileCMPInterface.getCmpInterfaceClassName();
	}

	public DeployableUnitID getDeployableUnit() {
		return deployableUnitID;
	}

	public ComponentID getID() {
		return componentID;
	}

	public void setID(ComponentID componentID) {
		this.componentID = componentID;
	}

	public LibraryID[] getLibraries() {
		return this.libraryRefs.toArray(new LibraryID[libraryRefs.size()]);

	}

	public String getName() {
		return this.profileSpecKey.getName();
	}

	public String getVendor() {
		return this.profileSpecKey.getVendor();
	}

	public String getVersion() {
		return this.profileSpecKey.getVersion();
	}

	public void checkDeployment() throws DeploymentException {
		// TODO Auto-generated method stub

	}

	public void setDeployableUnit(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;

	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDescription() {
		return description;
	}

	public ComponentKey getProfileSpecKey() {
		return profileSpecKey;
	}

	public ProfileSpecProfileCMPInterface getProfileCMPInterface() {
		return profileCMPInterface;
	}

	public ProfileSpecProfileManagementInterface getManagementInterface() {
		return managementInterface;
	}

	public ProfileSpecManagementAbstractClass getManagementAbstractClass() {
		return managementAbstractClass;
	}

	public Set<IndexedAttribue> getIndexedAttributes() {
		return indexedAttributes;
	}

	public ArrayList<ProfileSpecificationID> getProfileSpecReferences() {
		return profileSpecReferences;
	}

	public ArrayList<LibraryID> getLibraryRefs() {
		return libraryRefs;
	}

	public ArrayList<ProfileSpecCollator> getCollators() {
		return collators;
	}

	public ProfileSpecProfileTableInterface getProfileTableInterface() {
		return profileTableInterface;
	}

	public ProfileSpecProfileUsageParameterInterface getProfileUsageParameterInterface() {
		return profileUsageParameterInterface;
	}

	public ArrayList<EnvEntry> getEnvEntries() {
		return envEntries;
	}

	public ArrayList<QueryElement> getQueryElements() {
		return queryElements;
	}

	public ProfileSpecProfileLocalInterface getProfileLocalInterface() {
		return profileLocalInterface;
	}

	public String getProfileHints() {
		return profileHints;
	}

	public String getReadOnly() {
		return readOnly;
	}

	public String getEventsEnabled() {
		return eventsEnabled;
	}

	public SecurityPermision getSecurityPremissions() {
		return securityPremissions;
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
						.getUnmarshaller().unmarshal(profileSpecs);
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

				throw new DeploymentException(
						"Failed to parse xml descriptor of a profile jar due to: ",
						e);
			}

		} else {
			try {
				ProfileSpecJar psj = (ProfileSpecJar) JAXBBaseUtilityClass
						.getUnmarshaller().unmarshal(profileSpecs);
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

				throw new DeploymentException(
						"Failed to parse xml descriptor of a profile jar due to: ",
						e);
			}
		}
	}

}
