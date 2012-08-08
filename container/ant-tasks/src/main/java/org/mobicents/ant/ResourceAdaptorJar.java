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

// <resourceadaptorjar destfile="pathname.jar" resourceadaptorjarxml="foo/bar/resource-adaptor-jar.xml" autoinclude="yes" classpath="...">
//    <classpath ...>
//    <fileset ... >
// </resourceadaptortypejar>
//
// resourceadaptorjarxml is assumed to have a default value of resource-adaptor-jar.xml, so this property may be ommited if
// the metatinfbase property is used or inherited from an enclosing deployableunit task.

public class ResourceAdaptorJar extends SleeJar {
    public ResourceAdaptorJar() {
        super("resource-adaptor-jar", "create");
    }

    protected final void includeTypeSpecificClasses() throws BuildException {

        try {
	    Document rajarDoc = XMLParser.getDocument(getJarXml().toURL(), entityResolver);
	    Element raRoot = rajarDoc.getDocumentElement(); // <resource-adaptor>

	    List raNodes = XMLParser.getElementsByTagName(raRoot, "resource-adaptor");
	    Iterator iter = raNodes.iterator();
	    while (iter.hasNext()) {
		Element raNode = (Element) iter.next();

		// SLEE spec does not define the resource-adaptor-classes element.
		List raActivityTypeNodes = XMLParser.getElementsByTagName(raNode, "resource-adaptor-classes");
		Iterator i = raActivityTypeNodes.iterator();
		while (i.hasNext()) {
		    Element node = (Element) i.next();
		    //		String className = XMLParser.getTextElement(node, "activity-type-name");
		    //		includeResourceAdaptorClass(className);
		}
	    }
	} catch (IOException e) {
            throw new BuildException(e);
        }
    }

    //   resourceadaptortypejarxml="..."
    public void setResourceAdaptorjarxml(String resourceadaptortypejarxml) {
        setJarXml(resourceadaptortypejarxml);
    }

    protected final String getComponentType() { return "resourceadaptorjar"; }
    protected final String getJarXmlName() { return "resourceadaptorjarxml"; }

    private static final FileUtils fileUtils = FileUtils.newFileUtils();
    private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
}
