/**
 *   Copyright 2005 Open Cloud Ltd.
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
package org.mobicents.eclipslee.util;

/**
 * @author cath
 */
public class Utils {

	// No instantiation.
	protected Utils() {
	}
	
	/**
	 * Returns the package name from an OS formatted string that is of the form
	 * /[ProjectName]/[package]/[starts]/[here]
	 * @param containerName the output from Path.toOSString()
	 * @return the package portion of the input, "" if no package present.
	 */
	
	public static String getPackageFromContainer(String containerName) {
		
		// 2nd instance of / begins the package name
		
		int first = containerName.indexOf('/');
		if (first == -1)
			return "";
		
		int second = containerName.indexOf('/', first + 1);
		if (second == -1)
			return "";
		
		return containerName.substring(second + 1).replaceAll("/", ".");
	}
	
	/**
	 * Returns the input string with the first letter in lower case.
	 * @param name
	 * @return
	 */

	public static String uncapitalize(String name) {
		if (name == null)
			return null;
		
		if (name.length() == 0)
			return name;
		
		if (name.length() == 1)
			return name.substring(0,1).toLowerCase();
			
		return name.substring(0,1).toLowerCase() + name.substring(1);
	}
	
	public static String capitalize(String name) {
		if (name == null)
			return null;
		
		if (name.length() == 0)
			return name;
		
		if (name.length() == 1)
			return name.substring(0,1).toUpperCase();
	
		return name.substring(0, 1).toUpperCase() + name.substring(1);		
	}
	
	public static String getPackageTemplateValue(String packageName)
	{
		String defPackComment = "// This class is in the default package.";
		if(packageName == null) return defPackComment;
		if(packageName.length() == 0) return defPackComment;
		return "package " + packageName + ";";
	}
	
	public static String getSafePackagePrefix(String packageName)
	{
		String defPack = "";
		if(packageName == null) return defPack;
		if(packageName.length() == 0) return defPack;
		return packageName + ".";
	}
	
	public static String getSafePackageDir(String packageName)
	{
		String defPack = "";
		if(packageName == null) return defPack;
		if(packageName.length() == 0) return defPack;
		return packageName + "/";
	}

}
