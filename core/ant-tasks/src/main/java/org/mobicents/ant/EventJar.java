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

// <event destfile="pathname.jar" eventjarxml="foo/bar/event-jar.xml" autoinclude="yes" classpath="...">
//    <classpath ...>
//    <fileset ... >
// </event>
//
// eventjarxml is assumed to have a default value of event-jar.xml, so this property may be ommited if
// the metatinfbase property is used or inherited from an enclosing deployableunit task.

public class EventJar extends SleeJar {
    public EventJar() {
        super("event-jar", "create");
    }

    protected final void includeTypeSpecificClasses() throws BuildException {
	try {
	    Document eventjarDoc = XMLParser.getDocument(getJarXml().toURL(), entityResolver);
	    Element eventRoot = eventjarDoc.getDocumentElement(); // <event-jar>
	    List eventNodes = XMLParser.getElementsByTagName(eventRoot, "event-definition");

	    Iterator i = eventNodes.iterator();
	    while (i.hasNext()) {
		Element eventNode = (Element) i.next();
		
		String className = XMLParser.getTextElement(eventNode, "event-class-name");
		includeClass(className);
	    }
	} catch (IOException e) {
            throw new BuildException(e);
        }
    }

    //   eventjarxml="..."
    public void setEventjarxml(String eventjarxml) {
        setJarXml(eventjarxml);
    }

    protected final String getComponentType() { return "eventjar"; }
    protected final String getJarXmlName() { return "eventjarxml"; }

    private static final FileUtils fileUtils = FileUtils.newFileUtils();
    private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
}
