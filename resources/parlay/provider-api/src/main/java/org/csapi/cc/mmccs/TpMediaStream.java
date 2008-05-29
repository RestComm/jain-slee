package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMediaStream"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStream
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMediaStream(){}
	public org.csapi.cc.mmccs.TpMediaStreamDirection Direction;
	public org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest DataType;
	public int ChannelSessionID;
	public org.csapi.cc.mmccs.IpMultiMediaStream MediaStream;
	public TpMediaStream(org.csapi.cc.mmccs.TpMediaStreamDirection Direction, org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest DataType, int ChannelSessionID, org.csapi.cc.mmccs.IpMultiMediaStream MediaStream)
	{
		this.Direction = Direction;
		this.DataType = DataType;
		this.ChannelSessionID = ChannelSessionID;
		this.MediaStream = MediaStream;
	}
}
