/**
 * Start time:16:17:09 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

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
public class ProfileSpecCollator {

	
	
	private String description,strength,decomposition,alias,localeLanguage,localeCountry,localeVariant;
	private Collator collator=null;
	
	
	public ProfileSpecCollator(String description, String strength,
			String decomposition, String alias, String localeLanguage,
			String localeCountry, String localeVariant) {
		super();
		this.description = description;
		this.strength = strength;
		this.decomposition = decomposition;
		this.alias = alias;
		this.localeLanguage = localeLanguage;
		this.localeCountry = localeCountry;
		this.localeVariant = localeVariant;
	}

	public ProfileSpecCollator(Collator collator) throws DeploymentException
	{
		this.collator=collator;
		//init
		
		//Optional
		this.description=this.collator.getDescription()==null?null:this.collator.getDescription().getvalue();
		//Optional
		this.decomposition=this.collator.getDecomposition();
		if(this.collator.getCollatorAlias()==null || this.collator.getCollatorAlias().getvalue()==null)
			throw new DeploymentException("Collator Alias cant be null!");
		
		this.alias=this.collator.getCollatorAlias().getvalue();
		
		if(this.collator.getLocaleLanguage()==null || this.collator.getLocaleLanguage().getvalue()==null)
			throw new DeploymentException("Collator Locale Language cant be null!");
		
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

	public String getAlias() {
		return alias;
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
