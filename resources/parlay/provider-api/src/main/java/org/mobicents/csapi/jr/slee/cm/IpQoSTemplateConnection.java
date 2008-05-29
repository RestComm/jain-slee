package org.mobicents.csapi.jr.slee.cm;

/**
 * This interface provides access to a specific QoS template, such as Gold, offered by the provider. This interface provides get methods to discover the QoS service details, and set methods to set the requested values for a new VPrP. The template specifies the QoS parameters and their default values. Each service template parameter is tagged by the service provider to indicate one of the following:																							-· Provider specified: the value cannot be modified for this template.														-· Enterprise operator specified: operator may change the default value set by the provider. The default value can be blank to indicate that there is no default value for this parameter, and the user can change it according to advice typically given by the description parameters.																						-· Unspecified: the parameter is not used for this template, and the Enterprise operator cannot change it.																																					For example, maximum delay for a Gold template may be provider specified, while maximum bandwidth may be Enterprise operator specified, meaning that its values can be changed by the setProvisionedQoSInfo, or by setPipeQoSInfo methods. Guidance how to change the default values may be provided by the template description parameter.																															The tag of a parameter cannot be changed by any set method of this interface, i.e., if a parameter is tagged unspecified, or provider specified, Enterprise operator cannot override the value of this tag to say operator specified.																																			This template is a temporary interface created as a copy of the original template that stores all the template parameters. The temporary interface is created with the getTemplate method of IpQoSMenu interface. The values passed to this template interface by the set methods replace (if permitted by the tags) the default values stored in this template interface, i.e., a get following a set method to this template interface will fetch the new values set by the Enterprise operator. Once a new VPrP is created by the create method in IpVPrN, the temporary interface might not be accessible anymore.
 *
 * 
 * 
 */
public interface IpQoSTemplateConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method is used to get the template type, e.g., Gold.
@return templateType : This parameter contains the template type.
If template type is not found, then P_UNKNOWN_TEMPLATE_TYPE exception is raised.
     * 
     */
    String getTemplateType() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get a description of the QoS service stored in this template interface. Connectivity manager APIs support default values set by the provider for each QoS parameter, i.e., a template (e.g., Gold template) may have a set of default values (e.g., a default value for minimum delay, a default value for maximum delay, etc.). If the network service provider allows (using the tags described above) the enterprise operator to change a specific default value, the provider can use this description to advise the user the conditions under which they can be changed, and the alternate values that can be used. 
