package org.mobicents.csapi.jr.slee.cm;

/**
 * This interface stores enterprise network information maintained by the provider as it relates to the virtual private network service and the virtual provisioned network service that the enterprise had already established with the service provider network. The enterprise operator can only retrieve but not change the information stored with this interface. The methods of this interface enable the enterprise operator to obtain the handle to the interface that holds information regarding an existing VPrN,  to list the sites connected to the VPN, and get the handle to a specific site interface that store information about the site. 
 *
 * 
 * 
 */
public interface IpEnterpriseNetworkConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method is used to get the list of enterprise network site IDs. These IDs identify the sites that are inter-connected through the provider network. These IDs were set when the VPN was provisioned in the provider network in the provider network.
@return siteList : This parameter lists the site IDs (e.g., research, marketing, Middletown Building D5, London) of the enterprise network that are serviced by the provider network.  	If no site is found, then a P_UNKNOWN_SITES exception is raised.
     * 
     */
    String[] getSiteList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITES,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get a handle to the interface that holds information regarding a previously provisioned Virtual Private Network (VPrN).
@return vPrNRef : This parameter is a handle to the VPrN interface that holds information about previously provisioned VPrN.
If no VPrN is found for this enterprise network, then a P_UNKNOWN_VPRN exception is raised.
     * 
     */
    org.csapi.IpInterface getVPrN() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRN,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get a handle to an interface that holds information about a specific site. 
@return siteRef : This parameter is a reference to the site interface.
     *     @param siteID This parameter is the ID given to a particular site. The ID is not assigned via OSA APIs, but previously when a new VPN (or VLL) is established for the enterprise on the provider network. These ID are typically names that refer to objects that are meaningful in the context of the enterprise network, such as: Marketing, New York, or Bulling 4. This site ID can be used as an endpoint of a provisioned virtual provisioned pipe (VPrP).       										-  If the string representation of the siteID does not obey the rules for site identification, then a P_ILLEGAL_SITE_ID exception is raised.      																					-  If the site ID representation is legal but there is no site with this ID, then P_UNKNOWN_SITE_ID exception is raised.

     */
    org.csapi.IpInterface getSite(String siteID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_SITE_ID,javax.slee.resource.ResourceException;


} // IpEnterpriseNetworkConnection

