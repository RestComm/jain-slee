package org.mobicents.csapi.jr.slee.cm;

/**
 * This interface holds the QoS menu offered by the provider. Each QoS service offered (e.g., Gold, Silver) is specified in a separate template. The template specifies the parameters and their default values from which the operator may choose to create a VPrP. When the operator asks for a specific template from the list of templates (getTemplate method), a temporary template interface is created. This temporary template interface holds all the parameters (e.g., all the Gold parameters) and their default values offered by the provider for this template. 
 *
 * 
 * 
 */
public interface IpQoSMenuConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method is used to get an interface reference to a specific template. The provider creates a temporary copy of the original template that contains all the QoS parameters for this template (e.g., Gold). 
@return templateRef : This parameter contains a reference to the template interface.  Note that if the reference to this temporary template is lost, there is no way to recall it. To create a new temporary template this method has to be applied again, however, any values that were set in the old temporary template are lost.
     *     @param templateType This parameter contains template type.  

     */
    org.csapi.IpInterface getTemplate(String templateType) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     This method is used to get a list of templates, each of which specifies a QoS service, such as Gold or Silver. 
@return templateList : This parameter contains a list of QoS service templates IDs.
If no templates are found, then a P_UNKNOWN_TEMPLATES exception is raised.
     * 
     */
    String[] getTemplateList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_TEMPLATES,javax.slee.resource.ResourceException;


} // IpQoSMenuConnection

