package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpLoadStatisticEntityID"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticEntityID
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.fw.TpLoadStatisticEntityType discriminator;
	private java.lang.String FrameworkID;
	private java.lang.String ServiceID;
	private java.lang.String ClientAppID;

	public TpLoadStatisticEntityID ()
	{
	}

	public org.csapi.fw.TpLoadStatisticEntityType discriminator ()
	{
		return discriminator;
	}

	public java.lang.String FrameworkID ()
	{
		if (discriminator != org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_FW_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return FrameworkID;
	}

	public void FrameworkID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_FW_TYPE;
		FrameworkID = _x;
	}

	public java.lang.String ServiceID ()
	{
		if (discriminator != org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_SVC_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ServiceID;
	}

	public void ServiceID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_SVC_TYPE;
		ServiceID = _x;
	}

	public java.lang.String ClientAppID ()
	{
		if (discriminator != org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_APP_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ClientAppID;
	}

	public void ClientAppID (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpLoadStatisticEntityType.P_LOAD_STATISTICS_APP_TYPE;
		ClientAppID = _x;
	}

}
