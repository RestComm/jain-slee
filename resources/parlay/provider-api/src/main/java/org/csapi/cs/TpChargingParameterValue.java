package org.csapi.cs;

/**
 *	Generated from IDL definition of union "TpChargingParameterValue"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterValue
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cs.TpChargingParameterValueType discriminator;
	private int IntValue;
	private float FloatValue;
	private java.lang.String StringValue;
	private boolean BooleanValue;
	private byte[] OctetValue;

	public TpChargingParameterValue ()
	{
	}

	public org.csapi.cs.TpChargingParameterValueType discriminator ()
	{
		return discriminator;
	}

	public int IntValue ()
	{
		if (discriminator != org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_INT32)
			throw new org.omg.CORBA.BAD_OPERATION();
		return IntValue;
	}

	public void IntValue (int _x)
	{
		discriminator = org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_INT32;
		IntValue = _x;
	}

	public float FloatValue ()
	{
		if (discriminator != org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_FLOAT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return FloatValue;
	}

	public void FloatValue (float _x)
	{
		discriminator = org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_FLOAT;
		FloatValue = _x;
	}

	public java.lang.String StringValue ()
	{
		if (discriminator != org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_STRING)
			throw new org.omg.CORBA.BAD_OPERATION();
		return StringValue;
	}

	public void StringValue (java.lang.String _x)
	{
		discriminator = org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_STRING;
		StringValue = _x;
	}

	public boolean BooleanValue ()
	{
		if (discriminator != org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_BOOLEAN)
			throw new org.omg.CORBA.BAD_OPERATION();
		return BooleanValue;
	}

	public void BooleanValue (boolean _x)
	{
		discriminator = org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_BOOLEAN;
		BooleanValue = _x;
	}

	public byte[] OctetValue ()
	{
		if (discriminator != org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_OCTETSET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return OctetValue;
	}

	public void OctetValue (byte[] _x)
	{
		discriminator = org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_OCTETSET;
		OctetValue = _x;
	}

}
