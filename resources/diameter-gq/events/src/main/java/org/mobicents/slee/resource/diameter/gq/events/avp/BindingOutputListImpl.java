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

package org.mobicents.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.gq.events.avp.BindingOutputList;
import net.java.slee.resource.diameter.gq.events.avp.V4TransportAddress;
import net.java.slee.resource.diameter.gq.events.avp.V6TransportAddress;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link BindingOutputList}
 *
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class BindingOutputListImpl extends GroupedAvpImpl implements BindingOutputList 
{
	public BindingOutputListImpl() 
	{
	    super();
	}

	/**
	* @param code
	* @param vendorId
	* @param mnd
	* @param prt
	* @param value
	*/
	public BindingOutputListImpl( int code, long vendorId, int mnd, int prt, byte[] value ) 
	{
	    super( code, vendorId, mnd, prt, value );
	}
	
	@Override
	/* (non-Javadoc)
	* @see net.java.slee.resource.diameter.gq.events.avp.BindingInputList#getV4TransportAddress()
	*/
	public V4TransportAddress[] getV4TransportAddress() 
	{
		return (V4TransportAddress[])getAvpsAsCustom(DiameterGqAvpCodes.ETSI_V4_TRANSPORT_ADDRESS,DiameterGqAvpCodes.ETSI_VENDOR_ID,V4TransportAddressImpl.class);
	}

	@Override
	/* (non-Javadoc)
	* @see net.java.slee.resource.diameter.gq.events.avp.BindingInputList#getV6TransportAddress()
	*/
	public V6TransportAddress[] getV6TransportAddress() 
	{
		return (V6TransportAddress[])getAvpsAsCustom(DiameterGqAvpCodes.ETSI_V6_TRANSPORT_ADDRESS,DiameterGqAvpCodes.ETSI_VENDOR_ID,V6TransportAddressImpl.class);
	}
	
	@Override
	/* (non-Javadoc)
	* @see net.java.slee.resource.diameter.gq.events.avp.BindingInputList#setV4TransportAddress()
	*/
	public void setV4TransportAddress(V4TransportAddress v4TransportAddress) 
	{
		addAvp(v4TransportAddress);	
		//addAvp(DiameterGqAvpCodes.ETSI_V4_TRANSPORT_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID, v4TransportAddress.byteArrayValue());
	}
	
	@Override
	/* (non-Javadoc)
	* @see net.java.slee.resource.diameter.gq.events.avp.BindingInputList#setV4TransportAddresses()
	*/
	public void setV4TransportAddresses(V4TransportAddress[] v4TransportAddress) 
	{
		for(V4TransportAddress currAddress : v4TransportAddress) {
			setV4TransportAddress(currAddress);
		    }
	}

	@Override
	/* (non-Javadoc)
	* @see net.java.slee.resource.diameter.gq.events.avp.BindingInputList#setV6TransportAddress()
	*/
	public void setV6TransportAddress(V6TransportAddress v6TransportAddress) 
	{
		addAvp(v6TransportAddress);
		//addAvp(DiameterGqAvpCodes.ETSI_V6_TRANSPORT_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID, v6TransportAddress.byteArrayValue());
	}
	
	@Override
	/* (non-Javadoc)
	* @see net.java.slee.resource.diameter.gq.events.avp.BindingInputList#setV6TransportAddresses()
	*/
	public void setV6TransportAddresses(V6TransportAddress[] v6TransportAddress) 
	{
		for(V6TransportAddress currAddress : v6TransportAddress) {
			setV6TransportAddress(currAddress);
		    }
	}
}
