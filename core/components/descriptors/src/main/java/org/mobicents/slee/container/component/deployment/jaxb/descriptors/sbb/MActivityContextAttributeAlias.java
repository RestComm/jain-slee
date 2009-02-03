/**
 * Start time:14:06:45 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import java.util.HashSet;
import java.util.Set;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ActivityContextAttributeAlias;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbActivityContextAttributeName;

/**
 * Start time:14:06:45 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MActivityContextAttributeAlias {

	
	private ActivityContextAttributeAlias activityContextAttributeAlias=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ActivityContextAttributeAlias llActivityContextAttributeAlias=null;
	
	private String description=null;
	private String attributeAliasName=null;
	//This is set of attributes that fall in this allias?
	private Set<String> sbbActivityContextAttributeName=null;
	public String getDescription() {
		return description;
	}
	public String getAttributeAliasName() {
		return attributeAliasName;
	}
	public Set<String> getSbbActivityContextAttributeName() {
		return sbbActivityContextAttributeName;
	}
	public MActivityContextAttributeAlias(
			ActivityContextAttributeAlias activityContextAttributeAlias) {
		super();
		this.activityContextAttributeAlias = activityContextAttributeAlias;
		this.description=this.activityContextAttributeAlias.getDescription()==null?null:this.activityContextAttributeAlias.getDescription().getvalue();
		this.attributeAliasName=this.activityContextAttributeAlias.getAttributeAliasName().getvalue();
		
		//there has to be inimaly one attribute specified, this is validated by validators
		this.sbbActivityContextAttributeName=new HashSet<String>();
		for(SbbActivityContextAttributeName sacan:this.activityContextAttributeAlias.getSbbActivityContextAttributeName())
		{
			this.sbbActivityContextAttributeName.add(sacan.getvalue());
		}
		
	}
	public MActivityContextAttributeAlias(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ActivityContextAttributeAlias llActivityContextAttributeAlias) {
		super();
		this.llActivityContextAttributeAlias = llActivityContextAttributeAlias;
		
		this.description=this.llActivityContextAttributeAlias.getDescription()==null?null:this.llActivityContextAttributeAlias.getDescription().getvalue();
		this.attributeAliasName=this.llActivityContextAttributeAlias.getAttributeAliasName().getvalue();
		
		//there has to be inimaly one attribute specified, this is validated by validators
		this.sbbActivityContextAttributeName=new HashSet<String>();
		for(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbActivityContextAttributeName sacan:this.llActivityContextAttributeAlias.getSbbActivityContextAttributeName())
		{
			this.sbbActivityContextAttributeName.add(sacan.getvalue());
		}
	}
	
	
	
}
