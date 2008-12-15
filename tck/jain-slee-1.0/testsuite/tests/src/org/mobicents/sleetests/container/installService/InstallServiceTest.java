package org.mobicents.sleetests.container.installService;

import java.rmi.RemoteException;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.management.BadAttributeValueExpException;




import com.opencloud.sleetck.lib.AbstractSleeTCKTest;

import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;

import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;

import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;


public class InstallServiceTest extends AbstractSleeTCKTest {
	

	public void setUp() throws Exception {
		super.setUp();

		getLog()
				.info(
						"\n========================\nConnecting to resource\n========================\n");
		TCKResourceListener resourceListener = new TestResourceListenerImpl();
		setResourceListener(resourceListener);
	
		
		
		try
		{
			getLog().info(" == LOADING PROPS FROM: test.properties ==");
			props.load(getClass().getResourceAsStream("test.properties"));
			jnpHostURL=props.getProperty("jnpHost",jnpHostURL);
			raTypeDUName=props.getProperty("raTypeDUName",raTypeDUName);
			raDUName=props.getProperty("raDUName",raDUName);
			raLINK=props.getProperty("raLINK",raLINK);
			testServiceDUName=props.getProperty("testServiceDUName",testServiceDUName);
			raID=props.getProperty("raID",raID);
			serviceID=props.getProperty("serviceID",serviceID);
			sid = new ComponentKey(this.serviceID);
			service = new ServiceIDImpl(sid);
			raId = new ComponentKey(this.raID);
			ra = new ResourceAdaptorIDImpl(raId);
			getLog().info(" == FINISHED LOADING PROPS ==");
		}catch(Exception IOE)
		{
			getLog().info("FAILED TO LOAD: test.properties");
		
			throw IOE;
		}
		
		
		utils()
				.getLog()
				.info(
						"\n===================\nSTARTING DEPLOYMENT IN FEW uS\n===================\nACTIVITY:"
								+ activityName
								+ "\n=======================================");

		String mcHOME = System.getProperty("MOBICENTS_HOME");
		if(mcHOME==null)
			throw new BadAttributeValueExpException(" == The System Property MOBICENTS_HOME is required, but does not exist!! ==");
				
		
		
		
		
		SCI=new SleeCommandInterface(jnpHostURL);
		 dusPATH="file://"+mcHOME + "/tests/lib/container/";

		
		 raTypeInstalled=raInstalled=raEntityActivated=raEntityCreated=raLinkCreated=serviceActivated=serviceInstalled=false;
		 //TODO: Add tests here to remove everything from possible dirty run
	}

	protected FutureResult result;

	

