package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpLoadStatisticInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadStatisticInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.fw.TpLoadStatisticInfoType discriminator;
	private org.csapi.fw.TpLoadStatisticData LoadStatisticData;
	private org.csapi.fw.TpLoadStatisticError LoadStatisticError;

	public TpLoadStatisticInfo ()
	{
	}

	public org.csapi.fw.TpLoadStatisticInfoType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.fw.TpLoadStatisticData LoadStatisticData ()
	{
		if (discriminator != org.csapi.fw.TpLoadStatisticInfoType.P_LOAD_STATISTICS_VALID)
			throw new org.omg.CORBA.BAD_OPERATION();
		return LoadStatisticData;
	}

	public void LoadStatisticData (org.csapi.fw.TpLoadStatisticData _x)
	{
		discriminator = org.csapi.fw.TpLoadStatisticInfoType.P_LOAD_STATISTICS_VALID;
		LoadStatisticData = _x;
	}

	public org.csapi.fw.TpLoadStatisticError LoadStatisticError ()
	{
		if (discriminator != org.csapi.fw.TpLoadStatisticInfoType.P_LOAD_STATISTICS_INVALID)
			throw new org.omg.CORBA.BAD_OPERATION();
		return LoadStatisticError;
	}

	public void LoadStatisticError (org.csapi.fw.TpLoadStatisticError _x)
	{
		discriminator = org.csapi.fw.TpLoadStatisticInfoType.P_LOAD_STATISTICS_INVALID;
		LoadStatisticError = _x;
	}

}
