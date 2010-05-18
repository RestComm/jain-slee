/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.jcc.inap;

import java.io.IOException;
import java.util.Enumeration;
import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccConnectionEvent;

import javax.csapi.cc.jcc.JccEvent;
import org.mobicents.jcc.inap.address.JccCalledPartyBCDNumber;
import org.mobicents.jcc.inap.protocol.CallInformationReport;
import org.mobicents.jcc.inap.protocol.EventReportBCSM;
import org.mobicents.jcc.inap.protocol.InitialDP;
import org.mobicents.jcc.inap.protocol.Operation;

import org.mobicents.jcc.inap.protocol.UnknownOperation;
//import org.mobicents.jcc.inap.protocol.OperationCode;
import org.mobicents.jcc.inap.protocol.parms.BCSMEvent;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyBcdNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.CallingPartyNumber;


import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.DialogIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 *
 * @author Oleg Kulikov
 */
public class TCHandler implements Runnable {

    private JccInapProviderImpl provider = null;
    private SccpAddress calledPartyAddress;
    private SccpAddress callingPartyAddress;
    private byte[] data;
    private DialogIndication message;
	private TCAPProvider tcapProvider;
    private static Logger logger = Logger.getLogger(TCHandler.class);

    /** Creates a new instance of Handler */
    public TCHandler(JccInapProviderImpl provider, TCAPProvider tcapProvider, DialogIndication message) {
        this.provider = provider;
        this.tcapProvider = tcapProvider;
        this.calledPartyAddress = message.getDialog().getLocalAddress();
        this.callingPartyAddress = message.getDialog().getRemoteAddress();
        this.message = message;
    }

    private boolean isOutgoingCall(InitialDP initialDP) {
        return initialDP.getCalledPartyBcdNumber() != null;
    }

    private boolean isOutAllowed(InitialDP initialDP) {
        if (initialDP.getCallingPartyNumber().getAddress().endsWith("9023629581")
                || initialDP.getCallingPartyNumber().getAddress().endsWith("9023802866")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isIncAllowed(InitialDP initialDP) {
        if ((initialDP.getCalledPartyNumber() != null && initialDP.getCalledPartyNumber().getAddress().endsWith("9023629581"))
                || (initialDP.getCalledPartyNumber() != null && initialDP.getCalledPartyNumber().getAddress().endsWith("9023629581"))) {
            return true;
        } else {
            return false;
        }
    }

    public void run() {
        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean existOriginatingConnection(JccCallImpl call, JccAddress callingNumber) {
        JccConnection[] connections = call.getConnections();
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] instanceof OriginatingConnection) {
                String address = connections[i].getAddress().getName();
                return address.equals(callingNumber.getName());
            }
        }
        return false;
    }

