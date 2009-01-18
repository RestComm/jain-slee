/**
 * Start time:13:41:11 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

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

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.deployment.*;
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
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.SecurityPermision;
import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileIndex;
import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpec;
import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpecJar;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Collator;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Query;
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
		implements ProfileSpecificationDescriptor, DeployedComponent {

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
	// This possibly shoudl also be object?
	private Set<IndexedAttribue> indexedAttributes = null;
	//FIXME: add hints here?
	
	
	// 1.1 Stuff
	private ArrayList<ProfileSpecificationID> profileSpecReferences = null;
	private ArrayList<LibraryID> libraryRefs = null;
	private ArrayList<ProfileSpecCollator> collators = null;

	private ProfileSpecProfileTableInterface profileTableInterface = null;
	private ProfileSpecProfileUsageParameterInterface profileUsageParameterInterface = null;
	private ArrayList<EnvEntry> envEntries = null;
	private ArrayList<QueryElement> queryElements = null;
	private ProfileSpecProfileLocalInterface profileLocalInterface = null;
	//those are profile-spec jar wide, so we include in each descriptor :)
	
	private Set<SecurityPermision> securityPermisions=null;
	
	
	
	/**
	 * @param doc
	 * @throws DeploymentException
	 */
	private ProfileSpecificationDescriptorImpl(Document doc)
			throws DeploymentException {
		super(doc);

	}

	private ProfileSpecificationDescriptorImpl(Document doc,
			ProfileSpecJar profileSpecJar, int index)
			throws DeploymentException {
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
	public void buildDescriptionMap() throws DeploymentException {
		if (isSlee11()) {
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpec specs=this.llProfileSpecJar.getProfileSpec().get(index);
			this.description=specs.getDescription()!=null?specs.getDescription().getvalue():null;
			this.profileSpecKey = new ComponentKey(specs.getProfileSpecName()
					.getvalue(), specs.getProfileSpecVendor().getvalue(), specs
					.getProfileSpecVersion().getvalue());
			
			//Here we ignore description elements for now :)
			if(specs.getLibraryRef()!=null && specs.getLibraryRef().size()>0)
			{
				for(LibraryRef ref:specs.getLibraryRef())
				{
					//FIXME: add library ID
					//libraryRefs
				}
			}
			
			if(specs.getProfileSpecRef()!=null && specs.getProfileSpecRef().size()>0)
			{
				for(ProfileSpecRef ref:specs.getProfileSpecRef())
				{
					//FIXME: add profile ID
					//profileSpecReferences
				}
			}
			
			//Collator, what ever that is
			this.collators=new ArrayList<ProfileSpecCollator>();
			if(specs.getCollator()!=null && specs.getCollator().size()>0)
			{
				for(Collator collator:specs.getCollator())
				{
					this.collators.add(new ProfileSpecCollator(collator));
				}
			}
		
			//Obligatory
			this.profileCMPInterface=new ProfileSpecProfileCMPInterface(specs.getProfileClasses().getProfileCmpInterface());
			
			//Optional
			if(specs.getProfileClasses().getProfileLocalInterface()!=null)
			{
				this.profileLocalInterface=new ProfileSpecProfileLocalInterface(specs.getProfileClasses().getProfileLocalInterface());
			}
			//Optional
			if(specs.getProfileClasses().getProfileManagementInterface()!=null)
			{
				this.managementInterface=new ProfileSpecProfileManagementInterface(specs.getProfileClasses().getProfileManagementInterface());
			}
			
			//Optional
			if(specs.getProfileClasses().getProfileAbstractClass()!=null)
			{
				this.managementAbstractClass=new ProfileSpecManagementAbstractClass(specs.getProfileClasses().getProfileAbstractClass());
			}
			
			
			//Optional
			if(specs.getProfileClasses().getProfileTableInterface()!=null)
			{
				this.profileTableInterface=new ProfileSpecProfileTableInterface(specs.getProfileClasses().getProfileTableInterface());
			}
			
			//Optional
			this.envEntries=new ArrayList<EnvEntry>();
			if(specs.getEnvEntry()!=null && specs.getEnvEntry().size()>0)
			{
				for(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry entry:specs.getEnvEntry())
				{
					this.envEntries.add(new EnvEntry(entry));
				}
			}
			
			//Optional
			this.queryElements=new ArrayList<QueryElement>();
			if(specs.getQuery()!=null && specs.getQuery().size()>0)
			{
				for(Query q:specs.getQuery())
				{
					this.queryElements.add(new QueryElement(q));
				}
			}
		} else {
			ProfileSpec specs = this.profileSpecJar.getProfileSpec().get(index);
				//FIXME: should catch Runtime and throw Deployment			
			this.description=specs.getDescription()!=null?specs.getDescription().getvalue():null;
			this.profileSpecKey = new ComponentKey(specs.getProfileSpecName()
					.getvalue(), specs.getProfileSpecVendor().getvalue(), specs
					.getProfileSpecVersion().getvalue());

			//Obligatory
			this.profileCMPInterface=new ProfileSpecProfileCMPInterface(specs.getProfileClasses().getProfileCmpInterfaceName());
			
			//Optional
			if(specs.getProfileClasses().getProfileManagementInterfaceName()!=null)
				this.managementInterface=new ProfileSpecProfileManagementInterface(specs.getProfileClasses().getProfileManagementInterfaceName());
			
			//Optional
			if(specs.getProfileClasses().getProfileManagementAbstractClassName()!=null)
				this.managementAbstractClass=new ProfileSpecManagementAbstractClass(specs.getProfileClasses().getProfileManagementAbstractClassName());
	
			
			this.indexedAttributes=new HashSet<IndexedAttribue>();
			if(specs.getProfileIndex()!=null && specs.getProfileIndex().size()>0)
			{
				for(ProfileIndex index:specs.getProfileIndex())
				{
					this.indexedAttributes.add(new IndexedAttribue(index));
				}
			}
			
			//FIXME: Profile hints!!!!

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
		// TODO Auto-generated method stub
		return null;
	}

	public DeployableUnitID getDeployableUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	public ComponentID getID() {
		// TODO Auto-generated method stub
		return null;
	}

	public LibraryID[] getLibraries() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVendor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	public void checkDeployment() throws DeploymentException {
		// TODO Auto-generated method stub

	}

	public void setDeployableUnit(DeployableUnitID deployableUnitID) {
		// TODO Auto-generated method stub

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
			Document profileSpecs, DeployableUnitID duID) {
		return null;
	}

}
