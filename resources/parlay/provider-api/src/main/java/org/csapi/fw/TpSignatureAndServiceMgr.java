package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpSignatureAndServiceMgr"
 *	@author JacORB IDL compiler 
 */

public final class TpSignatureAndServiceMgr
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpSignatureAndServiceMgr(){}
	public byte[] DigitalSignature;
	public org.csapi.IpService ServiceMgrInterface;
	public TpSignatureAndServiceMgr(byte[] DigitalSignature, org.csapi.IpService ServiceMgrInterface)
	{
		this.DigitalSignature = DigitalSignature;
		this.ServiceMgrInterface = ServiceMgrInterface;
	}
}
