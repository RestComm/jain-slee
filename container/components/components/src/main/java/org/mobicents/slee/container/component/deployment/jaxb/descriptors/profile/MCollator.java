/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.profile.query.CollatorDescriptor;

/**
 * Represents collator.
 * Start time:16:17:09 2009-01-18<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MCollator implements CollatorDescriptor {

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
