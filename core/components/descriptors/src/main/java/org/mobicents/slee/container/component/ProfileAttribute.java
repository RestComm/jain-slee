package org.mobicents.slee.container.component;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.slee.SLEEException;

/**
 * Object representing a slee profile attribute.
 * 
 * @author martins
 *
 */
public class ProfileAttribute {

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
	 * 
	 * @param name
	 * @param type
	 */
	public ProfileAttribute(String name, Class<?> type) throws NullPointerException {
		
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
			}
			else if (type == byte.class) {
				nonPrimitiveType = Byte.class;
			}
			else if (type == byte[].class) {
				nonPrimitiveType = Byte[].class;
			}
			else if (type == char.class) {
				nonPrimitiveType = Character.class;
			}
			else if (type == char[].class) {
				nonPrimitiveType = Character[].class;
			}
			else if (type == short.class) {
				nonPrimitiveType = Short.class;
			}
			else if (type == short[].class) {
				nonPrimitiveType = Short[].class;
			}
			else if (type == int.class) {
				nonPrimitiveType = Integer.class;
			}
			else if (type == int[].class) {
				nonPrimitiveType = Integer[].class;
			}
			else if (type == long.class) {
				nonPrimitiveType = Long.class;
			}
			else if (type == long[].class) {
				nonPrimitiveType = Long[].class;
			}
			else if (type == double.class) {
				nonPrimitiveType = Double.class;
			}
			else if (type == double[].class) {
				nonPrimitiveType = Double[].class;
			}
			else if (type == float.class) {
				nonPrimitiveType = Float.class;
			}
			else if (type == float[].class) {
				nonPrimitiveType = Float[].class;
			}
			else {
				throw new SLEEException("unexpected primitive type "+type);
			}
		}
		else {
			nonPrimitiveType = type;
		}
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
			return ((ProfileAttribute)obj).name.equals(this.name);			
		}
		else {
			return false;
		}
	}
	
	/**
	 * Indicates if the attribute type is a java primitive or not
	 * @return
	 */
	public boolean isTypePrimitive() {
		return getType() != getNonPrimitiveType();
	}
	
	
	/**
	 * the class names for allowed types for a profile attribute value
	 */
	public final static Set<String> ALLOWED_PROFILE_ATTRIBUTE_TYPES = getAllowedTypes();
	
	private static final Set<String> getAllowedPrimitiveTypes() {
		Set<String> tmp = new HashSet<String>();
		tmp.add(int.class.getName());
		tmp.add(boolean.class.getName());
		tmp.add(byte.class.getName());
		tmp.add(char.class.getName());
		tmp.add(double.class.getName());
		tmp.add(float.class.getName());
		tmp.add(long.class.getName());
		tmp.add(short.class.getName());
		tmp.add(int[].class.getName());
		tmp.add(boolean[].class.getName());
		tmp.add(byte[].class.getName());
		tmp.add(char[].class.getName());
		tmp.add(double[].class.getName());
		tmp.add(float[].class.getName());
		tmp.add(long[].class.getName());
		tmp.add(short[].class.getName());
		return Collections.unmodifiableSet(tmp);
	}
	
	private static final Set<String> getAllowedNonPrimitiveTypes() {
		Set<String> tmp = new HashSet<String>();
		tmp.add(java.lang.String.class.getName());
		tmp.add(javax.slee.Address.class.getName());
		tmp.add(java.lang.String[].class.getName());
		tmp.add(javax.slee.Address[].class.getName());
		tmp.add(Integer.class.getName());
		tmp.add(Boolean.class.getName());
		tmp.add(Byte.class.getName());
		tmp.add(Character.class.getName());
		tmp.add(Double.class.getName());
		tmp.add(Float.class.getName());
		tmp.add(Long.class.getName());
		tmp.add(Short.class.getName());
		tmp.add(Integer[].class.getName());
		tmp.add(Boolean[].class.getName());
		tmp.add(Byte[].class.getName());
		tmp.add(Character[].class.getName());
		tmp.add(Double[].class.getName());
		tmp.add(Float[].class.getName());
		tmp.add(Long[].class.getName());
		tmp.add(Short[].class.getName());
		return Collections.unmodifiableSet(tmp);
	}
	
	private static final Set<String> getAllowedTypes() {	
		Set<String> tmp = new HashSet<String>();
		tmp.addAll(getAllowedPrimitiveTypes());
		tmp.addAll(getAllowedNonPrimitiveTypes());
		return Collections.unmodifiableSet(tmp);
	}
}
