package org.csapi.cs;

/**
 *	Generated from IDL definition of union "TpAppInformation"
 *	@author JacORB IDL compiler 
 */

public final class TpAppInformation
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cs.TpAppInformationType discriminator;
	private java.lang.String Timestamp;

	public TpAppInformation ()
	{
	}

	public org.csapi.cs.TpAppInformationType discriminator ()
	{
		return discriminator;
	}

	public java.lang.String Timestamp ()
	{
		if (discriminator != org.csapi.cs.TpAppInformationType.P_APP_INF_TIMESTAMP)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Timestamp;
	}

	public void Timestamp (java.lang.String _x)
	{
		discriminator = org.csapi.cs.TpAppInformationType.P_APP_INF_TIMESTAMP;
		Timestamp = _x;
	}

}
