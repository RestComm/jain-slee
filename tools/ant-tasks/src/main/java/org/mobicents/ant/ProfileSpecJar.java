package org.mobicents.ant;
 
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.List;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.mobicents.util.XMLParser;

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
