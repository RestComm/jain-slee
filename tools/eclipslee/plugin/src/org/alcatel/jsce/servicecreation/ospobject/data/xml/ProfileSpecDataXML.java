
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.servicecreation.ospobject.data.xml;

import java.util.Random;

import org.alcatel.jsce.util.log.SCELogger;
import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * Represent the profile-spec-data> tag in the osp-profile-spec-data-jar.xml
 * 
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ProfileSpecDataXML extends DTDXML {

	/**
	 * @param document the XML document 
	 * @param root the local root
	 * @param dtd
	 */
	public ProfileSpecDataXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}

	public String getPackageName() {
		return getChildText(getRoot(), "profile-cmp-interface-package");
	}

	public void setPackageName(String packageName) {
		setChildText(getRoot(),"profile-cmp-interface-package", packageName );
	}

	public String getProfileCMPInterface() {
		return getChildText(getRoot(), "profile-cmp-interface-name");
	}

	public void setProfileCMPInterface(String profileCMPInterface) {
		setChildText(getRoot(),"profile-cmp-interface-name", profileCMPInterface);
	}
	
	public int getClassNbr() {
		String id =  getChildText(getRoot(), "class-nbr");
		try {
			if(id!=null){
				Integer classNbr = new Integer(id);
				return Math.abs(classNbr.intValue());
			}else{
				SCELogger.logInfo("No class number define in the OSP profile spec data jar XML -> create a new one");
				Random random = new Random(System.currentTimeMillis());
				int newClassNbr = random.nextInt();
				setClassNbr(newClassNbr);
				SCELogger.logInfo("No class number define in the OSP profile spec data jar XML -> "+ newClassNbr+" created");
				return newClassNbr;
			}
			
		} catch (Exception e) {
			SCELogger.logError("The class number is not a long", e);
			return 0;
		}
	}

	public void setClassNbr(int classNbr) {
		setChildText(getRoot(),"class-nbr", (new Integer(classNbr)).toString());
	}

}
