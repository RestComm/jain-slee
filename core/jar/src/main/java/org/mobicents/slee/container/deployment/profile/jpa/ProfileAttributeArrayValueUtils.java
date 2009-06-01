package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.slee.Address;
import javax.slee.SLEEException;

/**
 * A class providing static utility methods to deal with conversion of arrays to
 * {@link ProfileEntityArrayAttributeValue} and vice-versa
 * 
 * @author martins
 * 
 */
public class ProfileAttributeArrayValueUtils {

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @return
	 */
	private static ProfileEntityArrayAttributeValue newProfileEntityArrayAttributeValueInstance(
			Class<?> profileAttrArrayValueClass) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		try {
			profileAttrArrayValue = (ProfileEntityArrayAttributeValue) profileAttrArrayValueClass
					.newInstance();
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(), e);
		}
		return profileAttrArrayValue;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, boolean[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (boolean b : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setboolean(b);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, byte[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (byte b : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setbyte(b);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, char[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (char c : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setchar(c);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, double[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (double d : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setdouble(d);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, float[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (float f : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setfloat(f);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, int[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (int i : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setint(i);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, long[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (long l : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setlong(l);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, short[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (short s : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setshort(s);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Boolean[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (boolean b : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setBoolean(b);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Byte[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (byte b : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setByte(b);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Character[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (char c : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setCharacter(c);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Double[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (double d : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setDouble(d);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Float[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (float f : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setFloat(f);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Integer[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (int i : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setInteger(i);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Long[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (long l : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setLong(l);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Short[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (short s : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setShort(s);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, String[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (String s : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setString(s);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Address[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (Address o : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setAddress(o);
			result.add(profileAttrArrayValue);
		}
		return result;
	}

	/**
	 * 
	 * @param profileAttrArrayValueClass
	 * @param arg0
	 * @return
	 */
	public static List<Object> toProfileAttributeArrayValueList(
			Class<?> profileAttrArrayValueClass, Serializable[] arg0) {
		ProfileEntityArrayAttributeValue profileAttrArrayValue = null;
		List<Object> result = new LinkedList<Object>();
		for (Serializable o : arg0) {
			profileAttrArrayValue = newProfileEntityArrayAttributeValueInstance(profileAttrArrayValueClass);
			profileAttrArrayValue.setSerializable(o);
			result.add(profileAttrArrayValue);
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
