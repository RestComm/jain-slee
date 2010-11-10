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
 The ProfileSpecJar task automates the construction of a profile specification
 jar file that may be included in a deployable unit jar.
 <pre>
 &lt;profilespec destfile="pathname.jar" profilespecjarxml="foo/bar/profile-spec-jar.xml" autoinclude="yes" classpath="..."&gt;
    &lt;classpath ...&gt;
    &lt;fileset ... &gt;
 &lt;/profilespec&gt;
 </pre>
 profilespecjarxml is assumed to have a default value of profile-spec-jar.xml, so this property may be ommited if
 the metatinfbase property is used or inherited from an enclosing deployableunit task.
 */

public class ProfileSpecJar extends SleeJar {
    public ProfileSpecJar() {
        super("profile-spec-jar", "create");
    }

    protected final void includeTypeSpecificClasses() throws BuildException {

        // Parse the profile-spec-jar.xml and look for profile classes to include.
        String[] profileClasses = { "profile-cmp-interface-name",
                                "profile-management-interface-name",
                                "profile-management-abstract-class-name", };

        try {
            Document profileSpecJarDoc = XMLParser.getDocument(getJarXml().toURL(), entityResolver);
            Element profileSpecRoot = profileSpecJarDoc.getDocumentElement(); // <profile-spec-jar>
            List profileSpecNodes = XMLParser.getElementsByTagName(profileSpecRoot, "profile-spec"); // <profile-spec>
            for (Iterator i = profileSpecNodes.iterator(); i.hasNext(); ) {
                Element profileSpecNode = (Element)i.next();
                Element profileSpecClassesNode = XMLParser.getUniqueElement(profileSpecNode, "profile-classes");
                for (int j = 0; j < profileClasses.length; ++j) {
                    String className = XMLParser.getOptionalTextElement(profileSpecClassesNode, profileClasses[j] );
                    if(className != null) includeClass(className);
                }
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    protected final String getComponentType() { return "profilespecjar"; }
    protected final String getJarXmlName() { return "profilespecjarxml"; }

    //   profilespecjarxml="..."
    public void setProfilespecjarxml(String profilespecjarxml) {
        setJarXml(profilespecjarxml);
    }

    private static final FileUtils fileUtils = FileUtils.newFileUtils();
    private static final SleeDTDResolver entityResolver = new SleeDTDResolver();
}
