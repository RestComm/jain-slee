package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpCorrelationID"
 *	@author JacORB IDL compiler 
 */

public final class TpCorrelationID
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCorrelationID(){}
	public int CorrelationID;
	public int CorrelationType;
	public TpCorrelationID(int CorrelationID, int CorrelationType)
	{
		this.CorrelationID = CorrelationID;
		this.CorrelationType = CorrelationType;
	}
}
