/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter.sh.events;

import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.sh.events.DiameterShMessage;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.SupportedFeaturesAvp;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation of common methods for Sh messages.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DiameterShMessageImpl extends DiameterMessageImpl implements DiameterShMessage {

  private static final long serialVersionUID = -4044514560899824511L;

  protected String longMessageName = null;
  protected String shortMessageName = null;

  public DiameterShMessageImpl(Message msg) {
    super(msg);
  }

  @Override
  public String getLongName() {
    return this.longMessageName;
  }

  @Override
  public String getShortName() {
    return this.shortMessageName;
  }

  public AuthSessionStateType getAuthSessionState() {
    return !hasAuthSessionState() ? null : (AuthSessionStateType) getAvpAsEnumerated(Avp.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(DiameterShAvpCodes.SUPPORTED_FEATURES, DiameterShAvpCodes.SH_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(DiameterShAvpCodes.SUPPORTED_FEATURES, DiameterShAvpCodes.SH_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for(SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }
}
