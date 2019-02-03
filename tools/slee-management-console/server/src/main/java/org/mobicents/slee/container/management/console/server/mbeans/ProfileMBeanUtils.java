package org.mobicents.slee.container.management.console.server.mbeans;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;
import javax.slee.Address;
import javax.slee.AddressPlan;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.server.Logger;
import org.mobicents.slee.container.management.console.server.profiles.ProfileServiceImpl;

public class ProfileMBeanUtils {
	private MBeanServerConnection mbeanServer;

	private ObjectName sleeManagementMBean;

	private ProfileProvisioningMBeanUtils profileProvisioningMBeanUtils;



	public ProfileMBeanUtils(MBeanServerConnection mbeanServer,
			ProfileProvisioningMBeanUtils profileProvisioningMBeanUtils) throws ManagementConsoleException {
		this.mbeanServer = mbeanServer;
		this.profileProvisioningMBeanUtils = profileProvisioningMBeanUtils;
	}

	public void commitProfile(ObjectName profileMBean) throws ManagementConsoleException {
		Exception ex = null;
		try {
			mbeanServer.invoke(profileMBean, "commitProfile", new Object[] {}, new String[] {});

		} catch (Exception e) {
			ex = e;
			e.printStackTrace();
		} finally {
			close(profileMBean);
			if (ex != null) {
				throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(ex));
			}
		}
	}

	public void editProfile(ObjectName profileMBean) throws ManagementConsoleException {
		try {
			mbeanServer.invoke(profileMBean, "editProfile", new Object[] {}, new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}
	}

	public void closeProfile(ObjectName profileMBean) throws ManagementConsoleException {
		try {
			mbeanServer.invoke(profileMBean, "closeProfile", new Object[] {}, new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}
	}

	public void restoreProfile(ObjectName profileMBean) throws ManagementConsoleException {
		try {
			mbeanServer.invoke(profileMBean, "restoreProfile", new Object[] {}, new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}
	}

	public void close(ObjectName profileMBean) throws ManagementConsoleException {
		try {
			mbeanServer.invoke(profileMBean, "close", new Object[] {}, new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}
	}

	private MBeanAttributeInfo findAttribute(String attr_name, MBeanAttributeInfo[] attribute_info) {
		for (int i = 0; i < attribute_info.length; i++) {
			MBeanAttributeInfo mBeanAttributeInfo = attribute_info[i];
			if (mBeanAttributeInfo.getName().equals(attr_name))
				return mBeanAttributeInfo;
		}
		return null;
	}

	public void setAttribute(ObjectName profileMBean, Attribute attribute) throws ManagementConsoleException {
		String type = null;
		Class attributeClass;
		try {
			Attribute newAttribute = null;

			MBeanAttributeInfo[] mBeanAttributeInfos = mbeanServer.getMBeanInfo(profileMBean).getAttributes();

			MBeanAttributeInfo info = findAttribute(attribute.getName(), mBeanAttributeInfos);

			type = info.getType();

			if (attribute.getValue() == null) {
				newAttribute = new Attribute(attribute.getName(), null);
			} else if (type.equals("int")) {
				int value = Integer.parseInt((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);

			} else if (type.equals("short")) {
				short value = Short.parseShort((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("long")) {
				long value = Long.parseLong((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("float")) {
				float value = Float.parseFloat((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("boolean")) {
				boolean value = Boolean.parseBoolean((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("byte")) {
				byte value = Byte.parseByte((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);

			} else if (type.equals("double")) {
				double value = Double.parseDouble((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);

			} else {

				attributeClass = Class.forName(type);
				if (attributeClass.isArray()) {

					Class arrayValuesClass = attributeClass.getComponentType();

					String[] values = ((String) attribute.getValue()).split(ProfileServiceImpl.ARRAY_SEPARATOR);

					Object array = Array.newInstance(arrayValuesClass, values.length);

					for (int j = 0; j < values.length; j++) {

						if (arrayValuesClass.isPrimitive()) {

							String token = values[j];

							String canonicalName = array.getClass().getComponentType().getCanonicalName();

							if (canonicalName.equals("byte")) {
								Array.set(array, j, Byte.parseByte(token));
							} else if (canonicalName.equals("short")) {
								Array.set(array, j, Short.parseShort(token));
							} else if (canonicalName.equals("int")) {
								Array.set(array, j, Integer.parseInt(token));
							} else if (canonicalName.equals("long")) {
								Array.set(array, j, Long.parseLong(token));
							} else if (canonicalName.equals("float")) {
								Array.set(array, j, Float.parseFloat(token));
							} else if (canonicalName.equals("double")) {
								Array.set(array, j, Double.parseDouble(token));
							} else if (canonicalName.equals("boolean")) {
								Array.set(array, j, Boolean.parseBoolean(token));
							} else if (canonicalName.equals("char")) {
								Array.set(array, j, token.charAt(0));
							} else {
								Logger.error("Did not find cast method for type: " + arrayValuesClass.getCanonicalName());
							}

						} else {
							Object value = null;
							if (arrayValuesClass.getName().equals("javax.slee.Address")) {
								try {
									int delimerSize = 2;
									int delimiter = values[j].indexOf(": ");
									if (delimiter == -1) {
										delimerSize = 1;
										delimiter = values[j].indexOf(":");
									}
									if (delimiter == -1) {
										throw new IllegalArgumentException(
												"\nAddress arg should be \"address plan as string\" + \": \" + \"address as string\"\n");
									}
									String addressPlan = values[j].substring(0, delimiter);
									String address = values[j].substring(delimiter + delimerSize);
									value = new Address(AddressPlan.fromString(addressPlan), address);
								} catch (Exception ex) {
									throw new IllegalArgumentException(ex.getMessage(), ex);
								}
							} else {
								Constructor con = arrayValuesClass.getConstructor(String.class);
								if (con != null) {
									value = con.newInstance((String) values[j]);
								}
							}
							Array.set(array, j, value);
						}

					}
					newAttribute = new Attribute(attribute.getName(), array);

				} else {
					Object value = null;
					if (attributeClass.getName().equals("javax.slee.Address")) {
						try {

							int delimerSize = 2;
							int delimiter = ((String) attribute.getValue()).indexOf(": ");
							if (delimiter == -1) {
								delimerSize = 1;
								delimiter = ((String) attribute.getValue()).indexOf(":");
							}
							if (delimiter == -1) {
								throw new IllegalArgumentException(
										"\nAddress arg should be \"address plan as string\" + \": \" + \"address as string\"\n");
							}
							String addressPlan = ((String) attribute.getValue()).substring(0, delimiter);
							String address = ((String) attribute.getValue()).substring(delimiter + delimerSize);
							value = new Address(AddressPlan.fromString(addressPlan), address);
						} catch (Exception ex) {
							throw new IllegalArgumentException(ex.getMessage(), ex);
						}
					} else {
						Constructor con = attributeClass.getConstructor(String.class);
						if (con != null) {
							value = con.newInstance((String) attribute.getValue());
						} else {
							throw new ClassNotFoundException(
									"can not find constructor for attribute " + attribute.getName());
						}
					}
					newAttribute = new Attribute(attribute.getName(), value);

				}

			}

			mbeanServer.setAttribute(profileMBean, newAttribute);
		} catch (Exception e) {
			Logger.error("closing Profile due to unvalid attribute value");
			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}

	}
	public void setAttribute(ObjectName profileMBean, Attribute attribute,String type) throws ManagementConsoleException {
		Class attributeClass;
		try {
			Attribute newAttribute = null;


			if (attribute.getValue() == null) {
				newAttribute = new Attribute(attribute.getName(), null);
			} else if (type.equals("int")) {
				int value = Integer.parseInt((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);

			} else if (type.equals("short")) {
				short value = Short.parseShort((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("long")) {
				long value = Long.parseLong((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("float")) {
				float value = Float.parseFloat((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("boolean")) {
				boolean value = Boolean.parseBoolean((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);
			} else if (type.equals("byte")) {
				byte value = Byte.parseByte((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);

			} else if (type.equals("double")) {
				double value = Double.parseDouble((String) attribute.getValue());
				newAttribute = new Attribute(attribute.getName(), value);

			} else {

				attributeClass = Class.forName(type);
				if (attributeClass.isArray()) {

					Class arrayValuesClass = attributeClass.getComponentType();

					String[] values = ((String) attribute.getValue()).split(ProfileServiceImpl.ARRAY_SEPARATOR);

					Object array = Array.newInstance(arrayValuesClass, values.length);

					for (int j = 0; j < values.length; j++) {

						if (arrayValuesClass.isPrimitive()) {

							String token = values[j];

							String canonicalName = array.getClass().getComponentType().getCanonicalName();

							if (canonicalName.equals("byte")) {
								Array.set(array, j, Byte.parseByte(token));
							} else if (canonicalName.equals("short")) {
								Array.set(array, j, Short.parseShort(token));
							} else if (canonicalName.equals("int")) {
								Array.set(array, j, Integer.parseInt(token));
							} else if (canonicalName.equals("long")) {
								Array.set(array, j, Long.parseLong(token));
							} else if (canonicalName.equals("float")) {
								Array.set(array, j, Float.parseFloat(token));
							} else if (canonicalName.equals("double")) {
								Array.set(array, j, Double.parseDouble(token));
							} else if (canonicalName.equals("boolean")) {
								Array.set(array, j, Boolean.parseBoolean(token));
							} else if (canonicalName.equals("char")) {
								Array.set(array, j, token.charAt(0));
							} else {
								Logger.error("Did not find cast method for type: " + arrayValuesClass.getCanonicalName());
							}

						} else {
							Object value = null;
							if (arrayValuesClass.getName().equals("javax.slee.Address")) {
								try {
									int delimerSize = 2;
									int delimiter = values[j].indexOf(": ");
									if (delimiter == -1) {
										delimerSize = 1;
										delimiter = values[j].indexOf(":");
									}
									if (delimiter == -1) {
										throw new IllegalArgumentException(
												"\nAddress arg should be \"address plan as string\" + \": \" + \"address as string\"\n");
									}
									String addressPlan = values[j].substring(0, delimiter);
									String address = values[j].substring(delimiter + delimerSize);
									value = new Address(AddressPlan.fromString(addressPlan), address);
								} catch (Exception ex) {
									throw new IllegalArgumentException(ex.getMessage(), ex);
								}
							} else {
								Constructor con = arrayValuesClass.getConstructor(String.class);
								if (con != null) {
									value = con.newInstance((String) values[j]);
								}
							}
							Array.set(array, j, value);
						}

					}
					newAttribute = new Attribute(attribute.getName(), array);

				} else {
					Object value = null;
					if (attributeClass.getName().equals("javax.slee.Address")) {
						try {

							int delimerSize = 2;
							int delimiter = ((String) attribute.getValue()).indexOf(": ");
							if (delimiter == -1) {
								delimerSize = 1;
								delimiter = ((String) attribute.getValue()).indexOf(":");
							}
							if (delimiter == -1) {
								throw new IllegalArgumentException(
										"\nAddress arg should be \"address plan as string\" + \": \" + \"address as string\"\n");
							}
							String addressPlan = ((String) attribute.getValue()).substring(0, delimiter);
							String address = ((String) attribute.getValue()).substring(delimiter + delimerSize);
							value = new Address(AddressPlan.fromString(addressPlan), address);
						} catch (Exception ex) {
							throw new IllegalArgumentException(ex.getMessage(), ex);
						}
					} else {
						Constructor con = attributeClass.getConstructor(String.class);
						if (con != null) {
							value = con.newInstance((String) attribute.getValue());
						} else {
							throw new ClassNotFoundException(
									"can not find constructor for attribute " + attribute.getName());
						}
					}
					newAttribute = new Attribute(attribute.getName(), value);

				}

			}

			mbeanServer.setAttribute(profileMBean, newAttribute);
		} catch (Exception e) {
			Logger.error("closing Profile due to unvalid attribute value");

			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}

	}
	public String[] getAllAttributes(ObjectName profileMBean) throws ManagementConsoleException {
		String[] attributesNames;
		try {
			MBeanAttributeInfo[] attributeInfos = mbeanServer.getMBeanInfo(profileMBean).getAttributes();
			attributesNames = new String[attributeInfos.length];

			for (int i = 0; i < attributeInfos.length; i++) {
				attributesNames[i] = attributeInfos[i].getName() + ProfileServiceImpl.NAME_TYPE_SEPARATOR+ attributeInfos[i].getType();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}
		return attributesNames;
	}

	public String getAttribute(ObjectName profileMBean, String attributeName) throws ManagementConsoleException {
		String attributeValue;
		try {
			attributeValue = (String) mbeanServer.getAttribute(profileMBean, attributeName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
		}
		return attributeValue;
	}

	public String[] getAttributes(ObjectName profileMBean, String[] attributesNames) throws ManagementConsoleException {
		String[] attributesValues = new String[attributesNames.length];

		for (int i = 0; i < attributesNames.length; i++) {

			Object objectvalue = null;

			try {
				objectvalue = mbeanServer.getAttribute(profileMBean, attributesNames[i]);
			} catch (AttributeNotFoundException e) {
				e.printStackTrace();
				throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
			} catch (InstanceNotFoundException e) {
				e.printStackTrace();
				throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
			} catch (MBeanException e) {
				e.printStackTrace();
				throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
			} catch (ReflectionException e) {
				e.printStackTrace();
				throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
			} catch (IOException e) {
				e.printStackTrace();
				throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
			} catch (RuntimeMBeanException e) {
				attributesValues[i] = null;
				continue;
			}

			if (objectvalue != null) {
				Class clazz = objectvalue.getClass();


				if (clazz.equals(long[].class)) {

					long[] values = (long[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}

					attributesValues[i] = "0";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = attributesValues[i] + "," + String.valueOf(values[j]);
						}
					}
				} else if (clazz.equals(short[].class)) {
					short[] values = (short[]) objectvalue;

					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}

					attributesValues[i] = "0";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = String.valueOf(values[j - 1]) + "," + String.valueOf(values[j]);
						}
					}

				} else if (clazz.equals(int[].class)) {
					int[] values = (int[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}

					attributesValues[i] = "0";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = attributesValues[i] + "," + String.valueOf(values[j]);
						}
					}
				} else if (clazz.equals(double[].class)) {
					double[] values = (double[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}
					attributesValues[i] = "0";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = attributesValues[i] + "," + String.valueOf(values[j]);
						}
					}
				} else if (clazz.equals(float[].class)) {
					float[] values = (float[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}
					attributesValues[i] = "0";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = attributesValues[i] + "," + String.valueOf(values[j]);
						}
					}
				} else if (clazz.equals(byte[].class)) {
					byte[] values = (byte[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}
					attributesValues[i] = "0";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = attributesValues[i] + "," + String.valueOf(values[j]);
						}
					}
				} else if (clazz.equals(boolean[].class)) {
					boolean[] values = (boolean[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}
					attributesValues[i] = "false";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = attributesValues[i] + "," + String.valueOf(values[j]);
						}
					}
				} else if (clazz.equals(char[].class)) {
					char[] values = (char[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}
					attributesValues[i] = " ";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = String.valueOf(values[j]);
						} else {
							attributesValues[i] = attributesValues[i] + "," + String.valueOf(values[j]);
						}
					}
				} else if (clazz.equals(String[].class)) {
					String[] values = (String[]) objectvalue;
					if (values.length == 0) {
						attributesValues[i] = null;
						continue;
					}
					attributesValues[i] = "null";
					for (int j = 0; j < values.length; j++) {
						if (j == 0) {
							attributesValues[i] = values[j];
						} else {
							attributesValues[i] = attributesValues[i] + "," + values[j];
						}
					}
				} else {
					attributesValues[i] = objectvalue.toString();

				}
			} else {
				attributesValues[i] = null;
			}
		}

		return attributesValues;
	}


}
