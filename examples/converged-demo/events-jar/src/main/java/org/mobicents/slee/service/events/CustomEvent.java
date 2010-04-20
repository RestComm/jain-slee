/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
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
