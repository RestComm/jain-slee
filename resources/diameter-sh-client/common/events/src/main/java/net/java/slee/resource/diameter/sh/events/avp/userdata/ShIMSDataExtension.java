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

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension2;

/**
 * <p>Interface for tShIMSDataExtension complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tShIMSDataExtension">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PSIActivation" type="{}tPSIActivation" minOccurs="0"/>
 *         &lt;element name="Extension" type="{}tShIMSDataExtension2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ShIMSDataExtension {

  /**
   * Gets the value of the psiActivation property.
   * 
   * @return
   *     possible object is
   *     {@link Short }
   *     
   */
  public abstract Short getPSIActivation();

  /**
   * Sets the value of the psiActivation property.
   * 
   * @param value
   *     allowed object is
   *     {@link Short }
   *     
   */
  public abstract void setPSIActivation(Short value);

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TShIMSDataExtension2 }
   *     
   */
  public abstract ShIMSDataExtension2 getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TShIMSDataExtension2 }
   *     
   */
  public abstract void setExtension(ShIMSDataExtension2 value);

}