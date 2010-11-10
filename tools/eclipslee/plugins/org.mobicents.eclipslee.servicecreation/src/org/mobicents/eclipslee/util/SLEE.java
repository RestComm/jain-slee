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

import java.util.StringTokenizer;

/**
 * @author cath
 */
public class SLEE {

	// Prevent instantiation
	protected SLEE() { }
	
	public static boolean isValidComponentName(String name) {

		if (name == null)
			return false;
		
		if (name.trim().length() == 0)
			return false;
		
		return true;		
	}
	
	public static boolean isValidComponentVersion(String version) {
		if (version == null) return false;
		if (version.trim().length() == 0) return false;
		
		return true;
	}
	
	public static boolean isValidComponentVendor(String vendor) {
		if (vendor == null) return false;
		if (vendor.trim().length() == 0) return false;
		
		return true;
	}
	
	public static String getName(String profileID) {
		StringTokenizer tok = new StringTokenizer(profileID, ",");
		return tok.nextToken(); // name, version, vendor
	}
	
	public static String getVendor(String profileID) {
		StringTokenizer tok = new StringTokenizer(profileID, ",");
		tok.nextToken();
		tok.nextToken();
		return tok.nextToken(); // name, version, vendor
	}
	
	public static String getVersion(String profileID) {
		StringTokenizer tok = new StringTokenizer(profileID, ",");
		tok.nextToken();
		return tok.nextToken(); // name, version, vendor
	}

}
