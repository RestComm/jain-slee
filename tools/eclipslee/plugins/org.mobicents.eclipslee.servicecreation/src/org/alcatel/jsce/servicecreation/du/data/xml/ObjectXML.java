
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

import java.util.ArrayList;
import java.util.List;

import org.alcatel.jsce.util.log.SCELogger;
import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.DuplicateComponentException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  Description:
 * <p>
 * Represents the <i>object</i> XML tag in osp-deployable-unit.xml file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectXML extends DTDXML {

	/**
	 * @param document the XML document
	 * @param root the root of the SBB
	 * @param dtd the corresponding DTD
	 */
	public ObjectXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}
	
	public PartitionXML[] getPartitionXML() {
		Element partitions[] = getNodes("object/partitions/partition");
		List partitionsData = new ArrayList();
		for (int i = 0; i < partitions.length; i++) {
			Element partition_i = partitions[i];
			partitionsData.add(new PartitionXML(document, partition_i, dtd));
		}
		return (PartitionXML[]) partitionsData.toArray(new PartitionXML[partitionsData.size()]);
	}
	
	public PartitionXML addPartitionXML(String id, String tuples) {		
		Element partitions[] = getNodes("object/partitions");
		if (partitions.length > 0) {
			//There is only one such a node
			Element partitionNode = addElement(partitions[0], "partition");
			PartitionXML partitionXML =  new PartitionXML(document, partitionNode, dtd);
			partitionXML.setTuples(tuples);
			partitionXML.setId(id);
			return partitionXML;
		} else {
			/*We must create the partitions node*/
			Element partitionsNode = addElement(getRoot(), "partitions");
			Element partition = addElement(partitionsNode, "partition");
			PartitionXML partitionXML =  new PartitionXML(document, partition, dtd);
			partitionXML.setTuples(tuples);
			partitionXML.setId(id);
			return partitionXML;
		}
	}
	
	public MethodXML[] getMethodXML() {
		Element methods[] = getNodes("object/methods/method");
		List methodsData = new ArrayList();
		for (int i = 0; i < methods.length; i++) {
			Element method_i = methods[i];
			methodsData.add(new MethodXML(document, method_i, dtd));
		}
		return (MethodXML[]) methodsData.toArray(new MethodXML[methodsData.size()]);
	}
	
	public MethodXML addMethodXML(boolean isInstaller,boolean istProvider, boolean isSubscriber, String name, String signature) {		
		Element methods[] = getNodes("object/methods");
		if (methods.length > 0) {
			//There is only one such a node
			Element methodNode = addElement(methods[0], "method");
			MethodXML methodXML =  new MethodXML(document, methodNode, dtd);
			methodXML.setInstaller(isInstaller);
			methodXML.setProvider(istProvider);
			methodXML.setSubscriber(isSubscriber);
			methodXML.setSignature(signature);
			methodXML.setName(name);
			return methodXML;
		} else {
			/*We must create the partitions node*/
			Element methodsNode = addElement(getRoot(), "methods");
			Element method = addElement(methodsNode, "method");
			MethodXML methodXML =  new MethodXML(document, method, dtd);
			methodXML.setInstaller(isInstaller);
			methodXML.setProvider(istProvider);
			methodXML.setSubscriber(isSubscriber);
			methodXML.setSignature(signature);
			methodXML.setName(name);
			return methodXML;
		}
	}
	
	public ObjectImplXML getObjectImplXML(){
		Element[] nodes = getNodes("object/object-impl");
		if(nodes.length>0){
			ObjectImplXML objectImplXML = new ObjectImplXML(document, nodes[0], dtd);
			return objectImplXML;
		}else{
			SCELogger.logError("No object-impl was found in the XML document ", new IllegalStateException("No object-impl found !"));
			return null;
		}
	}
	
	public ObjectImplXML setObjectImplXML(String sleeImpl, String smfImpl){
		//Get the object-impl node
		Element[] nodes = getNodes("object/object-impl");
		if(nodes.length>0){
			//already exists
			ObjectImplXML objectImplXML = new ObjectImplXML(document, nodes[0], dtd);
			objectImplXML.setSleeImpl(sleeImpl);
			objectImplXML.setSmfImpl(smfImpl);
			return objectImplXML;
		}else{
			//The node have'nt been created yet
			Element node = addElement(getRoot(), "object-impl");
			ObjectImplXML objectImplXML = new ObjectImplXML(document, node, dtd);
			objectImplXML.setSleeImpl(sleeImpl);
			objectImplXML.setSmfImpl(smfImpl);
			return objectImplXML;
		}
	}
	
	/**
	 * @return the corresponding
	 * @link OSPProfileSpecIdXML
	 * @throws ComponentNotFoundException
	 */
	public IdentityXML getObjectcIdXML() throws ComponentNotFoundException {
		Element[] elements = getNodes("object/identity");
		if (elements.length > 0) {
			IdentityXML specs = new IdentityXML(document, elements[0], dtd);
			return specs;
		} else {
			throw new ComponentNotFoundException("The profile spec ID was not found !");
		}
	}

	
	public IdentityXML addObjectcId(String name, String vendor, String version, String description)
			throws DuplicateComponentException, ComponentNotFoundException {

		boolean found = true;
		try {
			getObjectcIdXML();
		} catch (ComponentNotFoundException e) {
			found = false;
		} finally {
			if (found)
				throw new DuplicateComponentException(
						"A profile specification id with the same name, vendor and version combination already exists.");
		}

		Element profile_spec[] = getNodes("object");
		if (profile_spec.length > 0) {
			// already exists
			Element newSpec = addElement(profile_spec[0], "identity");
			addElement(newSpec, "description").appendChild(document.createTextNode(description));
			addElement(newSpec, "name").appendChild(document.createTextNode(name));
			addElement(newSpec, "vendor").appendChild(document.createTextNode(vendor));
			addElement(newSpec, "version").appendChild(document.createTextNode(version));
			return new IdentityXML(document, newSpec, dtd);
		} else {
			// does'nt exists yet.
			Element newSpecId = addElement(root, "object");
			Element newSpec = addElement(newSpecId, "identity");
			addElement(newSpec, "description").appendChild(document.createTextNode(description));
			addElement(newSpec, "name").appendChild(document.createTextNode(name));
			addElement(newSpec, "vendor").appendChild(document.createTextNode(vendor));
			addElement(newSpec, "version").appendChild(document.createTextNode(version));
			return new IdentityXML(document, newSpec, dtd);
		}
	}


}
