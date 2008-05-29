package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCarrier"
 *	@author JacORB IDL compiler 
 */

public final class TpCarrier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCarrier(){}
	public byte[] CarrierID;
	public org.csapi.cc.TpCarrierSelectionField CarrierSelectionField;
	public TpCarrier(byte[] CarrierID, org.csapi.cc.TpCarrierSelectionField CarrierSelectionField)
	{
		this.CarrierID = CarrierID;
		this.CarrierSelectionField = CarrierSelectionField;
	}
}
