
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
 * Represents the generation data tag of the osp-profile-spec-data-jar.xml
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class GenerationDataXML extends DTDXML {

	/**
	 * @param document the XML document 
	 * @param root the local root
	 * @param dtd
	 */
	public GenerationDataXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}

	public String getGenerationTarget() {
		return getChildText(getRoot(), "generation-target");
	}

	public void setTargetName(String target) {
		setChildText(getRoot(),"generation-target", target );
	}

	public String getObjectPath() {
		return getChildText(getRoot(), "object-path");
	}

	public void setObjectpath(String path) {
		setChildText(getRoot(),"object-path", path);
	}
	
	public String getGeneratorsList() {
		return getChildText(getRoot(), "generators-list");
	}

	public void setGeneratorListh(String list) {
		setChildText(getRoot(),"generators-list", list);
	}
	
	public String getSubFolder() {
		return getChildText(getRoot(), "sub-folder");
	}

	public void setSubFolder(String folder) {
		setChildText(getRoot(),"sub-folder", folder);
	}

	

}
