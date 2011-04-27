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

package org.mobicents.slee.resource.diameter;

import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.events.DiameterCommand;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;

import org.jdiameter.api.validation.AvpRepresentation;
import org.jdiameter.api.validation.Dictionary;
import org.jdiameter.api.validation.MessageRepresentation;
import org.jdiameter.api.validation.ValidatorLevel;
import org.jdiameter.client.impl.DictionarySingleton;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ValidatorImpl implements Validator {


  //NOTE: this class possibly should be Singleton, however some impl may use something more to perform validation
  //hence, its not static, each RA provides instance through RA Sbb Interface.

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.java.slee.resource.diameter.Validator#validate(net.java.slee.resource
   * .diameter.base.events.DiameterMessage)
   */
  @Override
  public void validate(DiameterMessage msg) throws AvpNotAllowedException {
    Dictionary dictionary = DictionarySingleton.getDictionary();
    if (dictionary.isConfigured() && dictionary.isEnabled()) {
      DiameterCommand com = msg.getCommand();
      MessageRepresentation rep = dictionary.getMessage(com.getCode(), com.getApplicationId(), com.isRequest());
      if (rep != null) {
        DiameterMessageImpl impl = (DiameterMessageImpl) msg;
        try {
          rep.validate(impl.getGenericData(), ValidatorLevel.ALL);
        }
        catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
          throw new AvpNotAllowedException("Failed to validate message.", e, e.getAvpCode(), e.getVendorId());
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.Validator#
   * validate(net.java.slee.resource.diameter.base.events.avp.DiameterAvp)
   */
  @Override
  public void validate(DiameterAvp avp) throws AvpNotAllowedException {
    Dictionary dictionary = DictionarySingleton.getDictionary();
    if (dictionary.isConfigured() && dictionary.isEnabled()) {
      AvpRepresentation rep = dictionary.getAvp(avp.getCode(), avp.getVendorId());
      // check for grouped?
      if (rep != null && rep.isGrouped()) {
        try {
          GroupedAvpImpl impl = (GroupedAvpImpl) avp;
          rep.validate(impl.getGenericData());
        }
        catch (ClassCastException cce) {
          throw new AvpNotAllowedException("Failed to validate avp, its not grouped!", cce, avp.getCode(), avp.getVendorId());
        }
      }
    }
  }

}
