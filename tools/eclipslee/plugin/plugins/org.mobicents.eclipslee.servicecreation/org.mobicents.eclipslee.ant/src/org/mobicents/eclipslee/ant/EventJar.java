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
import org.apache.tools.ant.util.FileUtils;
import org.mobicents.eclipslee.util.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 The EventJar task automates the construction of an event jar file that may
 be included in a deployable unit jar.
 <pre>
 &lt;event destfile="pathname.jar" eventjarxml="foo/bar/event-jar.xml" autoinclude="yes" classpath="..."&gt;
    &lt;classpath ...&gt;
    &lt;fileset ... &gt;
 &lt;/event&gt;
 </pre>
 eventjarxml is assumed to have a default value of event-jar.xml, so this property may be ommited if
 the metatinfbase property is used or inherited from an enclosing deployableunit task.
*/

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
