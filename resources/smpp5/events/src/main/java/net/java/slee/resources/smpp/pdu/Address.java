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

package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface Address {

	/**
	 * Define the Type Of Number (TON). See Section 4.7.1 addr_ton, source_addr_ton, dest_addr_ton, esme_addr_ton of
	 * SMPP Version 5.0 Specs
	 */
	public static final byte TON_UNKNOWN = 0;
	public static final byte TON_INTERNATIONAL = 1;
	public static final byte TON_NATIONAL = 2;
	public static final byte TON_NETWORK_SPECIFIC = 3;
	public static final byte TON_SUBSCRIBER_NUMBER = 4;
	public static final byte TON_ALPHANUMERIC = 5;
	public static final byte TON_ABBREVIATED = 6;

	/**
	 * Define the Numeric Plan Indicator (NPI). See Section 4.7.2 addr_npi, source_addr_npi, dest_addr_npi,
	 * esme_addr_npi of SMPP Version 5.0 Specs
	 */
	public static final byte NPI_UNKNOWN = 0;
	public static final byte NPI_ISDN = 1;
	public static final byte NPI_DATA = 3;
	public static final byte NPI_TELEX = 4;
	public static final byte NPI_LAND_MOBILE = 6;
	public static final byte NPI_NATIONAL = 8;
	public static final byte NPI_PRIAVTE = 9;
	public static final byte NPI_ERMES = 10;
	public static final byte NPI_INTERNET = 14;
	public static final byte NPI_WAP = 18;

	public String getAddress();

	public int getAddressNpi();

	public int getAddressTon();

}
