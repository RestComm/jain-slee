package org.mobicents.eclipslee.xml;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NullEntityResolver implements EntityResolver {
  public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
    return null;
  }
}
