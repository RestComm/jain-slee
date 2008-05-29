package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMediaStreamRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMediaStreamRequest(){}
	public org.csapi.cc.mmccs.TpMediaStreamDirection Direction;
	public org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest DataTypeRequest;
	public org.csapi.cc.TpCallMonitorMode MediaMonitorMode;
	public TpMediaStreamRequest(org.csapi.cc.mmccs.TpMediaStreamDirection Direction, org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest DataTypeRequest, org.csapi.cc.TpCallMonitorMode MediaMonitorMode)
	{
		this.Direction = Direction;
		this.DataTypeRequest = DataTypeRequest;
		this.MediaMonitorMode = MediaMonitorMode;
	}
}
