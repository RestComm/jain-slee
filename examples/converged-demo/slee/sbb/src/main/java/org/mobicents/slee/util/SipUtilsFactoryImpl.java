 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

import net.java.slee.resource.sip.SleeSipProvider;



public class SipUtilsFactoryImpl implements SipUtilsFactory {

	
	private String jndiEnvPath;
	private String jndiSipFactoryProviderPath;
	
	
	public SipUtilsFactoryImpl() {
		super();
		jndiEnvPath = "java:comp/env";
		jndiSipFactoryProviderPath = "slee/resources/jainsip/1.2/provider";
	}

	public SipUtils getSipUtils() throws NamingException {
		MessageFactory messageFactory = null;
		AddressFactory addressFactory = null;
		HeaderFactory headerFactory = null;
		SleeSipProvider sipProvider = null;
		
		
		Context myEnv = (Context) new InitialContext()
				.lookup(jndiEnvPath);
		// Getting JAIN SIP Resource Adaptor interfaces
		sipProvider = (SleeSipProvider) myEnv
				.lookup(jndiSipFactoryProviderPath);

		addressFactory = sipProvider.getAddressFactory();
		headerFactory = sipProvider.getHeaderFactory();
		messageFactory = sipProvider.getMessageFactory();
		
					
		return new SipUtilsImpl(sipProvider, headerFactory, messageFactory, addressFactory);
	}
	
	/**
	 * @param jndiEnvPath The jndiEnvPath to set.
	 */
	public void setJndiEnvPath(String jndiEnvPath) {
		this.jndiEnvPath = jndiEnvPath;
	}

	/**
	 * @param jndiSipFactoryProviderPath The jndiSipFactoryProviderPath to set.
	 */
	public void setJndiSipFactoryProviderPath(String jndiSipFactoryProviderPath) {
		this.jndiSipFactoryProviderPath = jndiSipFactoryProviderPath;
	}


}
