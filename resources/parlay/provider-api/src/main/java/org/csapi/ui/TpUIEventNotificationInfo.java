package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventNotificationInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUIEventNotificationInfo(){}
	public org.csapi.TpAddress OriginatingAddress;
	public org.csapi.TpAddress DestinationAddress;
	public java.lang.String ServiceCode;
	public org.csapi.ui.TpUIEventInfoDataType DataTypeIndication;
	public byte[] UIEventData;
	public TpUIEventNotificationInfo(org.csapi.TpAddress OriginatingAddress, org.csapi.TpAddress DestinationAddress, java.lang.String ServiceCode, org.csapi.ui.TpUIEventInfoDataType DataTypeIndication, byte[] UIEventData)
	{
		this.OriginatingAddress = OriginatingAddress;
		this.DestinationAddress = DestinationAddress;
		this.ServiceCode = ServiceCode;
		this.DataTypeIndication = DataTypeIndication;
		this.UIEventData = UIEventData;
	}
}
