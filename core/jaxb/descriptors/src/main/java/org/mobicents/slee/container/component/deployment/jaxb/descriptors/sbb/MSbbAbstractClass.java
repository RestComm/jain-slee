/**
 * Start time:11:19:14 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.CmpField;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetProfileCmpMethod;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetChildRelationMethod;





/**
 * Start time:11:19:14 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbAbstractClass {

	private org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbAbstractClass sAbstractClass=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbAbstractClass llsAbstractClass=null;
	
	private boolean reentrant=false;
	private String description=null;
	private String sbbAbstractClassName=null;
	private Map<String,MSbbCMPField> cmpFields=null;
	//it shoudl be getProfileCMPMethods -- but getter would be getGetxxx
	private ArrayList<MGetProfileCMPMethod> profileCMPMethods=null;
	//it shoudl be getChildRelationMethods -- but getter would be getGetxxx
	private ArrayList<MGetChildRelationMethod> childRelationMethods=null;
	public MSbbAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbAbstractClass sbbAbstractClass) {
		super();
		this.sAbstractClass = sbbAbstractClass;
		this.description=this.sAbstractClass.getDescription()==null?null:this.sAbstractClass.getDescription().getvalue();
		this.sbbAbstractClassName=this.sAbstractClass.getSbbAbstractClassName().getvalue();
		
		this.cmpFields=new HashMap<String, MSbbCMPField>();
		if(this.sAbstractClass.getCmpField()!=null)
			for(CmpField cf:this.sAbstractClass.getCmpField())
			{
				MSbbCMPField scf=new MSbbCMPField(cf);
				this.cmpFields.put(scf.getCmpFieldName(),scf);
			}
		
		this.profileCMPMethods=new ArrayList<MGetProfileCMPMethod>();
		if(this.sAbstractClass.getGetProfileCmpMethod()!=null)
		{
			for(GetProfileCmpMethod gpc:this.sAbstractClass.getGetProfileCmpMethod())
			{
				MGetProfileCMPMethod mgpcm=new MGetProfileCMPMethod(gpc);
				this.profileCMPMethods.add(mgpcm);
			}
		}
		
		this.childRelationMethods=new ArrayList<MGetChildRelationMethod>();
		if(this.sAbstractClass.getGetChildRelationMethod()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetChildRelationMethod gcrm:this.sAbstractClass.getGetChildRelationMethod())
			{
				MGetChildRelationMethod mg=new MGetChildRelationMethod(gcrm);
				this.childRelationMethods.add(mg);
			}
		}
		
		String v=this.sAbstractClass.getReentrant();
		if(v==null || !Boolean.parseBoolean(v))
		{
			this.reentrant=false;
		}else
		{
			this.reentrant=true;
		}
	}
	public MSbbAbstractClass(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbAbstractClass llSbbAbstractClass) {
		super();
		this.llsAbstractClass = llSbbAbstractClass;
		
		this.description=this.llsAbstractClass.getDescription()==null?null:this.llsAbstractClass.getDescription().getvalue();
		this.sbbAbstractClassName=this.llsAbstractClass.getSbbAbstractClassName().getvalue();
		
		this.cmpFields=new HashMap<String, MSbbCMPField>();
		if(this.llsAbstractClass.getCmpField()!=null)
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.CmpField cf:this.llsAbstractClass.getCmpField())
			{
				MSbbCMPField scf=new MSbbCMPField(cf);
				this.cmpFields.put(scf.getCmpFieldName(),scf);
			}
		
		this.profileCMPMethods=new ArrayList<MGetProfileCMPMethod>();
		if(this.llsAbstractClass.getGetProfileCmpMethod()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod gpc:this.llsAbstractClass.getGetProfileCmpMethod())
			{
				MGetProfileCMPMethod mgpcm=new MGetProfileCMPMethod(gpc);
				this.profileCMPMethods.add(mgpcm);
			}
		}
		
		this.childRelationMethods=new ArrayList<MGetChildRelationMethod>();
		if(this.llsAbstractClass.getGetChildRelationMethod()!=null)
		{
			for(GetChildRelationMethod gcrm:this.llsAbstractClass.getGetChildRelationMethod())
			{
				MGetChildRelationMethod mg=new MGetChildRelationMethod(gcrm);
				this.childRelationMethods.add(mg);
			}
		}
		
		String v=this.llsAbstractClass.getReentrant();
		if(v==null || !Boolean.parseBoolean(v))
		{
			this.reentrant=false;
		}else
		{
			this.reentrant=true;
		}
	}
	public boolean isReentrant() {
		return reentrant;
	}
	public String getDescription() {
		return description;
	}
	public String getSbbAbstractClassName() {
		return sbbAbstractClassName;
	}
	public Map<String, MSbbCMPField> getCmpFields() {
		return cmpFields;
	}
	public ArrayList<MGetProfileCMPMethod> getProfileCMPMethods() {
		return profileCMPMethods;
	}
	public ArrayList<MGetChildRelationMethod> getChildRelationMethods() {
		return childRelationMethods;
	}
	
	
	
	
}
