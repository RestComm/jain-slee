package org.mobicents.slee.resource.diameter.base.events.avp.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AvpDictionary
{
  
  public final static AvpDictionary INSTANCE = new AvpDictionary();
  
  private static HashMap<String, AvpRepresentation> avpMap = new HashMap<String, AvpRepresentation>();
  
  private static HashMap<String, String> vendorMap = new HashMap<String, String>();

  private static HashMap<String, String> commandMap = new HashMap<String, String>();

  private static HashMap<String, String> typedefMap = new HashMap<String, String>();

  private AvpDictionary() {
    // Exists only to defeat instantiation.
  }
  
  public void parseDictionart(String filename) throws Exception
  {
    parseDictionary( new FileInputStream( filename ) );  
  }
  
  public void parseDictionary(InputStream is) throws Exception
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating( false );
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse( is );
    
    doc.getDocumentElement().normalize();
    
    System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

    /**************************************************************************
     *  VENDORS 
     */

    /*
     * <!-- ************************* Vendors **************************** -->
     * <vendor vendor-id="None" code="0" name="None"/>
     * <vendor vendor-id="HP"    code="11"    name="Hewlett Packard"/>
     * <vendor vendor-id="Merit" code="61" name="Merit Networks"/>
     * <vendor vendor-id="Sun" code="42" name="Sun Microsystems, Inc."/>
     * <vendor vendor-id="USR" code="429" name="US Robotics Corp."/>
     * <vendor vendor-id="3GPP2" code="5535" name="3GPP2"/>
     * <vendor vendor-id="TGPP"  code="10415" name="3GPP"/>
     * <vendor vendor-id="TGPPCX" code="16777216" name="3GPP CX/DX"/>
     * <vendor vendor-id="Ericsson"  code="193" name="Ericsson"/>
     * <vendor vendor-id="ETSI"  code="13019" name="ETSI"/>
     * <vendor vendor-id="Vodafone" code="12645" name="Vodafone"/>
     * <!-- *********************** End Vendors ************************** -->
     */

    NodeList vendorNodes = doc.getElementsByTagName("vendor");
    
    for (int v = 0; v < vendorNodes.getLength(); v++) {
      
      Node vendorNode = vendorNodes.item(v);
      
      if (vendorNode.getNodeType() == Node.ELEMENT_NODE)
      {
        Element vendorElement = (Element) vendorNode;
        
        String vendorCode = vendorElement.getAttribute( "code" );
        String vendorId = vendorElement.getAttribute( "vendor-id" );
        
        vendorMap.put( vendorId, vendorCode );
      }
    }

    /**************************************************************************
     * COMMAND-CODES
     */
    
    /*
     * <!-- *********************** Commands ***************************** -->
     * <!-- Diameter Base Protocol Command Codes -->
     * <command name="Capabilities-Exchange" code="257" vendor-id="None"/>
     * <command name="Re-Auth" code="258" vendor-id="None"/>
     * <command name="Accounting" code="271" vendor-id="None"/>
     * <command name="Abort-Session" code="274" vendor-id="None">
     * </command>
     * <command name="Session-Termination" code="275" vendor-id="None"/>
     * <command name="Device-Watchdog" code="280" vendor-id="None"/>
     * <command name="Disconnect-Peer" code="282" vendor-id="None"/>
     * <!-- ********************** End Commands ************************** -->
     */
    
    NodeList commandNodes = doc.getElementsByTagName("command");
    
    for (int c = 0; c < commandNodes.getLength(); c++)
    {
      Node vendorNode = commandNodes.item(c);
      
      if (vendorNode.getNodeType() == Node.ELEMENT_NODE)
      {
        Element vendorElement = (Element) vendorNode;
        
        String commandName = vendorElement.getAttribute( "name" );
        String commandCode = vendorElement.getAttribute( "code" );
        String commandVendorId = vendorElement.getAttribute( "vendor-id" );
        
        String commandVendorCode = vendorMap.get( commandVendorId );
        
        commandMap.put( commandVendorCode + "-" + commandCode, commandName );
      }
    }
    

    /**************************************************************************
     * TYPE DEFINITIONS
     */
    
    /*
     * <typedefn type-name="OctetString"/>
     * <typedefn type-name="UTF8String" type-parent="OctetString"/>
     * <typedefn type-name="VendorId" type-parent="Unsigned32"/>
     */
    
    NodeList typedefNodes = doc.getElementsByTagName("typedefn");
    
    for (int td = 0; td < typedefNodes.getLength(); td++)
    {
      Node typedefNode = typedefNodes.item(td);
      
      if (typedefNode.getNodeType() == Node.ELEMENT_NODE)
      {
        Element typedefElement = (Element) typedefNode;
        
        String typeName = typedefElement.getAttribute( "type-name" );
        String typeParent = typedefElement.getAttribute( "type-parent" );
        
        if(typeParent.equals(""))
          typeParent = typeName;
        
        typedefMap.put(typeName, typeParent);
      }
    }
    
    /**************************************************************************
     *  AVPs 
     */
    
    NodeList avpNodes = doc.getElementsByTagName("avp");
    
    for (int s = 0; s < avpNodes.getLength(); s++) {

      Node avpNode = avpNodes.item(s);
      
      /*
       * <!ELEMENT avp ((type | grouped), (enum*))>
       * <!ATTLIST avp
       *   name ID #REQUIRED
       *   description CDATA #IMPLIED
       *   code CDATA #REQUIRED
       *   may-encrypt (yes | no) "yes"
       *   mandatory (must | may | mustnot | shouldnot) "may"
       *   protected (must | may | mustnot | shouldnot) "may"
       *   vendor-bit (must | may | mustnot | shouldnot) "mustnot"
       *   vendor-id IDREF #IMPLIED
       *   constrained (true | false) "false"
       * >
       */
      if (avpNode.getNodeType() == Node.ELEMENT_NODE)
      {
        Element avpElement = (Element) avpNode;
        
        String avpName = avpElement.getAttribute("name");
        System.out.println("AVP Name........."  + avpName);
        
        String avpDescription = avpElement.getAttribute("description");
        System.out.println("AVP Description.."  + avpDescription);
        
        String avpCode = avpElement.getAttribute("code");
        System.out.println("AVP Code........."  + avpCode);
        
        String avpMayEncrypt = avpElement.getAttribute("may-encrypt");
        System.out.println("AVP May-Encrypt.."  + avpMayEncrypt);
        
        String avpMandatory = avpElement.getAttribute("mandatory");
        System.out.println("AVP Mandatory...."  + avpMandatory);
        
        String avpProtected = avpElement.getAttribute("protected").equals("") ? "may" : avpElement.getAttribute("protected");
        System.out.println("AVP Protected...."  + avpProtected);
        
        String avpVendorBit = avpElement.getAttribute("vendor-bit");
        System.out.println("AVP Vendor-Bit..."  + avpVendorBit);
        
        String avpVendorId = avpElement.getAttribute("vendor-id");
        System.out.println("AVP Vendor-Id...."  + avpVendorId);
        
        String avpConstrained = avpElement.getAttribute("constrained");
        System.out.println("AVP Constrained.."  + avpConstrained);
        
        String avpType = "";
        
        // Now either we have type or grouped
        NodeList avpChildNodes = avpNode.getChildNodes();
        
        for (int j = 0; j < avpChildNodes.getLength(); j++)
        {
          Node avpChildNode = avpChildNodes.item(j);
          
          if (avpChildNode.getNodeType() == Node.ELEMENT_NODE)
          {
            Element avpChildElement = (Element) avpChildNode;
           
            if(avpChildElement.getNodeName().equals("grouped"))
            {
              // All we need to know is that's a grouped AVP.
              avpType = "Grouped";
              System.out.println("AVP Type........."  + avpType);
            }
            else if(avpChildElement.getNodeName().equals("type"))
            {
              avpType = avpChildElement.getAttribute("type-name");
              avpType = typedefMap.get( avpType );
              System.out.println("AVP Type........."  + avpType);
            }
            
          }
        }
        
        System.out.println("---------------------------------");
        
        AvpRepresentation avp = new AvpRepresentation(avpName, avpDescription, Integer.valueOf(avpCode), 
            avpMayEncrypt.equals("yes"), avpMandatory, avpProtected, avpVendorBit, getVendorCode(avpVendorId), 
            avpConstrained.equals("true"), avpType);
        
        avpMap.put( avp.getVendorId() + "-" + avp.getCode(), avp );
      }
    }
  }
  
  public AvpRepresentation getAvp(int code)
  {
    return getAvp( code, 0 );
  }
  
  public AvpRepresentation getAvp(int code, long vendorId)
  {
    AvpRepresentation avp = avpMap.get(vendorId + "-" + code);
    return avp;
  }

  private String getVendorCode(String vendorId)
  {
    String vendorCode = vendorMap.get(vendorId);
    
    return vendorCode != null ? vendorCode : "0";
  }
}
