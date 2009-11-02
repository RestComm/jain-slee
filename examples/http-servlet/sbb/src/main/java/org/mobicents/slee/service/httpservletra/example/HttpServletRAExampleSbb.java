package org.mobicents.slee.service.httpservletra.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.SbbLocalObject;

import net.java.slee.resource.http.HttpServletRaActivityContextInterfaceFactory;
import net.java.slee.resource.http.HttpServletRaSbbInterface;
import net.java.slee.resource.http.HttpSessionActivity;
import net.java.slee.resource.http.events.HttpServletRequestEvent;

import org.apache.log4j.Logger;

public abstract class HttpServletRAExampleSbb implements javax.slee.Sbb {

	private static Logger log = Logger.getLogger(HttpServletRAExampleSbb.class);

	private SbbContext sbbContext; // This SBB's SbbContext

	private HttpServletRaSbbInterface httpServletRaSbbInterface;
	
	private HttpServletRaActivityContextInterfaceFactory httpServletRaActivityContextInterfaceFactory;
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	public void sbbActivate() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 */
	public void sbbCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception,
	 *      java.lang.Object, javax.slee.ActivityContextInterface)
	 */
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	public void sbbLoad() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	public void sbbPassivate() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */
	public void sbbPostCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	public void sbbRemove() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */
	public void sbbRolledBack(RolledBackContext arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	public void sbbStore() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	public void unsetSbbContext() {
		// TODO Auto-generated method stub

	}

	public void onPost(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		aci.detach(sbbLocalObject);
		try {
			HttpServletResponse response = event.getResponse();
			PrintWriter w = response.getWriter();
			w.print("onPost OK! Served by SBB = " + getSbbId());
			w.flush();
			response.flushBuffer();
			log
					.info("HttpServletRAExampleSbb: POST Request received and OK! response sent.");
		} catch (IOException e) {
			log.error(e);
		}

	}

	public void onGet(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		
		aci.detach(sbbContext.getSbbLocalObject());
		HttpServletResponse response = event.getResponse();
		PrintWriter w = null;
		try {
			w = response.getWriter();
			w.print("onGet OK! Served by SBB = " + getSbbId());
			w.flush();
			response.flushBuffer();
			log
					.info("HttpServletRAExampleSbb: GET Request received and OK! response sent.");
		} catch (Exception e) {
			log.error(e);
		}

	}

	public void onPut(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		aci.detach(sbbLocalObject);
		try {
			// here we will setup a session activity before sending the response back
			if (httpServletRaSbbInterface == null) {
				Context myEnv = (Context) new InitialContext().lookup("java:comp/env"); 
	        	httpServletRaSbbInterface = (HttpServletRaSbbInterface) myEnv.lookup("slee/resources/mobicents/httpservlet/sbbrainterface");
	        	httpServletRaActivityContextInterfaceFactory = (HttpServletRaActivityContextInterfaceFactory) myEnv.lookup("slee/resources/mobicents/httpservlet/acifactory");
			}
			HttpSession httpSession = event.getRequest().getSession();
			HttpSessionActivity httpSessionActivity = httpServletRaSbbInterface
					.getHttpSessionActivity(httpSession);
			ActivityContextInterface httpSessionActivityContextInterface = 
				httpServletRaActivityContextInterfaceFactory
					.getActivityContextInterface(httpSessionActivity);
			httpSessionActivityContextInterface.attach(sbbLocalObject);
			HttpServletResponse response = event.getResponse();
			PrintWriter w = response.getWriter();
			w.print("onPut OK! Served by SBB = " + getSbbId());
			w.flush();
			response.flushBuffer();
			log
					.info("HttpServletRAExampleSbb: PUT Request received and OK! response sent.");
		} catch (Exception e) {
			log.error(e);
		}

	}

	public void onDelete(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		
		aci.detach(sbbContext.getSbbLocalObject());
		HttpServletResponse response = event.getResponse();
		PrintWriter w = null;
		try {
			w = response.getWriter();
			w.print("onDelete OK! Served by SBB = " + getSbbId());
			w.flush();
			response.flushBuffer();
			log
					.info("HttpServletRAExampleSbb: DELETE Request received and OK! response sent.");
		} catch (IOException e) {
			log.error(e);
		}

	}

	public void onOptions(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		
		aci.detach(sbbContext.getSbbLocalObject());
		HttpServletResponse response = event.getResponse();
		PrintWriter w = null;
		try {
			w = response.getWriter();
			w.print("onOptions OK! Served by SBB = " + getSbbId());
			w.flush();
			response.flushBuffer();
			log
					.info("HttpServletRAExampleSbb: OPTIONS Request received and OK! response sent.");
		} catch (IOException e) {
			log.error(e);
		}

	}

	public void onTrace(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		
		aci.detach(sbbContext.getSbbLocalObject());
		HttpServletResponse response = event.getResponse();
		PrintWriter w = null;
		try {
			w = response.getWriter();
			w.print("onTrace OK! Served by SBB = " + getSbbId());
			w.flush();
			response.flushBuffer();
			log
					.info("HttpServletRAExampleSbb: TRACE Request received and OK! response sent.");
		} catch (IOException e) {
			log.error(e);
		}
	}

	public void onActivityEndEvent(ActivityEndEvent event, ActivityContextInterface aci) {
		log.info("Got an activity end for activity "+aci.getActivity());
	}
	
	private final String getSbbId() {
		SbbID sbbId = this.sbbContext.getSbb();
		return sbbId.toString();
	}

}
