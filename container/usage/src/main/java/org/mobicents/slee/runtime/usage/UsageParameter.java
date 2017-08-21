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

/**
 * 
 */
package org.mobicents.slee.runtime.usage;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * Simple pojo which holds param state.
 * 
 * @author baranowb
 */
public class UsageParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(UsageParameter.class);

	// NOTE: it seems that javassit does some sort of magic here - cause with
	// generated code and "long" tck passes ... 
	// private long value;
	private BigDecimal value;
	private long min;
	private long max;
	private long count;
	private double mean;

	private Serializable parentId;
	private String parameterName;

	public UsageParameter() {
	}

	/**
	 * 
	 */
	public UsageParameter(Serializable parentId, String parameterName) {
		this.parentId = parentId;
		this.parameterName = parameterName;
		reset();
	}

	public long getValue() {
		return value.longValue();
	}

	public void setValue(long value) {
		// this.value = value;
		this.value = new BigDecimal(value);
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void reset() {

		value = new BigDecimal(0);
		min = Long.MAX_VALUE;
		max = Long.MIN_VALUE;
		count = 0;
		mean = 0;
	}

	public void increment(long incValue) {
		if (logger.isTraceEnabled()) {
			logger.trace("Before increment: " + incValue + ". " + this);
		}
		this.count++;
		// this.value+=incValue;
		this.value = this.value.add(new BigDecimal(incValue));
		if (logger.isDebugEnabled()) {
			logger.debug("After increment: " + this);
		}

	}

	public void sample(long sample) {
		if (logger.isTraceEnabled()) {
			logger.trace("Before sample: " + sample + ". " + this);
		}
		// this.value+=sample;
		this.value = this.value.add(new BigDecimal(sample));
		this.count++;
		if (this.max < sample)
			this.max = sample;
		if (this.min > sample)
			this.min = sample;
		// this.mean = (this.value)/this.count;
		this.mean = this.value.divide(new BigDecimal(this.count), BigDecimal.ROUND_HALF_UP).longValue();
		if (logger.isDebugEnabled()) {
			logger.debug("After sample: " + this);
		}

	}

	public String toString() {
		return super.toString() + ":" + this.parentId + ":" + this.parameterName + ":" + count + ":" + value + ":" + min + ":" + max + ":" + mean;
	}

}
