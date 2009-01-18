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

	public ProfileSpecCollator(Collator collator)
	{
		this.collator=collator;
		//init
	}
	
	public ProfileSpecCollator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public void setDecomposition(String decomposition) {
		this.decomposition = decomposition;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setLocaleLanguage(String localeLanguage) {
		this.localeLanguage = localeLanguage;
	}

	public void setLocaleCountry(String localeCountry) {
		this.localeCountry = localeCountry;
	}

	public void setLocaleVariant(String localeVariant) {
		this.localeVariant = localeVariant;
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
