package org.mobicents.sleetests.ejbconnectionproxy.RemoteAccess;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OptionalDataException;
import java.net.URL;
import java.rmi.RemoteException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;

import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;


import org.mobicents.slee.connector.proxy.SleeConnectionProxyInterface;
import org.mobicents.slee.connector.server.RemoteSleeService;
import org.mobicents.slee.container.component.ComponentKey;

//import org.mobicents.sleetests.ra.sip.CommonClientPart;
//import org.mobicents.sleetests.ra.sip.CommonClientPart.TestResourceListenerImpl;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;
import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;
//import com.sun.org.apache.bcel.internal.util.ClassLoader;

public class RemoteAccessTest extends AbstractSleeTCKTest {
	// protected static Logger
	// logger=Logger.getLogger(SipResourceAdaptor.class);
	protected static Logger logger = Logger.getLogger(RemoteAccessTest.class.toString());

	public void setUp() throws Exception {
		super.setUp();

		getLog()
				.info(
						"\n========================\nConnecting to resource\n========================\n");
		TCKResourceListener resourceListener = new TestResourceListenerImpl();
		setResourceListener(resourceListener);
		/*
		 * Properties props = new Properties(); try {
		 * props.load(getClass().getResourceAsStream("sipStack.properties"));
		 *  } catch (IOException IOE) { logger.info("FAILED TO LOAD:
		 * sipStack.properties");
		 *  }
		 */

	}

	protected FutureResult result;

	

	private class TestResourceListenerImpl extends BaseTCKResourceListener {

		public synchronized void onSbbMessage(TCKSbbMessage message,
				TCKActivityID calledActivity) throws RemoteException {
			Map sbbData = (Map) message.getMessage();
			Boolean sbbPassed = (Boolean) sbbData.get("result");
			String sbbTestMessage = (String) sbbData.get("message");

			getLog().info(
					"Received message from SBB: passed=" + sbbPassed
							+ ", message=" + sbbTestMessage);

			if (sbbPassed.booleanValue()) {
				result.setPassed();
			} else {
				result.setFailed(0, sbbTestMessage);
			}
		}

		public void onException(Exception exception) throws RemoteException {
			getLog().warning("Received exception from SBB or resource:");
			getLog().warning(exception);
			result.setError(exception);
		}
	}

	private TCKActivityID tckActivityID = null;

	private String activityName = null;

	private SleeConnection connection = null;

	private RemoteSleeService service = null;

	private String eventName = "com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X1";

	private String eventVendor = "jain.slee.tck";

	private String eventVersion = "1.0";

	private String propsFileName="ejbconnection-proxy.properties";
	

	/**
	 * Reads properites file in order to create Properties for initial context
	 */
	private Properties readProps()
	{
		
		Properties returnprops=new Properties();
		Properties fileProps=new Properties();
		boolean exceptionCaught=false;

		try {
			
			InputStream IS=getClass().getResourceAsStream(propsFileName);
			
			
			fileProps.load(IS);
			returnprops.put(InitialContext.INITIAL_CONTEXT_FACTORY,fileProps.getProperty("InitialContext.INITIAL_CONTEXT_FACTORY","org.jnp.interfaces.NamingContextFactory"));
			returnprops.put(InitialContext.PROVIDER_URL,fileProps.getProperty("InitialContext.PROVIDER_URL","jnp://127.0.0.1:1099"));
			
		} catch (Exception e) {
			
			
			//exceptionCaught=true;
			e.printStackTrace();
		}
		//
		//if(exceptionCaught)
		//{
			//WE HAVE TO CHECH IF THOSE HAVE BEEN INTRIDUCED WITH -D ??
		//}
		 
		//return fileProps;
		return returnprops;
	}
	private InitialContext getContext() throws NamingException {
		Hashtable props = new Hashtable();
		
		props.put(InitialContext.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		props.put(InitialContext.PROVIDER_URL, "jnp://127.0.0.1:1099");

		Properties fileProps=readProps();
		
		// This establishes the security for authorization/authentication
		// props.put(InitialContext.SECURITY_PRINCIPAL,"username");
		// props.put(InitialContext.SECURITY_CREDENTIALS,"password");
		InitialContext initialContext=null;
		if(fileProps==null)
			initialContext = new InitialContext(props);
		else
			initialContext = new InitialContext(fileProps);
		return initialContext;
	}

	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);

		
		getLog().info(" == STARTING TEST ==");
		getLog().info(" == CREATING 3 EJBS ==");
		try {
			InitialContext ctx=getContext();
			getLog().info(" == CREATING  EJB ==");
			SleeConnectionProxyInterface myBean1 = (SleeConnectionProxyInterface) ctx.lookup("SleeConnectionProxyBean/remote");
			getLog().info(" == EJB CREATED ==");
			getLog().info(" == CREATING  EJB ==");
			SleeConnectionProxyInterface myBean2 = (SleeConnectionProxyInterface) ctx.lookup("SleeConnectionProxyBean/remote");
			getLog().info(" == EJB CREATED ==");
			getLog().info(" == CREATING  EJB ==");
			SleeConnectionProxyInterface myBean3 = (SleeConnectionProxyInterface) ctx.lookup("SleeConnectionProxyBean/remote");
			getLog().info(" == EJB CREATED ==");
			// --------------------------------------
			// This is the place you make your calls.
			// System.out.println(myBean.callYourMethod());
			getLog().info(" == EJB#1 CREATE ActivityHandle ==");
			ExternalActivityHandle ah = myBean1.createActivityHandle();
			getLog().info(" == CREATE AH[ " + ah + " ] ==");
			getLog().info(" == EJB#1 GET EventTypeID ==");
			EventTypeID sip = myBean1.getEventTypeID(eventName,eventVendor,eventVersion);
			//Response resp=new Response();
			getLog().info( " == EVENTTYPEID:[ "+sip+" ] ==");
			//ResponseEvent event=new ResponseEvent("OBJECT1",null,null,null);
			String []tmp={"111111","222222222","33333333333","444","5555"};
			Object[] message=new Object[7];
			int[] a={1,2,3,4,5};
			message[0]="ServiceID[SomeNotExistingService#mobicents#0.1]";
			message[1]=a;
			message[2]=tmp;
			message[3]=new ComponentKey("ALA","mA","KOTA");
			message[4]=new ComponentKey("ALA1","mA2","KOTA3");
			getLog().info(" == CREATING X1 EVENT ==");
			TCKResourceEventX event = new TCKResourceEventImpl(12,
					TCKResourceEventX.X1,message , null);
			getLog().info( "  == EVENT:[ "+event+" ]  ==" );
			getLog().info(" == EJB#2 FIRE EVENT ==");
			myBean2.fireEvent(event,sip,ah,null);
			// myBean.getEventTypeID();
			getLog().info(" == EJB#2 EVENT DISPATCHED ==");
		
			getLog().info(" == REMOVING EJB#3 ==");
			myBean3.close();
			getLog().info(" == REMOVING EJB#2 ==");
			myBean2.close();
			getLog().info(" == REMOVING EJB#1 ==");
			myBean1.close();
			getLog().info(" == REMOVED ALL BEANS ==");
		} catch (NamingException e) {
			result.setError(e);
			e.printStackTrace();
		} catch (ResourceException e) {
			result.setError(e);
			e.printStackTrace();
		} catch (UnrecognizedEventException e) {
			result.setError(e);
			e.printStackTrace();
		}
		
	}

}
