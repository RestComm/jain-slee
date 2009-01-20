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

	private org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbAbstractClass sbbAbstractClass=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbAbstractClass llSbbAbstractClass=null;
	
	private boolean reentrant=false;
	private String description=null;
	private String sbbAbstractClassName=null;
	private Map<String,MSbbCMPField> cmpFields=null;
	private ArrayList<MGetProfileCMPMethod> profileCMPMethods=null;
	private ArrayList<MGetChildRelationMethod> childRelationMethods=null;
	public MSbbAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbAbstractClass sbbAbstractClass) {
		super();
		this.sbbAbstractClass = sbbAbstractClass;
		this.description=this.sbbAbstractClass.getDescription()==null?null:this.sbbAbstractClass.getDescription().getvalue();
		this.sbbAbstractClassName=this.sbbAbstractClass.getSbbAbstractClassName().getvalue();
		
		this.cmpFields=new HashMap<String, MSbbCMPField>();
		if(this.sbbAbstractClass.getCmpField()!=null)
			for(CmpField cf:this.sbbAbstractClass.getCmpField())
			{
				MSbbCMPField scf=new MSbbCMPField(cf);
				this.cmpFields.put(scf.getFieldName(),scf);
			}
		
		this.profileCMPMethods=new ArrayList<MGetProfileCMPMethod>();
		if(this.sbbAbstractClass.getGetProfileCmpMethod()!=null)
		{
			for(GetProfileCmpMethod gpc:this.sbbAbstractClass.getGetProfileCmpMethod())
			{
				MGetProfileCMPMethod mgpcm=new MGetProfileCMPMethod(gpc);
				this.profileCMPMethods.add(mgpcm);
			}
		}
		
		this.childRelationMethods=new ArrayList<MGetChildRelationMethod>();
		if(this.sbbAbstractClass.getGetChildRelationMethod()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetChildRelationMethod gcrm:this.sbbAbstractClass.getGetChildRelationMethod())
			{
				MGetChildRelationMethod mg=new MGetChildRelationMethod(gcrm);
				this.childRelationMethods.add(mg);
			}
		}
		
		String v=this.sbbAbstractClass.getReentrant();
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
		this.llSbbAbstractClass = llSbbAbstractClass;
		
		this.description=this.llSbbAbstractClass.getDescription()==null?null:this.llSbbAbstractClass.getDescription().getvalue();
		this.sbbAbstractClassName=this.llSbbAbstractClass.getSbbAbstractClassName().getvalue();
		
		this.cmpFields=new HashMap<String, MSbbCMPField>();
		if(this.llSbbAbstractClass.getCmpField()!=null)
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.CmpField cf:this.llSbbAbstractClass.getCmpField())
			{
				MSbbCMPField scf=new MSbbCMPField(cf);
				this.cmpFields.put(scf.getFieldName(),scf);
			}
		
		this.profileCMPMethods=new ArrayList<MGetProfileCMPMethod>();
		if(this.llSbbAbstractClass.getGetProfileCmpMethod()!=null)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod gpc:this.llSbbAbstractClass.getGetProfileCmpMethod())
			{
				MGetProfileCMPMethod mgpcm=new MGetProfileCMPMethod(gpc);
				this.profileCMPMethods.add(mgpcm);
			}
		}
		
		this.childRelationMethods=new ArrayList<MGetChildRelationMethod>();
		if(this.llSbbAbstractClass.getGetChildRelationMethod()!=null)
		{
			for(GetChildRelationMethod gcrm:this.llSbbAbstractClass.getGetChildRelationMethod())
			{
				MGetChildRelationMethod mg=new MGetChildRelationMethod(gcrm);
				this.childRelationMethods.add(mg);
			}
		}
		
		String v=this.llSbbAbstractClass.getReentrant();
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
