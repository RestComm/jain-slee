package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUIEventInfo(){}
	public org.csapi.TpAddress OriginatingAddress;
	public org.csapi.TpAddress DestinationAddress;
	public java.lang.String ServiceCode;
	public org.csapi.ui.TpUIEventInfoDataType DataTypeIndication;
	public java.lang.String DataString;
	public TpUIEventInfo(org.csapi.TpAddress OriginatingAddress, org.csapi.TpAddress DestinationAddress, java.lang.String ServiceCode, org.csapi.ui.TpUIEventInfoDataType DataTypeIndication, java.lang.String DataString)
	{
		this.OriginatingAddress = OriginatingAddress;
		this.DestinationAddress = DestinationAddress;
		this.ServiceCode = ServiceCode;
		this.DataTypeIndication = DataTypeIndication;
		this.DataString = DataString;
	}
}
