/**
 * Start time:16:17:09 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Collator;

/**
 * Represents collartor.
 * Start time:16:17:09 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileSpecCollator {

	
	
	private String description,strength,decomposition,collatorAlias,localeLanguage,localeCountry,localeVariant;
	private Collator collator=null;
	


	public MProfileSpecCollator(Collator collator)
	{
		this.collator=collator;
		//init
		
		//Optional
		this.description=this.collator.getDescription()==null?null:this.collator.getDescription().getvalue();
		//Optional
		this.decomposition=this.collator.getDecomposition();
//		if(this.collator.getCollatorAlias()==null || this.collator.getCollatorAlias().getvalue()==null)
//			throw new DeploymentException("Collator Alias cant be null!");
		
		this.collatorAlias=this.collator.getCollatorAlias().getvalue();
		this.strength=this.collator.getStrength();
//		if(this.collator.getLocaleLanguage()==null || this.collator.getLocaleLanguage().getvalue()==null)
//			throw new DeploymentException("Collator Locale Language cant be null!");
		
		this.localeLanguage=this.collator.getLocaleLanguage().getvalue();
		//Optional
		if(this.collator.getLocaleCountry()!=null && this.collator.getLocaleCountry().getvalue()!=null)
			this.localeCountry=this.collator.getLocaleCountry().getvalue();
		//Optional
		if(this.collator.getLocaleVariant()!=null && this.collator.getLocaleVariant().getvalue()!=null)
			this.localeVariant=this.collator.getLocaleVariant().getvalue();
		
		
		
	}

	public String getDescription() {
		return description;
	}

	public String getStrength() {
		return strength;
	}

	public String getDecomposition() {
		return decomposition;
	}

	public String getCollatorAlias() {
		return collatorAlias;
	}

	public String getLocaleLanguage() {
		return localeLanguage;
	}

	public String getLocaleCountry() {
		return localeCountry;
	}

	public String getLocaleVariant() {
		return localeVariant;
	}
	
	
}
