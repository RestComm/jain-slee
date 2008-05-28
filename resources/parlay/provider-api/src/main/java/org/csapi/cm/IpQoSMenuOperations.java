package org.csapi.cm;

/**
 *	Generated from IDL interface "IpQoSMenu"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpQoSMenuOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.IpInterface getTemplate(java.lang.String templateType) throws org.csapi.TpCommonExceptions;
	java.lang.String[] getTemplateList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_TEMPLATES;
}
