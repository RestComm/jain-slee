/**
 * Start time:10:45:52 2009-02-09<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javassist.Modifier;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.SbbComponent;

/**
 * Start time:10:45:52 2009-02-09<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecificationComponentValidator implements Validator {

	private ComponentRepository repository = null;
	private ProfileSpecificationComponent component = null;
	private final static transient Logger logger = Logger
			.getLogger(ProfileSpecificationComponentValidator.class);
	// this does not include serializables
	private final static Set<String> _ALLOWED_CMPS_TYPES;
	static {
		Set<String> tmp = new HashSet<String>();
		tmp.add("int");
		tmp.add("boolean");
		tmp.add("byte");
		tmp.add("char");
		tmp.add("double");
		tmp.add("float");
		tmp.add("long");
		tmp.add("short");
		tmp.add(int[].class.toString());
		tmp.add(boolean[].class.toString());
		tmp.add(byte[].class.toString());
		tmp.add(char[].class.toString());
		tmp.add(double[].class.toString());
		tmp.add(float[].class.toString());
		tmp.add(long[].class.toString());
		tmp.add(short[].class.toString());
		_ALLOWED_CMPS_TYPES = Collections.unmodifiableSet(tmp);

	}

	private final static Set<String> _ENV_ENTRIES_TYPES;
	static {
		Set<String> tmp = new HashSet<String>();
		tmp.add(Integer.TYPE.getName());
		tmp.add(Boolean.TYPE.getName());
		tmp.add(Byte.TYPE.getName());
		tmp.add(Character.TYPE.getName());
		tmp.add(Double.TYPE.getName());
		tmp.add(Float.TYPE.getName());
		tmp.add(Long.TYPE.getName());
		tmp.add(Short.TYPE.getName());
		tmp.add(String.class.getName());
		_ENV_ENTRIES_TYPES = Collections.unmodifiableSet(tmp);

	}

	private final static Set<String> _FORBIDEN_METHODS;
	static {
		// section 10.18 in SLEE 1.1
		Set<String> _tmp = new HashSet<String>();
		Set<String> ignore = new HashSet<String>();
		ignore.add("java.lang.Object");
		Map<String, Method> tmpMethodsMap = ClassUtils.getAllInterfacesMethods(
				javax.slee.profile.ProfileManagement.class, ignore);
		_tmp.addAll(tmpMethodsMap.keySet());
		tmpMethodsMap = ClassUtils.getAllInterfacesMethods(
				javax.slee.profile.ProfileManagement.class, ignore);
		_tmp.addAll(tmpMethodsMap.keySet());
		tmpMethodsMap = ClassUtils.getAllInterfacesMethods(
				javax.slee.profile.ProfileMBean.class, ignore);
		_tmp.addAll(tmpMethodsMap.keySet());
		tmpMethodsMap = ClassUtils.getAllInterfacesMethods(
				javax.slee.profile.Profile.class, ignore);
		_tmp.addAll(tmpMethodsMap.keySet());
		tmpMethodsMap = ClassUtils.getAllInterfacesMethods(
				javax.management.DynamicMBean.class, ignore);
		_tmp.addAll(tmpMethodsMap.keySet());
		tmpMethodsMap = ClassUtils.getAllInterfacesMethods(
				javax.management.MBeanRegistration.class, ignore);
		_tmp.addAll(tmpMethodsMap.keySet());

		_FORBIDEN_METHODS = Collections.unmodifiableSet(_tmp);

	}

	public void setComponentRepository(ComponentRepository repository) {
		this.repository = repository;

	}

	public ProfileSpecificationComponent getComponent() {
		return component;
	}

	public void setComponent(ProfileSpecificationComponent component) {
		this.component = component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.component.validator.Validator#validate()
	 */
	public boolean validate() {

		return false;
	}

	boolean validateCMPInterface() {

		// this is kind of quick failure for each method/field, since when
		// something goes wrong for one method we can validate it further... -
		// CMPs in sbbs are different since we know their names.
		boolean passed = true;
		String errorBuffer = new String("");

		try {
			// this is obligatory
			Class interfaceClass = this.component.getProfileCmpInterfaceClass();

			if (!interfaceClass.isInterface()) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface class is not an interface. ",
						"10.6", errorBuffer);

				return passed;
			}

			if (this.component.isSlee11()
					&& interfaceClass.getPackage() == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface in SLEE 1.1 components must be defined in package. ",
						"10.6", errorBuffer);
			}

			Set<String> ignore = new HashSet<String>();
			ignore.add("java.lang.Object");
			Map<String, Method> interfaceMethods = ClassUtils
					.getAllInterfacesMethods(interfaceClass, ignore);

			// holds fieldName->occurances - EACH field MUST occure twice, as
			// setter and getter
			Map<String, Integer> fieldOccurances = new HashMap<String, Integer>();
			// holds fieldName->type mapp, setter and getter type must match
			Map<String, Class> fieldToType = new HashMap<String, Class>();

			Iterator<Entry<String, Method>> methodsIterator = interfaceMethods
					.entrySet().iterator();

			while (methodsIterator.hasNext()) {
				Entry<String, Method> entry = methodsIterator.next();
				Method m = entry.getValue();
				String methodName = m.getName();
				if (_FORBIDEN_METHODS.contains(entry.getKey())) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile cmp interface defines methods that are prohibited.: "
									+ methodName, "10.17", errorBuffer);
					methodsIterator.remove();
					continue;
				}
				if (!(methodName.startsWith("get") || methodName
						.startsWith("set"))) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile cmp interface methods must follow java bean setter/getter accessors. Offending method: "
									+ methodName, "10.6", errorBuffer);
					methodsIterator.remove();
					continue;
				}

				Class fieldType = null;
				String cmpFieldName = null;
				if (methodName.startsWith("get")) {
					if (!validateCMPInterfaceGetter(m)) {
						passed = false;
						methodsIterator.remove();
						continue;
					}
					fieldType = m.getReturnType();
					cmpFieldName = methodName.replaceFirst("get", "");
				} else {
					if (!validateCMPInterfaceSetter(m)) {
						passed = false;
						methodsIterator.remove();
						continue;
					}
					fieldType = m.getParameterTypes()[0];
					cmpFieldName = methodName.replaceFirst("set", "");
				}

		
				if (!(_ALLOWED_CMPS_TYPES.contains(fieldType.toString()) || validateSerializableType(fieldType,methodName))) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile cmp interface field has wrong type, only java primitives, serializables and arrays of those types are allowed. Offending field: "
									+ cmpFieldName, "10.6", errorBuffer);

				}

				Character c = cmpFieldName.charAt(0);
				if (!Character.isUpperCase(c)) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile cmp interface field has wrong type, of first char in name. Offending field: "
									+ cmpFieldName, "10.6", errorBuffer);

					// FIXME: should we fail here
					methodsIterator.remove();
					continue;
				}

				cmpFieldName = cmpFieldName.replaceFirst(c + "", Character
						.toLowerCase(c)
						+ "");

				// XXX: this will fail even for duplicate delcarations of
				// fields, but meesages might be missleading.
				if (!fieldOccurances.containsKey(cmpFieldName)) {
					fieldOccurances.put(cmpFieldName, new Integer(1));
					fieldToType.put(cmpFieldName, fieldType);

				} else if (fieldOccurances.get(cmpFieldName) == 1) {
					fieldOccurances.put(cmpFieldName, new Integer(2));

					if (fieldToType.get(cmpFieldName).getName().compareTo(
							fieldType.getName()) != 0) {
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile cmp interface field has wrong type, current type does not match previously encountered. Field: "
										+ cmpFieldName, "10.6", errorBuffer);
					}
				} else {
					// it should be one
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile cmp interface field has been defined more than once, offending method: "
									+ methodName, "10.6", errorBuffer);

					// FIXME: should we fail here
					methodsIterator.remove();
					continue;
				}

			}

		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;

	}

	boolean validateSerializableType(Class fieldType, String method) {

		boolean passed = true;
		String errorBuffer = new String("");

		try {

			// in case of array we have from getName() something like:
			// [Ljava.io.Serializable;
			if (fieldType.isArray()) {
				// ech
				String typStringName = fieldType.getComponentType().getName();
				try {
					Class type = fieldType.forName(typStringName);
					if (ClassUtils.checkInterfaces(fieldType,
							"java.io.Serializable") == null) {

						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile cmp interface allows to store primitive types and serializables, offending method: "
										+ method + " . Type: " + fieldType,
								"10.6", errorBuffer);

					}
				} catch (Exception e) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile cmp interface allows to store primitive types and serializables, offending method: "
									+ method + " . Type: " + fieldType, "10.6",
							errorBuffer);
				}

			} else {
				if (ClassUtils.checkInterfaces(fieldType,
						"java.io.Serializable") == null) {

					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile cmp interface allows to store primitive types and serializables, offending method: "
									+ method + " . Type: " + fieldType, "10.6",
							errorBuffer);

				}
			}
		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;
	}

	boolean validateCMPInterfaceSetter(Method m) {
		boolean passed = true;
		String errorBuffer = new String("");

		try {

			// no throws
			if (m.getExceptionTypes().length > 0) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface setter method must not defined throws clause, offending method: "
								+ m.getName(), "10.6", errorBuffer);
			}

			int modifiers = m.getModifiers();

			// its interface method so it has to be public and abstract ONLY!!!!
