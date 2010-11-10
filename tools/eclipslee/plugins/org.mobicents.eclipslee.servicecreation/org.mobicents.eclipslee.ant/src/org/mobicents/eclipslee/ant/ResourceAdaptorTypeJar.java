/**
 *   Copyright 2005 Open Cloud Ltd.
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

package org.mobicents.eclipslee.ant;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.mobicents.eclipslee.util.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 The ResourceAdaptorTypeJar task automates the construction of a resource adaptor
 type jar file that may be included in a deployable unit jar.
 <pre>
 &lt;resourceadaptortypejar destfile="pathname.jar" resourceadaptortypejarxml="foo/bar/resource-adaptor-type-jar.xml" autoinclude="yes" classpath="..."&gt;
    &lt;classpath ...&gt;
    &lt;fileset ... &gt;
 &lt;/resourceadaptortypejar&gt;
 </pre>
 resourceadaptortypejarxml is assumed to have a default value of resource-adaptor-type-jar.xml, so this property may be ommited if
 the metatinfbase property is used or inherited from an enclosing deployableunit task.
*/

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

    private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
}
