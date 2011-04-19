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

package org.mobicents.slee.tools.maven.plugins.library.pojos;

/**
 * 
 * LibraryRef.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryRef {

  private String name;
  private String vendor;
  private String version;
  
  private String description;

  public LibraryRef(String name, String vendor, String version)
  {
    this.name = name;
    this.vendor = vendor;
    this.version = version;
  }
  
  public LibraryRef(String name, String vendor, String version, String description)
  {
    this.name = name;
    this.vendor = vendor;
    this.version = version;
    
    this.description = description;
  }
  
  public String toXmlEntry()
  {
    return description != null ? "\t\t<description>" + description + "</description>\r\n" : "" + 
        "\t\t<library-name>" + this.name + "</library-name>\r\n\t\t<library-vendor>" + this.vendor + "</library-vendor>\r\n" +
        "\t\t<library-version>" + this.version + "</library-version>\r\n";
  }
  
  public String toXmlEntryWithRef()
  {
    return "\t\t<library-ref>\r\n" + toXmlEntry().replaceAll("\t\t", "\t\t\t") + "\t\t</library-ref>\r\n";
  }
  
  public String getName()
  {
    return name;
  }
  
  public String getVendor()
  {
    return vendor;
  }
  
  public String getVersion()
  {
    return version;
  }
  
  public String getDescription()
  {
    return description;
  }
}
