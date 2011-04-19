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

package org.mobicents.slee.container.component.profile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.slee.SLEEException;

import org.mobicents.slee.container.component.profile.ProfileAttribute;

/**
 * Object representing a slee profile attribute.
 * 
 * @author martins
 *
 */
public class ProfileAttributeImpl implements ProfileAttribute {

	/**
	 * the profile attribute name
	 */
	private final String name;
	
	/**
	 * the profile attribute type
	 */
	private final Class<?> type;
	
	/**
	 * the non primitive type, which is the equivalent object wrapper type in case the attribute type is a primitive
	 */
	private final Class<?> nonPrimitiveType;
	
	/**
	 * indicates if the profile attribute value must be unique 
	 */
	private boolean unique;
	
	/**
	 * indicates if the profile attribute is indexed or not
	 */
	private boolean index;

	/**
	 * indicates if the profile attribute value is of type array or not 
	 */
	private boolean array = false;
	/**
	 * 
	 * @param name
	 * @param type
	 */
	public ProfileAttributeImpl(String name, Class<?> type) throws NullPointerException {
		
		if (name == null) throw new NullPointerException("null attribute name");
		if (type == null) throw new NullPointerException("null attribute type");

		this.name = name;
		this.type = type;
		
		if (getAllowedPrimitiveTypes().contains(type.getName())) {
			if (type == boolean.class) {
				nonPrimitiveType = Boolean.class;
			}
			else if (type == boolean[].class) {
				nonPrimitiveType = Boolean[].class;
				array = true;
			}
			else if (type == byte.class) {
				nonPrimitiveType = Byte.class;
			}
			else if (type == byte[].class) {
				nonPrimitiveType = Byte[].class;
				array = true;
			}
			else if (type == char.class) {
				nonPrimitiveType = Character.class;
			}
			else if (type == char[].class) {
				nonPrimitiveType = Character[].class;
				array = true;
			}
			else if (type == short.class) {
				nonPrimitiveType = Short.class;
			}
			else if (type == short[].class) {
				nonPrimitiveType = Short[].class;
				array = true;
			}
			else if (type == int.class) {
				nonPrimitiveType = Integer.class;
			}
			else if (type == int[].class) {
				nonPrimitiveType = Integer[].class;
				array = true;
			}
			else if (type == long.class) {
				nonPrimitiveType = Long.class;
			}
			else if (type == long[].class) {
				nonPrimitiveType = Long[].class;
				array = true;
			}
			else if (type == double.class) {
				nonPrimitiveType = Double.class;
			}
			else if (type == double[].class) {
				nonPrimitiveType = Double[].class;
				array = true;
			}
			else if (type == float.class) {
				nonPrimitiveType = Float.class;
			}
			else if (type == float[].class) {
				nonPrimitiveType = Float[].class;
				array = true;
			}
			else {
				throw new SLEEException("unexpected primitive type "+type);
			}
		}
		else {
			if (type.isArray()) {
				array = true;
			}
			nonPrimitiveType = type;
		}
	}

	/**
	 * Indicates if the profile attribute value is of type array or not
	 * @return
	 */
	public boolean isArray() {
		return array;
	}
	
	/**
	 * Indicates if the profile attribute value must be unique
	 * @return
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * Defines if the profile attribute value must be unique 
	 * @param unique
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	/**
	 * Indicates if the profile attribute is indexed or not
	 * @return
	 */
	public boolean isIndex() {
		return index;
	}

	/**
	 * Defines if the profile attribute is indexed or not
	 * @param index
	 */
	public void setIndex(boolean index) {
		this.index = index;
	}

	/**
	 * Retrieves the profile attribute name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the profile attribute type
	 * @return
	 */
	public Class<?> getType() {
		return type;
	}
	
	/**
	 * Retrieves the profile attribute type, in case it is a primitive type the equivalent wrapper type is returned 
	 * @return
	 */
	public Class<?> getNonPrimitiveType() {
		return nonPrimitiveType;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ProfileAttributeImpl)obj).name.equals(this.name);			
		}
		else {
			return false;
		}
	}
	
	/**
	 * Indicates if the attribute type is a java primitive or not
	 * @return
	 */
	public boolean isPrimitive() {
		return getType() != getNonPrimitiveType();
	}
	
	private static final Set<String> PRIMITIVE_ALLOWED_PROFILE_ATTRIBUTE_TYPES = getAllowedPrimitiveTypes();
	
	private static final Set<String> getAllowedPrimitiveTypes() {
		Set<String> tmp = new HashSet<String>();
		for (String type : ProfileAttribute.PRIMITIVE_ALLOWED_PROFILE_ATTRIBUTE_TYPES) {
			tmp.add(type);
		}
		return Collections.unmodifiableSet(tmp);
	}
	
}
