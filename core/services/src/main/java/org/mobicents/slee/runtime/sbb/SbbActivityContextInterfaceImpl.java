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

package org.mobicents.slee.runtime.sbb;

import java.util.Map;

import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.facilities.TimerID;

import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextInterface;
import org.mobicents.slee.container.component.sbb.SbbComponent;

/**
 * Base class code for a concrete implementation of
 * {@link javax.slee.ActivityContextInterface} by an sbb.
 * 
 * @author martins
 * 
 */
public class SbbActivityContextInterfaceImpl implements
		ActivityContextInterface {

	private final ActivityContextInterface aciImpl;
	private final SbbComponent sbbComponent;

	public SbbActivityContextInterfaceImpl(
			ActivityContextInterface aciImpl, SbbComponent sbbComponent) {
		this.aciImpl = aciImpl;
		this.sbbComponent = sbbComponent;
	}

	public ActivityContext getActivityContext() {
		return aciImpl.getActivityContext();
	}

	public ActivityContextInterface getAciImpl() {
		return aciImpl;
	}

	@Override
	public int hashCode() {
		return aciImpl.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// even if there may be different java interfaces for an AC, equality
		// should be defined only by the AC
		if (obj != null && obj instanceof ActivityContextInterface) {
			return ((ActivityContextInterface) obj).getActivityContext()
					.equals(this.aciImpl.getActivityContext());
		} else {
			return false;
		}
	}

	public void attach(SbbLocalObject arg0) throws NullPointerException,
			TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {
		aciImpl.attach(arg0);
	}

	public void detach(SbbLocalObject arg0) throws NullPointerException,
			TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {
		aciImpl.detach(arg0);
	}

	public Object getActivity() throws TransactionRequiredLocalException,
			SLEEException {
		return aciImpl.getActivity();
	}

	public boolean isEnding() throws TransactionRequiredLocalException,
			SLEEException {
		return aciImpl.isEnding();
	}

	public boolean isAttached(SbbLocalObject arg0) throws NullPointerException,
			TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {
		return aciImpl.isAttached(arg0);
	}

	/**
	 * Computes the real aci data field name
	 * 
	 * @param fieldName
	 * @return
	 */
	private String getRealFieldName(String fieldName) {
		String realFieldName = sbbComponent.getDescriptor()
				.getActivityContextAttributeAliases().get(fieldName);
		if (realFieldName == null) {
			// not there then it has no alias, lets set one based on sbb id
			realFieldName = sbbComponent.getSbbID().toString() + "."
					+ fieldName;
			final Map<String, String> aliases = sbbComponent.getDescriptor()
					.getActivityContextAttributeAliases();
			synchronized (aliases) {
				aliases.put(fieldName, realFieldName);
			}
		}
		return realFieldName;
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, Object value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, byte value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, short value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, int value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, long value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, float value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, double value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, boolean value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Sets an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, char value) {
		String realFieldName = getRealFieldName(fieldName);
		aciImpl.getActivityContext().setDataAttribute(realFieldName, value);
	}

	/**
	 * Retrieves an sbb aci data field value
	 * 
	 * @param fieldName
	 * @param returnType
	 * @return
	 */
	public Object getFieldValue(String fieldName, Class<?> returnType) {

		String realFieldName = getRealFieldName(fieldName);

		Object value = aciImpl.getActivityContext().getDataAttribute(
				realFieldName);

		if (value == null) {
			if (returnType.isPrimitive()) {
				if (returnType.equals(Integer.TYPE)) {
					return Integer.valueOf(0);
				} else if (returnType.equals(Boolean.TYPE)) {
					return Boolean.FALSE;
				} else if (returnType.equals(Long.TYPE)) {
					return Long.valueOf(0);
				} else if (returnType.equals(Double.TYPE)) {
					return Double.valueOf(0);
				} else if (returnType.equals(Float.TYPE)) {
					return Float.valueOf(0);
				}
			}
		}

		return value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.ActivityContextInterfaceExt#getNamesBound()
	 */
	public String[] getNamesBound() {
		return aciImpl.getNamesBound();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.ActivityContextInterfaceExt#getTimers()
	 */
	public TimerID[] getTimers() {
		return aciImpl.getTimers();
	}
	
}
