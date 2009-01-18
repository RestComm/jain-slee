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
