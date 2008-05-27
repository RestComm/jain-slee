package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of union "TpMediaStreamDataTypeRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDataTypeRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType discriminator;
	private int Audio;
	private int Video;
	private int Data;

	public TpMediaStreamDataTypeRequest ()
	{
	}

	public org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType discriminator ()
	{
		return discriminator;
	}

	public int Audio ()
	{
		if (discriminator != org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_AUDIO_CAPABILITIES)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Audio;
	}

	public void Audio (int _x)
	{
		discriminator = org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_AUDIO_CAPABILITIES;
		Audio = _x;
	}

	public int Video ()
	{
		if (discriminator != org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_VIDEO_CAPABILITIES)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Video;
	}

	public void Video (int _x)
	{
		discriminator = org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_VIDEO_CAPABILITIES;
		Video = _x;
	}

	public int Data ()
	{
		if (discriminator != org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_DATA_CAPABILITIES)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Data;
	}

	public void Data (int _x)
	{
		discriminator = org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_DATA_CAPABILITIES;
		Data = _x;
	}

}
