package org.mobicents.sleetests.sleeservice.RemoteAccess;


import java.rmi.RemoteException;


import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;





import org.mobicents.slee.connector.server.RemoteSleeService;
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

public class RemoteAccessTest extends AbstractSleeTCKTest  {
	//protected static Logger logger=Logger.getLogger(SipResourceAdaptor.class);
	protected static Logger logger=Logger.getLogger(RemoteAccessTest.class.toString());


	
	public void setUp() throws Exception
	{
		super.setUp();
		
		getLog().info("\n========================\nConnecting to resource\n========================\n");
        TCKResourceListener resourceListener = new TestResourceListenerImpl();
        setResourceListener(resourceListener);
		/*Properties props = new Properties();
		try {
			props.load(getClass().getResourceAsStream("sipStack.properties"));

		} catch (IOException IOE) {
			logger.info("FAILED TO LOAD: sipStack.properties");
			
		}*/
		
	}
	
	
	
	
	

    protected FutureResult result;	
	
    
    
    //IN CASE SOME TESTS HAVE TO WAIT FOR RESPONSE
    
    /*
    protected void setResultPassed(String msg) throws Exception {
    	logger.info("Success: " + msg);

        HashMap sbbData = new HashMap();
        sbbData.put("result", Boolean.TRUE);
        sbbData.put("message", msg);
        TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
    
    protected void setResultFailed(String msg) throws Exception {
    	logger.info("Failed: " + msg);

    	HashMap sbbData = new HashMap();
    	sbbData.put("result", Boolean.FALSE);
    	sbbData.put("message", msg);
    	TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }*/
    
    
    private class TestResourceListenerImpl extends BaseTCKResourceListener {

    	public synchronized void onSbbMessage(TCKSbbMessage message, TCKActivityID calledActivity) throws RemoteException {
            Map sbbData = (Map)message.getMessage();
            Boolean sbbPassed = (Boolean)sbbData.get("result");
            String sbbTestMessage = (String)sbbData.get("message");

            getLog().info("Received message from SBB: passed=" + sbbPassed + ", message=" + sbbTestMessage);

            if (sbbPassed.booleanValue()) {
                result.setPassed();
            }
            else {
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

	
	private SleeConnection connection=null;
    private RemoteSleeService service=null;
    private String eventName="com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X1";
    private String eventVendor="jain.slee.tck";
    private String eventVersion="1.0";
	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);
		
		
		
		
		
		   
		getLog().info("  == PREpARING TO LOOKUP SERVICE FROM JNDI  ==");
		try{
            Properties properties = new Properties();
            //JNDI lookup properties
            properties.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
            properties.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
            String tmpIP="127.0.0.1";
            
            properties.put("java.naming.provider.url", "jnp://"+tmpIP+":1099");
            InitialContext ctx=new InitialContext(properties);
            
            //SleeConnectionFactory factory =(SleeConnectionFactory)ctx.lookup("java:comp/env/slee/MySleeConnectionFactory");
            // Obtain a connection to the SLEE from the factory
            //SleeConnection connection = factory.getConnection();
            getLog().info("  == LOOKING UP  ==");
            service=(RemoteSleeService)ctx.lookup("/SleeService");
            if(service==null)
            	result.setError(" == LookedUp Serice == null!!! ==");
            getLog().info("  == LOOKED UP  ==");
        }catch(NamingException ne) {
        	result.setError(ne);
            ne.printStackTrace();
        }catch(Exception E) {
        	result.setError(E);
            E.printStackTrace();
        }
        // }
        if(service!=null) {
            try {
                // Locate the event type, same data that WakeUpRequest-event-jar.xml contains
            	getLog().info("  == RETRIEVEING EventTypeID  ==");
                EventTypeID requestType = service.getEventTypeID(eventName,eventVendor,eventVersion);
                if(requestType==null)
                	result.setError(" == EventTypeID == null!! ==");
                getLog().info("  == RETRIEVAL SUCCESSFUL:"+requestType+"  ==");
                
                
                // Fire an asynchronous event
                getLog().info("  == RETRIEVING ExternalActivityHandle  ==");
                ExternalActivityHandle handle = service.createActivityHandle();
                if(handle==null)
                	result.setError(" == ExternalActivityHandle == null!! ==");
                getLog().info("  == RETRIEVAL SUCCESSFUL:"+handle+"  ==");
                TCKResourceEventX xEvent=new TCKResourceEventImpl(12,TCKResourceEventX.X1, "SLEE SERVICE TEST", null);
                getLog().info("  == FIRING EVENT:"+xEvent+"  ==");
                service.fireEvent(xEvent, requestType, handle, null);
                getLog().info("  == EVENT FIRED, WAITING FOR TestSbb TO RECEIVE IT  ==");
                //connection.endExternalActivity(handle);
            }catch(RemoteException RE) {
            	result.setError(RE);
                RE.printStackTrace();
            }
        }       
		
	}

	

	

}
