package org.mobicents.slee.resources.ss7.isup.ratype;

import java.io.IOException;
import java.io.Serializable;
import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;

import javax.slee.resource.StartActivityException;

import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.ParameterException;

public interface RAISUPProvider extends Serializable {
	
	/**
	 * Create client transaction activity
	 * @param arg0
	 * @return
	 * @throws TransactionAlredyExistsException
	 * @throws IllegalArgumentException
	 * @throws ActivityAlreadyExistsException
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 * @throws SLEEException
	 * @throws StartActivityException
	 */
	public CircuitActivity createCircuitActivity(ISUPMessage arg0,int dpc) throws IllegalArgumentException, ActivityAlreadyExistsException, 
		NullPointerException, IllegalStateException, SLEEException,StartActivityException;	

	/**
	 * Get message factory.
	 * @return
	 */
	public ISUPMessageFactory getMessageFactory();
	/**
	 * Get parameter factory.
	 * @return
	 */
	public ISUPParameterFactory getParameterFactory();
	/**
	 * Send message statelesly.
	 * @param arg0
	 * @throws ParameterRangeInvalidException
	 * @throws IOException
	 */
	public void sendMessage(ISUPMessage arg0, int dpc) throws ParameterException, IOException;
	
	/**
	 * Ends circuit activity
	 * @return
	 */
	public void cancelTimer(int cic, int dpc, int timerId);
	
	/**
	 * Notify blocked channel
	 * @return
	 */
	public void notifyBlockedChannel(int cic, int dpc);
	
	/**
	 * Notify reset channel
	 * @return
	 */
	public void notifyResetChannel(int cic, int dpc);
	
	/**
	 * Ends circuit activity
	 * @return
	 */
	public void endActivity(CircuitActivity ac);
	
	/**
	 * Determine if transport layer is connected and links are up.
	 * @return
	 */
	public boolean isTransportUp();
}
