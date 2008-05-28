package org.mobicents.csapi.jr.slee.cm;

/**
 * The enterprise operator can create a new virtual provisioned pipe (VPrP) in an existing virtual private network (VPN) with this VPN interface. Such a pipe is extended between specific SAPs/sites. Each such pipe is associated with QoS parameters identified by a specific DiffServ Codepoint. A packet that arrives at the SAP/site with a specific Codepoint, is "directed" to the virtual provisioned pipe that supports the QoS parameters provisioned for this pipe. The collection of all the virtual provisioned pipes (VPrPs), provisioned within the enterprise VPN, constitutes the virtual provisioned network (VPrN). Enterprise operator can create new VPrPs and delete existing VPrP using this interface. This interface provides also methods to get the list of already provisioned VPrPs, and a handle to a specific VPrP interface that holds information for this VPrP.
 *
 * 
 * 
 */
public interface IpVPrNConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method is used to get the list of VPrP IDs for the already established virtual provisioned pipes for the enterprise network. Each pipe is assigned an ID at the provisioning of the pipe.
@return vPrPList : This parameter lists the IDs of all the virtual provisioned pipes established in the virtual provisioned network.  
· If no VPrP is found, then the P_UNKNOWN_VPRP exception is raised.
     * 
     */
    String[] getVPrPList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRP,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get a handle to the virtual provisioned pipe.
@return vPrPRef : This parameter is the reference to a provision virtual provisioned pipe interface.  
     *     @param vPrPID This parameter is virtual provisioned pipe ID, a unique ID across all VPrNs (of different enterprises) in the provider network. This ID is assigned by the provider when a new VPrP is created by the create method of this interface.       		- If the string representation of the vPrPID does not obey the rules for site identification, then a P_ILLEGAL_VPRP_ID exception is raised.      																					- If the VPrP ID representation is legal but there is no VPrP with this ID, then the P_UNKNOWN_VPRP_ID exception is raised.       																															- Note that as soon a request to create a new VPrP (see IpVPrN interface) is submitted by the enterprise client, a new VPrP interface should be created by the provider. However, the provider might be in a situation where the evaluation of the request for a new VPrP has not been completed yet. In such a case, until the provider makes a decision, the status of the new VPrP should be set to Pending. The P_UNKNOWN_VPRP_ID exception should not be raised in this case.

     */
    org.csapi.IpInterface getVPrP(String vPrPID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VPRPID,org.csapi.cm.P_UNKNOWN_VPRP_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used to create a new virtual provisioned pipe, which includes the pipe QoS information, the provisioned QoS information, the SLA ID, and the selected pairs of SAP/Sites. The method returns a reference to the new virtual provisioned pipe interface that is added to an existing VPrN. This VPrP needs to be accessed in order to find the status of the request to create a new VPrP. The status can have one of the following values: Pending, Active, or Disallowed. The enterprise operator should delete disallowed VPrPs. The provider may remove VPrPs with a disallowed status, if it stays in this status for some pre-agreed length of time. 
@return vPrPRef : This parameter is the handle to the new VPrP interface created as a response to the createVPrP method. The new VPrP interface may not include yet the decision of the provider to the request to create a new VPrP. However, if the request is granted, the status flag of the VPrP is set to Active. If the request is denied, the status flag is set to DISALLOWED. The status of the new VPrP is held in the status parameter of the VPrP, which should be Pending if the processing of the request has not been completed by the provider.
     *     @param templateRef This parameter is a reference to the template interface that holds all the requested QoS parameters for a new VPrP. The requested QoS parameter values, stored in the template interface, are used by the provider to provision a new VPrP for the enterprise network.      																											- If the reference representation of the templateRef does not obey the rules for reference values, then a P_ILLEGAL_REF_VALUE exception is raised.     																				- If the reference representation is legal but there is no interface with this reference, then P_UNKNOWN_INTERFACE exception is raised.     																			- If the one of the parameter values requested in the template is not consistent with default values set by the provider or the advice given in one of its description parameters, this is considered to be an inconsistent VPrP. The provider can deny a request for an inconsistent VPrP. The reason of the denial would be specified in the denial reason parameter for that VPrP. Since it is not required that the provider renders a decision in real time, no exception parameter is defined for this createVPrP method for requesting an inconsistent VPrP.

     */
    org.csapi.IpInterface createVPrP(org.csapi.IpInterface templateRef) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_REF_VALUE,org.csapi.cm.P_UNKNOWN_INTERFACE,javax.slee.resource.ResourceException;


    /**
     *     This method is used to delete a virtual provisioned pipe. A VPrP may have one of the following status values: Active, Pending, Disallowed. The reasons for deleting a VPrP may vary. Here are some examples. If a VPrP is active, the delete method is used when the VPrP is not needed anymore. If the VPrP is pending approval, one can still delete the VPrP. If the VPrP is disallowed, the VPrP should be deleted, as it does not serve any useful purpose anymore.   
     *     @param vPrPID This parameter specifies the ID of the VPrP to be deleted.  If the VPrP cannot be deleted, then P_CANT_DELETE_VPRP exception is raised.  If the VPrP ID is not found, then P_UNKNOWN_VPRP_ID exception is raised.

     */
    void deleteVPrP(String vPrPID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_CANT_DELETE_VPRP,org.csapi.cm.P_UNKNOWN_VPRP_ID,javax.slee.resource.ResourceException;


} // IpVPrNConnection

