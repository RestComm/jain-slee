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