	/*
	 * protected void setResultPassed(String msg) throws Exception {
	 * logger.info("Success: " + msg);
	 * 
	 * HashMap sbbData = new HashMap(); sbbData.put("result", Boolean.TRUE);
	 * sbbData.put("message", msg);
	 * TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData); }
	 * 
	 * protected void setResultFailed(String msg) throws Exception {
	 * logger.info("Failed: " + msg);
	 * 
	 * HashMap sbbData = new HashMap(); sbbData.put("result", Boolean.FALSE);
	 * sbbData.put("message", msg);
	 * TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData); }
	 */

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
	private String raTypeDUName="sip-ra-type.jar";
	private String raDUName="sip-local-ra.jar";
	private String raLINK="SipRA";
	private String testServiceDUName="DummySbbService-DU.jar";
	private String raID="JSIPSTUB#net.java.slee.sip#1.2";
	private String serviceID="StateEventsPassedTestService#mobicents#0.1";
	private ComponentKey sid = new ComponentKey(this.serviceID);
	private ServiceIDImpl service = new ServiceIDImpl(sid);
	private ComponentKey raId = new ComponentKey(this.raID);
	private ResourceAdaptorIDImpl ra = new ResourceAdaptorIDImpl(raId);
	protected boolean raTypeInstalled,raInstalled,raEntityCreated,raEntityActivated,raLinkCreated,serviceInstalled,serviceActivated;
	protected Properties props=new Properties();
	protected String dusPATH=null;
	protected SleeCommandInterface SCI=null;
	protected ArrayList errors=new ArrayList(2);
	private Object opResult=null;
	protected void installAll() throws Exception 
	{
		

		deployRaType();
		deployRa();
		createRaEntity();
		activateRaEntity();
		createEntityLink();
		installService();
		activateService();
		
	}
	protected void installService() throws Exception
	{
		if(serviceInstalled)
			return;
		getLog().info(" == INSTALLING TEST SERVICE:"+dusPATH+testServiceDUName+" ==");
		opResult=SCI.invokeOperation("-install",dusPATH+testServiceDUName,null,null);
		getLog().info(" == SERVICE INSTALLED:"+opResult+" ==");
		serviceInstalled=true;
		for(long l=0;l<1000000000l;l++)
		{}
		
		
		
	}
	protected void createEntityLink() throws Exception 
	{
		if(raLinkCreated)
			return;
		getLog().info(" == CREATING RA LINK:"+raLINK+" ==");
		opResult=SCI.invokeOperation("-createRaLink", raLINK, raLINK, null);
		getLog().info(" == RA LINK CREATED:"+opResult+" ==");
		raLinkCreated=true;
		for(long l=0;l<1000000000l;l++)
		{}
	}
	protected void activateRaEntity() throws Exception 
	{
		if(raEntityActivated)
			return;
		getLog().info(" == ACTIVATING RA ENTITY:"+ra+" ==");
		opResult=SCI.invokeOperation("-activateRaEntity", raLINK, null, null);
		getLog().info(" == RA ENTITY ACTIVATED:"+opResult+" ==");
		raEntityActivated=true;
		for(long l=0;l<1000000000l;l++)
		{}
	}
	protected void createRaEntity() throws Exception
	{
		if(raEntityCreated)
			return;
		getLog().info(" == CREATING RA ENTITY:"+ra+" ==");
		opResult=SCI.invokeOperation("-createRaEntity",ra.toString(),raLINK,null);
		getLog().info(" == RA ENTITY CREATED:"+opResult+" ==");
		raEntityCreated=true;
		for(long l=0;l<1000000000l;l++)
		{}
	}
	
	protected void deployRa() throws Exception
	{
		if(raInstalled)
			return;
		getLog().info(" == DEPOYING RA :"+dusPATH+dusPATH+" ==");
		opResult=SCI.invokeOperation("-install",dusPATH+raDUName,null,null);
		getLog().info(" == DEPLOYED RA:"+opResult+" ==");
		raInstalled=true;
		for(long l=0;l<1000000000l;l++)
		{}
	}
	protected void deployRaType() throws Exception
	{
		if(raTypeInstalled)
			return;
		getLog().info(" == DEPOYING RA TYPE:"+dusPATH+raTypeDUName+" ==");
		opResult=SCI.invokeOperation("-install",dusPATH+raTypeDUName,null,null);
		getLog().info(" == DEPLOLYED RA TYPE:"+opResult+" ==");
		raTypeInstalled=true;
		for(long l=0;l<1000000000l;l++)
		{}
	}
	
	protected void activateService()throws Exception 
	{
		if(serviceActivated)
			return;
		
		getLog().info(" == ACTIVATING SERVICE ==");
		opResult=SCI.invokeOperation("-activateService", service.toString(), null, null);
		getLog().info(" == SERVICE ACTIVATED:"+opResult+" ==");
		serviceActivated=true;
		for(long l=0;l<1000000000l;l++)
		{}
	}
	
	
	protected void uninstallAll() throws Exception 
	{

		deactivateService();
		uninstallService();
		removaRaEntityLink();
		deactivateRaEntity();
		destroyRaEntity();
		uninstallRa();
		uninstallRaType();
	}
	
