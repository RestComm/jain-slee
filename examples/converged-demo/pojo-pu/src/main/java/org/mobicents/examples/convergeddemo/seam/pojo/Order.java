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

package org.mobicents.examples.convergeddemo.seam.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {

	//NOTE: this Order bean is fake - it does not allow to change mapping in other beans - like OrderLine, since it does 
	//not have mapping.
	
	private static final long serialVersionUID = -5451107485769007079L;

	public enum Status {
		OPEN, CANCELLED, PROCESSING, SHIPPED
	}

	public static BigDecimal TAX_RATE = new BigDecimal(".0825");

	long orderId;

	Date orderDate;

	Timestamp deliveryDate;

	BigDecimal netAmount = BigDecimal.ZERO;

	BigDecimal tax = BigDecimal.ZERO;

	BigDecimal totalAmount = BigDecimal.ZERO;

	Status status = Status.OPEN;

	String trackingNumber;

	@Id
	@GeneratedValue
	@Column(name = "ORDERID")
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long id) {
		this.orderId = id;
	}

	@Column(name = "ORDERDATE", nullable = false)
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date date) {
		this.orderDate = date;
	}

	@Column(name = "DELIVERYDATE", nullable = true)
	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Timestamp date) {
		this.deliveryDate = date;
	}

	@Column(name = "NETAMOUNT", nullable = false, precision = 12, scale = 2)
	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal amount) {
		this.netAmount = amount;
	}

	@Column(name = "TAX", nullable = false, precision = 12, scale = 2)
	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal amount) {
		this.tax = amount;
	}

	@Column(name = "TOTALAMOUNT", nullable = false, precision = 12, scale = 2)
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal amount) {
		this.totalAmount = amount;
	}

	@Column(name = "TRACKING")
	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	@Column(name = "STATUS")
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Transient
	public int getStatusCode() {
		return status.ordinal();
	}

	public void cancel() {
		setStatus(Status.CANCELLED);
	}

	public void process() {
		setStatus(Status.PROCESSING);
	}

	public void ship(String tracking) {
		setStatus(Status.SHIPPED);
		setTrackingNumber(tracking);
	}

	/**
	 * round a positive big decimal to 2 decimal points
	 */
	private BigDecimal round(BigDecimal amount) {
		return new BigDecimal(amount.movePointRight(2)
				.add(new BigDecimal(".5")).toBigInteger()).movePointLeft(2);
	}

	@Transient
	public boolean isOpen() {
		return status == Status.OPEN;
	}

}
