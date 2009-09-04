package org.mobicents.slee.service.httpclientra.example;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.facilities.Tracer;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientActivityContextInterfaceFactory;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;
import net.java.client.slee.resource.http.HttpMethodName;
import net.java.client.slee.resource.http.event.Response;
import net.java.client.slee.resource.http.event.ResponseEvent;

import org.apache.commons.httpclient.HttpMethod;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * This is a simple Service to demonstrate the usage of Http-client-ra 
 * This service reads the RSS Feed link from sbb-jar.xml and fetches the 
 * links periodically using ROME api (Look at https://rome.dev.java.net/ to know more about
 * ROME.) 
 * 
 * If there are changes in the RSS Feed, this service retrieves the content of the link
 * 
 * @author amit.bhayani
 *
 */
public abstract class HttpClientRAExampleSbb implements javax.slee.Sbb {

	private Tracer tracer;

	private SbbContext sbbContext; // This SBB's SbbContext

	private TimerFacility timerFacility;

	private HttpClientActivityContextInterfaceFactory httpClientAci;

	private HttpClientResourceAdaptorSbbInterface raSbbInterface;

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
		this.tracer = sbbContext.getTracer(HttpClientRAExampleSbb.class.getSimpleName());
		
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");

			httpClientAci = (HttpClientActivityContextInterfaceFactory) ctx
					.lookup("slee/resources/http-client-ra/http-client-ra-acif");

			raSbbInterface = (HttpClientResourceAdaptorSbbInterface) ctx
					.lookup("slee/resources/http-client-ra/org.mobicents/1.0.00/http-client-ra/sbb2ra");

			// Getting Timer Facility interface
			timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
		} catch (NamingException ne) {
			tracer.severe("Could not set SBB context:", ne);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	public void unsetSbbContext() {
		// TODO Auto-generated method stub

	}

	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		
		tracer.info("<><><><><> \n onStartServiceEvent of HttpClientRAExampleSbb \n <><><><><><><>");
		
		processRssFeed();

		setTimer(aci);
		
		aci.detach(sbbContext.getSbbLocalObject());
	}

	private void processRssFeed() {
		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");

			String rssFeedString = (String) myEnv.lookup("RSSFeedURL");
			URL rssFeedUrl = new URL(rssFeedString);

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(rssFeedUrl));

			if (feed.hashCode() != this.getFeedHashCode()) {
				tracer.info("There is a new entry in the RSS Feed "
						+ rssFeedString);
				this.setFeedHashCode(feed.hashCode());

				List<?> list = feed.getEntries();
				Iterator<?> itr = list.iterator();
				while (itr.hasNext()) {
					SyndEntryImpl syndFeed = (SyndEntryImpl) itr.next();
					tracer.info("Getting the Content for URL "
							+ syndFeed.getLink());

					HttpMethod httpMethod = raSbbInterface.createHttpMethod(
							HttpMethodName.GET, syndFeed.getLink());
					try {

						HttpClientActivity clientActivity = raSbbInterface
								.createHttpClientActivity(true);

						ActivityContextInterface clientAci = httpClientAci
								.getActivityContextInterface(clientActivity);
						clientAci.attach(sbbContext.getSbbLocalObject());

						clientActivity.executeMethod(httpMethod);

					} catch (Throwable e) {
						tracer.severe("Error while creating HttpClientActivity",
								e);
					}
				}

			} else {
				tracer.info("No new entry in the RSS Feed " + rssFeedString);
			}

		} catch (NamingException namingEx) {
			tracer.severe("NamingException", namingEx);
		} catch (MalformedURLException malformedEx) {
			tracer.severe("MalformedURLException", malformedEx);
		} catch (IOException ioEx) {
			tracer.severe("IOException", ioEx);
		} catch (FeedException feedEx) {
			tracer.severe("FeedException", feedEx);

		}
	}

	private void setTimer(ActivityContextInterface aci) {
		TimerOptions options = new TimerOptions();
		options.setPreserveMissed(TimerPreserveMissed.ALL);
		
		TimerID timerID;
		long refreshTime;

		try {
			Context initCtx = new InitialContext();
			Context myEnv = (Context) initCtx.lookup("java:comp/env");

			refreshTime = ((Long) myEnv.lookup("RefreshTime")).longValue();

			timerID = this.timerFacility.setTimer(aci, null, System
					.currentTimeMillis()
					+ refreshTime, options);

			// Setting Timer ID
			this.setTimerID(timerID);

		} catch (NamingException e) {
			tracer.severe(e.getMessage(), e);
		}
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		tracer.info("########## HttpClientRAExampleSbb: onTimerEvent ##########");

		processRssFeed();

		setTimer(aci);

	}

	public void onResponseEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		Response response = event.getResponse();
		
		tracer.info("********** onResponseEvent **************");
		tracer.info("Status Code = "+response.getStatusCode());
		tracer.info("Response Body = "+response.getResponseBodyAsString());
		tracer.info("*****************************************");
	}

	// 'timerID' CMP field setter
	public abstract void setTimerID(TimerID value);

	public abstract TimerID getTimerID();

	public abstract void setFeedHashCode(int feedHashCode);

	public abstract int getFeedHashCode();

}
