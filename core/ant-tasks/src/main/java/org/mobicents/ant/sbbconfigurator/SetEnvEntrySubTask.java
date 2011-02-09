/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.ant.sbbconfigurator;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.BuildException;
import org.mobicents.util.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import org.mobicents.ant.SleeDTDResolver;

/**
 * Ant subtask for SbbConfigurator that configures sbb descriptor's env entry elements.
 * @author Eduardo Martins / 2006 PT Inovação (www.ptinovacao.pt)
 */
public class SetEnvEntrySubTask implements SubTask {
	
	// Obtain a suitable logger.
    private static Logger logger = Logger.getLogger(org.mobicents.ant.sbbconfigurator.SetEnvEntrySubTask.class.getName());
    
    // Does all the work, that is, sets a new type and/or new value on a existent env-entry.
    public void run(File sbbDescriptor) {
    	
    	if (envEntryType == null && envEntryValue == null) {
    		// Log the warning
    		logger.log(Level.WARNING, "Skiping sbb description configuration of env-entry with name "+envEntryName+". Reason: new Type and new Value not defined.");
    	}
    	else {	
    		try {    			
    			try {
    				Document sbbjarDoc = XMLParser.getDocument(sbbDescriptor.toURL(), entityResolver);
    				Element sbbRoot = sbbjarDoc.getDocumentElement(); // <sbb-jar>
    				List sbbNodes = XMLParser.getElementsByTagName(sbbRoot, "sbb"); // <sbb>
    				boolean updateXml = false;
    				for (Iterator i = sbbNodes.iterator(); i.hasNext(); ) {    					
    					// get sbb node
    					Element sbbNode = (Element)i.next();    					
    					// check sbb node 
    					if (sbbName != null) {
    						Element sbbNameNode = XMLParser.getUniqueElement(sbbNode, "sbb-name"); // <sbb-name>
    						if (!sbbNameNode.getFirstChild().getNodeValue().equals(sbbName))
    							continue;
    					};    				
    					if (sbbVendor != null) {
    						Element sbbVendorNode = XMLParser.getUniqueElement(sbbNode, "sbb-vendor"); // <sbb-vendor>
    						if (!sbbVendorNode.getFirstChild().getNodeValue().equals(sbbVendor))
    							continue;
    					};    				
    					if (sbbVersion != null) {
    						Element sbbVersionNode = XMLParser.getUniqueElement(sbbNode, "sbb-version"); // <sbb-version>
    						if (!sbbVersionNode.getFirstChild().getNodeValue().equals(sbbVersion))
    							continue;
    					};    					
    					List envEntries = XMLParser.getElementsByTagName(sbbNode, "env-entry"); // <env-entry>
    					for (Iterator j = envEntries.iterator(); j.hasNext(); ) {    						
    						// get env entry node
    						Element envEntryNode = (Element)j.next();    						
    						// check env entry node
    						Element envEntryNameNode = XMLParser.getUniqueElement(envEntryNode, "env-entry-name"); // <env-entry-name>    					
    						if (!envEntryNameNode.getFirstChild().getNodeValue().equals(envEntryName))
    							continue;    						
    						// set env entry type?
    						if (envEntryType != null) {
    							Element envEntryTypeNode = XMLParser.getUniqueElement(envEntryNode, "env-entry-type"); // <env-entry-type>    							
    							if (!envEntryTypeNode.hasChildNodes()) {
    								envEntryTypeNode.appendChild(sbbjarDoc.createTextNode(envEntryType));
    							}
    							else {
    								envEntryTypeNode.getFirstChild().setNodeValue(envEntryType);
    							}
    							updateXml = true;
    						}    						
    						// set env entry value?
    						if (envEntryValue != null) {
    							Element envEntryValueNode = XMLParser.getUniqueElement(envEntryNode, "env-entry-value"); // <env-entry-value>
    							if (!envEntryValueNode.hasChildNodes()) {
    								envEntryValueNode.appendChild(sbbjarDoc.createTextNode(envEntryValue));
    							}
    							else {
    								envEntryValueNode.getFirstChild().setNodeValue(envEntryValue);
    							}
    							updateXml = true;
    						}    						
    					}
    				}    				    				
    				// write updated descriptor file?
    				if (updateXml) {
    					Source source = new DOMSource(sbbjarDoc);
    					Result result = new StreamResult(sbbDescriptor);
    					Transformer xformer = TransformerFactory.newInstance().newTransformer();
    					DocumentType docType = sbbjarDoc.getDoctype();    					
    					if (docType != null) {    						        					
    						String publicId = docType.getPublicId();    						
    						if(publicId != null) {
    							xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, publicId);    							
    						}
    						String systemId = docType.getSystemId();    						
    						if(systemId != null) {
    							xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemId);    							
    						}
    						//FIXME should we check more properties? 
    					}    	
    					else {
        					logger.warning("sbb descriptor with null doctype");
    					}
    					xformer.transform(source, result);
    				}
    			} catch (IOException e) {
    				throw new BuildException(e);
    			}
    		}
    		catch (Exception ex) {
    			// Log the error
    			logger.log(Level.WARNING, "Bad result: " + ex.getCause().toString());
    		}
    	}
    }
	    
	// The setter for the "name" attribute	
	public void setName(String name) {
		this.envEntryName = name;
	}
	
	// The setter for the "newType" attribute	
	public void setNewType(String newType) {
		this.envEntryType = newType;
	}
	
	// The setter for the "newValue" attribute	
	public void setNewValue(String newValue) {
		this.envEntryValue = newValue;
	}
	
	// The setter for the "sbbName" attribute	
	public void setSbbName(String sbbName) {
		this.sbbName = sbbName;
	}
	
	// The setter for the "sbbVendor" attribute	
	public void setSbbVendor(String sbbVendor) {
		this.sbbVendor = sbbVendor;
	}
	
	// The setter for the "sbbVersion" attribute	
	public void setSbbVersion(String sbbVersion) {
		this.sbbVersion = sbbVersion;
	}
	
	private String envEntryName = null;	
	private String envEntryType = null;
	private String envEntryValue = null;
	private String sbbName = null;	
	private String sbbVendor = null;
	private String sbbVersion = null;
	
	private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
       
}