	protected void deactivateService() throws Exception
	{

		if(serviceActivated)
		{
			opResult=SCI.invokeOperation("-deactivateService", service.toString(), null, null);
			serviceActivated=!serviceActivated;
			getLog().info(" == SERVICE DEACTIVATED:"+opResult+" ==");
			serviceActivated=false;
			for(long l=0;l<1000000000l;l++)
			{}
		}
	}
	protected void uninstallService() throws Exception 
	{
		if(serviceInstalled)
		{
			opResult=SCI.invokeOperation("-uninstall",dusPATH+testServiceDUName,null,null);
			serviceInstalled=!serviceInstalled;
			getLog().info(" == SERVICE UNINSTALLED:"+opResult+" ==");
			serviceInstalled=false;
			for(long l=0;l<1000000000l;l++)
			{}
		}
	}
	protected void removaRaEntityLink() throws Exception
	{
		if(raLinkCreated)
		{
			opResult=SCI.invokeOperation("-removeRaLink", raLINK, null, null);
			raLinkCreated=!raLinkCreated;
			getLog().info(" == RA LINK REMOVED:"+opResult+" ==");
			raLinkCreated=false;
			for(long l=0;l<1000000000l;l++)
			{}
		}
	}
	protected void deactivateRaEntity() throws Exception
	{
		if(raEntityActivated)
		{
			opResult=SCI.invokeOperation("-deactivateRaEntity", raLINK, null, null);
			raEntityActivated=!raEntityActivated;
			getLog().info(" == RA ENTITY DEACTIVATED:"+opResult+" ==");
			raEntityActivated=false;
			for(long l=0;l<1000000000l;l++)
			{}
		}
	}
	protected void destroyRaEntity() throws Exception
	{
		
		if(raEntityCreated)
		{
			opResult=SCI.invokeOperation("-removeRaEntity",raLINK,null,null);
			raEntityCreated=!raEntityCreated;
			getLog().info(" == RA ENTITY REMOVED:"+opResult+" ==");
			raEntityCreated=false;
			for(long l=0;l<1000000000l;l++)
			{}
		}
	}
	protected void uninstallRa() throws Exception
	{
		if(raInstalled)
		{
			opResult=SCI.invokeOperation("-uninstall",dusPATH+raDUName,null,null);
			raInstalled=!raInstalled;
			getLog().info(" == RA UNINSTALLED:"+opResult+" ==");
			raInstalled=false;
			for(long l=0;l<1000000000l;l++)
			{}
		}
	}
	protected void uninstallRaType() throws Exception
	{
		if(raTypeInstalled)
		{
			opResult=SCI.invokeOperation("-uninstall",dusPATH+raTypeDUName,null,null);
			raTypeInstalled=!raTypeInstalled;
			getLog().info(" == RA TYPE UNINSTALLED:"+opResult+" ==");
			raTypeInstalled=false;
			for(long l=0;l<1000000000l;l++)
			{}
		}
	}
	
	protected void handleErrors()
	{
		if(!errors.isEmpty())
		{
			getLog().info(" == SOME ERRORS OCURED!!: ==");
			StringBuffer sb=new StringBuffer(400);
			StringBuffer stackTraceSb=null;
			Iterator it=errors.iterator();
			int i=0;
			while(it.hasNext())
			{
				
				Exception ex=(Exception) it.next();
				StackTraceElement[] ste=ex.getStackTrace();
				if(ste!=null && ste.length >0)
				{
					stackTraceSb=new StringBuffer(1000);
					stackTraceSb.append("\n");
					for(int c=0;c<ste.length;c++)
						stackTraceSb.append("=>"+ste[c].getClassName()+" -> "+ste[c].getMethodName()+":"+ste[c].getLineNumber()+"\n");
				}
				sb.append("\n[#"+i+++"] -> "+ex.getMessage());
				if(stackTraceSb!=null)
					sb.append("\nTRACE:\n"+stackTraceSb+"\n");
				
				stackTraceSb=null;
			}
			getLog().info(" == ERROR INFO: ==\n"+sb);
		}
	}
	
	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		tckActivityID = resource.createActivity(activityName);

		
		
		
				getLog().info(" == STARTIGN DEPLOYMENT ==");
		
		
		
		try{
			installAll();
		}catch(Exception e)
		{
			e.printStackTrace();
			errors.add(e);
		}
		
		try
		{
			uninstallAll();
		}catch(Exception e)
		{
			errors.add(e);
		}
		
		handleErrors();
	}

}
