/**
 * Start time:16:33:45 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.util.ArrayList;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.CmpField;

/**
 * Start time:16:33:45 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecCMPField {

	
	//easy, there is no CMP Field in 1.0 :)
	private CmpField cmpFieldDesc=null;
	//FIXME: unique be boolean ?
	private String description,fieldName,unique,uniqueCollatorRef;
	private ArrayList<IndexHint> indexHints=null;
	
	public ProfileSpecCMPField(CmpField cmpFieldDesc) {
		this.cmpFieldDesc=cmpFieldDesc;
		this.description=this.cmpFieldDesc.getDescription()!=null?this.cmpFieldDesc.getDescription().getvalue():null;
		this.fieldName=this.cmpFieldDesc.getCmpFieldName().getvalue();
		this.unique=this.cmpFieldDesc.getUnique()==null?"False":this.cmpFieldDesc.getUnique();
		
		
		//FIXME: add more
//		A unique-collator-ref attribute.
//		This optional attribute applies only when the unique attribute is
//		“True”, and the Java type of the Profile CMP field is
//		java.lang.String. It references a collator by its collatoralias
//		that is specified within the same profile-spec element. It
//		is used to determine equality of the CMP field between the various Profiles
//		within the Profile Table. If this attribute is not specified, and the
//		Java type of the CMP field is java.lang.String then the
//		String.equals() method is used for determining equality.
		this.uniqueCollatorRef=this.cmpFieldDesc.getUniqueCollatorRef();
		
		this.indexHints=new ArrayList<IndexHint>();
		if(this.cmpFieldDesc.getIndexHint()!=null && this.cmpFieldDesc.getIndexHint().size()>0)
		{
			for(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.IndexHint ih:this.cmpFieldDesc.getIndexHint())
			{
				this.indexHints.add(new IndexHint(ih));
			}
		}
		
		
	}

	public String getDescription() {
		return description;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getUnique() {
		return unique;
	}

	public String getUniqueCollatorRef() {
		return uniqueCollatorRef;
	}

	public ArrayList<IndexHint> getIndexHints() {
		return indexHints;
	}

	
	
}
