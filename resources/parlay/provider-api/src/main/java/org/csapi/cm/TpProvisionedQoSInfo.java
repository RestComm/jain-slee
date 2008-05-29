package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpProvisionedQoSInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpProvisionedQoSInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpProvisionedQoSInfo(){}
	public org.csapi.cm.TpDelayDescriptor delayDescriptor;
	public org.csapi.cm.TpLossDescriptor lossDescriptor;
	public org.csapi.cm.TpJitterDescriptor jitterDescriptor;
	public org.csapi.cm.TpNameDescrpTagExcessLoadAction excessLoadAction;
	public org.csapi.cm.TpNameDescrpTagString description;
	public TpProvisionedQoSInfo(org.csapi.cm.TpDelayDescriptor delayDescriptor, org.csapi.cm.TpLossDescriptor lossDescriptor, org.csapi.cm.TpJitterDescriptor jitterDescriptor, org.csapi.cm.TpNameDescrpTagExcessLoadAction excessLoadAction, org.csapi.cm.TpNameDescrpTagString description)
	{
		this.delayDescriptor = delayDescriptor;
		this.lossDescriptor = lossDescriptor;
		this.jitterDescriptor = jitterDescriptor;
		this.excessLoadAction = excessLoadAction;
		this.description = description;
	}
}
