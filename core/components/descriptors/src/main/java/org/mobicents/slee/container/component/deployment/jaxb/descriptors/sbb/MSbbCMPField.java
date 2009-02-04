/**
 * Start time:11:27:39 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.CmpField;

/**
 * Start time:11:27:39 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbCMPField {

	private CmpField cmpField = null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.CmpField llCmpField = null;

	private String description = null;
	private String cmpFieldName = null;
	private String sbbAliasRef = null;

	public MSbbCMPField(
			
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.CmpField llCmpField) {
		super();
		
		this.llCmpField = llCmpField;
		this.description=this.llCmpField.getDescription()==null?null:this.llCmpField.getDescription().getvalue();
		this.cmpFieldName=this.llCmpField.getCmpFieldName().getvalue();
		if(this.llCmpField.getSbbAliasRef()!=null)
			this.sbbAliasRef=this.llCmpField.getSbbAliasRef().getvalue();
	}

	public MSbbCMPField(
			CmpField cmpField) {
		super();
		this.cmpField = cmpField;
		this.description=this.cmpField.getDescription()==null?null:this.cmpField.getDescription().getvalue();
		this.cmpFieldName=this.cmpField.getCmpFieldName().getvalue();
		if(this.cmpField.getSbbAliasRef()!=null)
			this.sbbAliasRef=this.cmpField.getSbbAliasRef().getvalue();
		//FIXME: check this, THIS MUST BE PRESENT FOR SBB LOs, can be checked only at runtime
		
	
	}

	public String getDescription() {
		return description;
	}

	public String getCmpFieldName() {
		return cmpFieldName;
	}

	public String getSbbAliasRef() {
		return sbbAliasRef;
	}

}
