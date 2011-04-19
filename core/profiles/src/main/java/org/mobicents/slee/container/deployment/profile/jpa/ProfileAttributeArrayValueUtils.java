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

package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.slee.Address;
import javax.slee.SLEEException;

import org.mobicents.slee.container.profile.entity.ProfileEntity;

/**
 * A class providing static utility methods to deal with conversion of arrays to
 * {@link ProfileEntityArrayAttributeValue} and vice-versa
 * 
 * @author martins
 * 
 */
public class ProfileAttributeArrayValueUtils {

	/**
	 * TODO
	 * @param profileAttrArrayValueClass
	 * @return
	 */
	private static ProfileEntityArrayAttributeValue newProfileEntityArrayAttributeValueInstance(
			Class<?> profileAttrArrayValueClass,ProfileEntity owner) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		try {
			profileAttrArrayValue = (ProfileEntityArrayAttributeValue) profileAttrArrayValueClass
					.newInstance();
			profileAttrArrayValue.setProfileEntity(owner);
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(), e);
		}
		return profileAttrArrayValue;
	}
	
	// ----------
	
	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, boolean[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				boolean b = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setboolean(b);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, byte[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				byte b = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setbyte(b);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, char[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				char c = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setchar(c);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, double[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				double d = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setdouble(d);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, float[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				float f = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setfloat(f);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, int[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				int j = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setint(j);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, long[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				long l = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setlong(l);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, short[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				short s = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setshort(s);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Boolean[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Boolean b = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setBoolean(b);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Byte[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Byte b = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setByte(b);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Character[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Character c = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setCharacter(c);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Double[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Double d = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setDouble(d);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Float[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Float f = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setFloat(f);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Integer[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Integer j = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setInteger(j);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Long[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Long l = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setLong(l);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Short[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Short s = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setShort(s);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, String[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				String s = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setString(s);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;	
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Address[] arg0) {		
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Address a = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setAddress(a);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;		
	}
	
	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<ProfileEntityArrayAttributeValue> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, ProfileEntity owner, List<ProfileEntityArrayAttributeValue> result, boolean uniqueAttribute, Serializable[] arg0) {
		// init result list if needed
		if (result == null) {
			result = new ArrayList<ProfileEntityArrayAttributeValue>();
		}		
		// make copy of result list if it is not empty
		List<ProfileEntityArrayAttributeValue> copy = null;
		if (!result.isEmpty()) {
			copy = new ArrayList<ProfileEntityArrayAttributeValue>(result);
			result.clear();
		}		
		if (arg0 != null) {
			ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
			for (int i=0;i<arg0.length;i++) {
				Serializable s = arg0[i];
				profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass,owner);
				profileAttrArrayValue.setSerializable(s);
				if (copy != null && i < copy.size()) {
					ProfileEntityArrayAttributeValue existentElement = copy.get(i);
					// reuse element if possible, to avoid unique constraint failures on inserts 
					if (existentElement != null && new ProfileAttributeArrayValueIdentity(existentElement).equals(new ProfileAttributeArrayValueIdentity(profileAttrArrayValue))) {
						profileAttrArrayValue = existentElement;
					}
				}
				result.add(profileAttrArrayValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static boolean[] tobooleanArray(
			List<ProfileEntityArrayAttributeValue> list) {
		boolean[] result = new boolean[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getboolean();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static byte[] tobyteArray(List<ProfileEntityArrayAttributeValue> list) {
		byte[] result = new byte[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getbyte();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static char[] tocharArray(List<ProfileEntityArrayAttributeValue> list) {
		char[] result = new char[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getchar();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static double[] todoubleArray(
			List<ProfileEntityArrayAttributeValue> list) {
		double[] result = new double[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getdouble();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static float[] tofloatArray(
			List<ProfileEntityArrayAttributeValue> list) {
		float[] result = new float[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getfloat();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static int[] tointArray(List<ProfileEntityArrayAttributeValue> list) {
		int[] result = new int[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getint();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static long[] tolongArray(List<ProfileEntityArrayAttributeValue> list) {
		long[] result = new long[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getlong();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static short[] toshortArray(
			List<ProfileEntityArrayAttributeValue> list) {
		short[] result = new short[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getshort();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Boolean[] toBooleanArray(
			List<ProfileEntityArrayAttributeValue> list) {
		Boolean[] result = new Boolean[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getBoolean();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Byte[] toByteArray(List<ProfileEntityArrayAttributeValue> list) {
		Byte[] result = new Byte[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getByte();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Character[] toCharacterArray(
			List<ProfileEntityArrayAttributeValue> list) {
		Character[] result = new Character[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getCharacter();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Double[] toDoubleArray(
			List<ProfileEntityArrayAttributeValue> list) {
		Double[] result = new Double[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getDouble();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Float[] toFloatArray(
			List<ProfileEntityArrayAttributeValue> list) {
		Float[] result = new Float[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getFloat();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Integer[] toIntegerArray(
			List<ProfileEntityArrayAttributeValue> list) {
		Integer[] result = new Integer[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getInteger();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Long[] toLongArray(List<ProfileEntityArrayAttributeValue> list) {
		Long[] result = new Long[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getLong();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Short[] toShortArray(
			List<ProfileEntityArrayAttributeValue> list) {
		Short[] result = new Short[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getShort();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static String[] toStringArray(
			List<ProfileEntityArrayAttributeValue> list) {
		String[] result = new String[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getString();
		}
		return result;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static Address[] toAddressArray(
			List<ProfileEntityArrayAttributeValue> list) {
		Address[] result = new Address[list.size()];
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = iterator.next().getAddress();
		}
		return result;
	}

	/**
	 * 
	 * @param <T>
	 * @param result
	 * @param list
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public static <T> T[] toSerializableArray(T[] result,
			List<ProfileEntityArrayAttributeValue> list) {
		Iterator<ProfileEntityArrayAttributeValue> iterator = list.iterator();
		for (int i = 0; i < list.size(); i++) {
			result[i] = (T) iterator.next().getSerializable();
		}
		return result;
	}
}
