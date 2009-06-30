package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Represents collator.
 * Start time:16:17:09 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MCollator {

  private String description;

  private String strength;
  private String decomposition;
  private String collatorAlias;

  private String localeLanguage;
  private String localeCountry;
  private String localeVariant;

  public MCollator(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Collator collator11)
  {    
    // This is not defined in JAIN SLEE Specification 1.1 (p233)
    this.description = collator11.getDescription() == null ? null : collator11.getDescription().getvalue();

    // Optional
    this.strength = collator11.getStrength();

    this.decomposition = collator11.getDecomposition();

    this.collatorAlias = collator11.getCollatorAlias().getvalue();

    this.localeLanguage = collator11.getLocaleLanguage().getvalue();
    // Optional
    this.localeCountry = collator11.getLocaleCountry() == null ? null : collator11.getLocaleCountry().getvalue();
    // Optional
    this.localeVariant = collator11.getLocaleVariant() == null ? null : collator11.getLocaleVariant().getvalue();
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
