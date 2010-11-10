/**
 *   Copyright 2006 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.util;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

/**
 *  Description:
 * <p>
 *  Gives some JAIN SLEE utility.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class JainUtils {
	public static final int UNKWNOWN_TYPEID = -1;
	public static final int EVENT_TYPEID = 2;
	public static final int SBB_TYPEID = 3;
	public static final int RES_TYPE_TYPEID = 4;
	public static final int RES_TYPEID = 5;
	public static final int PROFILE_TYPEID =6;
	public static final int SERVICE_TYPEID =7;

	public static final int DEPLOYABLE_UNIT_TYPEID = -1000;
	public static final int GENERATED_DEPLOYABLE_UNIT_TYPEID = -1001;

	public static final String DEPLOYABLE_UNIT_DESCRIPTOR_FILE = "META-INF/deployable-unit.xml";
	public static final String EVENT_DESCRIPTOR_FILE = "META-INF/event-jar.xml";
	public static final String SBB_DESCRIPTOR_FILE = "META-INF/sbb-jar.xml";
	public static final String PROFILE_SPEC_DESCRIPTOR_FILE = "META-INF/profile-spec-jar.xml";
	public static final String OSP_PROFILE_SPEC_DESCRIPTOR_FILE = "META-INF/osp-profile-spec-jar.xml";
	public static final String RESOURCE_ADAPTOR_TYPE_DESCRIPTOR_FILE = "META-INF/resource-adaptor-type-jar.xml";
	public static final String RESOURCE_ADAPTOR_DESCRIPTOR_FILE = "META-INF/resource-adaptor-jar.xml";
	public static final String RESOURCE_ADAPTOR_INSTANCES_DESCRIPTOR_FILE = "META-INF/resource-adaptor-instances-jar.xml";

	/** The singleton instance*/
	private static JainUtils instance = null;

	protected JainUtils() {
	}
	
	/**
	 * @return the singleton object.
	 */
	public static  JainUtils getInstance(){
		if(instance == null){
			instance = new JainUtils();
		}
		return instance;
	}

	/**
	 * Determine the type of the component.
	 * @param file the component Jar file
	 * @return the type of the component , for instance, if the component is a DU it will return
	 *  @link JainUtils#DEPLOYABLE_UNIT_TYPEID 
	 * @throws IOException
	 */
	public  int getJarType(File file) throws IOException {
		try {
			if (JarUtils.fileExists(file, EVENT_DESCRIPTOR_FILE)
					|| JarUtils.fileExists(file, EVENT_DESCRIPTOR_FILE.toLowerCase())) {
				return EVENT_TYPEID;
			} else if (JarUtils.fileExists(file, SBB_DESCRIPTOR_FILE)
					|| JarUtils.fileExists(file, SBB_DESCRIPTOR_FILE.toLowerCase())) {
				return SBB_TYPEID;
			} else if (JarUtils.fileExists(file, PROFILE_SPEC_DESCRIPTOR_FILE)
					|| JarUtils.fileExists(file, PROFILE_SPEC_DESCRIPTOR_FILE.toLowerCase())) {
				return PROFILE_TYPEID;
			} else if (JarUtils.fileExists(file, RESOURCE_ADAPTOR_TYPE_DESCRIPTOR_FILE)
					|| JarUtils.fileExists(file, RESOURCE_ADAPTOR_TYPE_DESCRIPTOR_FILE.toLowerCase())) {
				return RES_TYPE_TYPEID;
			} else if (JarUtils.fileExists(file, RESOURCE_ADAPTOR_DESCRIPTOR_FILE)
					|| JarUtils.fileExists(file, RESOURCE_ADAPTOR_DESCRIPTOR_FILE.toLowerCase())) {
				return RES_TYPEID;
			} else if (JarUtils.fileExists(file, DEPLOYABLE_UNIT_DESCRIPTOR_FILE)
					|| JarUtils.fileExists(file, DEPLOYABLE_UNIT_DESCRIPTOR_FILE.toLowerCase())) {
				return DEPLOYABLE_UNIT_TYPEID;
			} else {
				return UNKWNOWN_TYPEID;
			}
		} catch (ZipException zipException) {
			return UNKWNOWN_TYPEID; // not a zip
		}
	}

	public static void checkValidProfileType(Class type) throws IllegalArgumentException {
	}
	
	/**
	 * @param typeID
	 * @return the string corresponding to the type ID.
	 */
	public String getType(int typeID){
		switch (typeID) {
		case JainUtils.EVENT_TYPEID:
			return "Event";
		case JainUtils.PROFILE_TYPEID:
			return "Profile Specification";
		case JainUtils.DEPLOYABLE_UNIT_TYPEID:
			return "Deployable Unit";
		case JainUtils.RES_TYPE_TYPEID:
			return "Resource Adaptor type";
		case JainUtils.RES_TYPEID:
			return "Resource Adaptor";
		case JainUtils.SBB_TYPEID:
			return "SBB";
		default:
			return "?";
		}
	}

}
