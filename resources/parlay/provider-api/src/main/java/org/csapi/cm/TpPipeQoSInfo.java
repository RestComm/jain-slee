package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpPipeQoSInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPipeQoSInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPipeQoSInfo(){}
	public org.csapi.cm.TpNameDescrpTagDir directionality;
	public org.csapi.cm.TpEndpoint serviceOrigin;
	public org.csapi.cm.TpEndpoint serviceDestination;
	public org.csapi.cm.TpLoadDescriptor forwardLoad;
	public org.csapi.cm.TpLoadDescriptor reverseLoad;
	public org.csapi.cm.TpNameDescrpTagString description;
	public TpPipeQoSInfo(org.csapi.cm.TpNameDescrpTagDir directionality, org.csapi.cm.TpEndpoint serviceOrigin, org.csapi.cm.TpEndpoint serviceDestination, org.csapi.cm.TpLoadDescriptor forwardLoad, org.csapi.cm.TpLoadDescriptor reverseLoad, org.csapi.cm.TpNameDescrpTagString description)
	{
		this.directionality = directionality;
		this.serviceOrigin = serviceOrigin;
		this.serviceDestination = serviceDestination;
		this.forwardLoad = forwardLoad;
		this.reverseLoad = reverseLoad;
		this.description = description;
	}
}
