package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.header.Via;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.AddressFactory;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.StartActivityException;
import javax.slee.transaction.SleeTransaction;
import javax.slee.transaction.SleeTransactionManager;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip11.wrappers.ACKDummyTransaction;
import org.mobicents.slee.resource.sip11.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.RequestEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ResponseEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.TimeoutEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.TransactionTerminatedEventWrapper;
import org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface;

public class SipResourceAdaptor implements SipListener, ResourceAdaptor {

	static private Logger log = Logger
			.getLogger(SipResourceAdaptor.class);

	// Config Properties Names -------------------------------------------

	private static final String SIP_BIND_ADDRESS = "javax.sip.IP_ADDRESS";

	private static final String SIP_PORT_BIND = "javax.sip.PORT";

	private static final String TRANSPORTS_BIND = "javax.sip.TRANSPORT";

	private static final String STACK_NAME_BIND = "javax.sip.STACK_NAME";

	// Config Properties Values -------------------------------------------
	
	private int port;

	private Set<String> transports = new HashSet<String>();
	private String transportsProperty;

	private String stackName;
	
	private String stackAddress;

	/**
	 * allowed transports
	 */
	private Set<String> allowedTransports = new HashSet<String>();
	
	
	private SipProvider provider;

	private SleeSipProviderImpl providerProxy;
	
	// SIP Methods related -------------------------------------

	// METHODS - here we store methods which are rfc 3261 compilant
	private static Set rfc3261Methods = new HashSet();
	private static Set<String> stxedRequests = new HashSet<String>();
	// SOME INITIALIZATION
	static {
		String[] tmp = { Request.ACK, Request.BYE, Request.CANCEL,
				Request.INFO, Request.INVITE, Request.MESSAGE, Request.NOTIFY,
				Request.OPTIONS, Request.PRACK, Request.PUBLISH, Request.REFER,
				Request.REGISTER, Request.SUBSCRIBE, Request.UPDATE };
		for (int i = 0; i < tmp.length; i++)
			rfc3261Methods.add(tmp[i]);

		log.info("\n================SIP METHODS====================\n"
				+ rfc3261Methods
				+ "\n===============================================");

		stxedRequests.add(Request.ACK);
		stxedRequests.add(Request.CANCEL);
		stxedRequests.add(Request.BYE);
	}

	// SLEE Related Props --------------------------------------

	// Activity related ====================
	
	/**
	 * Holds mapping between fromTag+CallId to activity handle This amppping is
	 * arbitrary since from/to tags are exchangable between requests - in
	 * meaning, depending on which side makes request it puts its local tag into
	 * from header. However this isnt big pain as this is used to swallow late
	 * 2xx for UAC dialog forking and possiblt retransmissions
	 */
	private Map<String, SipActivityHandle> fromTagCallId2Handle = null;
	
	private Map<SipActivityHandle,WrapperSuperInterface> activities = null;

	/**
	 * caches the eventIDs, avoiding lookup in container
	 */
	public final EventIDCache eventIdCache = new EventIDCache();

	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private final EventIDFilter eventIDFilter = new EventIDFilter();

	/**
	 * 
	 */
	private ResourceAdaptorContext raContext;
	
	/**
	 * 
	 */
	private SipStack sipStack = null;

	/**
	 * 
	 */
	private SipFactory sipFactory = null;

	/**
	 * for all events we are interested in knowing when the event failed to be processed
	 */
	private static final int EVENT_FLAGS = getEventFlags();
	private static int getEventFlags() {
		int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
		EventFlags.setRequestProcessingFailedCallback(eventFlags);
		return eventFlags;
	}
		
	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;
	
	public SipResourceAdaptor() {
		// Those values are defualt
		this.port = 5060;
		// this.transport = "udp";
		allowedTransports.add("udp");
		allowedTransports.add("tcp");
		transports.add("udp");
		// this.stackAddress = "127.0.0.1";

		// this.stackPrefix = "gov.nist";
	}

