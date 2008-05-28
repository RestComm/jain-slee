package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAdditionalEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.cc.TpCallEventType discriminator;
	private org.csapi.TpAddress CollectedAddress;
	private org.csapi.TpAddress CalledAddress;
	private org.csapi.cc.TpCallServiceCode OriginatingServiceCode;
	private org.csapi.cc.TpReleaseCause OriginatingReleaseCause;
	private org.csapi.cc.TpReleaseCause TerminatingReleaseCause;
	private org.csapi.TpAddress ForwardAddress;
	private org.csapi.cc.TpCallServiceCode TerminatingServiceCode;
	private short Dummy;

	public TpCallAdditionalEventInfo ()
	{
	}

	public org.csapi.cc.TpCallEventType discriminator ()
	{
		return discriminator;
	}

	public org.csapi.TpAddress CollectedAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CollectedAddress;
	}

	public void CollectedAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED;
		CollectedAddress = _x;
	}

	public org.csapi.TpAddress CalledAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_ANALYSED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CalledAddress;
	}

	public void CalledAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_ANALYSED;
		CalledAddress = _x;
	}

	public org.csapi.cc.TpCallServiceCode OriginatingServiceCode ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_SERVICE_CODE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return OriginatingServiceCode;
	}

	public void OriginatingServiceCode (org.csapi.cc.TpCallServiceCode _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_SERVICE_CODE;
		OriginatingServiceCode = _x;
	}

	public org.csapi.cc.TpReleaseCause OriginatingReleaseCause ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_RELEASE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return OriginatingReleaseCause;
	}

	public void OriginatingReleaseCause (org.csapi.cc.TpReleaseCause _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_RELEASE;
		OriginatingReleaseCause = _x;
	}

	public org.csapi.cc.TpReleaseCause TerminatingReleaseCause ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_RELEASE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return TerminatingReleaseCause;
	}

	public void TerminatingReleaseCause (org.csapi.cc.TpReleaseCause _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_RELEASE;
		TerminatingReleaseCause = _x;
	}

	public org.csapi.TpAddress ForwardAddress ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_REDIRECTED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ForwardAddress;
	}

	public void ForwardAddress (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_REDIRECTED;
		ForwardAddress = _x;
	}

	public org.csapi.cc.TpCallServiceCode TerminatingServiceCode ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_SERVICE_CODE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return TerminatingServiceCode;
	}

	public void TerminatingServiceCode (org.csapi.cc.TpCallServiceCode _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_SERVICE_CODE;
		TerminatingServiceCode = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_UNDEFINED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ALERTING && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ANSWER && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_QUEUED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.cc.TpCallEventType.P_CALL_EVENT_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.cc.TpCallEventType _discriminator, short _x)
	{
		if (_discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_UNDEFINED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_CALL_ATTEMPT_AUTHORISED && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ALERTING && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_ANSWER && discriminator != org.csapi.cc.TpCallEventType.P_CALL_EVENT_QUEUED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
