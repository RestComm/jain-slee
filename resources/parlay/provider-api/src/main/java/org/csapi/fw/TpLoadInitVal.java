package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadInitVal"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadInitVal
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLoadInitVal(){}
	public org.csapi.fw.TpLoadLevel LoadLevel;
	public org.csapi.fw.TpLoadThreshold LoadThreshold;
	public TpLoadInitVal(org.csapi.fw.TpLoadLevel LoadLevel, org.csapi.fw.TpLoadThreshold LoadThreshold)
	{
		this.LoadLevel = LoadLevel;
		this.LoadThreshold = LoadThreshold;
	}
}
