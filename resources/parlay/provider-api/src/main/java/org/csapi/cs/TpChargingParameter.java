package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpChargingParameter"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameter
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargingParameter(){}
	public int ParameterID;
	public org.csapi.cs.TpChargingParameterValue ParameterValue;
	public TpChargingParameter(int ParameterID, org.csapi.cs.TpChargingParameterValue ParameterValue)
	{
		this.ParameterID = ParameterID;
		this.ParameterValue = ParameterValue;
	}
}
