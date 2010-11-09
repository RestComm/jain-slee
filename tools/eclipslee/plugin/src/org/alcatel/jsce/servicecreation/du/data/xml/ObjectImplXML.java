
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
 *  Represents the <i>object-impl</i> XML tag osp-deployable-unit.xml file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectImplXML extends DTDXML {


	/**
	 * @param document the XML document
	 * @param root the root of the SBB
	 * @param dtd the corresponding DTD
	 */
	public ObjectImplXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	/**
	 * @return the slee implementation
	 */
	public String getSleeImpl() {
		return getChildText(getRoot(), "slee-impl");
	}
	
	/**
	 * Set the  slee implementation
	 * @param impl
	 */
	public void setSleeImpl(String impl) {
		setChildText(getRoot(),"slee-impl", impl);
	}

	/**
	 * Set the  slee implementation
	 * @param impl
	 */
	public void setSmfImpl(String impl) {
		setChildText(getRoot(),"smf-impl", impl);
	}
	
	/**
	 * @return the slee implementation
	 */
	public String getSmfmpl() {
		return getChildText(getRoot(), "smf-impl");
	}

	

}
