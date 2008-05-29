package org.mobicents.csapi.jr.slee.cm;

/**
 * This interface stores enterprise network site information maintained by the provider.
 *
 * 
 * 
 */
public interface IpEnterpriseNetworkSiteConnection extends org.mobicents.csapi.jr.slee.cm.IpEnterpriseNetworkConnection{


    /**
     *     This method is used to get the list of SAP IDs of the enterprise VPN (i.e., on the provider network) that have previously been established for this site with the provider network.
@return sapList : This parameter is a list of SAP IDs. This SAP ID can be used as an endpoint of a provisioned virtual provisioned pipe (VPrP).
If no SAPs are found for this site, then P_UNKNOWN_SAPS exception is raised.
     * 
     */
    String[] getSAPList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SAPS,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the site ID for this site.
@return siteID : This parameter holds the value for the site ID.
If no site ID is found for this site, then P_UNKNOWN_SITE_ID exception is raised.
     * 
     */
    String getSiteID() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the site location.
@return siteLocation : This parameter holds the value for the site location. 
If no site location is found for this site, then P_UNKNOWN_SITE_LOCATION exception is raised.
     * 
     */
    String getSiteLocation() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_LOCATION,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the description associated with this site.
@return siteDescription : This parameter is a string that holds the site description.
If no description is found for this site, then P_UNKNOWN_SITE_DESCRIPTION exception is raised.
     * 
     */
    String getSiteDescription() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTION,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get IP subnet information for this site.
@return ipSubnet : This parameter lists the subnet information.
If no IP Subnet information is found for this site, then a P_UNKNOWN_IPSUBNET exception is raised.
     * 
     */
    org.csapi.cm.TpIPSubnet getIPSubnet() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_IPSUBNET,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the IP address of the SAP on the enterprise network.
@return ipSubnet : This parameter holds the IP address information for the SAP. This TpIPSubnet data type follows the DMTF CIM specification for IP sub-net.
     *     @param sapID This parameter holds the IP address information for the SAP.      																- If the string representation of the sapID does not obey the rules for site identification, then a P_ILLEGAL_SITE_ID exception is raised.      																					- If the site ID representation is legal but there is no site with this ID, then P_UNKNOWN_SAP exception is raised.      	- If no IP Subnet information is found for this SAP, then a P_UNKNOWN_IPSUBNET exception is raised.

     */
    org.csapi.cm.TpIPSubnet getSAPIPSubnet(String sapID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_SAP,org.csapi.cm.P_UNKNOWN_IPSUBNET,javax.slee.resource.ResourceException;


} // IpEnterpriseNetworkSiteConnection

