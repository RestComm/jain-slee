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
