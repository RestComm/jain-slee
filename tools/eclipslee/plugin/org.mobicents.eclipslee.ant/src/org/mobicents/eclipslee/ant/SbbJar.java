
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
 The SbbJar task automates the construction of an SBB jar file
 that may be included in a deployable unit jar.
 <pre>
 &lt;sbb destfile="pathname.jar" sbbjarxml="foo/bar/sbb-jar.xml" autoinclude="yes" classpath="..."&gt;
    &lt;classpath ...&gt;
    &lt;fileset ... &gt;
 &lt;/sbb&gt;
 </pre>
 sbbjarxml is assumed to have a default value of sbb-jar.xml, so this property may be ommited if
 the metatinfbase property is used or inherited from an enclosing deployableunit task.
*/

public class SbbJar extends SleeJar implements Component{
    public SbbJar() {
        super("sbb-jar", "create");
    }

    protected final void includeTypeSpecificClasses() throws BuildException {

        // Parse the sbb-jar.xml and look for SBB classes to include.
        String[] classTypes = { "sbb-abstract-class",
                                "sbb-local-interface",
                                "sbb-activity-context-interface",
	                        "sbb-usage-parameters-interface" };

        try {
            Document sbbjarDoc = XMLParser.getDocument(getJarXml().toURL(), entityResolver);
            Element sbbRoot = sbbjarDoc.getDocumentElement(); // <sbb-jar>
            List sbbNodes = XMLParser.getElementsByTagName(sbbRoot, "sbb"); // <sbb>
            for (Iterator i = sbbNodes.iterator(); i.hasNext(); ) {
                Element sbbNode = (Element)i.next();
                Element sbbClassesNode = XMLParser.getUniqueElement(sbbNode, "sbb-classes");
                for (int j = 0; j < classTypes.length; ++j) {
                    Element classNode = XMLParser.getOptionalElement(sbbClassesNode, classTypes[j]);
                    if (classNode != null) {
                        String className = XMLParser.getTextElement(classNode, classTypes[j] + "-name");
                        includeClass(className);
                    }
                }
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    //   sbbjarxml="..."
    public void setSbbjarxml(String sbbjarxml) {
        setJarXml(sbbjarxml);
    }

    protected final String getComponentType() { return "sbbjar"; }
    protected final String getJarXmlName() { return "sbbjarxml"; }

    private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
}
