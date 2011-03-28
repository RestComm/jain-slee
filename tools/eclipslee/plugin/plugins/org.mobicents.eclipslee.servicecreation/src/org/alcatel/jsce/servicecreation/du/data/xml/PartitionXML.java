
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
 * Represents the <i>partition</i> XML tags osp-deployable-unit.xml file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class PartitionXML extends DTDXML {


	/**
	 * @param document the XML document of the XML file
	 * @param root the local root (here partitions)
	 * @param dtd the corresponding DTD
	 */
	public PartitionXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	

	/**
	 * @return the id of the partition
	 */
	public String getId() {
		return getChildText(getRoot(), "id");
	}

	/**
	 * Set the  id of the partition
	 * @param idString
	 */
	public void setId(String idString) {
		setChildText(getRoot(),"id", idString);
	}

	/**
	 * @return the number of tuples
	 */
	public String getTuples() {
		return getChildText(getRoot(), "tuples");
	}

	/**
	 * Set the number of tuples in this partition
	 * @param tuples
	 */
	public void setTuples(String tuples) {
		setChildText(getRoot(),"tuples", tuples);
	}


}
