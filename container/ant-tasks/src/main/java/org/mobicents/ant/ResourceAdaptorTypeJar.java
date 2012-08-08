/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.ant;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.mobicents.util.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
// <resourceadaptortypejar destfile="pathname.jar" resourceadaptortypejarxml="foo/bar/resource-adaptor-type-jar.xml" autoinclude="yes" classpath="...">
//    <classpath ...>
//    <fileset ... >
// </resourceadaptortypejar>
//
// resourceadaptortypejarxml is assumed to have a default value of resource-adaptor-type-jar.xml, so this property may be ommited if
// the metatinfbase property is used or inherited from an enclosing deployableunit task.

public class ResourceAdaptorTypeJar extends SleeJar implements Component{
    public ResourceAdaptorTypeJar() {
        super("resource-adaptor-type-jar", "create");
    }

    protected final void includeTypeSpecificClasses() throws BuildException {

        try {
	    Document rajarDoc = XMLParser.getDocument(getJarXml().toURL(), entityResolver);
	    Element raRoot = rajarDoc.getDocumentElement(); // <resource-adaptor-type>
	    
	    List raTypeNodes = XMLParser.getElementsByTagName(raRoot, "resource-adaptor-type");
	    Iterator iter = raTypeNodes.iterator();
	    while (iter.hasNext()) {
		Element raTypeNode = (Element) iter.next();
		Element raClassesNode = XMLParser.getUniqueElement(raTypeNode, "resource-adaptor-type-classes");

		List raActivityTypeNodes = XMLParser.getElementsByTagName(raClassesNode, "activity-type");
		Iterator i = raActivityTypeNodes.iterator();
		while (i.hasNext()) {
		    Element node = (Element) i.next();
		    String className = XMLParser.getTextElement(node, "activity-type-name");
		    includeClass(className);
		}
		
		// Optional activity-context-interface-factory-interface element
		Element raACIFINode = XMLParser.getOptionalElement(raClassesNode, "activity-context-interface-factory-interface");
		if (raACIFINode != null) {
		    String className = XMLParser.getTextElement(raACIFINode, "activity-context-interface-factory-interface-name");
		    includeClass(className);
		}
		
		// Optional resource-adaptor-interface element.
		Element raIfaceNode = XMLParser.getOptionalElement(raClassesNode, "resource-adaptor-interface");
		if (raIfaceNode != null) {
		    String className = XMLParser.getTextElement(raIfaceNode, "resource-adaptor-interface-name");
		    includeClass(className);
		}
	    }
	} catch (IOException e) {
            throw new BuildException(e);
        }
    }

    //   resourceadaptortypejarxml="..."
    public void setResourceAdaptorTypejarxml(String resourceadaptortypejarxml) {
        setJarXml(resourceadaptortypejarxml);
    }

    protected final String getComponentType() { return "resourceadaptortypejar"; }
    protected final String getJarXmlName() { return "resourceadaptortypejarxml"; }

    private static final FileUtils fileUtils = FileUtils.newFileUtils();
    private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
}
