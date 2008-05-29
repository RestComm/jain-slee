package org.mobicents.csapi.jr.slee.cm;

/**
 * The service Connectivity Manager Interface is the entry point to the Connectivity Manager service. After the enterprise operator client is authenticated and authorised, the client application discovers the Connectivity Manager interface, then the operator can use this interface to step through the process of provisioning a new VPrP.  This interface has two methods, one to get the handle to the menu of QoS services offered by the provider, and the other one is a handle to the enterprise network interface that holds information about current services that the provider network delivers to the enterprise network.
 *
 * 
 * 
 */
public interface IpConnectivityManagerConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     A client uses this method to get a reference to the QoS menu interface.
@return menuRef : This parameter is a reference to the QoS menu interface.  If no menu is found, P_UNKNOWN_MENU exception is raised.
     * 
     */
    org.csapi.IpInterface getQoSMenu() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_MENU,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get a handle to the enterprise network interface, which holds information regarding network services that are already provisioned for the enterprise network in the provider network.
@return enterpriseNetworkRef : This parameter is a reference to the enterprise network interface. . If enterprise network is not found, P_UNKNOWN_ENTERPRISE_NETWORK exception is raised.
     * 
     */
    org.csapi.IpInterface getEnterpriseNetwork() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORK,javax.slee.resource.ResourceException;


} // IpConnectivityManagerConnection

