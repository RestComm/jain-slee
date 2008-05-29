package org.csapi.ui;

/**
 *	Generated from IDL definition of union "TpUIInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.ui.TpUIInfoType discriminator;
	private int InfoID;
	private java.lang.String InfoData;
	private java.lang.String InfoAddress;
	private byte[] InfoBinData;
	private java.lang.String InfoUUEncData;
	private byte[] InfoMimeData;
	private byte[] InfoWaveData;
	private byte[] InfoAuData;

	public TpUIInfo ()
	{
	}

	public org.csapi.ui.TpUIInfoType discriminator ()
	{
		return discriminator;
	}

	public int InfoID ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_ID)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoID;
	}

	public void InfoID (int _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_ID;
		InfoID = _x;
	}

	public java.lang.String InfoData ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_DATA)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoData;
	}

	public void InfoData (java.lang.String _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_DATA;
		InfoData = _x;
	}

	public java.lang.String InfoAddress ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_ADDRESS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoAddress;
	}

	public void InfoAddress (java.lang.String _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_ADDRESS;
		InfoAddress = _x;
	}

	public byte[] InfoBinData ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_BIN_DATA)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoBinData;
	}

	public void InfoBinData (byte[] _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_BIN_DATA;
		InfoBinData = _x;
	}

	public java.lang.String InfoUUEncData ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_UUENCODED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoUUEncData;
	}

	public void InfoUUEncData (java.lang.String _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_UUENCODED;
		InfoUUEncData = _x;
	}

	public byte[] InfoMimeData ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_MIME)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoMimeData;
	}

	public void InfoMimeData (byte[] _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_MIME;
		InfoMimeData = _x;
	}

	public byte[] InfoWaveData ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_WAVE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoWaveData;
	}

	public void InfoWaveData (byte[] _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_WAVE;
		InfoWaveData = _x;
	}

	public byte[] InfoAuData ()
	{
		if (discriminator != org.csapi.ui.TpUIInfoType.P_UI_INFO_AU)
			throw new org.omg.CORBA.BAD_OPERATION();
		return InfoAuData;
	}

	public void InfoAuData (byte[] _x)
	{
		discriminator = org.csapi.ui.TpUIInfoType.P_UI_INFO_AU;
		InfoAuData = _x;
	}

}
