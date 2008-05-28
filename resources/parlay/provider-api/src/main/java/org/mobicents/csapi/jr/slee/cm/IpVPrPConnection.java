package org.mobicents.csapi.jr.slee.cm;

/**
 * The virtual provisioned pipe interface provides information on a VPrP whose status can be in one of the following states:																																	-  Active:  a previously established VPrP, which indicates that a previous request to create the VPrP was granted by the provider. Packet that belong to this VPrP and meet the validity time requirements, are admitted to the enterprise VPrN																																	-  Pending:  a request to create a new VPrP is still pending response from the provider, indicating that the provider is still processing the request to create a new VPrP. Packet that belong to this VPrP are not admitted to the enterprise VPrN																																	-  Disallowed:  a request to create a new VPrP was denied. A description parameter may include the reason for the denial. This is an disallowed VPrP and packet that belong to this VPrP are not admitted to the enterprise VPrN.																																				A VPrP is composed of the following elements, each of which provides the following Provisioned QoS measures:	Element type                                     Element                                             QoS measure									 ------------------------------------------------------------------------------------------------------------------------			Endpoint                                     A SAP or a site						Pipe QoS information based 																									on VPrP endpoints: load parameters,																							 policing action.  Applied to traffic																								entering the enterprise VPrN.						 ------------------------------------------------------------------------------------------------------------------------				Packet header marking   			TOS bits marked 						Provisioned QoS information.															with DS Codepoint					One of the QoS levels established 																								by the provider in its backbone																									network to be applied to the 																									packets carrying this marking.						 ------------------------------------------------------------------------------------------------------------------------					Time Validity 					A time requirement					Packets are admitted to the VPrN														that is applied to 						only if they also meet validity 															an active VPrP.						time requirements.
 *
 * 
 * 
 */
public interface IpVPrPConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method is used to get the ID of the virtual provisioned pipe.
@return vPrPID : This parameter is the ID of the virtual provisioned pipe.
· If no VPrP ID is found, then P_UNKNOWN_VPRP_ID exception is raised.
     * 
     */
    String getVPrPID() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRP_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the ID of the service level agreement (SLA) if such was associated with the virtual provisioned pipe at the provisioning of the VPrP.
@return slaID : This parameter is the identifier for the service level description.  
· If no SLA ID is found, then P_UNKNOWN_SLA_ID exception is raised.
· Note that SLA ID is optional. If no SLA ID was entered when this VPrP was created, this exception will be raised.
     * 
     */
    String getSlaID() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SLA_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the status of the virtual provisioned pipe. It can be used to convey, for example, the status of an outstanding previously submitted provisioning request (which the provider could not verify in real time).
@return status : This parameter is used to convey status of the virtual provisioned pipe.  The semantics of each of these states is specified above.
· If status information is not found, Then P_UNKNOWN_STATUS exception is raised.
     * 
     */
    org.csapi.cm.TpVprpStatus getStatus() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_STATUS,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the provisioned QoS information set for this virtual provisioned pipe. This method is the same method with the same parameters as in the QoS Template interface. The only difference is that the tag value is meaningless here. The values for an Active VPrP are the values provisioned in the provider network, and the values for a Pending VPrP are the requested values.
@return provisionedQoSInfo : This parameter consists of delay, loss, jitter, and exceed load action parameters. The tag value (provider specified, operator specified, or unspecified) is used only with the template interface, and is meaningless for this VPrP interface. Delay priority, Loss priority, and Jitter priority are used to specify the priority of this VPrP relative to other VPrPs, instead of specifying explicit values for these parameters.      
· If no QoS information is found, then P_UNKNOWN_QOS_INFO exception is raised.
     * 
     */
    org.csapi.cm.TpProvisionedQoSInfo getProvisionedQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_QOS_INFO,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the time period for which the VPrP is valid. For the VPrP to be included in the VPrN, the VPrP has to be in active status and valid.
@return validityInfo : This parameter defines the time periods for which this VPrP is valid. The valid periods are evaluated by applying a logical AND operation of all the components of this parameter, without regard who specified the valid time (i.e., specified by operator, or provider).  
· If no validity information is found for this VPrP, then P_UNKNOWN_VALIDITY_INFO exception is raised. Note that if the validity information is found and validitySpecified  is FALSE this exception is not raised.
     * 
     */
    org.csapi.cm.TpValidityInfo getValidityInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VALIDITY_INFO,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get and set the pipe QoS information for this VPrP. For an Active VPrP the values of these parameters are provisioned in the provider network. For a Pending VPrP, the values are the requested values. The tag value is meaningless for this interface.
@return pipeQoSInfo : This parameter consists of load parameters, direction of the traffic, and the endpoints (SAP or site) of the virtual provisioned pipe. This data type is defined in the QoS Template.
If no pipe QoS information is found, then P_UNKNOWN_PIPEQOSINFO exception is raised. 
     * 
     */
    org.csapi.cm.TpPipeQoSInfo getPipeQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_PIPEQOSINFO,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the DiffServ Codepoint of the virtual provisioned pipe. This DS Codepoint is populated in the TOS bits of the packet header to identify to the service provider network the specific VPrP to service this packet entering the provider network.
@return dsCodepoint : This parameter holds the DS Codepoint for the VPrP.  It has two parameters: match and mask to enable the provider to locate the bit string in any location in the 6-bit long field. The actual Codepoint is a result of an AND operation bit by bit of the two parameters.
· If no DS Codepoint is found, then P_UNKNOWN_DSCODEPOINT exception is raised.
     * 
     */
    org.csapi.cm.TpDsCodepoint getDsCodepoint() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_DSCODEPOINT,javax.slee.resource.ResourceException;


} // IpVPrPConnection

