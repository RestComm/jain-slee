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
 * <p>
 * data_sm is a streamlined version of the submit_sm operation, designed for
 * packet-based applications that do not demand extended functionality normally
 * available in the submit_sm operation. ESMEs implementing WAP over a SMS
 * bearer typically use this operation.
 * 
 * </p>
 * 
 * <p>
 * Data_sm can also be used for message delivery from Message Centre to the
 * ESME. ESMEs implementing WAP over SMS typically use this operation.
 * 
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface DataSM extends SmppRequest {

	public abstract String getServiceType();

	public abstract void setServiceType(String serviceType);

	public abstract Address getDestAddress();

	public abstract void setDestAddress(Address address);

	public abstract Address getSourceAddress();

	public abstract void setSourceAddress(Address address);

	public abstract int getEsmClass();

	public abstract void setEsmClass(int esmClass);

	public abstract int getRegisteredDelivery();

	public abstract void setRegisteredDelivery(int registeredDelivery);

	public abstract int getDataCoding();

	public abstract void setDataCoding(int dataCoding);

}