//			if (Modifier.isStatic(modifiers)) {
//				passed = false;
//				errorBuffer = appendToBuffer(
//						"Profile specification profile cmp interface setter method must not be static, offending method: "
//								+ m.getName(), "10.6", errorBuffer);
//			}
//
//			if (!Modifier.isAbstract(modifiers)) {
//				passed = false;
//				errorBuffer = appendToBuffer(
//						"Profile specification profile cmp interface setter method must be abstract, offending method: "
//								+ m.getName(), "10.6", errorBuffer);
//			}

			if (m.getParameterTypes().length != 1) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface setter method must have exactly one parameter, offending method: "
								+ m.getName(), "10.6", errorBuffer);
			}

			if (m.getReturnType().getName().compareTo("void") != 0) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface setter method must not declare return type, offending method: "
								+ m.getName(), "10.6", errorBuffer);
			}

		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;

	}

	boolean validateCMPInterfaceGetter(Method m) {
		boolean passed = true;
		String errorBuffer = new String("");

		try {

			// no throws
			// no throws
			if (m.getExceptionTypes().length > 0) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface getter method must not defined throws clause, offending method: "
								+ m.getName(), "10.6", errorBuffer);
			}

			int modifiers = m.getModifiers();

			// its interface method so it has to be public and abstract ONLY!!!!
//			if (Modifier.isStatic(modifiers)) {
//				passed = false;
//				errorBuffer = appendToBuffer(
//						"Profile specification profile cmp interface setter method must not be static, offending method: "
//								+ m.getName(), "10.6", errorBuffer);
//			}
//
//			if (!Modifier.isAbstract(modifiers)) {
//				passed = false;
//				errorBuffer = appendToBuffer(
//						"Profile specification profile cmp interface setter method must be abstract, offending method: "
//								+ m.getName(), "10.6", errorBuffer);
//			}

			if (m.getParameterTypes().length > 0) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface getter method must have no parameters, offending method: "
								+ m.getName(), "10.6", errorBuffer);
			}

			if (m.getReturnType().getName().compareTo("void") == 0) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile cmp interface getter method must declare return type, offending method: "
								+ m.getName(), "10.6", errorBuffer);
			}
		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;

	}

	protected String appendToBuffer(String message, String section,
			String buffer) {
		buffer += (this.component.getDescriptor().getProfileSpecKey()
				+ " : violates section " + section
				+ " of jSLEE 1.1 specification : " + message + "\n");
		return buffer;
	}

}
