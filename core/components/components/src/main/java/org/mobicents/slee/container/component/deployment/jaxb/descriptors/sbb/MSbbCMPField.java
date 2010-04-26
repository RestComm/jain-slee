/**
 * Start time:11:27:39 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import javax.slee.SbbID;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.CmpField;
import org.mobicents.slee.container.component.sbb.CMPFieldDescriptor;

/**
 * Start time:11:27:39 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbCMPField implements CMPFieldDescriptor {

	/**
	 * the description of the cmp field
	 */
	private final String description;
	
	/**
	 * the cmp field name
	 */
	private final String cmpFieldName;
	
	/**
	 * the sbb alias reference
	 */
	private final String sbbAliasRef;
	
	/**
	 * the id of the sbb referenced through the alias
	 */
	private SbbID sbbRef = null;
	
	public MSbbCMPField(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.CmpField cmpField) {
		
		this.description=cmpField.getDescription()==null?null:cmpField.getDescription().getvalue();
		this.cmpFieldName=cmpField.getCmpFieldName().getvalue();
		if (cmpField.getSbbAliasRef() == null) {
			this.sbbAliasRef = null;
		}
		else {
			this.sbbAliasRef=cmpField.getSbbAliasRef().getvalue();
		}
	}

	public MSbbCMPField(
			CmpField cmpField) {
		this.description=cmpField.getDescription()==null?null:cmpField.getDescription().getvalue();
		this.cmpFieldName=cmpField.getCmpFieldName().getvalue();
		if (cmpField.getSbbAliasRef() == null) {
			this.sbbAliasRef = null;
		}
		else {
			this.sbbAliasRef=cmpField.getSbbAliasRef().getvalue();
		}
	}

	/**
	 * Retrieves the description of the cmp field
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Retrieves the cmp field name
	 * @return
	 */
	public String getCmpFieldName() {
		return cmpFieldName;
	}

	/**
	 * Retrieves the sbb alias reference
	 * @return
	 */
	public String getSbbAliasRef() {
		return sbbAliasRef;
	}

	/**
	 * Retrieves the id of the sbb referenced through the alias
	 * @return
	 */
	public SbbID getSbbRef() {
		return sbbRef;
	}
	
	/**
	 * Sets the id of the sbb referenced through the alias
	 * @param sbbRef
	 */
	public void setSbbRef(SbbID sbbRef) {
		this.sbbRef = sbbRef;
	}
	
}
