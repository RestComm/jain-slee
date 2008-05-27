package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallBearerService"
 *	@author JacORB IDL compiler 
 */

public final class TpCallBearerService
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_CALL_BEARER_SERVICE_UNKNOWN = 0;
	public static final TpCallBearerService P_CALL_BEARER_SERVICE_UNKNOWN = new TpCallBearerService(_P_CALL_BEARER_SERVICE_UNKNOWN);
	public static final int _P_CALL_BEARER_SERVICE_SPEECH = 1;
	public static final TpCallBearerService P_CALL_BEARER_SERVICE_SPEECH = new TpCallBearerService(_P_CALL_BEARER_SERVICE_SPEECH);
	public static final int _P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED = 2;
	public static final TpCallBearerService P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED = new TpCallBearerService(_P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED);
	public static final int _P_CALL_BEARER_SERVICE_DIGITALRESTRICTED = 3;
	public static final TpCallBearerService P_CALL_BEARER_SERVICE_DIGITALRESTRICTED = new TpCallBearerService(_P_CALL_BEARER_SERVICE_DIGITALRESTRICTED);
	public static final int _P_CALL_BEARER_SERVICE_AUDIO = 4;
	public static final TpCallBearerService P_CALL_BEARER_SERVICE_AUDIO = new TpCallBearerService(_P_CALL_BEARER_SERVICE_AUDIO);
	public static final int _P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES = 5;
	public static final TpCallBearerService P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES = new TpCallBearerService(_P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES);
	public static final int _P_CALL_BEARER_SERVICE_VIDEO = 6;
	public static final TpCallBearerService P_CALL_BEARER_SERVICE_VIDEO = new TpCallBearerService(_P_CALL_BEARER_SERVICE_VIDEO);
	public int value()
	{
		return value;
	}
	public static TpCallBearerService from_int(int value)
	{
		switch (value) {
			case _P_CALL_BEARER_SERVICE_UNKNOWN: return P_CALL_BEARER_SERVICE_UNKNOWN;
			case _P_CALL_BEARER_SERVICE_SPEECH: return P_CALL_BEARER_SERVICE_SPEECH;
			case _P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED: return P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED;
			case _P_CALL_BEARER_SERVICE_DIGITALRESTRICTED: return P_CALL_BEARER_SERVICE_DIGITALRESTRICTED;
			case _P_CALL_BEARER_SERVICE_AUDIO: return P_CALL_BEARER_SERVICE_AUDIO;
			case _P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES: return P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES;
			case _P_CALL_BEARER_SERVICE_VIDEO: return P_CALL_BEARER_SERVICE_VIDEO;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpCallBearerService(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
