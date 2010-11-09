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

package org.mobicents.eclipslee.servicecreation.util;

import java.util.HashMap;

import org.mobicents.eclipslee.util.slee.xml.components.SbbEnvEntryXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;


/**
 * @author cath
 */
public class EnvEntryUtil {

	public static HashMap[] getEnvEntries(SbbXML sbb) {	
		SbbEnvEntryXML entries[] = sbb.getEnvEntries();
		HashMap map[] = new HashMap[entries.length];
		
		for (int i = 0; i < entries.length; i++) {
			map[i] = new HashMap();
			map[i].put("Name", entries[i].getName() == null ? "" : entries[i].getName());
			map[i].put("Value", entries[i].getValue() == null ? "" : entries[i].getValue());
			map[i].put("Type", entries[i].getType() == null ? "" : entries[i].getType());
			map[i].put("Description", entries[i].getDescription() == null ? "" : entries[i].getDescription());
		}
		
		return map;		
	}
}
