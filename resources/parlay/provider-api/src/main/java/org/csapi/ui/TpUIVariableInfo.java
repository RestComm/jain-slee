package org.csapi.ui;

/**
 *	Generated from IDL definition of union "TpUIVariableInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariableInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.ui.TpUIVariablePartType discriminator;
	private int VariablePartInteger;
	private java.lang.String VariablePartAddress;
	private java.lang.String VariablePartTime;
	private java.lang.String VariablePartDate;
	private java.lang.String VariablePartPrice;

	public TpUIVariableInfo ()
	{
	}

	public org.csapi.ui.TpUIVariablePartType discriminator ()
	{
		return discriminator;
	}

	public int VariablePartInteger ()
	{
		if (discriminator != org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_INT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return VariablePartInteger;
	}

	public void VariablePartInteger (int _x)
	{
		discriminator = org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_INT;
		VariablePartInteger = _x;
	}

	public java.lang.String VariablePartAddress ()
	{
		if (discriminator != org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return VariablePartAddress;
	}

	public void VariablePartAddress (java.lang.String _x)
	{
		discriminator = org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_ADDRESS;
		VariablePartAddress = _x;
	}

	public java.lang.String VariablePartTime ()
	{
		if (discriminator != org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_TIME)
			throw new org.omg.CORBA.BAD_OPERATION();
		return VariablePartTime;
	}

	public void VariablePartTime (java.lang.String _x)
	{
		discriminator = org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_TIME;
		VariablePartTime = _x;
	}

	public java.lang.String VariablePartDate ()
	{
		if (discriminator != org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_DATE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return VariablePartDate;
	}

	public void VariablePartDate (java.lang.String _x)
	{
		discriminator = org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_DATE;
		VariablePartDate = _x;
	}

	public java.lang.String VariablePartPrice ()
	{
		if (discriminator != org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_PRICE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return VariablePartPrice;
	}

	public void VariablePartPrice (java.lang.String _x)
	{
		discriminator = org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_PRICE;
		VariablePartPrice = _x;
	}

}
