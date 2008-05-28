package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpLoadStatisticData"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLoadStatisticData(){}
	public float LoadValue;
	public org.csapi.fw.TpLoadLevel LoadLevel;
	public TpLoadStatisticData(float LoadValue, org.csapi.fw.TpLoadLevel LoadLevel)
	{
		this.LoadValue = LoadValue;
		this.LoadLevel = LoadLevel;
	}
}
