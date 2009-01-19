/**
 * Start time:16:22:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileCmpInterfaceName;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.CmpField;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileCmpInterface;

/**
 * This class represents CMP inteface element from profile-spec- definition
 * Start time:16:22:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecProfileCMPInterface {

	private ProfileCmpInterface llProfileCmpInterface = null;
	private ProfileCmpInterfaceName profileCmpInterface = null;
	private String cmpInterfaceClassName = null;
	private String description = null;
	private Map<String,ProfileSpecCMPField> cmpFields = null;


	public ProfileSpecProfileCMPInterface(
			ProfileCmpInterface llProfileCmpInterface)
			throws DeploymentException {
		this.llProfileCmpInterface = llProfileCmpInterface;
		if (this.llProfileCmpInterface == null
				|| this.llProfileCmpInterface.getProfileCmpInterfaceName() == null
				|| this.llProfileCmpInterface.getProfileCmpInterfaceName().getvalue()==null
				|| this.llProfileCmpInterface.getProfileCmpInterfaceName().getvalue().compareTo("")==0) {
			throw new DeploymentException(
			"CMP Interface class name can not be empty or null value.");
		}

		// init
		
		this.cmpInterfaceClassName=this.llProfileCmpInterface.getProfileCmpInterfaceName().getvalue();
		this.cmpFields=new HashMap<String,ProfileSpecCMPField>();
		if(this.llProfileCmpInterface.getCmpField()!=null && this.llProfileCmpInterface.getCmpField().size()>0)
		{
			for(int i=0;i<this.llProfileCmpInterface.getCmpField().size();i++)
			{
				CmpField cf=this.llProfileCmpInterface.getCmpField().get(i);
				if(cf.getCmpFieldName()==null||cf.getCmpFieldName().getvalue()==null||cf.getCmpFieldName().getvalue().compareTo("")==0)
				{
					throw new DeploymentException("CMP Field name can not be null or empty. Index: "+i);
				}
				this.cmpFields.put(cf.getCmpFieldName().getvalue(),new ProfileSpecCMPField(cf));
			}
		}
	}

	public ProfileSpecProfileCMPInterface(
			ProfileCmpInterfaceName profileCmpInterface)
			throws DeploymentException {
		this.profileCmpInterface = profileCmpInterface;
		// init
		if (profileCmpInterface == null
				|| profileCmpInterface.getvalue() == null
				|| profileCmpInterface.getvalue().compareTo("") == 0) {
			throw new DeploymentException(
					"CMP Interface class name can not be empty or null value.");
		}
		this.cmpInterfaceClassName = this.profileCmpInterface.getvalue();

	}

	public String getCmpInterfaceClassName() {
		return cmpInterfaceClassName;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, ProfileSpecCMPField> getCmpFields() {
		return cmpFields;
	}

	
	
}
