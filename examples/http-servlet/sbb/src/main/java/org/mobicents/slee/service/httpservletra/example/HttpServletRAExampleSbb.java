package org.mobicents.slee.service.httpservletra.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbID;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

import org.apache.log4j.Logger;

public abstract class HttpServletRAExampleSbb implements javax.slee.Sbb {

	private static Logger log = Logger.getLogger(HttpServletRAExampleSbb.class);

	private SbbContext sbbContext; // This SBB's SbbContext

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
		try {

			HttpServletResponse response = event.getResponse();

			PrintWriter w = response.getWriter();

			w.print("onPost OK! Served by SBB = " + getSbbId());

			w.flush();

			response.flushBuffer();

			log
					.info("HttpServletRAExampleSbb: POST Request received and OK! response sent.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onGet(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		try {

			HttpServletResponse response = event.getResponse();

			PrintWriter w = response.getWriter();

			w.print("onGet OK! Served by SBB = " + getSbbId());

			w.flush();

			response.flushBuffer();

			log
					.info("HttpServletRAExampleSbb: GET Request received and OK! response sent.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onHead(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		// The HEAD method is identical to GET except that the server MUST NOT
		// return a message-body in the response.
		log.info("HttpServletRAExampleSbb: HEAD Request received.");

	}

	public void onPut(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		try {

			HttpServletResponse response = event.getResponse();

			PrintWriter w = response.getWriter();

			w.print("onPut OK! Served by SBB = " + getSbbId());

			w.flush();

			response.flushBuffer();

			log
					.info("HttpServletRAExampleSbb: PUT Request received and OK! response sent.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onDelete(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		try {

			HttpServletResponse response = event.getResponse();

			PrintWriter w = response.getWriter();

			w.print("onDelete OK! Served by SBB = " + getSbbId());

			w.flush();

			response.flushBuffer();

			log
					.info("HttpServletRAExampleSbb: DELETE Request received and OK! response sent.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onOptions(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		try {

			HttpServletResponse response = event.getResponse();

			PrintWriter w = response.getWriter();

			w.print("onOptions OK! Served by SBB = " + getSbbId());

			w.flush();

			response.flushBuffer();

			log
					.info("HttpServletRAExampleSbb: OPTIONS Request received and OK! response sent.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onTrace(HttpServletRequestEvent event,
			ActivityContextInterface aci) {
		try {

			HttpServletResponse response = event.getResponse();

			PrintWriter w = response.getWriter();

			w.print("onTrace OK! Served by SBB = " + getSbbId());

			w.flush();

			response.flushBuffer();

			log
					.info("HttpServletRAExampleSbb: TRACE Request received and OK! response sent.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private final String getSbbId() {
		SbbID sbbId = this.sbbContext.getSbb();
		return sbbId.toString();
	}

}
