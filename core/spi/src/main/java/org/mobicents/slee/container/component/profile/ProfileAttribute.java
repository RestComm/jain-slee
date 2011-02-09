/**
 * 
 */
package org.mobicents.slee.container.component.profile;

/**
 * @author martins
 *
 */
public interface ProfileAttribute {

	/**
	 * Indicates if the profile attribute value is of type array or not
	 * @return
	 */
	public boolean isArray();
	
	/**
	 * Indicates if the profile attribute value must be unique
	 * @return
	 */
	public boolean isUnique();
	
	/**
	 * Indicates if the profile attribute is indexed or not
	 * @return
	 */
	public boolean isIndex();

	/**
	 * Retrieves the profile attribute name
	 * @return
	 */
	public String getName();

	/**
	 * Retrieves the profile attribute type
	 * @return
	 */
	public Class<?> getType();
	
	/**
	 * Retrieves the profile attribute type, in case it is a primitive type the equivalent wrapper type is returned 
	 * @return
	 */
	public Class<?> getNonPrimitiveType();
	
	/**
	 * Indicates if the attribute type is a java primitive or not
	 * @return
	 */
	public boolean isPrimitive();
	
	/**
	 * the primitive class names for allowed types for a profile attribute value
	 */
	public final static String[] PRIMITIVE_ALLOWED_PROFILE_ATTRIBUTE_TYPES = {
			int.class.getName(), boolean.class.getName(), byte.class.getName(),
			char.class.getName(), double.class.getName(),
			float.class.getName(), long.class.getName(), short.class.getName(),
			int[].class.getName(), boolean[].class.getName(),
			byte[].class.getName(), char[].class.getName(),
			double[].class.getName(), float[].class.getName(),
			long[].class.getName(), short[].class.getName() };

	/**
	 * the non primitive class names for allowed types for a profile attribute
	 * value
	 */
	public final static String[] NON_PRIMITIVE_ALLOWED_PROFILE_ATTRIBUTE_TYPES = {
			java.lang.String.class.getName(),
			javax.slee.Address.class.getName(),
			java.lang.String[].class.getName(),
			javax.slee.Address[].class.getName(), Integer.class.getName(),
			Boolean.class.getName(), Byte.class.getName(),
			Character.class.getName(), Double.class.getName(),
			Float.class.getName(), Long.class.getName(), Short.class.getName(),
			Integer[].class.getName(), Boolean[].class.getName(),
			Byte[].class.getName(), Character[].class.getName(),
			Double[].class.getName(), Float[].class.getName(),
			Long[].class.getName(), Short[].class.getName() };
		
}
