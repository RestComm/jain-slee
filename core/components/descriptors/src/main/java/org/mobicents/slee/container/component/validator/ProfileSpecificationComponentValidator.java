/**
 * Start time:10:45:52 2009-02-09<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.slee.SLEEException;

import javassist.Modifier;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQuery;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQueryExpression;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQueryParameter;

import com.sun.org.apache.xpath.internal.operations.Mod;

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

	private final static Set<String> _ALLOWED_MANAGEMENT_TYPES;
	static {
		Set<String> tmp = new HashSet<String>();
		// Section 10.17
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
		tmp.add(Integer.class.toString());
		tmp.add(Boolean.class.toString());
		tmp.add(Byte.class.toString());
		tmp.add(Character.class.toString());
		tmp.add(Double.class.toString());
		tmp.add(Float.class.toString());
		tmp.add(Long.class.toString());
		tmp.add(Short.class.toString());
		tmp.add(Integer[].class.toString());
		tmp.add(Boolean[].class.toString());
		tmp.add(Byte[].class.toString());
		tmp.add(Character[].class.toString());
		tmp.add(Double[].class.toString());
		tmp.add(Float[].class.toString());
		tmp.add(Long[].class.toString());
		tmp.add(Short[].class.toString());

		tmp.add(Serializable.class.toString());
		tmp.add(Serializable[].class.toString());
		// tmp.add(String[].class.toString());
		// tmp.add(String.class.toString());
		_ALLOWED_MANAGEMENT_TYPES = Collections.unmodifiableSet(tmp);

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

	private final static Set<String> _ALLOWED_QUERY_PARAMETER_TYPES;
	static {
		Set<String> tmp = new HashSet<String>();
		// Section 10.17
		tmp.add("int");
		tmp.add("boolean");
		tmp.add("byte");
		tmp.add("char");
		tmp.add("double");
		tmp.add("float");
		tmp.add("long");
		tmp.add("short");
		tmp.add(Integer.class.getName());
		tmp.add(Boolean.class.getName());
		tmp.add(Byte.class.getName());
		tmp.add(Character.class.getName());
		tmp.add(Double.class.getName());
		tmp.add(Float.class.getName());
		tmp.add(Long.class.getName());
		tmp.add(Short.class.getName());
		tmp.add(String.class.getName());
		_ALLOWED_QUERY_PARAMETER_TYPES = Collections.unmodifiableSet(tmp);

	}

	private boolean requriedProfileAbstractClass = false;

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

		
		boolean passed = true;
		
		try{
			
			if(!validateDescriptor())
			{
				//this is quick fail
				passed = false;
				return passed;
			}
			// we cant validate some parts on fail here
			
			if(!validateCMPInterface())
			{
				passed = false;
			
			}else
			{
				if(!validatePorfileTableInterface())
				{
					passed = false;
				}
				
				
			}
			
			if(!validateProfileLocalInterface())
			{
				passed = false;
			}
			
			if(!validateProfileManagementInterface())
			{
				passed = false;
			}
			
			if(!validateAbstractClass())
			{
				passed = false;
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		return passed;
		
		
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

				if (!(_ALLOWED_CMPS_TYPES.contains(fieldType.toString()) || validateSerializableType(
						fieldType, methodName))) {
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
			// if (Modifier.isStatic(modifiers)) {
			// passed = false;
			// errorBuffer = appendToBuffer(
			// "Profile specification profile cmp interface setter method must not be static, offending method: "
			// + m.getName(), "10.6", errorBuffer);
			// }
			//
			// if (!Modifier.isAbstract(modifiers)) {
			// passed = false;
			// errorBuffer = appendToBuffer(
			// "Profile specification profile cmp interface setter method must be abstract, offending method: "
			// + m.getName(), "10.6", errorBuffer);
			// }

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
			// if (Modifier.isStatic(modifiers)) {
			// passed = false;
			// errorBuffer = appendToBuffer(
			// "Profile specification profile cmp interface setter method must not be static, offending method: "
			// + m.getName(), "10.6", errorBuffer);
			// }
			//
			// if (!Modifier.isAbstract(modifiers)) {
			// passed = false;
			// errorBuffer = appendToBuffer(
			// "Profile specification profile cmp interface setter method must be abstract, offending method: "
			// + m.getName(), "10.6", errorBuffer);
			// }

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

	/**
	 * Should not be called when CMP interface validation fails cause it depends
	 * on result of it
	 * 
	 * @return
	 */
	boolean validateProfileManagementInterface() {
		// this is optional
		boolean passed = true;
		String errorBuffer = new String("");

		if (this.component.getProfileManagementInterfaceClass() == null) {
			// it can hapen when its not present
			return passed;
		}

		try {
			Class profileManagementInterfaceClass = this.component
					.getProfileManagementInterfaceClass();

			if (!profileManagementInterfaceClass.isInterface()) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile management interface is not an interface class!!!",
						"10.10", errorBuffer);
				return passed;

			}

			if (this.component.isSlee11()
					&& profileManagementInterfaceClass.getPackage() == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile management interface must be declared within named package.",
						"10.10", errorBuffer);
			}

			if (!Modifier.isPublic(profileManagementInterfaceClass
					.getModifiers())) {

				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile management interface must be decalred as public.",
						"10.10", errorBuffer);

			}

			// now here comes the fun. methods are subject to restrictions form
			// 10.17 and 10.18, BUT interface may implement or just define CMPs
			// that MUST reasemlbe CMPs definition from CMP interface - that is
			// all getter/setter methods defined
			Set<String> ignore = new HashSet<String>();
			ignore.add("java.lang.Object");
			Map<String, Method> cmpInterfaceMethods = ClassUtils
					.getAllInterfacesMethods(this.component
							.getProfileCmpInterfaceClass(), ignore);
			Map<String, Method> managementInterfaceMethods = ClassUtils
					.getAllInterfacesMethods(profileManagementInterfaceClass,
							ignore);

			// we cant simply remove all methods from
			// managementInterfaceMethods.removeAll(cmpInterfaceMethods) since
			// with abstract classes it becomes comp;licated
			// we can have doubling definition with for instance return type or
			// throws clause (as diff) which until concrete class wont be
			// noticed - and is an error....
			// FIXME: does mgmt interface have to define both CMP accessors?
			Iterator<Entry<String, Method>> entryIterator = managementInterfaceMethods
					.entrySet().iterator();
			while (entryIterator.hasNext()) {

				Entry<String, Method> entry = entryIterator.next();
				String key = entry.getKey();

				if (cmpInterfaceMethods.containsKey(key)) {
					if (!compareMethod(entry.getValue(), cmpInterfaceMethods
							.get(key))) {
						// return type or throws clause, or modifiers differ
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile management interface declares method which has signature simlar to CMP method, but it has different throws clause, return type or modifiers, which is wrong.",
								"10.10", errorBuffer);

					} else {
						// we can have setter/getter like as stand alone ?
						if (_FORBIDEN_METHODS.contains(key)) {
							// this is forrbiden, section 10.18
							errorBuffer = appendToBuffer(
									"Profile specification profile management interface declares method from forbiden list, method: "
											+ entry.getKey(), "10.18",
									errorBuffer);

							continue;
						}
						// is this the right place? This tells validator
						// wheather it should require profile abstract class in
						// case of 1.1
						requriedProfileAbstractClass = true;
						// we know that name is ok.
						// FIXME: SPECS Are weird - Management methods may not
						// have the same name and arguments as a Profile CMP
						// field get or set accessor method. <---- ITS CMP
						// METHOD< SIGNATURE IS NAME AND
						// PARAMETERS and its implemented if its doubled from
						// CMP or this interface extends CMP
						// interface.....!!!!!!!!!!!!!!!!!!
						if (key.startsWith("ejb")) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Profile specification profile management interface declares method with wrong prefix, method: "
											+ entry.getKey(), "10.18",
									errorBuffer);

							continue;
						}

						// there are no reqs other than parameters?
						Class[] params = entry.getValue().getParameterTypes();
						for (int index = 0; index < params.length; index++) {
							if (_ALLOWED_MANAGEMENT_TYPES
									.contains(params[index].toString())) {

							} else {
								passed = false;
								errorBuffer = appendToBuffer(
										"Profile specification profile management interface declares management method with wrong parameter at index["
												+ index
												+ "], method: "
												+ entry.getKey(), "10.18",
										errorBuffer);
							}
						}

					}
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

	boolean validateProfileLocalInterface() {

		boolean passed = true;
		String errorBuffer = new String("");

		// FIXME: should not this return generic?
		if (!this.component.isSlee11()
				|| this.component.getProfileLocalInterfaceClass() == null) {
			// its nto mandatory
			return passed;
		}

		try {

			Class profileLocalObjectInterface = this.component
					.getProfileLocalInterfaceClass();

			if (!profileLocalObjectInterface.isInterface()) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile local interface is not an interface class!!!",
						"10.7.4", errorBuffer);
				return passed;

			}

			if (profileLocalObjectInterface.getPackage() == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile local interface must be declared within named package.",
						"10.7.4", errorBuffer);
			}

			if (!Modifier.isPublic(profileLocalObjectInterface.getModifiers())) {

				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile local interface must be declared as public.",
						"10.7.4", errorBuffer);

			}

			Class genericProfleLocalObject = ClassUtils.checkInterfaces(
					profileLocalObjectInterface,
					"javax.slee.profile.ProfileLocalObject");
			if (genericProfleLocalObject == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile local interface must implement javax.slee.profile.ProfileLocalObject.",
						"10.7.4", errorBuffer);
			}
			// now here comes the fun. methods are subject to restrictions form
			// 10.17 and 10.18, BUT interface may implement or just define CMPs
			// that MUST reasemlbe CMPs definition from CMP interface - that is
			// all getter/setter methods defined
			Set<String> ignore = new HashSet<String>();
			ignore.add("java.lang.Object");
			ignore.add("javax.slee.profile.ProfileLocalObject");
			Map<String, Method> cmpInterfaceMethods = ClassUtils
					.getAllInterfacesMethods(this.component
							.getProfileCmpInterfaceClass(), ignore);
			Map<String, Method> managementInterfaceMethods = ClassUtils
					.getAllInterfacesMethods(profileLocalObjectInterface,
							ignore);

			// we cant simply remove all methods from
			// managementInterfaceMethods.removeAll(cmpInterfaceMethods) since
			// with abstract classes it becomes comp;licated
			// we can have doubling definition with for instance return type or
			// throws clause (as diff) which until concrete class wont be
			// noticed - and is an error....
			// FIXME: does mgmt interface have to define both CMP accessors?
			Iterator<Entry<String, Method>> entryIterator = managementInterfaceMethods
					.entrySet().iterator();

			while (entryIterator.hasNext()) {

				Entry<String, Method> entry = entryIterator.next();
				String key = entry.getKey();

				if (cmpInterfaceMethods.containsKey(key)) {
					if (!compareMethod(entry.getValue(), cmpInterfaceMethods
							.get(key))) {
						// return type or throws clause, or modifiers differ
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile local interface declares method which has signature similar to CMP method, but it has different throws clause, return type or modifiers, which is wrong.",
								"10.7.4", errorBuffer);

					} else {
						// we can have setter/getter like as stand alone ?
						if (_FORBIDEN_METHODS.contains(key)) {
							// this is forrbiden, section 10.18
							errorBuffer = appendToBuffer(
									"Profile specification profile local interface declares method from forbiden list, method: "
											+ entry.getKey(), "10.18",
									errorBuffer);

							continue;
						} else if (key.startsWith("get")
								|| key.startsWith("set")) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Profile specification profile local interface declares method which is setter/getter and does not match CMP interface method, method: "
											+ entry.getKey(), "10.18",
									errorBuffer);

							continue;
						}

						// is this the right place? This tells validator
						// wheather it should require profile abstract class in
						// case of 1.1
						requriedProfileAbstractClass = true;

						// we know that name is ok.
						// FIXME: SPECS Are weird - Management methods may not
						// have the same name and arguments as a Profile CMP
						// field get or set accessor method. <---- ITS CMP
						// METHOD< SIGNATURE IS NAME AND
						// PARAMETERS and its implemented if its doubled from
						// CMP or this interface extends CMP
						// interface.....!!!!!!!!!!!!!!!!!!
						if (key.startsWith("ejb")) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Profile specification profile local interface declares method with wrong prefix, method: "
											+ entry.getKey(), "10.18",
									errorBuffer);

							continue;
						}

						Class[] params = entry.getValue().getParameterTypes();
						for (int index = 0; index < params.length; index++) {
							// FIXME: whichc should we use here?
							if ((_ALLOWED_CMPS_TYPES.contains(params[index]
									.toString()) || validateSerializableType(
									params[index], key))) {

							} else {
								passed = false;
								errorBuffer = appendToBuffer(
										"Profile specification profile management interface declares management method with wrong parameter at index["
												+ index
												+ "], method: "
												+ entry.getKey(), "10.18",
										errorBuffer);
							}
						}

						// lets check exceptions, we can define all except
						// java.rmi.RemoteException
						for (Class exceptionClass : entry.getValue()
								.getExceptionTypes()) {
							// FIXME: should we check unckecked exceptions here
							// ?
							if (ClassUtils.checkClasses(exceptionClass,
									"java.rmi.RemoteException") != null) {
								passed = false;
								errorBuffer = appendToBuffer(
										"Profile specification profile management interface declares management method with wrong exception in throws clause, it can not throw java.rmi.RemoteException(or its sub classes), method: "
												+ entry.getKey(), "10.7.4",
										errorBuffer);
							}

						}
					}
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

	/**
	 * shoudl not be run if other interfaces vaildation fails.
	 * 
	 * @return
	 */
	boolean validateAbstractClass() {
		boolean passed = true;
		String errorBuffer = new String("");

		try {

			if (this.component.getDescriptor().getProfileAbstractClass() == null) {

				if (this.requriedProfileAbstractClass) {
					errorBuffer = appendToBuffer(
							"Profile specification profile management abstract class must be present",
							"3.X", errorBuffer);
					return passed;
				}

			} else {

				if (this.component.getProfileAbstractClass() == null) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile management abstract class has not been loaded",
							"3.X", errorBuffer);
					return passed;
				}

			}

			Class profileAbstractClass = this.component
					.getProfileAbstractClass();

			// if (profileAbstractClass.isInterface()
			// || profileAbstractClass.isEnum()) {
			// passed = false;
			// errorBuffer = appendToBuffer(
			// "Profile specification profile abstract class in not a clas.",
			// "10.11", errorBuffer);
			// return passed;
			// }

			if (this.component.isSlee11()
					&& profileAbstractClass.getPackage() == null) {

				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile abstract class must be defined in package.",
						"10.11", errorBuffer);

			}

			int modifiers = profileAbstractClass.getModifiers();

			if (!Modifier.isAbstract(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile abstract class must be defined abstract.",
						"10.11", errorBuffer);
			}

			if (!Modifier.isPublic(modifiers)) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile abstract class must be defined public.",
						"10.11", errorBuffer);
			}

			// in case of 1.0 it has to implement as concrete methods from
			// javax.slee.profile.ProfileManagement - section 10.8 of 1.0 specs
			Map<String, Method> requiredLifeCycleMethods = null;
			Set<String> ignore = new HashSet<String>();
			ignore.add("java.lang.Object");
			if (this.component.isSlee11()) {
				Class javaxSleeProfileProfileClass = ClassUtils
						.checkInterfaces(profileAbstractClass,
								"javax.slee.profile.Profile");
				if (javaxSleeProfileProfileClass == null) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile abstract class must implement javax.slee.profile.Profile.",
							"10.11", errorBuffer);
				}
				requiredLifeCycleMethods = ClassUtils.getAllInterfacesMethods(
						javaxSleeProfileProfileClass, ignore);
			} else {
				Class javaxSleeProfileProfileManagement = ClassUtils
						.checkInterfaces(profileAbstractClass,
								"javax.slee.profile.ProfileManagement");
				if (javaxSleeProfileProfileManagement == null) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile abstract class must implement javax.slee.profile.ProfileManagement.",
							"10.8", errorBuffer);
				}
				requiredLifeCycleMethods = ClassUtils.getAllInterfacesMethods(
						javaxSleeProfileProfileManagement, ignore);
			}

			Map<String, Method> abstractMethods = ClassUtils
					.getAbstractMethodsFromClass(profileAbstractClass);
			Map<String, Method> abstractMethodsFromSuperClasses = ClassUtils
					.getAbstractMethodsFromSuperClasses(profileAbstractClass);

			Map<String, Method> concreteMethods = ClassUtils
					.getConcreteMethodsFromClass(profileAbstractClass);
			Map<String, Method> concreteMethodsFromSuperClasses = ClassUtils
					.getConcreteMethodsFromSuperClasses(profileAbstractClass);

			for (Entry<String, Method> entry : requiredLifeCycleMethods
					.entrySet()) {

				Method m = entry.getValue();
				//
				Method methodFromClass = ClassUtils.getMethodFromMap(m
						.getName(), m.getParameterTypes(), concreteMethods,
						concreteMethodsFromSuperClasses);

				if (methodFromClass == null) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile abstract class must implement certain lifecycle methods. Method not found in concrete(non private) methods: "
									+ m.getName(), "10.11", errorBuffer);
					continue;
				}

				// it concrete - must check return type
				if (m.getReturnType().getName().compareTo(
						methodFromClass.getReturnType().getName()) != 0) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile abstract class must implement certain lifecycle methods. Method with name: "
									+ m.getName()
									+ " found in concrete(non private) methods has different return type: "
									+ methodFromClass.getReturnType()
									+ ", than one declared in interface: "
									+ m.getReturnType(), "10.11", errorBuffer);
				}

				if (!Arrays.equals(m.getExceptionTypes(), methodFromClass
						.getExceptionTypes())) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile abstract class must implement certain lifecycle methods. Method with name: "
									+ m.getName()
									+ " found in concrete(non private) methods has different throws clause than one found in class.",
							"10.11", errorBuffer);
				}

			}

			// in 1.1 and 1.0 it must implement CMP interfaces, but methods
			// defined there MUST stay abstract
			Class profileCMPInterface = ClassUtils.checkInterfaces(
					profileAbstractClass, this.component
							.getProfileCmpInterfaceClass().getName());

			// abstract class implements CMP Interface, but leaves all methods
			// as abstract
			Map<String, Method> cmpInterfaceMethods = ClassUtils
					.getAllInterfacesMethods(profileCMPInterface, ignore);

			if (profileCMPInterface == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile abstract class must implement defined profile CMP interface.",
						"10.11", errorBuffer);
			} else {

				for (Entry<String, Method> entry : cmpInterfaceMethods
						.entrySet()) {

					Method m = entry.getValue();
					//
					Method methodFromClass = ClassUtils.getMethodFromMap(m
							.getName(), m.getParameterTypes(), concreteMethods,
							concreteMethodsFromSuperClasses);

					if (methodFromClass == null) {
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile abstract class must leave CMP interface methods as abstract, it can not be concrete: "
										+ m.getName(), "10.11", errorBuffer);
						continue;
					}

					// it concrete - must check return type
					if (m.getReturnType().getName().compareTo(
							methodFromClass.getReturnType().getName()) != 0) {
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile abstract class must not decalre methods from CMP interface with different return type. Method with name: "
										+ m.getName()
										+ " found in (non private) class methods has different return type: "
										+ methodFromClass.getReturnType()
										+ ", than one declared in interface: "
										+ m.getReturnType(), "10.11",
								errorBuffer);
					}

					if (!Arrays.equals(m.getExceptionTypes(), methodFromClass
							.getExceptionTypes())) {
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile abstract class must not change throws clause. Method with name: "
										+ m.getName()
										+ " found in (non private) class methods has different throws clause than one found in class.",
								"10.11", errorBuffer);
					}

					// FIXME: should we do that?
					abstractMethods.remove(entry.getKey());
					abstractMethodsFromSuperClasses.remove(entry.getKey());
				}

			}

			// those checks are......
			// 1.0 and 1.1 if we define management interface we have to
			// implement it, and all methods that are not CMPs
			if (this.component.getDescriptor().getProfileManagementInterface() != null) {
				Class profileManagementInterfaceClass = this.component
						.getProfileManagementInterfaceClass();
				Map<String, Method> profileManagementInterfaceMethods = ClassUtils
						.getAllInterfacesMethods(
								profileManagementInterfaceClass, ignore);
				// methods except those defined in CMP interface must be
				// concrete

				for (Entry<String, Method> entry : profileManagementInterfaceMethods
						.entrySet()) {

					Method m = entry.getValue();

					// CMP methods must stay abstract
					// check if this method is the same as in CMP interface is
					// done elsewhere
					// that check shoudl be ok to run this one!!! XXX
					if (cmpInterfaceMethods.containsKey(entry.getKey())) {
						// we do nothing, cmp interface is validate above

					} else {
						// 10.8/10.11
						Method concreteMethodFromAbstractClass = ClassUtils
								.getMethodFromMap(m.getName(), m
										.getParameterTypes(), concreteMethods,
										concreteMethodsFromSuperClasses);
						if (concreteMethodFromAbstractClass == null) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Profile specification profile abstract class must implement as non private methods from profile management interface other than CMP methods",
									"10.11", errorBuffer);
							continue;
						}

						int concreteMethodModifiers = concreteMethodFromAbstractClass
								.getModifiers();
						// public, and cannot be static,abstract, or final.
						if (!Modifier.isPublic(concreteMethodModifiers)) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Profile specification profile abstract class must implement methods from profile management interface as public, offending method: "
											+ concreteMethodFromAbstractClass
													.getName(), "10.11",
									errorBuffer);
						}

						if (Modifier.isStatic(concreteMethodModifiers)) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Profile specification profile abstract class must implement methods from profile management interface as not static, offending method: "
											+ concreteMethodFromAbstractClass
													.getName(), "10.11",
									errorBuffer);
						}

						if (Modifier.isFinal(concreteMethodModifiers)) {
							passed = false;
							errorBuffer = appendToBuffer(
									"Profile specification profile abstract class must implement methods from profile management interface as not final, offending method: "
											+ concreteMethodFromAbstractClass
													.getName(), "10.11",
									errorBuffer);
						}
					}

				}

			}

			if (this.component.isSlee11()) {
				// ProfileLocalObject and UsageInterface are domains of 1.1
				// uff, ProfileLocal again that stupid check cross two
				// interfaces and one abstract class.....

				if (this.component.getDescriptor().getProfileLocalInterface() != null) {

					// abstract class MUST NOT implement it
					if (ClassUtils.checkInterfaces(profileAbstractClass,
							this.component.getDescriptor()
									.getProfileLocalInterface()
									.getProfileLocalInterfaceName()) != null) {
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile abstract class must not implement profile local interface in any way",
								"10.11", errorBuffer);
					}

					Class profileLocalObjectClass = this.component
							.getProfileLocalInterfaceClass();
					Map<String, Method> profileLocalObjectInterfaceMethods = ClassUtils
							.getAllInterfacesMethods(profileLocalObjectClass,
									ignore);
					// methods except those defined in CMP interface must be
					// concrete

					for (Entry<String, Method> entry : profileLocalObjectInterfaceMethods
							.entrySet()) {

						Method m = entry.getValue();

						// CMP methods must stay abstract
						// check if this method is the same as in CMP interface
						// is done elsewhere
						// that check shoudl be ok to run this one!!! XXX
						if (cmpInterfaceMethods.containsKey(entry.getKey())) {
							// we do nothing, cmp interface is validate above

						} else {
							// 10.8/10.11
							Method concreteMethodFromAbstractClass = ClassUtils
									.getMethodFromMap(m.getName(), m
											.getParameterTypes(),
											concreteMethods,
											concreteMethodsFromSuperClasses);
							if (concreteMethodFromAbstractClass == null) {
								passed = false;
								errorBuffer = appendToBuffer(
										"Profile specification profile abstract class must implement as non private methods from profile local interface other than CMP methods",
										"10.11", errorBuffer);
								continue;
							}

							int concreteMethodModifiers = concreteMethodFromAbstractClass
									.getModifiers();
							// public, and cannot be static,abstract, or final.
							if (!Modifier.isPublic(concreteMethodModifiers)) {
								passed = false;
								errorBuffer = appendToBuffer(
										"Profile specification profile abstract class must implement methods from profile local interface as public, offending method: "
												+ concreteMethodFromAbstractClass
														.getName(), "10.11",
										errorBuffer);
							}

							if (Modifier.isStatic(concreteMethodModifiers)) {
								passed = false;
								errorBuffer = appendToBuffer(
										"Profile specification profile abstract class must implement methods from profile local interface as not static, offending method: "
												+ concreteMethodFromAbstractClass
														.getName(), "10.11",
										errorBuffer);
							}

							if (Modifier.isFinal(concreteMethodModifiers)) {
								passed = false;
								errorBuffer = appendToBuffer(
										"Profile specification profile abstract class must implement methods from profile management interface as not final, offending method: "
												+ concreteMethodFromAbstractClass
														.getName(), "10.11",
										errorBuffer);
							}
						}

					}
				}

				// usage parameters
				if (this.component.getDescriptor()
						.getProfileUsageParameterInterface() != null) {
					if (!validateProfileUsageInterface(abstractMethods,
							abstractMethodsFromSuperClasses)) {
						passed = false;
					}

				}

			}

			// FIXME: add check on abstract methods same as in SBB ?

		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;
	}

	boolean validateProfileUsageInterface(
			Map<String, Method> abstractClassMethods,
			Map<String, Method> abstractMethodsFromSuperClasses) {
		boolean passed = true;
		String errorBuffer = new String("");

		// FIXME: should not this return generic?
		if (!this.component.isSlee11()) {
			// its nto mandatory
			return passed;
		}

		try {
			if (this.component.getProfileUsageInterfaceClass() == null) {
				if (this.component.getDescriptor()
						.getProfileUsageParameterInterface() != null) {
					passed = false;

					errorBuffer = appendToBuffer(
							"Profile specification profile usage interface class is null, it should not be.",
							"10.X", errorBuffer);
					return passed;
				} else {
					return passed;
				}
			}

			// we only validate usage interface here
			if (!UsageInterfaceValidator
					.validateProfileSpecificationUsageParameterInterface(
							this.component, abstractClassMethods,
							abstractMethodsFromSuperClasses)) {
				passed = false;
			}

		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;
	}

	boolean validatePorfileTableInterface() {

		boolean passed = true;
		String errorBuffer = new String("");

		// FIXME: should not this return generic?
		if (!this.component.isSlee11()
				|| this.component.getDescriptor().getProfileTableInterface() == null) {
			// its nto mandatory
			return passed;
		}

		try {

			Class profileTableInterface = this.component
					.getProfileTableInterfaceClass();

			// must be in pacakge
			if (profileTableInterface.getPackage() == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile table interface must be declared inside pacakge.",
						"10.8", errorBuffer);

			}

			if (!Modifier.isPublic(profileTableInterface.getModifiers())) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile table interface must be declared as public.",
						"10.8", errorBuffer);
			}

			Class genericProfileTableInterface = ClassUtils.checkInterfaces(
					profileTableInterface, "javax.slee.profile.ProfileTable");

			if (genericProfileTableInterface == null) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile table interface must extend in some way javax.slee.profile.ProfileTable.",
						"10.8", errorBuffer);
				// we fail here fast?
				return passed;
			}
			Set<String> ignore = new HashSet<String>();
			ignore.add("java.lang.Object");

			// There is no clause in specs saying custom can not override
			// methods, this will fail on concrete class generation though, let
			// filter
			Map<String, Method> javaxSleeProfileProfileTableMethods = ClassUtils
					.getAllInterfacesMethods(genericProfileTableInterface,
							ignore);

			ignore.add("javax.slee.profile.ProfileTable");

			Map<String, Method> profileTableInterfaceMethods = ClassUtils
					.getAllInterfacesMethods(profileTableInterface, ignore);

			// if we have common part here, this means that methods are double
			// declared, either exactly the same or with different return type
			// or exceptions
			Set<String> tmpKeySet = new HashSet<String>();
			Set<String> tmpKeySetToCompare = new HashSet<String>();

			tmpKeySet.addAll(javaxSleeProfileProfileTableMethods.keySet());
			tmpKeySetToCompare.addAll(profileTableInterfaceMethods.keySet());
			tmpKeySet.retainAll(tmpKeySetToCompare);
			if (tmpKeySet.size() != 0) {
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification profile table interface declares methods that double generic profile table interface, this may cause concrete class generation/instantion to fail.",
						"10.8", errorBuffer);
			}

			// else its query validation, here we have slightly different
			// approach, let iterate over methods, as we shoudl validate them
			// here we asume that xml constraints/conent is ok, - for intance
			// compare->parameter == query-parameter->name, parameter types have
			// been checked
			// here we just validate method against required ones
			// by now all queries are validated, no doubling
			Iterator<Entry<String, Method>> iterator = profileTableInterfaceMethods
					.entrySet().iterator();

			// FIXME: all queries have to match?
			Map<String, MQuery> nameToQueryMap = this.component.getDescriptor()
					.getQueriesMap();

			Class cmpInterfaceClass = this.component
					.getProfileCmpInterfaceClass();

			while (iterator.hasNext()) {
				Entry<String, Method> entry = iterator.next();
				Method m = entry.getValue();
				String methodName = m.getName();

				if (!methodName.startsWith("query")) {
					passed = false;
					iterator.remove();
					errorBuffer = appendToBuffer(
							"Profile specification profile table interface declares method with wrong name: "
									+ methodName, "10.8.2", errorBuffer);
					continue;
				}

				String queryName = methodName.replace("query", "");
				queryName = queryName.replaceFirst(queryName.charAt(0) + "",
						Character.toLowerCase(queryName.charAt(0)) + "");

				if (!nameToQueryMap.containsKey(queryName)) {
					passed = false;
					iterator.remove();
					errorBuffer = appendToBuffer(
							"Profile specification profile table interface declares wrong method with name: "
									+ methodName
									+ ", it does not match any query defined in descriptor",
							"10.8.2", errorBuffer);
					continue;
				}

				MQuery query = nameToQueryMap.get(queryName);

				// defined parameter types are ok in case of xml, we need to
				// check method
				Class returnType = m.getReturnType();

				if (returnType.getName().compareTo("java.util.Collection") != 0) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile table interface declares wrong return type: "
									+ returnType + " in method with name: "
									+ methodName
									+ ", it should be java.util.Collection.",
							"10.8.2", errorBuffer);
				}

				Class[] exceptions = m.getExceptionTypes();

				boolean foundTransactionRequiredLocalException = false;
				boolean foundSLEEException = false;
				for (Class c : exceptions) {
					if (c.getName().compareTo(
							"javax.slee.TransactionRequiredLocalException") == 0)
						foundTransactionRequiredLocalException = true;
					else if (c.getName().compareTo("javax.slee.SLEEException") == 0) {
						foundSLEEException = true;
					} else {
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile table interface declares method with wrong exception in throws method with name: "
										+ methodName
										+ ", exception: "
										+ c.getName(), "10.8.2", errorBuffer);
					}
				}

				if (foundSLEEException
						&& foundTransactionRequiredLocalException) {
					// do nothing
				} else {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile table interface declares method with wrong exception in throws method with name: "
									+ methodName
									+ ", it shoudl declare SLEEException["
									+ foundSLEEException
									+ "] and TransactionRequiredLocalException["
									+ foundTransactionRequiredLocalException
									+ "]", "10.8.2", errorBuffer);
				}

				// lets see params - param type must match declared in MQuery,
				// also type must match CMP field from interface (and there must
				// be that kind of cmp

				Class[] parameterTypes = m.getParameterTypes();

				List<MQueryParameter> queryParameters = new ArrayList<MQueryParameter>();
				queryParameters.addAll(query.getQueryParameters());

				if (parameterTypes.length != queryParameters.size()) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification profile table interface declares method with wrong parameters count, present: "
									+ parameterTypes.length
									+ ", expected: "
									+ queryParameters.size(), "10.8.2",
							errorBuffer);
				} else {

					// yes, a bit different
					String parametersErrorBuffer = "Parameters that did not match descriptor: ";
					boolean failedOnQueries = false;
					for (int index = 0; index < parameterTypes.length; index++) {

						// we can make some checks
						// first lets check type

						if (parameterTypes[index].getName().compareTo(
								queryParameters.get(index).getType()) != 0) {
							failedOnQueries = true;
							passed = false;
							parametersErrorBuffer += " parameter: "
									+ queryParameters.get(index).getName()
									+ " declared type: "
									+ queryParameters.get(index).getType()
									+ " method type: " + parameterTypes[index]
									+ " in query method at index: " + index
									+ ",";
						}
						
						//ech, this could go into xml validation
						if(!_ALLOWED_QUERY_PARAMETER_TYPES.contains(parameterTypes[index].getName()))
						{
							parametersErrorBuffer += " method parameter: "
								+ queryParameters.get(index).getName()+
								"has wrong type: " + parameterTypes[index]+", ";
						}

					}

					if (failedOnQueries) {
						passed = false;
						errorBuffer = appendToBuffer(
								"Profile specification profile table interface declares wrong method["+methodName+"] to match declared query, failed to match parameters - \n"
										+ parametersErrorBuffer, "10.20.2",
								errorBuffer);
					}

					if (!validateQueryAgainstCMPFields(queryName,
							cmpInterfaceClass, query.getQueryExpression())) {
						passed = false;
					}

				}

				// now we have to validate CMP part

			}

		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;
	}

	boolean validateQueryAgainstCMPFields(String queryName,
			Class cmpInterfaceClass, MQueryExpression expression) {
		boolean passed = true;
		String attributeName = null;
		String errorBuffer = new String("");
		try {
			switch (expression.getType()) {
			// "complex types"
			case And:
				for (MQueryExpression mqe : expression.getAnd()) {
					if (!validateQueryAgainstCMPFields(queryName,
							cmpInterfaceClass, mqe)) {
						passed = false;
					}
				}
				break;
			case Or:
				for (MQueryExpression mqe : expression.getOr()) {
					if (!validateQueryAgainstCMPFields(queryName,
							cmpInterfaceClass, mqe)) {
						passed = false;
					}
				}
				break;
			// "simple" types
			case Not:
				// this is one akward case :)
				switch (expression.getNot().getType()) {
				// "complex types"

				case Compare:
					attributeName = expression.getNot().getCompare()
							.getAttributeName();
					break;
				case HasPrefix:
					attributeName = expression.getNot().getHasPrefix()
							.getAttributeName();
					break;
				case LongestPrefixMatch:
					attributeName = expression.getNot().getLongestPrefixMatch()
							.getAttributeName();
					break;
				case RangeMatch:
					attributeName = expression.getNot().getRangeMatch()
							.getAttributeName();
					break;
				}

				break;

			case Compare:
				attributeName = expression.getCompare().getAttributeName();
				break;
			case HasPrefix:
				attributeName = expression.getHasPrefix().getAttributeName();
				break;
			case LongestPrefixMatch:
				attributeName = expression.getLongestPrefixMatch()
						.getAttributeName();
				break;
			case RangeMatch:
				attributeName = expression.getRangeMatch().getAttributeName();
				break;
			}

			// now we have to validate CMP field and type
			try {
				Method m = cmpInterfaceClass.getMethod(
						"get"
								+ (attributeName.replaceFirst(""
										+ attributeName.charAt(0), ""
										+ Character.toUpperCase(attributeName
												.charAt(0)))), null);
				if (m.getReturnType().getName().compareTo(
						java.lang.String.class.getName()) != 0) {
					passed = false;
					errorBuffer = appendToBuffer(
							"Profile specification declared wrong static query - operator references wrong type cmp field, cmp attribute: "
									+ attributeName
									+ ", type: "
									+ m.getReturnType()
									+ ", type can onyl be java.lang.String",
							"10.20.2", errorBuffer);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				passed = false;
				errorBuffer = appendToBuffer(
						"Profile specification declared wrong static query - operator does not match against cmp field, requested cmp attribute: "
								+ attributeName, "10.20.2", errorBuffer);
			}

		} finally {

			if (!passed) {
				logger.error(errorBuffer);
				System.err.println(errorBuffer);
			}

		}

		return passed;
	}

	/**
	 * Validated descriptor against some basic constraints: all references are
	 * correct, some fields are decalred properly, no double definitions, if
	 * proper elements are present - for instance some elements exclude others.
	 * 
	 * @return
	 */
	boolean validateDescriptor() {

		return false;
		
	}

	boolean compareMethod(Method m1, Method m2) {

		// those two are in key
		if (m1.getModifiers() != m2.getModifiers())
			return false;
		if (!Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes())) {
			return false;
		}

		if (m1.getName().compareTo(m2.getName()) != 0)
			return false;

		if (!Arrays.equals(m1.getExceptionTypes(), m2.getExceptionTypes())) {
			return false;
		}

		if (m1.getReturnType().getName()
				.compareTo(m2.getReturnType().getName()) != 0) {
			return false;
		}

		return true;
	}

	protected String appendToBuffer(String message, String section,
			String buffer) {
		buffer += (this.component.getDescriptor().getProfileSpecificationID()
				+ " : violates section " + section
				+ " of jSLEE 1.1 specification : " + message + "\n");
		return buffer;
	}

}
