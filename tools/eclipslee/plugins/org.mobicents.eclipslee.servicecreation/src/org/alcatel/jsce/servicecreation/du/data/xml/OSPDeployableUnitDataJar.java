
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.alcatel.jsce.util.log.SCELogger;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.xml.SLEEEntityResolver;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *  Description:
 * <p>
 * Represents the data needed to re-build an OSP deployable unit.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class OSPDeployableUnitDataJar extends DTDXML {
	public static final String QUALIFIED_NAME = "osp-deployable-unit-data";
	public static final String PUBLIC_ID = "-//Alcatel Bell, n.v.//DTD OSP JAIN SLEE Deploybale Unit Data 1.0//EN";
	public static final String SYSTEM_ID = "http://www.etb.bel.alcatel.be/dtd/osp-deployable-unit-data_1_0.dtd";

	/**
	 * Parse the provided InputStream as though it contains JAIN SLEE OSP deployable unit data XML Data.
	 * 
	 * @param stream
	 */

	public OSPDeployableUnitDataJar(InputStream stream, EntityResolver resolver, InputSource dummyXML)
			throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);

		// Verify that this is really an OSP deployable unit datar XML file.
		if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
			throw new SAXException("This was not an osp deployable unit data XML file.");

		readDTDVia(resolver, dummyXML);
	}

	/** @OSP modification by Sabri Skhiri */
	public OSPDeployableUnitDataJar(InputStream stream, SLEEEntityResolver resolver, InputSource dummyXML,
			String qualified_name) throws SAXException, IOException, ParserConfigurationException {
		super(stream, resolver);

		// Verify that this is really an OSP deployable unit data XML file.
		if (!getRoot().getNodeName().equals(qualified_name))
			throw new SAXException("This was not an osp deployable unit data XML file.");

		readDTDVia(resolver, dummyXML);
	}
	
	/**
	 * Create a new OSPDeployableUnitDataJar with the specified EntityResolver.
	 * 
	 * @param resolver resolves the dtd location
	 * @throws ParserConfigurationException
	 */

	public OSPDeployableUnitDataJar(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
		super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
		readDTDVia(resolver, dummyXML);
	}
	
	public ConfigurationXML getConfigurationXML(){
		Element[] nodes = getNodes("osp-deployable-unit-data/configuration");
		if(nodes.length>0){
			return new ConfigurationXML(document, nodes[0], dtd);
		}else{
			String msg = " The node configuration was not found in " + getRoot();
			SCELogger.logError(new IllegalStateException(msg));
			return null;
		}
	}
	
	
	public ConfigurationXML setConfiguration(String label, String duFile){
		Element nodes[] = getNodes("osp-deployable-unit-data/configuration");
		if(nodes.length>0){
			//There is only one such a node
			ConfigurationXML configXML =  new ConfigurationXML(document, nodes[0], dtd);
			configXML.setProductionLabel(label);
			configXML.setDuFileName(duFile);
			return configXML;
		}else{
			Element node = addElement(getRoot(), "configuration");
			ConfigurationXML configXML =  new ConfigurationXML(document, node, dtd);
			configXML.setProductionLabel(label);
			configXML.setDuFileName(duFile);
			return configXML;
		}
	}
	
	public ServiceXML getServiceXML(String xmlFileName) {
		Element services[] = getNodes("osp-deployable-unit-data/services/service");
		for (int i = 0; i < services.length; i++) {
			Element service_i = services[i];
			ServiceXML serviceXML_i = new ServiceXML(document, service_i, dtd);
			if(serviceXML_i.getServiceFile().equals(xmlFileName)){
				return serviceXML_i;
			}
		}
		return null;
	}
	
	public ServiceXML[] getServiceXML() {
		Element services[] = getNodes("osp-deployable-unit-data/services/service");
		List servicesData = new ArrayList();
		for (int i = 0; i < services.length; i++) {
			Element partition_i = services[i];
			servicesData.add(new ServiceXML(document, partition_i, dtd));
		}
		return (ServiceXML[]) servicesData.toArray(new ServiceXML[servicesData.size()]);
	}
	
	public ServiceXML addServiceXML() {		
		Element services[] = getNodes("osp-deployable-unit-data/services");
		if (services.length > 0) {
			//There is only one such a node
			Element serviceNode = addElement(services[0], "service");
			ServiceXML serviceXML =  new ServiceXML(document, serviceNode, dtd);
			return serviceXML;
		} else {
			/*We must create the services node*/
			Element servicesNode = addElement(getRoot(), "services");
			Element serviceNode = addElement(servicesNode, "service");
			return new ServiceXML(document, serviceNode, dtd);
		}
	}
	
	
	public void addLib(String lib) {		
		Element partitions[] = getNodes("osp-deployable-unit-data/libs");
		if (partitions.length > 0) {
			//There is only one such a node
			addChildText(partitions[0],"relative-path", lib);
		} else {
			/*We must create the partitions node*/
			Element partitionsNode = addElement(getRoot(), "libs");
			addChildText(partitionsNode,"relative-path", lib);
		}
	}
	
	public String[] getLibs() {
		Element methods[] = getNodes("osp-deployable-unit-data/libs/relative-path");
		List methodsData = new ArrayList();
		for (int i = 0; i < methods.length; i++) {
			Element method_i = methods[i];
			methodsData.add(getTextData(method_i));
		}
		return (String[]) methodsData.toArray(new String[methodsData.size()]);
	}
	
	/**
	 * @return the corresponding
	 * @link OSPProfileSpecIdXML
	 * @throws ComponentNotFoundException
	 */
	public OSPDeployableUnitSbbChildren getSbbChildren() throws ComponentNotFoundException {
		Element[] elements = getNodes("osp-deployable-unit-data/children");
		if (elements.length > 0) {
			OSPDeployableUnitSbbChildren specs = new OSPDeployableUnitSbbChildren(document, elements[0], dtd);
			return specs;
		} else {
			throw new ComponentNotFoundException("The profile spec ID was not found !");
		}
	}
	
	/**
	 * @return the sbb children node. If it does not exist, it will automatically create 
	 * a new one.
	 */
	public OSPDeployableUnitSbbChildren addSbbChildren() {		
		Element children[] = getNodes("osp-deployable-unit-data/children");
		if (children.length > 0) {
			//There is only one such a node
		return new OSPDeployableUnitSbbChildren(document, children[0], dtd);
		} else {
			/*We must create the partitions node*/
			Element childrenNode = addElement(getRoot(), "children");
			return new OSPDeployableUnitSbbChildren(document, childrenNode, dtd);
		}
	}

	

}
