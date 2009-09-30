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
		if (logger.isDebugEnabled()) {
			logger.debug("Before increment:" + incValue + ": " + this);
		}
		this.count++;
		// this.value+=incValue;
		this.value = this.value.add(new BigDecimal(incValue));
		if (logger.isDebugEnabled()) {
			logger.debug("After increment: " + this);
		}

	}

	public void sample(long sample) {
		if (logger.isDebugEnabled()) {
			logger.debug("Before sample:" + sample + ": " + this);
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