	// XXX -- SipListenerMethods - here we process incoming data

	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void processRequest(RequestEvent req) {

		if (log.isInfoEnabled()) {
		 log.info("Received Request:\n"+req.getRequest());
		}

		ServerTransaction st = req.getServerTransaction();
		ServerTransactionWrapper stw = null;
		// CANCEL, ACK and BYE have Transaction created for them

		if (st == null || st.getApplicationData() == null) {
			try {
				if (req.getDialog() == null && req.getRequest().getMethod().equals(Request.ACK)) {
					// create fake ack server transaction
					st = new ACKDummyTransaction(req.getRequest());
				}
				stw = (ServerTransactionWrapper) this.providerProxy
						.getNewServerTransaction(req.getRequest(), st, false);
				st = (ServerTransaction) stw.getWrappedTransaction();

				if (log.isDebugEnabled()) {
					log
							.debug("\n----------------- CREATED NEW STx ---------------------\nBRANCH: "
									+ st.getBranchId()
									+ "\n-------------------------------------------------------");
				}

			} 
			catch (TransactionAlreadyExistsException e) {
				if (log.isDebugEnabled()) {
					log.debug(
				
						"Request where the server tx already exists, should be a retransmission and will be dropped. Request: \n"
								+ req.getRequest()
								+ "\n-------------------------", e);
				}
				return;
			}
			catch (Exception e) {
				log.error(
						"\n-------------------------\nREQUEST:\n-------------------------\n"
								+ req.getRequest()
								+ "\n-------------------------", e);
				sendErrorResponse(st, req.getRequest(),
						Response.SERVER_INTERNAL_ERROR, e.getMessage());
				return;
			}
		} else {
			// TODO:add error send response on type cast exctption

			stw = (ServerTransactionWrapper) st.getApplicationData();
		}

		if (req.getRequest().getMethod().equals(Request.CANCEL)) {
			processCancelRequest(st, stw, req);
		} else {
			processNotCancelRequest(st, stw, req);
		}

	}

