
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

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * Represents the object-index-data tag in the osp-profile-spec-data-jar.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectIndexDataXML extends DTDXML {

	/**
	 * @param document
	 * @param root
	 * @param dtd
	 */
	public ObjectIndexDataXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public boolean isSlee(){
		String result = getChildText(getRoot(), "is-slee");
		if(result.equals("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void setSlee(boolean compose){
		if(compose)
			setChildText(getRoot(), "is-slee", "true");
		else
			setChildText(getRoot(), "is-slee", "false");
	}
	
	public boolean isSmf(){
		String result = getChildText(getRoot(), "is-smf");
		if(result.equals("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void setSmf(boolean compose){
		if(compose)
			setChildText(getRoot(), "is-smf", "true");
		else
			setChildText(getRoot(), "is-smf", "false");
	}
	

}
