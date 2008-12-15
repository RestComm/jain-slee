package org.mobicents.sleetests.container.entitiesRemoval;

import java.rmi.RemoteException;

import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;

import org.mobicents.slee.connector.server.RemoteSleeService;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;


public class EntitiesRemovalTest extends AbstractSleeTCKTest {
	

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

	private String jnpHostURL="jnp://127.0.0.1:1099";
	
	private String serviceID="EntitiesRemovalTestService#mobicents#0.1";
	private ComponentKey sid = new ComponentKey(this.serviceID);
	private ServiceIDImpl service = new ServiceIDImpl(sid);
	
	
	
	private SleeConnection connection=null;
    private RemoteSleeService sleeService=null;
    /** Creates a new instance of SleeConnector */
   
    private String xEventName="com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X";
    private String yEventName="com.opencloud.sleetck.lib.resource.events.TCKResourceEventY.Y";
    private String vendor="jain.slee.tck";
    private String version="1.0"; 
    private  ExternalActivityHandle ah=null;
    private void sendRequest( boolean isX) {
        
        // if(connection==null) {
        try{
            Properties properties = new Properties();
            //JNDI lookup properties
            properties.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
            properties.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
            String tmpIp="127.0.0.1";
          
            
            properties.put("java.naming.provider.url", "jnp://"+tmpIp+":1099");
            InitialContext ctx=new InitialContext(properties);
            
            //SleeConnectionFactory factory =(SleeConnectionFactory)ctx.lookup("java:comp/env/slee/MySleeConnectionFactory");
            // Obtain a connection to the SLEE from the factory
            //SleeConnection connection = factory.getConnection();
            sleeService=(RemoteSleeService)ctx.lookup("/SleeService");
        }catch(NamingException ne) {
        	result.setError(ne);
            ne.printStackTrace();
        }catch(Exception E) {
        	result.setError(E);
            E.printStackTrace();
        }
        // }
        if(sleeService!=null) {
            try {
            	String eventName=isX?(xEventName+"1"):(yEventName+"1");
                // Locate the event type, same data that WakeUpRequest-event-jar.xml contains
                EventTypeID requestType = sleeService.getEventTypeID(eventName,vendor,version);
                TCKResourceEventImpl event=new TCKResourceEventImpl(1,isX?TCKResourceEventX.X1:TCKResourceEventY.Y1,"TEST", null);
                if(ah==null)
                {
                	ExternalActivityHandle handle = sleeService.createActivityHandle();
                	ah=handle;
                }
               
                sleeService.fireEvent(event,requestType,ah,null);
                
                //connection.endExternalActivity(handle);
            }catch(RemoteException RE) {
                result.setError(RE);
                RE.printStackTrace();
            }
        }       
    }
	
	
	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);

		utils()
				.getLog()
				.info(
						"\n===================\nSTARTING DEPLOYMENT IN FEW uS\n===================\nACTIVITY:"
								+ activityName
								+ "\n=======================================");
		
		SleeCommandInterface SCI=new SleeCommandInterface(jnpHostURL);
		getLog().info(" FIRING X1 - should be received");
		//TckResourceEventX.X1 should be receied twice
		//resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1, tckActivityID, null);
		sendRequest(true);
		Thread.sleep(100);
		try
		{
			Object opResult=null;
			
				getLog().info(" == DEACTIVATING SERVICE ==");
				opResult=SCI.invokeOperation("-deactivateService", service.toString(), null, null);
				
				getLog().info(" == SERVICE DEACTIVATED:"+opResult+" ==");
			
			
		}catch(Exception e)
		{
			result.setError(e);
			e.printStackTrace();
		}
		Thread.currentThread().sleep(100);
		getLog().info(" == FIRING Y1(should not be received) ==");
		//resource.fireEvent(TCKResourceEventY.Y1, TCKResourceEventY.Y1, tckActivityID, null);
		sendRequest(false);
		Thread.sleep(150);
		getLog().info(" == FIRING  X1(should be received) ==");
		sendRequest(true);
		//resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1, tckActivityID, null);
		
	}

}
