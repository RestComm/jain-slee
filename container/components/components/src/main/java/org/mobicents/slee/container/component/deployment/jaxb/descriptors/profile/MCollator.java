/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.profile.query.CollatorDescriptor;

/**
 * Represents collator.
 * Start time:16:17:09 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
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