    private boolean existTerminatingConnection(JccCallImpl call, JccAddress callingNumber) {
        JccConnection[] connections = call.getConnections();
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] instanceof TerminatingConnection) {
                String address = connections[i].getOriginatingAddress().getName();
                return address.equals(callingNumber.getName());
            }
        }
        return false;
    }

    public void process() {
        
    	Dialog dialog = message.getDialog();
    	long txID = dialog.getDialogId();
    	if(logger.isInfoEnabled())
    	{
    		logger.info("Processing messsage: "+message.getType()+", for dialog: "+dialog);
    	}
        switch (message.getType()) {
            case Begin:
                TCBeginIndication begin = (TCBeginIndication) message;

                Component[] components = begin.getComponents();
                if(components == null || components.length == 0)
                {
                	logger.error("NO COMPONENTS TO PROCESS: TCBegin");
                	return;
                }
                for(Component c:components)
                {
                	if(c.getType() != ComponentType.Invoke)
                	{
                		logger.error("Received non invoke component: "+c.getType()+", skipping!");
                		continue;
                	}
                Invoke invoke = (Invoke) c;
                
                //decode operation and parameter, its not TCAP part !
                org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode oc = invoke.getOperationCode();
                if(invoke.getParameter() == null)
                {
                	logger.error("Received invoke component without parameter, op code: "+oc.getCode()+", skipping!");
            		continue;
                }
                //get data, regardles of type, we get buffer of passed param.
                byte[] buff = invoke.getParameter().getData();
                Operation inapOp = null;
                //its ok, long is general representation 
				switch (oc.getCode().intValue()) {
				case Operation.INITIAL_DP:
					try {
						inapOp = new InitialDP(buff);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Operation.CALL_INFORMATION_REPORT:
					try {
						inapOp = new CallInformationReport(buff);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Operation.EVENT_REPORT_BCSM:
					try {
						inapOp = new EventReportBCSM(buff);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					try {
						inapOp = new UnknownOperation(oc.getCode().intValue(), buff);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
                
                if( !(inapOp instanceof InitialDP))
                {
                	logger.error("Operation is not InitialDP!: "+inapOp);
                	continue;
                }
                InitialDP initialDP = (InitialDP) inapOp;
        
                if (initialDP.getCallingPartyNumber() == null) {
                    logger.warn("txID = " + txID + ", [InitialDP] CallingPartyNumber missing");
                    return;
                }

//                if (!(isIncAllowed(initialDP) || isOutAllowed(initialDP))) {
//                    return;
//                }

                //creating calling party address
                CallingPartyNumber callingPartyNumber = initialDP.getCallingPartyNumber();
                JccAddress callingNumber = provider.createAddress(callingPartyNumber);

                //lookup or creating new call
                JccCallImpl call = provider.calls.containsKey(callingNumber.getName())
                        ? provider.getCall(callingNumber)
                        : provider.createCall(callingNumber);

                ConnectionID connectionID = new ConnectionID(txID, calledPartyAddress, callingPartyAddress);
                JccAddress calledNumber = null;

                if (initialDP.getCalledPartyBcdNumber() != null) {

                    if (existOriginatingConnection(call, callingNumber)) {
                        logger.info("Forcing expired call release: " + call);
                        call.forceRelease();
                        call = provider.createCall(callingNumber);
                    }

                    OriginatingConnection connection = new OriginatingConnection(
                            connectionID, call, callingNumber,this.tcapProvider,dialog);
                    //handle o_connection created event
                    JccEvent evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_CREATED,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    connection.queueEvent(evt);


                    CalledPartyBcdNumber bcdNumber = initialDP.getCalledPartyBcdNumber();
                    calledNumber = provider.createAddress(bcdNumber);

                    evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    ((JccConnectionEventImpl) evt).destAddress = (JccCalledPartyBCDNumber) calledNumber;
                    connection.queueEvent(evt);
                } else if (initialDP.getCalledPartyNumber() != null) {
                    if (existTerminatingConnection(call, callingNumber)) {
                        logger.info("Forcing expired call release: " + call);
                        call.forceRelease();
                        call = provider.createCall(callingNumber);
                    }

                    CalledPartyNumber calledPartyNumber = initialDP.getCalledPartyNumber();
                    calledNumber = provider.createAddress(calledPartyNumber);

                    TerminatingConnection connection = new TerminatingConnection(
                            connectionID, call, calledNumber, callingNumber,this.tcapProvider,dialog);

                    //handle connection created event
                    JccEvent evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_CREATED,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    connection.queueEvent(evt);

                    //handle authorize_call_attempt event
                    evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    connection.queueEvent(evt);
                } else {
                    logger.warn("txID = " + txID + ", [InitialDP] either "
                            + "CalledPartyNumber or CalledPartyBCDNumber missing");
                }
                }
                break;
            case Continue:
                AbstractConnection connection = provider.getConnection(txID);
                if (connection == null) {
                    logger.warn("Unknown connection " + txID);
                    return;
                    //TODO send abort
                }

                TCContinueIndication continueInd = (TCContinueIndication) message;
                components = continueInd.getComponents();

                if (components == null) {
                    logger.warn("One or more components are missing");
                    return;
                }

                
                for(Component c: components){
                	if(c.getType()!=ComponentType.Invoke)
                	{
                		logger.error("Skipping non invoke component: "+c.getType());
                		continue;
                	}
                    Invoke invoke = (Invoke) c;
                    org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode operation = (OperationCode) invoke.getOperationCode();
                    switch (operation.getCode().intValue()) {
                        case Operation.EVENT_REPORT_BCSM:
						EventReportBCSM bcsmEvent;
						try {
							bcsmEvent = new EventReportBCSM(invoke.getParameter().getData());
						
                            switch (bcsmEvent.getEventType()) {
                                case BCSMEvent.O_ANSWER:
                                case BCSMEvent.T_ANSWER:
                                    JccConnectionEvent evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_CONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_NORMAL);
                                    connection.queueEvent(evt);
                                    break;
                                case BCSMEvent.O_DISCONNECT:
                                case BCSMEvent.T_DISCONNECT:
                                    evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_DISCONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_NORMAL);
                                    connection.queueEvent(evt);
                                    break;
                                case BCSMEvent.O_CALLED_PARTY_BUSY:
                                case BCSMEvent.T_BUSY:
                                    evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_DISCONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_BUSY);
                                    connection.queueEvent(evt);
                                    break;
                                case BCSMEvent.O_NO_ANSWER:
                                case BCSMEvent.T_NO_ANSWER:
                                    evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_DISCONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_NO_ANSWER);
                                    connection.queueEvent(evt);
                                    break;
                            }
                            } catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
                            break;
                        default:
                            if (logger.isDebugEnabled()) {
                                logger.debug("Ignoring operation: "+operation.getCode());
                            }
                            break;
                    }
                }
                break;
            case UAbort:
            case PAbort:
                connection = provider.getConnection(txID);
                if (connection == null) {
                    logger.warn("Unknown connection " + txID);
                    return;
                }

                JccConnectionEvent evt = new JccConnectionEventImpl(
                        JccConnectionEvent.CONNECTION_FAILED,
                        connection,
                        JccEvent.CAUSE_CALL_CANCELLED);
                connection.queueEvent(evt);
        }

    }
}
