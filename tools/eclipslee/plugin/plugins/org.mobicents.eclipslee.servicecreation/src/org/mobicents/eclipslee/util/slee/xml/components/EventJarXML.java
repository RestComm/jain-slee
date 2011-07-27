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

package org.mobicents.eclipslee.util.slee.xml.components;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class for reading and writing of event-jar.xml files.
 * 
 * @author cath
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EventJarXML extends DTDXML {

  public static final String QUALIFIED_NAME = "event-jar";
  public static final String PUBLIC_ID_1_0 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN";
  public static final String SYSTEM_ID_1_0 = "http://java.sun.com/dtd/slee-event-jar_1_0.dtd";

  public static final String PUBLIC_ID_1_1 = "-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.1//EN";
  public static final String SYSTEM_ID_1_1 = "http://java.sun.com/dtd/slee-event-jar_1_1.dtd";

  public static final String PUBLIC_ID = PUBLIC_ID_1_1;
  public static final String SYSTEM_ID = SYSTEM_ID_1_1;

  public EventJarXML(EntityResolver resolver, InputSource dummyXML) throws ParserConfigurationException {
    super(QUALIFIED_NAME, PUBLIC_ID, SYSTEM_ID, resolver);
    readDTDVia(resolver, dummyXML);
  }

  /**
   * Parse the provided InputStream as though it contains JAIN SLEE Event XML Data.
   * @param stream
   */

  public EventJarXML(InputStream stream, EntityResolver resolver, InputSource dummyXML) throws SAXException, IOException, ParserConfigurationException {
    super(stream, resolver);

    // Verify that this is really an event-jar XML file.
    if (!getRoot().getNodeName().equals(QUALIFIED_NAME))
      throw new SAXException("This was not an event jar XML file.");		

    readDTDVia(resolver, dummyXML);
  }

  public void setDescription(String description) throws Exception {

    if (description == null) description = "";

    Element descriptionNodes[] = getNodes("event-jar/description");

    // Remove existing description nodes.
    for (int i = 0; i < descriptionNodes.length; i++)
      descriptionNodes[i].getParentNode().removeChild(descriptionNodes[i]);

    Element desc = addElement(getRoot(), "description");
    desc.appendChild(document.createTextNode(description));
  }

  public EventXML[] getEvents() {
    Element nodes[] = getNodes("event-jar/event-definition");
    EventXML events[] = new EventXML[nodes.length];

    for (int i = 0; i < nodes.length; i++)
      events[i] = new EventXML(document, nodes[i], dtd);

    return events;
  }

  /**
   * Gets the Element at the root of the first event with the specified class name.
   * 
   * @param className
   * @return
   * @throws ComponentNotFoundException
   * @throws NullPointerException
   */

  public EventXML getEvent(String className) throws ComponentNotFoundException, NullPointerException {

    if (className == null) throw new NullPointerException("ClassName cannot be null.");

    EventXML events[] = getEvents();
    for (int i = 0; i < events.length; i++) {
      if (className.equals(events[i].getEventClassName()))
        return events[i];
    }

    throw new ComponentNotFoundException("No event found with specified class name.");		
  }

  /**
   * Gets the Element at the root of the specified event.
   * 
   * @param name the name component of the event identity
   * @param vendor the vendor component of the event identity
   * @param version the version component of the event identity
   * @return the Element at the root of the specified event
   * @throws ComponentNotFoundException if the specified event could not be found
   */

  public EventXML getEvent(String name, String vendor, String version) throws ComponentNotFoundException, NullPointerException {

    if (name == null) throw new NullPointerException("Name cannot be null.");
    if (vendor == null) throw new NullPointerException("Vendor cannot be null.");
    if (version == null) throw new NullPointerException("Version cannot be null.");

    Element events[] = getNodes("event-jar/event-definition");

    for (int i = 0; i < events.length; i++) {
      Element event = events[i];

      String eventName = getChildText(event, "event-type-name");
      String eventVendor = getChildText(event, "event-type-vendor");
      String eventVersion = getChildText(event, "event-type-version");

      if (name.equals(eventName) && vendor.equals(eventVendor) && version.equals(eventVersion))
        return new EventXML(document, event, dtd);

    }

    throw new ComponentNotFoundException("Specified event not found.");		
  }

  public EventXML addEvent() throws DuplicateComponentException, Exception {
    return addEvent("", "", "", "", "");		
  }

  /**
   * Adds an event with the specified name, vendor, version, description and class name elements.
   * 
   * @param name
   * @param vendor
   * @param version
   * @param description
   * @param className
   * @return the root Element of the new event data
   * @throws DuplicateComponentException if the event already exists
   * @throws Exception
   */

  public EventXML addEvent(String name, String vendor, String version, String description, String className) throws DuplicateComponentException, Exception {

    if (name == null) throw new NullPointerException("Name cannot be null.");
    if (vendor == null) throw new NullPointerException("Vendor cannot be null.");
    if (version == null) throw new NullPointerException("Version cannot be null.");

    if (description == null) description = "";

    boolean found = true;
    try {
      getEvent(name, vendor, version);
    } catch (ComponentNotFoundException e) {
      found = false;
    }

    if (found)
      throw new DuplicateComponentException("There is already an event of that identity defined in this EventXML.");

    Element elements[] = this.getNodes("event-jar");
    if (elements.length != 1)
      throw new Exception("Number of 'event-jar' nodes in this data set is incorrect: " + elements.length);

    Element newElement = addElement(elements[0], "event-definition");

    addElement(newElement, "description").appendChild(document.createTextNode(description));
    addElement(newElement, "event-type-name").appendChild(document.createTextNode(name));
    addElement(newElement, "event-type-vendor").appendChild(document.createTextNode(vendor));
    addElement(newElement, "event-type-version").appendChild(document.createTextNode(version));
    addElement(newElement, "event-class-name").appendChild(document.createTextNode(className));

    return new EventXML(document, newElement, dtd);	
  }

  /**
   * Removes the specified event from the Event XML data.
   * @param name
   * @param vendor
   * @param version
   * @throws ComponentNotFoundException if the specified event could not be found
   */

  public void removeEvent(String name, String vendor, String version) throws ComponentNotFoundException {
    EventXML event = getEvent(name, vendor, version);
    removeEvent(event);
  }

  public void removeEvent(EventXML event) {
    if (event == null) throw new NullPointerException("event must be non-null");
    event.getRoot().getParentNode().removeChild(event.getRoot());		
  }

  public LibraryRefXML[] getLibraryRefs() {
    Element nodes[] = getNodes("event-jar/library-ref");
    LibraryRefXML xml[] = new LibraryRefXML[nodes.length];
    for (int i = 0; i < nodes.length; i++) {
      xml[i] = new LibraryRefXML(document, nodes[i], dtd); 
    }

    return xml;
  }

  public LibraryRefXML addLibraryRef(LibraryXML library) {
    Element ele = addElement(getRoot(), "library-ref");
    LibraryRefXML xml = new LibraryRefXML(document, ele, dtd);

    xml.setName(library.getName());
    xml.setVendor(library.getVendor());
    xml.setVersion(library.getVersion());

    return xml;
  }

  public void removeLibraryRef(LibraryRefXML libraryRef) {
    libraryRef.getRoot().getParentNode().removeChild(libraryRef.getRoot());
  }

  public String toString() {
    String output = "";
    EventXML events[] = getEvents();
    for (int i = 0; i < events.length; i++) {
      if (i > 0)
        output += ", ";
      output += "[" + events[i].toString() + "]";
    }
    return output;
  }

}
