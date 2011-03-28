
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
package org.alcatel.jsce.servicecreation.du.data.xml;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * Represents the <i>method</i> XML tag osp-deployable-unit.xml file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class MethodXML extends DTDXML {

	/**
	 * @param document the XML document
	 * @param root the root of the SBB
	 * @param dtd the corresponding DTD
	 */
	public MethodXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public boolean isInstaller(){
		String result = getChildText(getRoot(), "is-installer");
		if(result.equalsIgnoreCase("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void setInstaller(boolean rigth){
		if(rigth)
			setChildText(getRoot(), "is-installer", "true");
		else
			setChildText(getRoot(), "is-installer", "false");
	}
	
	public boolean isProvider(){
		String result = getChildText(getRoot(), "is-provider");
		if(result.equalsIgnoreCase("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void setProvider(boolean rigth){
		if(rigth)
			setChildText(getRoot(), "is-provider", "true");
		else
			setChildText(getRoot(), "is-provider", "false");
	}
	
	public boolean isSubscriber(){
		String result = getChildText(getRoot(), "is-subscriber");
		if(result.equalsIgnoreCase("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void setSubscriber(boolean rigth){
		if(rigth)
			setChildText(getRoot(), "is-subscriber", "true");
		else
			setChildText(getRoot(), "is-subscriber", "false");
	}
	
	public String getName() {
		return getChildText(getRoot(), "name");
	}
	
	public void setName(String name) {
		setChildText(getRoot(),"name", name);
	}

	public String getSignature() {
		return getChildText(getRoot(), "signature");
	}
	
	public void setSignature(String sig) {
		setChildText(getRoot(),"signature", sig);
	}
}
