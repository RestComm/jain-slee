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

// <profilespec destfile="pathname.jar" profilespecjarxml="foo/bar/profile-spec-jar.xml" autoinclude="yes" classpath="...">
//    <classpath ...>
//    <fileset ... >
// </profilespec>
//
// profilespecjarxml is assumed to have a default value of profile-spec-jar.xml, so this property may be ommited if
// the metatinfbase property is used or inherited from an enclosing deployableunit task.

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
