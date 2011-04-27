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

package net.java.slee.resource.diameter.sh.events.avp.userdata;

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentityExtension2;

/**
 * <p>Interface for tPublicIdentityExtension complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tPublicIdentityExtension">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdentityType" type="{}tIdentityType" minOccurs="0"/>
 *         &lt;element name="WildcardedPSI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="Extension" type="{}tPublicIdentityExtension2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface PublicIdentityExtension {

  /**
   * Gets the value of the identityType property.
   * 
   * @return
   *     possible object is
   *     {@link Short }
   *     
   */
  public abstract Short getIdentityType();

  /**
   * Sets the value of the identityType property.
   * 
   * @param value
   *     allowed object is
   *     {@link Short }
   *     
   */
  public abstract void setIdentityType(Short value);

  /**
   * Gets the value of the wildcardedPSI property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getWildcardedPSI();

  /**
   * Sets the value of the wildcardedPSI property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setWildcardedPSI(String value);

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TPublicIdentityExtension2 }
   *     
   */
  public abstract PublicIdentityExtension2 getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TPublicIdentityExtension2 }
   *     
   */
  public abstract void setExtension(PublicIdentityExtension2 value);

}