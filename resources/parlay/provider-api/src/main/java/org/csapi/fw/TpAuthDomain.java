package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpAuthDomain"
 *	@author JacORB IDL compiler 
 */

public final class TpAuthDomain
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAuthDomain(){}
	public org.csapi.fw.TpDomainID DomainID;
	public org.csapi.IpInterface AuthInterface;
	public TpAuthDomain(org.csapi.fw.TpDomainID DomainID, org.csapi.IpInterface AuthInterface)
	{
		this.DomainID = DomainID;
		this.AuthInterface = AuthInterface;
	}
}
