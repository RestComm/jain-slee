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

package org.mobicents.slee.service.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Random;

/**
 * CustomEvent to communicate between SBB Entities belonging to different
 * Services
 * 
 * @author amit bhayani
 * 
 */
public class CustomEvent implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private long orderId;

	private BigDecimal ammount;

	private String customerName;

	private String customerPhone;
	
	private String  userName;

	public CustomEvent(long orderId, BigDecimal ammount, String customerName,
			String customerPhone, String  userName) {
		id = new Random().nextLong() ^ System.currentTimeMillis();
		this.orderId = orderId;
		this.ammount = ammount;
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.userName = userName;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null)
			return false;
		return (o instanceof CustomEvent) && ((CustomEvent) o).id == id;
	}

	public int hashCode() {
		return (int) id;
	}

	public BigDecimal getAmmount() {
		return ammount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public long getOrderId() {
		return orderId;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}	

	public Object clone() {
		CustomEvent clonedCustomEvent = new CustomEvent(this.getOrderId(), this
				.getAmmount(), this.getCustomerName(), this.getCustomerPhone(), this.getUserName());
		return clonedCustomEvent;
	}



}
