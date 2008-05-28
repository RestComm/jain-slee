package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpDomainID"
 *	@author JacORB IDL compiler 
 */

public final class TpDomainID
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.fw.TpDomainIDType discriminator;
	private java.lang.String FwID;
	private java.lang.String ClientAppID;
	private java.lang.String EntOpID;
	private java.lang.String ServiceID;
	private java.lang.String ServiceSupplierID;

	public TpDomainID ()
	{
	}

	public org.csapi.fw.TpDomainIDType discriminator ()
	{
		return discriminator;
	}

	public java.lang.String FwID ()
	{
		if (discriminator != org.csapi.fw.TpDomainIDType.P_FW)
			throw new org.omg.CORBA.BAD_OPERATION();
		return FwID;
	}

	public void FwID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpDomainIDType.P_FW;
		FwID = _x;
	}

	public java.lang.String ClientAppID ()
	{
		if (discriminator != org.csapi.fw.TpDomainIDType.P_CLIENT_APPLICATION)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ClientAppID;
	}

	public void ClientAppID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpDomainIDType.P_CLIENT_APPLICATION;
		ClientAppID = _x;
	}

	public java.lang.String EntOpID ()
	{
		if (discriminator != org.csapi.fw.TpDomainIDType.P_ENT_OP)
			throw new org.omg.CORBA.BAD_OPERATION();
		return EntOpID;
	}

	public void EntOpID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpDomainIDType.P_ENT_OP;
		EntOpID = _x;
	}

	public java.lang.String ServiceID ()
	{
		if (discriminator != org.csapi.fw.TpDomainIDType.P_SERVICE_INSTANCE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ServiceID;
	}

	public void ServiceID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpDomainIDType.P_SERVICE_INSTANCE;
		ServiceID = _x;
	}

	public java.lang.String ServiceSupplierID ()
	{
		if (discriminator != org.csapi.fw.TpDomainIDType.P_SERVICE_SUPPLIER)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ServiceSupplierID;
	}

	public void ServiceSupplierID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpDomainIDType.P_SERVICE_SUPPLIER;
		ServiceSupplierID = _x;
	}

}
