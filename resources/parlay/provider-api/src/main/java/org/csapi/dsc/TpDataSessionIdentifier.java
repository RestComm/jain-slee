package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionIdentifier(){}
	public org.csapi.dsc.IpDataSession DataSessionReference;
	public int DataSessionID;
	public TpDataSessionIdentifier(org.csapi.dsc.IpDataSession DataSessionReference, int DataSessionID)
	{
		this.DataSessionReference = DataSessionReference;
		this.DataSessionID = DataSessionID;
	}
}