@return description : This parameter contains a description of the service for this template and may also be used to convey any advice to the user such as what values can be selected instead of default values.
If the description is not found, then P_UNKNOWN_DESCRIPTION exception is raised. Note that if the description is found, but it contains no description, this should not raise the exception.
     * 
     */
    String getDescription() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_DESCRIPTION,javax.slee.resource.ResourceException;


    /**
     *     This method is used to store an existing service level agreement (SLA) identifier associated it with a specific VPrP. SLA ID is optional and is not required to be part of every VPrP. Each time this method is performed, the new value replaces the old value in the template.
     *     @param slaID This parameter contains the SLA ID.  If the string representation of the SLA ID does not obey the rules for SLA identification, then a P_ILLEGAL_SLA_ID exception is raised.

     */
    void setSlaID(String slaID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SLA_ID,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get pipe QoS information consisting of load parameters, direction of the traffic, and the endpoint (SAP or site) of a virtual provisioned pipe offered by this template.   
@return pipeQoSInfo : This parameter includes the pipe QoS  default parameters for this template.  The endpoints are not specified in the getpipeQoS  method. The directionality and load parameters are tagged, and can be set by the provider or left for the operator to be set.     
· If no pipe QoS information is found, then P_UNKNOWN_PIPEQOSINFO exception is raised.
     * 
     */
    org.csapi.cm.TpPipeQoSInfo getPipeQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_PIPEQOSINFO,javax.slee.resource.ResourceException;


    /**
     *     This method is used to request pipe QoS parameters consisting of load parameters, direction of the traffic, and the endpoint (SAP or site) of the virtual provisioned pipe, as selected by the operator from the set of values offered by the provider. To modify any default value, the tag has to be set to OperatorSpecified. The parameters name, description, and tag are ignored with this method.
     *     @param pipeQoSInfo This parameter includes the virtual provisioned pipe information regarding the flow direction, the load on the endpoint of the pipe, and the load on the endpoints.      																						- If a parameter is tagged with providerSpecified, or unspecified, then the P_ILLEGAL_TAG exception is raised.       	- If a value requested for a specific parameter by this method is not consistent with the advice given by the provider for choosing parameter values, the P_ILLEGAL_VALUE is raised. This is an optional exception that would be applied if the provider can verify consistency on the fly. Otherwise, the new VPrP request will be denied by setting its status flag to disallowed.      																												- If a combination of requested parameters is illegal, then P_ILLEGAL_COMBINATION is raised.

     */
    void setPipeQoSInfo(org.csapi.cm.TpPipeQoSInfo pipeQoSInfo) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_TAG,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION,javax.slee.resource.ResourceException;


    /**
     *     The operator uses this method to get the default time period set by the provider for the template. Applying a logical AND operation of all the components of this parameter evaluates the valid period(s).
@return validityInfo : This parameter provides the default validity information.      
· If no validity information is found for this template, then P_UNKNOWN_VALIDITY_INFO exception is raised. Note that if the validity information is found and validitySpecified, is FALSE this exception is not raised.
     * 
     */
    org.csapi.cm.TpValidityInfo getValidityInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VALIDITY_INFO,javax.slee.resource.ResourceException;


    /**
     *     The operator uses this method to set the required time period for a new VPrP. The requested time and the default time set by the provider are all ANDed together to determine the final valid time period for this VPrP. Note that only those components that are tagged as operator specified can be set by the operator. 
     *     @param validityInfo This parameter provides the requested validity information for a new VPrP.        												- If a parameter is tagged with providerSpecified, or unspecified, then the P_ILLEGAL_TAG exception is raised.      	- If a value requested for a specific parameter by this method is not consistent with the advice given by the provider for choosing parameter values, the P_ILLEGAL_VALUE is raised. This is an optional exception that would be applied if the provider can verify consistency on the fly. Otherwise, the new VPrP request will be denied by setting its status flag to disallowed.     																												- If a combination of requested parameters is illegal, then P_ILLEGAL_COMBINATION is raised. . For example, if the specified time duration is longer than 24 hours for a time-of-day parameter, or the integer value representing day-of-week or month-of-year is outside the permitted range.

     */
    void setValidityInfo(org.csapi.cm.TpValidityInfo validityInfo) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_TAG,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION,javax.slee.resource.ResourceException;


    /**
     *     The Enterprise operator uses this method to set the requested values for the QoS parameters. The values passed by this method replace the default values in the temporary template interface. Tag values associated with each parameter can be set only by the provider and cannot be changed by the operator. If tag values are included in this method, they should be ignored. Only those parameters that are tagged with the value operator specified can be modified using this method.  With this method, the name and description parameters are ignored.
     *     @param provisionedQoSInfo This parameter consists of delay, loss, jitter, and exceed load action parameters.       
· If a parameter from the delayParameters, lossParameters, jitterParameters, or  excessLoadAction is tagged in the template with the values provider specified, or unspecified, then the P_ILLEGAL_TAG exception is raised.       
· If a value requested for a specific parameter by this method is not consistent with the advice given by the provider for choosing parameter values, the P_ILLEGAL_VALUE is raised. This is an optional exception that would be applied if the provider can verify consistency on the fly. Otherwise, the new VPrP request will be denied by setting its status flag to disallowed.      
· If a combination of requested parameters or parameter values is illegal, then P_ILLEGAL_COMBINATION is raised. An example of a illegal combination is maximum delay parameter and delay priority, as only one of the two can be used.

     */
    void setProvisionedQoSInfo(org.csapi.cm.TpProvisionedQoSInfo provisionedQoSInfo) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_TAG,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the default values associated with this template (e.g., delay default value, loss default value).  
@return provisionedQoSInfo:
This parameter consists of delay, loss, jitter, and exceed load action parameters.      											· If no QoS information is found, then P_UNKNOWN_QOS_INFO exception is raised.        																																								The Provisioned QoS Information has the following information:     														The Delay descriptor lists the delay default values, i.e., default values for mean delay, maximum delay, minimum delay, and delay priority. A provider may choose to tag any number of delay parameters as provider specified, Enterprise operator specified, or unspecified. For example, a Gold template may have a default value just for the mean delay, leaving the other parameters either unspecified, or some set to enterprise operator specified.       						The loss descriptor lists the packet loss default values, i.e., mean loss, maximum loss, minimum loss, and loss priority. A provider may choose to tag any number of loss parameters as provider specified, Enterprise operator specified, or unspecified. For example, a Gold template may have a default value just for the mean loss only, leaving the other parameters either unspecified, or some be Enterprise operator specified.      										The jitter descriptor lists the jitter default values (the delay measured between arriving packets), that is, mean jitter, maximum jitter, minimum jitter, and jitter priority. A provider may choose to tag any number of jitter parameters as provider specified Enterprise operator specified, or unspecified. For example, a Gold template may have a default value just for the mean jitter only, leaving the other parameters either unspecified, or some may be Enterprise operator specified.      																														The excess load action parameter specifies the policing treatment for traffic that exceeds the load parameters set for the virtual provisioned pipe. This policing function can take the following actions when the provider network detects that the traffic trying to enter the VPrP exceeds the load parameters specified in the pipe QoS loads parameters:      		· Drop: 	drop packets (i.e., do not ever transmit them) that exceed the load traffic parameters that were set for the VPrP       																																- Transmit:	transmit packets even though transmitting them will create a load in excess of the load traffic parameters that were set for the VPrP      																							-  Reshape: 	reshape the entering traffic by trying to keep the packet (and not drop them yet) waiting for the entering traffic load to come down below the load conditions set for the VPrP, and if it does, transmit the packets then.     	-  Remark :	remark the packet for a lower QoS service, then transmit them (i.e., transfer the packet through some other less demanding VPrP). This may result in increased packet loss (i.e., the excess packets may have now higher probability of being dropped before reaching their SAP or Site destination), or increased packet delay and / or packet jitter.
     * 
     */
    org.csapi.cm.TpProvisionedQoSInfo getProvisionedQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_QOS_INFO,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get the DiffServ Codepoint of QoS service offered by the template.
@return dsCodepoint : This parameter holds the DS Codepoint for the VPrP.  It has two parameters: match and mask to enable the provider to locate the bit string in any location in the 6-bit long field. The actual Codepoint is a result of an AND operation bit by bit of the two parameters.       
· If no DS Codepoint is found, then P_UNKNOWN_DSCODEPOINT exception is raised.
     * 
     */
    org.csapi.cm.TpDsCodepoint getDsCodepoint() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_DSCODEPOINT,javax.slee.resource.ResourceException;


} // IpQoSTemplateConnection

