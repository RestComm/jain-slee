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
