
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
 The ResourceAdaptorJar task automates the construction of a resource adaptor
 jar file that may be included in a deployable unit jar.
 <pre>
 &lt;resourceadaptorjar destfile="pathname.jar" resourceadaptorjarxml="foo/bar/resource-adaptor-jar.xml" autoinclude="yes" classpath="..."&gt;
    &lt;classpath ...&gt;
    &lt;fileset ... &gt;
 &lt;/resourceadaptortypejar&gt;
 </pre>
 resourceadaptorjarxml is assumed to have a default value of resource-adaptor-jar.xml, so this property may be omitted if
 the metatinfbase property is used or inherited from an enclosing deployableunit task.
*/

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
		    // Element node = (Element) i.next();
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

    private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
}