	private void processCancelRequest(ServerTransaction st,
			ServerTransactionWrapper STW, RequestEvent req) {
		SipActivityHandle inviteHandle = STW.getInviteHandle();
	
		ServerTransactionWrapper inviteSTW = (ServerTransactionWrapper) getActivity(inviteHandle);
		CancelRequestEvent REW = null;
		boolean inDialog = false;
		SipActivityHandle SAH = null;
		// FIXME: There is no mentioen about state in specs...
		if (inviteSTW != null) {
			 if (log.isDebugEnabled()) {
				 log.debug("Found INVITE transaction CANCEL[" + STW + "] \nINVITE["
					+ inviteSTW + "]");
			 }

			if ((inviteSTW.getState() == TransactionState.TERMINATED)
					|| (inviteSTW.getState() == TransactionState.COMPLETED)
					|| (inviteSTW.getState() == TransactionState.CONFIRMED)) {

				log
						.error("Invite transaction has been found in state other than proceeding("+inviteSTW.getState()+"), final response sent, sending BAD_REQUEST");

				// FINAL
				// FINAL RESPONSE
				// HAS
				// BEEN
				// SENT?
				Response response;
				try {
					response = this.providerProxy.getMessageFactory()
							.createResponse(Response.BAD_REQUEST,
									req.getRequest());
					STW.sendResponse(response);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			}

			if (inviteSTW.getDialog() != null) {
				 if (log.isDebugEnabled()) {
					 log.debug("Found DIALOG transaction CANCEL["
								+ STW
								+ "]\nINVITE["
								+ inviteSTW
								+ "]\nDialog["
								+ inviteSTW.getDialog()
								+ "]\nSEQUENCE:Send200ToCANCEL,FireEventOnDialog,Send487ToInvite");
				 }
				SAH = ((DialogWrapper) inviteSTW.getDialog())
						.getActivityHandle();
				inDialog = true;

				REW = new CancelRequestEvent(this.providerProxy, STW,
						inviteSTW, inviteSTW != null ? inviteSTW.getDialog()
								: null, req.getRequest());
				// RequestEventWrapper REW = new RequestEventWrapper(
				// this.providerProxy, STW, inviteSTW != null ? inviteSTW
				// .getDialog() : null, req.getRequest());

			} else {
				 if (log.isDebugEnabled()) {
					 log.debug("DIALOG not found transaction CANCEL[" + STW
						+ "]\nINVITE[" + inviteSTW + "]\nDialog["
						+ inviteSTW.getDialog()
						+ "]\nSEQUENCE:FireEventOnInvite");
				 }
				SAH = inviteSTW.getActivityHandle();
				inDialog = false;
				REW = new CancelRequestEvent(this.providerProxy, STW,
						inviteSTW, inviteSTW != null ? inviteSTW.getDialog()
								: null, req.getRequest());

			}

		} else {
			 if (log.isDebugEnabled()) {
				 log.debug("INVITE not found transaction CANCEL[" + STW
					+ "]\nSEQUENCE:FireEventOnCancel");
			 }
			SAH = STW.getActivityHandle();
			inDialog = false;

			REW = new CancelRequestEvent(this.providerProxy, STW, inviteSTW,
					inviteSTW != null ? inviteSTW.getDialog() : null, req
							.getRequest());

		}

		FireableEventType eventID = eventIdCache.getEventId(raContext.getEventLookupFacility(), REW.getRequest(),
				inDialog);
		
		fireEvent(REW, SAH, eventID, new Address(AddressPlan.SIP,
				((ToHeader) req.getRequest().getHeader(ToHeader.NAME))
						.getAddress().toString()),false);

	}

	private void processNotCancelRequest(ServerTransaction st,
			ServerTransactionWrapper STW, RequestEvent req) {
		// WE HAVE SET UP ALL WHAT WE NEED, NOW DO SOMETHING

		SipActivityHandle SAH = null;

		DialogWrapper DW = null;
		boolean inDialog = false;

		final SleeTransactionManager txManager = raContext.getSleeTransactionManager();

		boolean terminateTx = false;
		try {
			txManager.begin();
			terminateTx = true;

			if (st.getDialog() != null) {

				// TODO: add check for fork?

				Dialog d = st.getDialog();

				if (d.getApplicationData() != null
						&& d.getApplicationData() instanceof DialogActivity) {
					DW = (DialogWrapper) d.getApplicationData();
					inDialog = true;
					SAH = DW.getActivityHandle();
					DW.addOngoingTransaction(STW);
				} else {
					if (log.isDebugEnabled()) {
						log
						.debug("Dialog ["
								+ d
								+ "] exists, but no wrapper is present. Delivering event on TX");
					}
					inDialog = false;
					SAH = STW.getActivityHandle();
				}

			} else {
				// This means that ST is activity
				SAH = STW.getActivityHandle();
				if (activities.get(SAH) == null) {
					if (!addActivity(SAH, STW)) {
						sendErrorResponse(req.getServerTransaction(), req.getRequest(),
								Response.SERVER_INTERNAL_ERROR,
						"Failed to deliver request event to JAIN SLEE container");
						terminateTx = false;
						txManager.rollback();
						return;
					}
				}
			}

			RequestEventWrapper REW = new RequestEventWrapper(this.providerProxy,
					STW, DW, req.getRequest());

			if (!fireEvent(REW, SAH, eventIdCache.getEventId(raContext.getEventLookupFacility(), REW
					.getRequest(), inDialog), new Address(AddressPlan.SIP,
							((ToHeader) req.getRequest().getHeader(ToHeader.NAME))
							.getAddress().toString()),true)) {
				if (!inDialog) {
					// since the activity would be created only in slee with a sucessfull
					// event firing then remove it from the map (was added by provider) here
					activities.remove(STW.getActivityHandle());
					STW.cleanup();
				}
				sendErrorResponse(req.getServerTransaction(), req.getRequest(),
						Response.SERVER_INTERNAL_ERROR,
				"Failed to deliver request event to JAIN SLEE container");
				terminateTx = false;
				txManager.rollback();
				return;
			}
			else if (!inDialog && STW.getWrappedTransaction() instanceof ACKDummyTransaction) {
				processTransactionTerminated(new TransactionTerminatedEventWrapper(this.providerProxy,
						(ServerTransaction) STW.getWrappedTransaction()));
			}
			terminateTx = false;
			txManager.commit();			
		}
		catch (Throwable e) {
			log.error(e.getMessage(),e);
			if (terminateTx) {
				try {
					txManager.rollback();
				}
				catch (Throwable f) {
					log.error(f.getMessage(),f);
				}
			}
		}
		
	}

	public void processResponse(ResponseEvent resp) {
		
		try{
		if (log.isInfoEnabled()) {
			log.info("Received Response:\n"+resp.getResponse());
		}

		ClientTransaction ct = resp.getClientTransaction();
		if (ct == null) {
			processLateResponse(resp);
			return;
		}

		final int statusCode = resp.getResponse().getStatusCode();
		final String method = ((CSeqHeader) resp.getResponse().getHeader(
				CSeqHeader.NAME)).getMethod();
		if (ct.getApplicationData() == null
				|| !(ct.getApplicationData() instanceof ClientTransactionWrapper)) {
			// ERROR?
			log.error("Received app data[" + ct.getApplicationData()
					+ "] - should be instance of wrapper class!!");
			// TODO: Send SERVER_ERROR ?
			return;
		}

		// WE HAVE SET UP ALL WHAT WE NEED, NOW DO SOMETHING
		// ComponentKey eventKey = null;
		SipActivityHandle SAH = null;
		ClientTransactionWrapper CTW = (ClientTransactionWrapper) ct.getApplicationData();
		DialogWrapper DW = null;
		boolean inDialog = false;

		if (ct.getDialog() != null) {

			Dialog d = ct.getDialog();

			if (d.getApplicationData() != null && d.getApplicationData() instanceof DialogActivity) {
				DW = (DialogWrapper) d.getApplicationData();
				SAH = DW.getActivityHandle();
				inDialog = true;

			} else {
				if (log.isDebugEnabled()) {
					log.debug("Dialog [" + d + "] exists, but no wrapper is present. Delivering event on TX");
				}
				inDialog = false;
				SAH = CTW.getActivityHandle();

			}

		} else {

			SAH = CTW.getActivityHandle();

		}

		//DW.processIncomingResponse - it fires messages in some special casses.
		if (inDialog && DW.processIncomingResponse(resp)) {

		} else {

			ResponseEventWrapper REW = new ResponseEventWrapper(this.providerProxy, CTW, DW, resp.getResponse());
			FireableEventType eventID = eventIdCache.getEventId(raContext.getEventLookupFacility(), REW.getResponse());

			fireEvent(REW, SAH, eventID, new Address(AddressPlan.SIP, ((FromHeader) resp.getResponse().getHeader(FromHeader.NAME)).getAddress().toString()),false);
		}
		if ((statusCode == 481 || statusCode == 408) && inDialog && (!method.equals(Request.INVITE) || !method.equals(Request.SUBSCRIBE))) {

			try {
				Request bye = DW.createRequest(Request.BYE);
				this.provider.sendRequest(bye);
			} catch (SipException e) {

				e.printStackTrace();
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
		//if (statusCode > 299 && DW.getState() != DialogState.CONFIRMED) {
		//	try {
		//
		//		DW.delete();

		//	} catch (Exception e) {
		//		e.printStackTrace();
		//	}
		//}

	}

	private void processLateResponse(ResponseEvent resp)
	{
	
		
	
			int _statusCode = resp.getResponse().getStatusCode();
			String callId, _method, branchId;
			callId = ((CallID) resp.getResponse().getHeader(CallID.NAME)).getCallId();
			_method = ((CSeq) resp.getResponse().getHeader(CSeq.NAME)).getMethod();
			branchId = ((Via) resp.getResponse().getHeaders(Via.NAME).next()).getBranch();
			String toTag = ((ToHeader) resp.getResponse().getHeader(ToHeader.NAME)).getTag();
			ClientTransaction ct = resp.getClientTransaction();
			if (log.isInfoEnabled()) {
				log.info("ClientTransaction is null posible late 2xx. ToTag[" + toTag + "] Dialog[" + resp.getDialog() + "] CALLID[" + callId + "] BRANCH[" + branchId
						+ "] METHOD[" + _method + "] CODE[" + _statusCode + "]");
			}

			if ((DialogWrapper.dialogCreatingMethods.contains(_method))) {

				SipActivityHandle forkMasterDialogHandle = getMasterHandleForCall(resp.getResponse());
				if (forkMasterDialogHandle != null) {
					DialogWrapper dw = (DialogWrapper) getActivity(forkMasterDialogHandle);
					if (dw != null) {
						dw.processIncomingResponse(resp);
						if (log.isDebugEnabled()) {
							log.debug("Message was late 2xx, dialog wrapper processed it. CALLID[" + callId + "] BRANCH[" + branchId + "] METHOD[" + _method + "] CODE["
									+ _statusCode + "]");
						}

					} else {
						if (log.isDebugEnabled()) {
							log.debug("No master dialog wrapper, using default. CALLID[" + callId + "] BRANCH[" + branchId + "] METHOD[" + _method + "] CODE[" + _statusCode
									+ "]");
						}
						new DialogWrapper(providerProxy, this).doTerminateOnLate2xx(resp);

					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("No Handle for dialog with such from and callId, using default. CALLID[" + callId + "] BRANCH[" + branchId + "] METHOD[" + _method
								+ "] CODE[" + _statusCode + "]");
					}
					new DialogWrapper(providerProxy, this).doTerminateOnLate2xx(resp);
				}

			} else {
				if (log.isDebugEnabled()) {
					log.debug("===> ClientTransaction is NULL, along with dialog - RTR ? CALLID[" + callId + "] BRANCH[" + branchId + "] METHOD[" + _method + "] CODE["
							+ _statusCode + "]");
				}

				// FIXME:, add default termiantion of late 2xx??
			}

			return;
		
	}
	
	public void processTimeout(TimeoutEvent arg0) {

		Transaction t = null;

		boolean dialogPresent = false;
		SipActivityHandle handleToFire = null;
		TimeoutEventWrapper tew = null;
		//int eventID = -1;
		String address = null;
		boolean isServerTransaction = false;
		DialogWrapper dw = null;
		String method = null;
		try {

			if (log.isInfoEnabled()) {
				if (arg0.isServerTransaction()) {
					log.info("Server transaction "
							+ arg0.getServerTransaction().getBranchId()
							+ " timer expired");
				} else {
					log.info("Client transaction "
							+ arg0.getClientTransaction().getBranchId()
							+ " timer expired");
				}
			}

			if (!arg0.isServerTransaction()) {

				t = arg0.getClientTransaction();

				if (t.getApplicationData() == null
						|| !(t.getApplicationData() instanceof ClientTransactionWrapper)) {
					log
							.error("FAILURE on processTimeout - CTX. Wrong app data["
									+ t.getApplicationData() + "]");
					return;
				} else {
					tew = new TimeoutEventWrapper(this.providerProxy,
							(ClientTransaction) t.getApplicationData(), arg0
									.getTimeout());
					ClientTransactionWrapper ctw = (ClientTransactionWrapper) t
							.getApplicationData();
					// method = ctw.getRequest().getMethod();
					if (ctw.getDialog() != null
							&& ctw.getDialog() instanceof DialogWrapper) {
						dialogPresent = true;
						// handleToFire = ((DialogWrapper)
						// ctw.getDialog()).getActivityHandle();
						dw = (DialogWrapper) ctw.getDialog();
					} else {
						handleToFire = ctw.getActivityHandle();
					}

				}

			} else {
				isServerTransaction = true;
				t = arg0.getServerTransaction();
				ServerTransactionWrapper stw = (ServerTransactionWrapper) t
						.getApplicationData();
				if (stw == null) {
					log
							.error("FAILURE on processTimeout - STX. Wrong app data["
									+ t.getApplicationData() + "]");
					return;
				}
				// method = stw.getRequest().getMethod();
				if ((stw.getDialog()) != null
						&& stw.getDialog() instanceof DialogWrapper) {
					dialogPresent = true;
					// handleToFire = ((DialogWrapper)
					// stw.getDialog()).getActivityHandle();
					dw = (DialogWrapper) stw.getDialog();
				}
			}

			if (dialogPresent) {
				if (!isServerTransaction) {
					handleToFire = dw.getActivityHandle();
				}

			}
			method = t.getRequest().getMethod();
			address = ((FromHeader) t.getRequest().getHeader(FromHeader.NAME))
					.getAddress().toString();
			
		} catch(RuntimeException re){
			re.printStackTrace();
			throw re;
		}finally {

			// NOTE: Dialog gets terminated if transaction is dialog creating
			if (handleToFire != null)
				fireEvent(tew, handleToFire, eventIdCache.getTransactionTimeoutEventId(
						raContext.getEventLookupFacility(), dialogPresent), new Address(
						AddressPlan.SIP, address),false);
			//Stack will fire TXTerminated
			//if (wsi != null)
			//	sendActivityEndEvent(wsi.getActivityHandle());
			if (method!=null && method.equals(Request.BYE) && dw != null) {
				dw.delete();
			}
		}
	}

	public void processTransactionTerminated(
			TransactionTerminatedEvent txTerminatedEvent) {

		Transaction t = null;
		if (!txTerminatedEvent.isServerTransaction()) {
			t = txTerminatedEvent.getClientTransaction();
		} else {
			t = txTerminatedEvent.getServerTransaction();
		}
		if (log.isInfoEnabled()) {
			log.info("SIP Transaction "+t.getBranchId()+" terminated");
		}
		
		//HACK FOR ACK.......
		if(t.getApplicationData()==null && t.getRequest().getMethod().equals(Request.ACK))
		{
			SipActivityHandle _sah=new SipActivityHandle(t.getBranchId()+"_"+Request.ACK);
			t.setApplicationData(activities.get(_sah));
		}
		if (t.getApplicationData() != null) {

			WrapperSuperInterface wsi = (WrapperSuperInterface) t
					.getApplicationData();
			DialogWrapper dw = null;
			if (t.getDialog() != null
					&& t.getDialog().getApplicationData() instanceof DialogWrapper) {
				dw = (DialogWrapper) t.getDialog().getApplicationData();
				if (wsi instanceof ServerTransactionWrapper) {
					dw.removeOngoingTransaction((ServerTransactionWrapper) wsi);
				} else if (wsi instanceof ClientTransactionWrapper) {
					dw.removeOngoingTransaction((ClientTransactionWrapper) wsi);
				} else {
					log
							.error("Unknown type "
									+ wsi.getClass()
									+ " of SIP Transaction, can't remove from dialog wrapper");
				}
			}

			if (!this.sendActivityEndEvent(wsi.getActivityHandle())) {
				// not a tx activity, cleanup references now
				wsi.cleanup();
			}
		} else {
			if (log.isInfoEnabled()) {
				log
						.info("TransactionTerminatedEvent dropped. There is no activity for transaction = "
								+ t.getBranchId()
								+ " , request method = "
								+ t.getRequest().getMethod());
			}
		}
	}

	public void processDialogTerminated(DialogTerminatedEvent dte) {

		DialogWrapper dw = null;

		if (dte.getDialog() instanceof DialogWrapper) {
			dw = (DialogWrapper) dte.getDialog();
		} else if (dte.getDialog().getApplicationData() != null) {
			dw = (DialogWrapper) dte.getDialog().getApplicationData();
		}
		if (log.isInfoEnabled()) {
			if (dw != null)
				log.info("SIP Dialog " + dw.getActivityHandle() + " terminated");
		}

		if (dw != null) {
			if (dw.getActivityHandle() == null) {
				log.error(" FAILED: CLEANED DIALOG:" + dw);
			}
			this.sendActivityEndEvent(dw.getActivityHandle());
		} else {
			if (log.isDebugEnabled()) {
				log.debug("DialogTerminatedEvent droping due to null app data.");
			}
		}

	}


	// *************** Event Life cycle

	/**
	 * 
	 */
	public boolean sendActivityEndEvent(SipActivityHandle ah) {
		try {
			if (this.activities.containsKey(ah)) {
				this.raContext.getSleeEndpoint().endActivity(ah);
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			//This could be called when TX times out, we could call this twice, but we dont
		}
		return false;
	}
	
	public boolean addActivity(SipActivityHandle sah,
			WrapperSuperInterface wrapperActivity) {

		if (log.isDebugEnabled()) {
			log.debug("Adding sip activity handle " + sah);
		}

		try {
			raContext.getSleeEndpoint().startActivityTransacted(sah,wrapperActivity,ACTIVITY_FLAGS);
		}
		catch (Throwable e) {
			log.error(e.getMessage(),e);
			return false;
		}
		activities.put(sah, wrapperActivity);
		return true;
	}

	public void removeActivity(SipActivityHandle sah) {

		if (log.isDebugEnabled()) {
			log.debug("Removing sip activity handle " + sah);
		}
		this.activities.remove(sah);
	}

	// ------- END OF PROVISIONING

	// *********************** DEBUG PART]
	/*
	private Timer debugTimer = new Timer();

	private org.apache.log4j.Logger debugLogger = org.apache.log4j.Logger.getLogger("org.mobicents.slee.resource.sip.DEBUG");

	private HashMap receivedEvents = new HashMap();
	private ArrayList orderOfEvent = new ArrayList();
	private HashMap timeStamps = new HashMap();

	private class EventTimerTask extends TimerTask {

		int runCount = 0;

		@Override
		public void run() {

			debugLogger.info("-------------------- DEUMP RUN[" + runCount + "]-----------------");
			debugLogger.info("[" + runCount + "] ACTIVITIES DUMP");
			TreeMap ac = new TreeMap(activities);
			int count = 0;
			for (Object key : ac.keySet()) {
				debugLogger.info("[" + runCount + "] AC[" + count++

				+ "] KEY[" + key + "] A[" + ac.get(key) + "]");
			}
			debugLogger.info("[" + runCount + "] --- EVENTS RECEVIED");
			ArrayList orderCopy = new ArrayList(orderOfEvent);
			count = 0;
			for (Object event : orderCopy) {
				debugLogger.info("[" + runCount + "] EVENT[" + count++ + "] E[" + event + "] STAMP[" + timeStamps.get(event) + "] A[" + receivedEvents.get(event) + "]");
			}

			debugLogger.info("----- "+Arrays.toString(fromTagCallId2Handle.keySet().toArray())+" -----");
			
			debugLogger.info("[" + runCount + "]  ================================================");

			runCount++;
		}
	}
	
	private void initDebug() {
		debugTimer.scheduleAtFixedRate(new EventTimerTask(), 5000, 5000);
	}

	private void tearDownDebug() {
		debugTimer.cancel();
	}
	*/
	public boolean fireEvent(Object event, ActivityHandle handle, FireableEventType eventID, Address address, boolean useFiltering, boolean transacted) {

		if (useFiltering && eventIDFilter.filterEvent(eventID)) {
			if (log.isDebugEnabled()) {
				log.debug("Event " + eventID + " filtered");
			}
		} else if (eventID == null) {
			log.error("Event id for " + eventID + " is unknown, cant fire!!!");
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Firing event " + event + " on handle " + handle);
			}
			try {
				if (transacted){
					this.raContext.getSleeEndpoint().fireEventTransacted(handle, eventID, event, address, null, EVENT_FLAGS);
				}
				else {
					this.raContext.getSleeEndpoint().fireEvent(handle, eventID, event, address, null, EVENT_FLAGS);
				}				
				return true;
			} catch (Exception e) {
				log.error("Error firing event.", e);
			}
		}
		return false;
	}

	public boolean fireEvent(Object event, ActivityHandle handle, FireableEventType eventId, Address address, boolean transacted) {
		return this.fireEvent(event, handle, eventId, address, true, transacted);
	}

	// --- XXX - error responses to be a good citizen
	private void sendErrorResponse(ServerTransaction serverTransaction,
			Request request, int code, String msg) {
		if (!request.getMethod().equals(Request.ACK)) {
			try {
				ContentTypeHeader contentType = this.providerProxy
						.getHeaderFactory().createContentTypeHeader("text",
								"plain");
				Response response = providerProxy.getMessageFactory()
						.createResponse(code, request, contentType,
								msg.getBytes());
				if (serverTransaction != null) {
					serverTransaction.sendResponse(response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void addClientDialogMaping(String key, SipActivityHandle handle) {

		this.fromTagCallId2Handle.put(key, handle);
		
	}

	public void removeClientDialogMapping(String key) {
		
	
		this.fromTagCallId2Handle.remove(key);
	}

	public SipActivityHandle getMasterHandleForCall(Response msg) {

		String key = ((FromHeader) msg.getHeader(FromHeader.NAME)).getTag() + "_" + ((CallIdHeader) msg.getHeader(CallIdHeader.NAME)).getCallId();
		SipActivityHandle forkMasterDialogHandle = fromTagCallId2Handle.get(key);

		return forkMasterDialogHandle;
	}
	
	// LIFECYLE
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raActive()
	 */
	public void raActive() {
		
		try {

			// this.mux = this.registerMultiplexer();

			Properties properties = new Properties();
			// load properties for the stack
			properties.load(getClass().getResourceAsStream("sipra.properties"));
			// now load config properties
			properties.setProperty(SIP_BIND_ADDRESS, this.stackAddress);
			properties.setProperty(STACK_NAME_BIND, this.stackName);
			properties.setProperty(TRANSPORTS_BIND, transportsProperty);
			properties.setProperty(SIP_PORT_BIND, Integer.toString(this.port));
			
			this.sipFactory = SipFactory.getInstance();
			this.sipFactory.setPathName("gov.nist"); // hmmm
			this.sipStack = this.sipFactory.createSipStack(properties);
			this.sipStack.start();

			activities = new ConcurrentHashMap();
			
			fromTagCallId2Handle = new ConcurrentHashMap<String, SipActivityHandle>();
			
			boolean created = false;

			if (log.isDebugEnabled()) {
				log
						.debug("---> START "
								+ Arrays.toString(transports.toArray()));
			}

			for (Iterator it = transports.iterator(); it.hasNext();) {
				String trans = (String) it.next();
				ListeningPoint lp = this.sipStack.createListeningPoint(
						this.stackAddress, this.port, trans);

				if (!created) {
					this.provider = this.sipStack.createSipProvider(lp);
					// this.provider
					// .setAutomaticDialogSupportEnabled(automaticDialogSupport);
					created = true;
				} else
					this.provider.addListeningPoint(lp);

				this.provider.addSipListener(this);
				
			}

			// LETS CREATE FP
			// SipFactory sipFactory = SipFactory.getInstance();
			AddressFactory addressFactory = sipFactory.createAddressFactory();
			HeaderFactory headerFactory = sipFactory.createHeaderFactory();
			MessageFactory messageFactory = sipFactory.createMessageFactory();

			this.providerProxy = new SleeSipProviderImpl(addressFactory,
					headerFactory, messageFactory, sipStack, this, provider);

		} catch (Throwable ex) {
			String msg = "error in initializing resource adaptor";
			log.error(msg, ex);	
			throw new RuntimeException(msg,ex);
		}		
		
		if (log.isDebugEnabled()) {
			log.debug("Sip Resource Adaptor entity active.");
		}	
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raInactive()
	 */
	public void raInactive() {
		
		this.provider.removeSipListener(this);
		
		ListeningPoint[] listeningPoints = this.provider.getListeningPoints();
		
		for (int i = 0; i < listeningPoints.length; i++) {
			ListeningPoint lp = listeningPoints[i];
			for (int k = 0; k < 10; k++) {
				try {
					this.sipStack.deleteListeningPoint(lp);
					this.sipStack.deleteSipProvider(this.provider);
					break;
				} catch (ObjectInUseException ex) {
					log
							.error(
									"Object in use -- retrying to delete listening point",
									ex);
					try {
						Thread.sleep(100);
					} catch (Exception e) {

					}
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("Sip Resource Adaptor entity inactive.");
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raStopping()
	 */
	public void raStopping() {
		// TODO Auto-generated method stub
		
	}
	
	//	EVENT PROCESSING CALLBACKS
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int, javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle ah,
			FireableEventType arg1, Object event, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
		
		String id = ((SipActivityHandle) ah).getID();

		if (!id.endsWith(Request.CANCEL)
				|| !(event instanceof CancelRequestEvent))
			return;

		// PROCESSING FAILED, WE HAVE TO SEND 481 response to CANCEL
		try {
			Response txDoesNotExistsResponse = this.providerProxy
					.getMessageFactory().createResponse(
							Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST,
							((CancelRequestEvent) event).getRequest());
			ServerTransactionWrapper stw = (ServerTransactionWrapper) getActivity(ah);
			// provider.sendResponse(txDoesNotExistsResponse);
			stw.sendResponse(txDoesNotExistsResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// not used		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventUnreferenced(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO 
		
	}
	
	//	RA CONFIG
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigurationUpdate(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigurationUpdate(ConfigProperties arg0) {
		// this ra does not support config update while entity is active		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigure(ConfigProperties properties) {
		
		if (log.isDebugEnabled()) {
			log.debug("Configuring RA.");
		}
		
		this.stackName = "SipResourceAdaptorStack_" + (String) properties.getProperty(STACK_NAME_BIND).getValue();

		this.port = (Integer) properties.getProperty(SIP_PORT_BIND).getValue();

		this.stackAddress = (String) properties.getProperty(SIP_BIND_ADDRESS).getValue();
		if (this.stackAddress.equals("null")) {
			this.stackAddress = System.getProperty("jboss.bind.address");				
		}

		this.transportsProperty = (String) properties.getProperty(TRANSPORTS_BIND).getValue();
		for (String transport : this.transportsProperty.split(",")) {
			this.transports.add(transport);
		}

		log.info("RA bound to " + this.stackName + ":" + this.port);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
	 */
	public void raUnconfigure() {		
		this.port = -1;
		this.stackName = null;
		this.stackAddress = null;
		this.transports.clear();		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.resource.ConfigProperties)
	 */
	public void raVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException {
		
		try {
			// get port
			Integer port = (Integer) properties.getProperty(SIP_PORT_BIND).getValue();
			// get host
			String stackAddress = (String) properties.getProperty(SIP_BIND_ADDRESS).getValue();
			if (stackAddress.equals("null")) {
				stackAddress = System.getProperty("jboss.bind.address");				
			}
			// try to open socket
			InetSocketAddress sockAddress = new InetSocketAddress(stackAddress,
					port);
			new DatagramSocket(sockAddress).close();
			// check transports			
			String transports = (String) properties.getProperty(TRANSPORTS_BIND).getValue();
			String[] transportsArray = transports.split(",");
			boolean validTransports = true;
			if (transportsArray.length > 0) {
				for (String transport : transportsArray) {
					if (!allowedTransports.contains(transport.toLowerCase()))
						validTransports = false;
					break;
				}
			}
			else {
				validTransports = false;
			}
			if (!validTransports) {
				throw new IllegalArgumentException(TRANSPORTS_BIND+" config property with invalid value: "+transports);
			}
		}
		catch (Throwable e) {
			throw new InvalidConfigurationException(e.getMessage(),e);
		}
	}
	
	//	EVENT FILTERING
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource.ReceivableService)
	 */
	public void serviceActive(ReceivableService receivableService) {
		eventIDFilter.serviceActive(receivableService);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource.ReceivableService)
	 */
	public void serviceInactive(ReceivableService receivableService) {
		eventIDFilter.serviceInactive(receivableService);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource.ReceivableService)
	 */
	public void serviceStopping(ReceivableService receivableService) {
		eventIDFilter.serviceStopping(receivableService);
		
	}
	
	// RA CONTEXT
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.raContext = raContext;		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
	 */
	public void unsetResourceAdaptorContext() {
		this.raContext = null;		
	}

	/**
	 * Retrieves the RA context
	 */
	public ResourceAdaptorContext getRaContext() {
		return raContext;
	}
	
	// ACTIVITY MANAGEMENT
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource.ActivityHandle)
	 */
	public void activityEnded(ActivityHandle handle) {

		if (log.isDebugEnabled()) {
			log.debug("Removing activity for handle[" + handle + "] activity["
					+ this.activities.get(handle) + "].");
		}

		WrapperSuperInterface activity = (WrapperSuperInterface) this.activities
				.remove(handle);
		if (activity != null) {
			activity.cleanup();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource.ActivityHandle)
	 */
	public void administrativeRemove(ActivityHandle activityHandle) {
		// TODO
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource.ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle arg0) {
		// not used		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.ActivityHandle)
	 */
	public Object getActivity(ActivityHandle arg0) {
		if (arg0 instanceof SipActivityHandle) {
			return this.activities.get(arg0);
		} else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object activity) {
		if (activity instanceof WrapperSuperInterface) {
			WrapperSuperInterface wrapperSuperInterface = (WrapperSuperInterface) activity;
			if (activities.containsKey(wrapperSuperInterface.getActivityHandle())) {
				return wrapperSuperInterface.getActivityHandle();
			}
			else {
				return null;
			}			
		}
		else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource.ActivityHandle)
	 */
	public void queryLiveness(ActivityHandle arg0) {
		// TODO Auto-generated method stub
		
	}
	
	// OTHER GETTERS
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.lang.String)
	 */
	public Object getResourceAdaptorInterface(String raTypeSbbInterfaceclassName) {
		// this ra implements a single ra type
		return providerProxy;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
	 */
	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}	

}